package br.com.lorenzatti.minhafortuna.backend.security;

import br.com.lorenzatti.minhafortuna.backend.user.model.User;

public class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(User usuario) {
        return new JwtUser(usuario.getId(), usuario.getEmail(), usuario.getPassword());
    }

}
