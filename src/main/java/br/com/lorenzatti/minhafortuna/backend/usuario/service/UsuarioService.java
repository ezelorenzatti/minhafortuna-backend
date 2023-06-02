package br.com.lorenzatti.minhafortuna.backend.usuario.service;

import br.com.lorenzatti.minhafortuna.backend.usuario.model.Usuario;

import java.util.Optional;

public interface UsuarioService {

    Usuario salvar(Usuario usuario);

    Optional<Usuario> getUsuarioById(Long id);

    Optional<Usuario> findByEmail(String email);
}
