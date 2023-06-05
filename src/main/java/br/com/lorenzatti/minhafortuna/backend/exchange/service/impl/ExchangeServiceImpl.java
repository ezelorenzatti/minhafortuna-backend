package br.com.lorenzatti.minhafortuna.backend.exchange.service.impl;

import br.com.lorenzatti.minhafortuna.backend.exchange.model.Exchange;
import br.com.lorenzatti.minhafortuna.backend.exchange.repository.ExchangeRepository;
import br.com.lorenzatti.minhafortuna.backend.exchange.service.ExchangeService;
import br.com.lorenzatti.minhafortuna.backend.shared.utils.Utils;
import br.com.lorenzatti.minhafortuna.backend.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Override
    public Optional<Exchange> findById(Long exchangeId) {
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

    @Override
    public List<Exchange> findByUserId(Long userId){
        return exchangeRepository.findByUserId(userId);
    }

    @Override
    public void delete(Exchange exchange) {
        exchangeRepository.delete(exchange);
    }

    @Override
    public void deleteByUserId(Long id) {
        exchangeRepository.deleteByUserId(id);
    }

    @Override
    public void simulate(User user) {
        List<String> names = Arrays.asList("Empresa Internacional de Cambio", "Banco Inter", "Banco Bradesco", "XP Corretora", "Banco do Brasil");
        List<String> sortedNames = new ArrayList<>();
        while (sortedNames.size() < 2) {
            String name = Utils.selectRandomString(names);
            if (!sortedNames.contains(name)) {
                sortedNames.add(name);
            }
        }

        for (String name : sortedNames) {
            Exchange exchange = new Exchange();
            exchange.setName(name);
            exchange.setUser(user);
            exchangeRepository.save(exchange);
        }
    }
}
