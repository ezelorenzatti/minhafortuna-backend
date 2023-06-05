package br.com.lorenzatti.minhafortuna.backend.plataform.repository;

import br.com.lorenzatti.minhafortuna.backend.plataform.model.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExchangeRepository extends JpaRepository<Exchange, Long> {

    List<Exchange> findByUserId(Long userId);

}
