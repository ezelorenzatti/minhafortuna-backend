package br.com.lorenzatti.minhafortuna.backend.exchange.repository;

import br.com.lorenzatti.minhafortuna.backend.exchange.model.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExchangeRepository extends JpaRepository<Exchange, Long> {

    List<Exchange> findByUserId(Long userId);

    void deleteByUserId(Long id);
}
