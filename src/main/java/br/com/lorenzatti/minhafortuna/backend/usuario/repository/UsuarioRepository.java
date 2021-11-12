package br.com.lorenzatti.minhafortuna.backend.usuario.repository;

import br.com.lorenzatti.minhafortuna.backend.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByLogin(String login);

    Optional<Usuario> findById(Long id);
}
