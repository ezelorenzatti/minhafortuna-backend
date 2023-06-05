package br.com.lorenzatti.minhafortuna.backend.operation.service.impl;

import br.com.lorenzatti.minhafortuna.backend.bcbapi.dto.BCBCurrency;
import br.com.lorenzatti.minhafortuna.backend.bcbapi.service.BcbApi;
import br.com.lorenzatti.minhafortuna.backend.currency.model.Currency;
import br.com.lorenzatti.minhafortuna.backend.currency.service.CurrencyService;
import br.com.lorenzatti.minhafortuna.backend.history.model.History;
import br.com.lorenzatti.minhafortuna.backend.history.service.HistoryService;
import br.com.lorenzatti.minhafortuna.backend.operation.enums.EnumOperationType;
import br.com.lorenzatti.minhafortuna.backend.operation.model.Operation;
import br.com.lorenzatti.minhafortuna.backend.operation.repository.OperationRepository;
import br.com.lorenzatti.minhafortuna.backend.operation.service.OperationService;
import br.com.lorenzatti.minhafortuna.backend.exchange.model.Exchange;
import br.com.lorenzatti.minhafortuna.backend.exchange.service.ExchangeService;
import br.com.lorenzatti.minhafortuna.backend.shared.utils.ColorUtils;
import br.com.lorenzatti.minhafortuna.backend.shared.utils.Utils;
import br.com.lorenzatti.minhafortuna.backend.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OperationServiceImpl implements OperationService {

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private BcbApi bcbApi;

    @Override
    public Operation save(Operation operation) {
        return operationRepository.save(operation);
    }

    @Override
    public List<Operation> findAllByUserIdAndOperationTypeInAndDateBetween(Long userId, List<EnumOperationType> type, Date startDate, Date endDate) {
        return operationRepository.findAllByUserIdAndOperationTypeInAndDateBetweenOrderByDateDesc(userId, type, startDate, endDate);
    }

    @Override
    public Optional<Operation> findById(Long id) {
        return operationRepository.findById(id);
    }

    @Override
    public void delete(Operation operation) {
        operationRepository.delete(operation);
    }

    @Override
    public Integer countByExchangeId(Long id) {
        return operationRepository.countByExchangeId(id);
    }

    @Override
    public Integer countByCurrencyCode(String code) {
        return operationRepository.countByCurrencyCode(code);
    }

    @Override
    public void simulate(Date start, Date end, Integer operations, User user) {
        Integer count = 0;
        Integer failCount = 0;
        while (count < operations || failCount >= operations) {
            try {
                Date sortedDate = Utils.getRandomDate(start, end);

                Operation operation = new Operation();
                operation.setDate(sortedDate);
                operation.setUser(user);

                List<String> operationTypes = Arrays.asList(EnumOperationType.BUY.name(), EnumOperationType.SELL.name());
                EnumOperationType operationType = EnumOperationType.valueOf(Utils.selectRandomString(operationTypes));
                operation.setOperationType(operationType);

                List<Currency> currencies = currencyService.getCurrenciesDefaultCurrencies();
                if (currencies.size() == 0) {
                    List<String> ignoreColors = new ArrayList<>();
                    List<BCBCurrency> bcbCurrencies = bcbApi.getCurrencies();
                    bcbCurrencies.forEach(bcbCurrency -> {
                        Currency currency = new Currency();
                        currency.setCode(bcbCurrency.getCode());
                        currency.setName(bcbCurrency.getName());
                        currency.setAllowChange(false);
                        List<String> colors = ColorUtils.generateRandomColors(1, ignoreColors);
                        ignoreColors.add(colors.get(0));
                        currency.setColor(colors.get(0));
                        currencyService.save(currency);
                    });
                }

                Optional<Currency> currencyOpt = currencyService.findByCode("USD");
                Date dayBefore = Utils.getDayBefore();
                Optional<History> historyOpt = historyService.findByDateEqualsAndCode(dayBefore, "USD");
                while (!historyOpt.isPresent()) {
                    bcbApi.updatePeriodValues(dayBefore, dayBefore);
                    historyOpt = historyService.findByDateEqualsAndCode(dayBefore, "USD");
                    dayBefore = Utils.getDayBefore(dayBefore);
                }

                if (currencyOpt.isPresent()) {
                    Currency currency = currencyOpt.get();
                    History history = historyOpt.get();

                    operation.setCurrency(currency);

                    Double buyValue = history.getBuy();
                    Integer amount = Utils.generateRandomNumber(1, 500);
                    Double totalValue = buyValue * amount;

                    operation.setUnitValue(buyValue);
                    operation.setAmount(new Double(amount));
                    operation.setTotal(totalValue);

                    List<Exchange> exchanges = exchangeService.findByUserId(user.getId());
                    List<Integer> ids = new ArrayList<>();
                    for (Exchange exchange : exchanges) {
                        ids.add(exchange.getId().intValue());
                    }
                    Integer sortNumber = Utils.sortNumber(ids);
                    Optional<Exchange> exchange = exchangeService.findById(new Long(sortNumber));
                    operation.setExchange(exchange.get());

                    operationRepository.save(operation);

                    count++;
                }
            } catch (Exception e) {
                failCount++;
            }
        }
    }


}
