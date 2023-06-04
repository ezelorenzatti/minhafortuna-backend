package br.com.lorenzatti.minhafortuna.backend.currency.repository;

import br.com.lorenzatti.minhafortuna.backend.currency.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, String> {

    Currency findByCode(String code);
    List<Currency> findAll();
    List<Currency> findAllByUserId(Long id);
    List<Currency> findCurrenciesByCustomIs(Boolean isCustom);
}
