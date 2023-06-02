package br.com.lorenzatti.minhafortuna.backend.security.services;

import br.com.lorenzatti.minhafortuna.backend.security.JwtUserFactory;
import br.com.lorenzatti.minhafortuna.backend.usuario.model.Usuario;
import br.com.lorenzatti.minhafortuna.backend.usuario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Qualifier("jwtUserDetailsServiceImpl")
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuaro = usuarioService.findByEmail(username);
        if (usuaro.isPresent()) {
            return JwtUserFactory.create(usuaro.get());
        }
        throw new UsernameNotFoundException("Email não encontrado.");
    }
}
