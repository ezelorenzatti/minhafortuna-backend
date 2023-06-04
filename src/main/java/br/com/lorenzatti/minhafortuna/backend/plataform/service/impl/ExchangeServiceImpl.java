package br.com.lorenzatti.minhafortuna.backend.plataform.service.impl;

import br.com.lorenzatti.minhafortuna.backend.plataform.model.Exchange;
import br.com.lorenzatti.minhafortuna.backend.plataform.repository.ExchangeRepository;
import br.com.lorenzatti.minhafortuna.backend.plataform.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Override
    public Optional<Exchange> getExchangeById(Long exchangeId) {
        return Optional.ofNullable(exchangeRepository.getById(exchangeId));
    }

    @Override
    public Exchange save(Exchange exchange) {
        return exchangeRepository.save(exchange);
    }

    @Override
    public List<Exchange> exchanges() {
        return exchangeRepository.findAll();
    }
}
