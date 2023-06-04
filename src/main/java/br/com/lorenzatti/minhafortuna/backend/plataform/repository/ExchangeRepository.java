package br.com.lorenzatti.minhafortuna.backend.plataform.repository;

import br.com.lorenzatti.minhafortuna.backend.plataform.model.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRepository extends JpaRepository<Exchange, Long> {

}
