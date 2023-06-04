package br.com.lorenzatti.minhafortuna.backend.currency.service.impl;

import br.com.lorenzatti.minhafortuna.backend.currency.model.Currency;
import br.com.lorenzatti.minhafortuna.backend.currency.repository.CurrencyRepository;
import br.com.lorenzatti.minhafortuna.backend.currency.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Override
    public Optional<Currency> getCurrenciesById(String currencyCode) {
        return Optional.ofNullable(currencyRepository.getById(currencyCode));
    }

    @Override
    public List<Currency> getCurrenciesByUserId(Long id) {
        return currencyRepository.findAllByUserId(id);
    }

    @Override
    public List<Currency> getCurrenciesDefaultCurrencies() {
        return currencyRepository.findCurrenciesByCustomIs(false);
    }

    @Override
    public Currency save(Currency currency) {
        return currencyRepository.save(currency);
    }
}
