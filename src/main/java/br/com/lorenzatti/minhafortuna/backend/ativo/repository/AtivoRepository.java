package br.com.lorenzatti.minhafortuna.backend.ativo.repository;

import br.com.lorenzatti.minhafortuna.backend.ativo.model.Ativo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtivoRepository extends JpaRepository<Ativo, String> {

}
