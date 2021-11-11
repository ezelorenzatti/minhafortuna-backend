package br.com.lorenzatti.minhafortuna.backend.historico.repository;

import br.com.lorenzatti.minhafortuna.backend.historico.model.Historico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface HistoricoRepository extends JpaRepository<Historico, Long> {

    List<Historico> findBySiglaAndDataBetween(String sigla, Date inicio, Date fim);
}
