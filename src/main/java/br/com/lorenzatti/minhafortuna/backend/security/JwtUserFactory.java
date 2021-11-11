package br.com.lorenzatti.minhafortuna.backend.security;

import br.com.lorenzatti.minhafortuna.backend.usuario.model.Usuario;

public class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(Usuario usuario) {
        return new JwtUser(usuario.getId(), usuario.getEmail(), usuario.getSenha(), true);
    }

}
