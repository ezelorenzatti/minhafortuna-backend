package br.com.lorenzatti.minhafortuna.backend.operacao.repository;

import br.com.lorenzatti.minhafortuna.backend.operacao.model.Operacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperacaoRepository extends JpaRepository<Operacao, Long> {

}
