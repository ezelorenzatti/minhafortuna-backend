package br.com.lorenzatti.minhafortuna.backend.plataform.service;

import br.com.lorenzatti.minhafortuna.backend.plataform.model.Exchange;

import java.util.List;
import java.util.Optional;

public interface ExchangeService {

    Exchange save(Exchange plataforma);

    List<Exchange> exchanges();

    Optional<Exchange> getExchangeById(Long plataformId);
}
