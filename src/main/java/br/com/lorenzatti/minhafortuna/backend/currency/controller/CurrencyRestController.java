package br.com.lorenzatti.minhafortuna.backend.currency.controller;

import br.com.lorenzatti.minhafortuna.backend.currency.dto.CurrencyDto;
import br.com.lorenzatti.minhafortuna.backend.currency.model.Currency;
import br.com.lorenzatti.minhafortuna.backend.currency.service.impl.CurrencyServiceImpl;
import br.com.lorenzatti.minhafortuna.backend.operation.dto.OperationDto;
import br.com.lorenzatti.minhafortuna.backend.operation.service.OperationService;
import br.com.lorenzatti.minhafortuna.backend.security.JwtUser;
import br.com.lorenzatti.minhafortuna.backend.shared.response.Response;
import br.com.lorenzatti.minhafortuna.backend.user.model.User;
import br.com.lorenzatti.minhafortuna.backend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("currency")
public class CurrencyRestController {

    @Autowired
    private CurrencyServiceImpl currencyService;

    @Autowired
    private UserService userService;

    @Autowired
    private OperationService operationService;

    @GetMapping()
    public ResponseEntity<Response> getCurrencies(Authentication authentication) {
        Response<List<CurrencyDto>> response = new Response<>();
        JwtUser loggedUser = (JwtUser) authentication.getPrincipal();
        Optional<User> userOpt = userService.getUserById(loggedUser.getId());
        if (userOpt.isPresent()) {
            List<CurrencyDto> currenciesDto = new ArrayList<>();
            List<Currency> customCurrencies = currencyService.getCurrenciesByUserId(loggedUser.getId());
            List<Currency> defaultCurrencies = currencyService.getCurrenciesDefaultCurrencies();
            customCurrencies.addAll(defaultCurrencies);
            customCurrencies.forEach(currency -> {
                CurrencyDto currencyDto = new CurrencyDto();
                currencyDto.setCode(currency.getCode());
                currencyDto.setName(currency.getName());
                currencyDto.setColor(currency.getColor());
                currencyDto.setAllowChange(currency.getAllowChange());
                currenciesDto.add(currencyDto);
            });
            response.setSuccess(true);
            response.setData(currenciesDto);
            return ResponseEntity.ok(response);
        } else {
            response.setError("Operação não autorizada!");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Response> save(@RequestBody CurrencyDto currencyDto, Authentication authentication) {
        JwtUser loggedUser = (JwtUser) authentication.getPrincipal();
        Optional<User> userOpt = userService.getUserById(loggedUser.getId());
        Response<String> response = new Response<>();
        if (userOpt.isPresent()) {
            Currency currency = new Currency();
            currency.setName(currencyDto.getName());
            currency.setCode(currencyDto.getCode());
            currency.setColor(currencyDto.getColor());
            currency.setAllowChange(true);
            currency.setUser(userOpt.get());
            currencyService.save(currency);

            response.setSuccess(true);
            response.setMessage("Cadastro salvo!");
            return ResponseEntity.ok(response);
        } else {
            response.setError("Operação não autorizada!");
            return ResponseEntity.badRequest().body(response);
        }
    }


    @GetMapping(value = "/{code}")
    public ResponseEntity<Response> getByCode(@PathVariable String code, Authentication authentication) {
        Response<CurrencyDto> response = new Response<>();

        JwtUser loggedUser = (JwtUser) authentication.getPrincipal();
        Optional<User> userOpt = userService.getUserById(loggedUser.getId());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Optional<Currency> currencyOpt = currencyService.findByCode(code);
            if (currencyOpt.isPresent()) {
                Currency currency = currencyOpt.get();
                if (!currency.getUser().getId().equals(user.getId())) {
                    response.setError("Operação não autorizada!");
                    return ResponseEntity.badRequest().body(response);
                }
                CurrencyDto currencyDto = new CurrencyDto();
                currencyDto.setName(currency.getName());
                currencyDto.setCode(currency.getCode());
                currencyDto.setColor(currency.getColor());
                currencyDto.setAllowChange(currency.getAllowChange());
                currencyDto.setIsUsed(operationService.countByCurrencyCode(code) > 0);

                response.setData(currencyDto);
                response.setSuccess(true);
                return ResponseEntity.ok(response);
            }
        }
        response.setError("Operação não autorizada!");
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping(value = "/delete/{code}")
    public ResponseEntity<Response> deleteById(@PathVariable String code, Authentication authentication) {
        Response<OperationDto> response = new Response<>();

        JwtUser loggedUser = (JwtUser) authentication.getPrincipal();
        Optional<User> userOpt = userService.getUserById(loggedUser.getId());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Optional<Currency> currencyOpt = currencyService.findByCode(code);
            if (currencyOpt.isPresent()) {
                Currency currency = currencyOpt.get();
                if (!currency.getUser().getId().equals(user.getId())) {
                    response.setError("Operação não autorizada!");
                    return ResponseEntity.badRequest().body(response);
                }
                currencyService.delete(currency);
                return ResponseEntity.noContent().build();
            }
        }
        response.setError("Operação não autorizada!");
        return ResponseEntity.badRequest().body(response);
    }
}
