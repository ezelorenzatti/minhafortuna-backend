package br.com.lorenzatti.minhafortuna.backend.usuario.repository;

import br.com.lorenzatti.minhafortuna.backend.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
