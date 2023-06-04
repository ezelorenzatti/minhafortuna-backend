package br.com.lorenzatti.minhafortuna.backend.operation.controller;

import br.com.lorenzatti.minhafortuna.backend.currency.model.Currency;
import br.com.lorenzatti.minhafortuna.backend.currency.service.CurrencyService;
import br.com.lorenzatti.minhafortuna.backend.history.model.History;
import br.com.lorenzatti.minhafortuna.backend.history.service.HistoryService;
import br.com.lorenzatti.minhafortuna.backend.operation.dto.OperationDto;
import br.com.lorenzatti.minhafortuna.backend.operation.enums.EnumOperationType;
import br.com.lorenzatti.minhafortuna.backend.operation.model.Operation;
import br.com.lorenzatti.minhafortuna.backend.operation.service.OperationService;
import br.com.lorenzatti.minhafortuna.backend.plataform.model.Exchange;
import br.com.lorenzatti.minhafortuna.backend.plataform.service.ExchangeService;
import br.com.lorenzatti.minhafortuna.backend.security.JwtUser;
import br.com.lorenzatti.minhafortuna.backend.shared.converter.DataConverter;
import br.com.lorenzatti.minhafortuna.backend.shared.response.Response;
import br.com.lorenzatti.minhafortuna.backend.shared.utils.DataUtils;
import br.com.lorenzatti.minhafortuna.backend.user.model.User;
import br.com.lorenzatti.minhafortuna.backend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("operation")
public class OperationRestController {

    @Autowired
    private OperationService operationService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private DataConverter dataConverter;

    @Autowired
    private HistoryService historyService;


    @PostMapping
    public ResponseEntity<Response> save(@RequestBody OperationDto operationDto, Authentication authentication) {
        Response<String> response = new Response<>();
        JwtUser loggedUser = (JwtUser) authentication.getPrincipal();
        Optional<User> userOpt = userService.getUserById(loggedUser.getId());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Operation operation = new Operation();
            if (Optional.ofNullable(operationDto.getId()).isPresent()) {
                Optional<Operation> operationOpt = operationService.findById(operationDto.getId());
                if (operationOpt.isPresent()) {
                    operation = operationOpt.get();
                    if (!user.getId().equals(operation.getUser().getId())) {
                        response.setError("Operação não autorizada!");
                        return ResponseEntity.badRequest().body(response);
                    }
                }
            } else {
                operation.setUser(userOpt.get());
            }

            Optional<Exchange> plataformOpt = exchangeService.getExchangeById(operationDto.getExchangeId());
            if (plataformOpt.isPresent()) {
                operation.setExchange(plataformOpt.get());
            } else {
                response.setError("Não foi possível continuar, plataforma não localizada!");
                return ResponseEntity.badRequest().body(response);
            }

            Optional<Currency> currencyOpt = currencyService.getCurrenciesById(operationDto.getCurrencyCode());
            if (currencyOpt.isPresent()) {
                operation.setCurrency(currencyOpt.get());
            } else {
                response.setError("Não foi possível continuar, moeda não localizada!");
                return ResponseEntity.badRequest().body(response);
            }

            operation.setOperationType(operationDto.getOperationType());
            operation.setDate(dataConverter.toDate(operationDto.getDate()));
            operation.setAmount(operationDto.getAmount());
            operation.setUnitValue(operationDto.getUnitValue());
            operation.setTotal(operationDto.getTotal());
            operation.setTaxes(operationDto.getTaxes());
            operationService.save(operation);

            response.setSuccess(true);
            response.setMessage("Cadastro salvo!");
            return ResponseEntity.ok(response);
        } else {
            response.setError("Operação não autorizada!");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping()
    public ResponseEntity<Response> operations(
            @RequestParam("operationType") List<EnumOperationType> operationTypes,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            Authentication authentication) {
        Response<List<OperationDto>> response = new Response<>();
        Optional<Authentication> authenticationOpt = Optional.ofNullable(authentication);
        if(authenticationOpt.isPresent()){
            JwtUser loggedUser = (JwtUser) authenticationOpt.get().getPrincipal();
            Optional<User> userOpt = userService.getUserById(loggedUser.getId());
            if (userOpt.isPresent()) {
                Date start = dataConverter.toDate(startDate);
                Date end = dataConverter.toDate(endDate);
                List<OperationDto> operationDtos = new ArrayList<>();
                List<Operation> operations = operationService.findAllByUserIdAndOperationTypeInAndDateBetween(loggedUser.getId(), operationTypes, start, end);
                operations.forEach(operation -> {
                    OperationDto operationDto = new OperationDto();
                    operationDto.setId(operation.getId());
                    operationDto.setOperationType(operation.getOperationType());
                    operationDto.setCurrencyCode(operation.getCurrency().getCode());
                    operationDto.setCurrencyName(operation.getCurrency().getName());
                    operationDto.setColor(operation.getCurrency().getColor());
                    operationDto.setAmount(operation.getAmount());
                    operationDto.setTotal(operation.getTotal());
                    operationDto.setTaxes(operation.getTaxes());
                    operationDto.setUnitValue(operation.getUnitValue());
                    operationDto.setExchangeId(operation.getExchange().getId());
                    operationDto.setExchangeName(operation.getExchange().getName());
                    operationDto.setDate(dataConverter.toString(operation.getDate()));

                    History history = historyService.findLatestByCode(operation.getCurrency().getCode());
                    Double lastValue = operation.getAmount() * history.getSell();
                    operationDto.setLastValue(lastValue);
                    operationDto.setLastValueDate(dataConverter.toString(history.getDate()));

                    operationDtos.add(operationDto);
                });
                response.setData(operationDtos);
                return ResponseEntity.ok(response);
            }
        }
        response.setError("Operação não autorizada!");
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping(value = "/simulate")
    public ResponseEntity<Response> simulate(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("pattern") String pattern,
            @RequestParam("operations") Integer operations,
            Authentication authentication) {
        Response<String> response = new Response<>();

        JwtUser loggedUser = (JwtUser) authentication.getPrincipal();
        Optional<User> userOpt = userService.getUserById(loggedUser.getId());
        if (userOpt.isPresent()) {
            Date start = dataConverter.toDate(startDate, pattern);
            Date end = dataConverter.toDate(endDate, pattern);

            Integer count = 0;
            while (count < operations) {
                Date sortedDate = DataUtils.getRandomDate(start, end);

                Operation operation = new Operation();
                operation.setDate(sortedDate);
                operation.setUser(userOpt.get());

                List<String> operationTypes = Arrays.asList(EnumOperationType.BUY.name(), EnumOperationType.SELL.name());
                EnumOperationType operationType = EnumOperationType.valueOf(selectRandomString(operationTypes));
                operation.setOperationType(operationType);

                List<Currency> currencies = currencyService.getCurrenciesDefaultCurrencies();
                List<String> currenciesCode = new ArrayList<>();
                currencies.forEach(currency -> currenciesCode.add(currency.getCode()));
                String currencyCode = selectRandomString(currenciesCode);
                Optional<Currency> currencyOpt = currencyService.getCurrenciesById(currencyCode);
                Optional<History> historyOpt = historyService.findByDateEqualsAndCode(sortedDate, currencyCode);
                if (currencyOpt.isPresent() && historyOpt.isPresent()) {
                    Currency currency = currencyOpt.get();
                    History history = historyOpt.get();

                    operation.setCurrency(currency);

                    Double buyValue = history.getBuy();
                    Integer amount = generateRandomNumber(1, 500);
                    Double totalValue = buyValue * amount;

                    operation.setUnitValue(buyValue);
                    operation.setAmount(amount);
                    operation.setTotal(totalValue);

                    Integer taxPercentage = generateRandomNumber(1, 5);
                    Double taxValue = calculatePercentage(totalValue, taxPercentage);
                    operation.setTaxes(taxValue);

                    Long plataformId = new Long(generateRandomNumber(1, 3));
                    Optional<Exchange> plataform = exchangeService.getExchangeById(plataformId);
                    operation.setExchange(plataform.get());

                    operationService.save(operation);

                    count++;
                }
            }
            response.setMessage("Carga de dados realizada !!");
            response.setSuccess(true);
            return ResponseEntity.ok(response);
        } else {
            response.setError("Operação não autorizada!");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Response> getById(@PathVariable Long id, Authentication authentication) {
        Response<OperationDto> response = new Response<>();

        JwtUser loggedUser = (JwtUser) authentication.getPrincipal();
        Optional<User> userOpt = userService.getUserById(loggedUser.getId());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Optional<Operation> operationOpt = operationService.findById(id);
            if (operationOpt.isPresent()) {
                Operation operation = operationOpt.get();
                if (!operation.getUser().getId().equals(user.getId())) {
                    response.setError("Operação não autorizada!");
                    return ResponseEntity.badRequest().body(response);
                }
                OperationDto operationDto = new OperationDto();
                operationDto.setId(operation.getId());
                operationDto.setOperationType(operation.getOperationType());
                operationDto.setCurrencyCode(operation.getCurrency().getCode());
                operationDto.setColor(operation.getCurrency().getColor());
                operationDto.setAmount(operation.getAmount());
                operationDto.setTotal(operation.getTotal());
                operationDto.setTaxes(operation.getTaxes());
                operationDto.setUnitValue(operation.getUnitValue());
                operationDto.setExchangeId(operation.getExchange().getId());
                operationDto.setDate(dataConverter.toString(operation.getDate()));

                response.setData(operationDto);
                response.setSuccess(true);
                return ResponseEntity.ok(response);
            }
        }
        response.setError("Operação não autorizada!");
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping(value = "/delete/{id}")
    public ResponseEntity<Response> deleteById(@PathVariable Long id, Authentication authentication) {
        Response<OperationDto> response = new Response<>();

        JwtUser loggedUser = (JwtUser) authentication.getPrincipal();
        Optional<User> userOpt = userService.getUserById(loggedUser.getId());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Optional<Operation> operationOpt = operationService.findById(id);
            if (operationOpt.isPresent()) {
                Operation operation = operationOpt.get();
                if (!operation.getUser().getId().equals(user.getId())) {
                    response.setError("Operação não autorizada!");
                    return ResponseEntity.badRequest().body(response);
                }
                operationService.delete(operation);
                return ResponseEntity.noContent().build();
            }
        }
        response.setError("Operação não autorizada!");
        return ResponseEntity.badRequest().body(response);
    }


    public String selectRandomString(List<String> stringList) {
        if (stringList == null || stringList.isEmpty()) {
            throw new IllegalArgumentException("A lista de strings não pode ser nula ou vazia.");
        }

        Random random = new Random();
        int randomIndex = random.nextInt(stringList.size());

        return stringList.get(randomIndex);
    }

    public static int generateRandomNumber(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("O valor mínimo deve ser menor ou igual ao valor máximo.");
        }
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static double calculatePercentage(double originalValue, double percentage) {
        double result = (originalValue * percentage) / 100;
        return result;
    }

}
