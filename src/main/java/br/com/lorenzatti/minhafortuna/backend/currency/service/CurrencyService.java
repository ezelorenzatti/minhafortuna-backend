package br.com.lorenzatti.minhafortuna.backend.currency.service;

import br.com.lorenzatti.minhafortuna.backend.currency.model.Currency;

import java.util.List;
import java.util.Optional;

public interface CurrencyService {

    Currency save(Currency currency);

    List<Currency> getCurrenciesByUserId(Long id);

    List<Currency> getCurrenciesDefaultCurrencies();

    Optional<Currency> findByCode(String code);

    void delete(Currency currency);

    void deleteByUserId(Long id);
}
