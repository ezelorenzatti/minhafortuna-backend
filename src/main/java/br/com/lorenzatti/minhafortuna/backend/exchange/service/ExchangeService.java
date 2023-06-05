package br.com.lorenzatti.minhafortuna.backend.exchange.service;

import br.com.lorenzatti.minhafortuna.backend.exchange.model.Exchange;
import br.com.lorenzatti.minhafortuna.backend.user.model.User;

import java.util.List;
import java.util.Optional;

public interface ExchangeService {

    Exchange save(Exchange exchange);

    List<Exchange> exchanges();

    Optional<Exchange> findById(Long exchangeId);

    List<Exchange> findByUserId(Long userId);

    void simulate(User user);

    void delete(Exchange exchange);
}
