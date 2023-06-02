package br.com.lorenzatti.minhafortuna.backend.usuario.controller;

import br.com.lorenzatti.minhafortuna.backend.security.JwtUser;
import br.com.lorenzatti.minhafortuna.backend.shared.response.Response;
import br.com.lorenzatti.minhafortuna.backend.usuario.dto.UsuarioDto;
import br.com.lorenzatti.minhafortuna.backend.usuario.model.Usuario;
import br.com.lorenzatti.minhafortuna.backend.usuario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("usuario")
public class UsuarioRestController {


    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Response> salvar(@RequestBody UsuarioDto usuarioDto, Authentication authentication) {
        Response<String> response = new Response<>();

        JwtUser usuarioLogado = (JwtUser) authentication.getPrincipal();
        if (usuarioLogado.getId().equals(usuarioDto.getId())) {
            Usuario usuario = new Usuario();
            if (StringUtils.hasText(usuarioDto.getSenha()) && StringUtils.hasText(usuarioDto.getConfirmarSenha())) {
                if (!usuarioDto.getSenha().equals(usuarioDto.getConfirmarSenha())) {
                    response.setError("Senha e Confirmação de senha devem ser iguais");
                    return ResponseEntity.badRequest().body(response);
                } else {
                    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    usuario.setSenha(passwordEncoder.encode(usuarioDto.getSenha()));
                }
            }
            usuario.setId(usuarioDto.getId());
            usuario.setNome(usuarioDto.getNome());
            usuario.setEmail(usuarioDto.getEmail());
            usuario.setFone(usuarioDto.getFone());

            usuarioService.salvar(usuario);
            response.setSucesso(true);
            response.setData("Cadastro salvo !");
            return ResponseEntity.ok(response);
        } else {
            response.setError("Operação não autorizada");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping(value = "/profile")
    public ResponseEntity<Response> getUsuarioLogado(Authentication authentication) {
        Response<UsuarioDto> response = new Response<>();
        response.setSucesso(true);
        JwtUser usuarioLogado = (JwtUser) authentication.getPrincipal();
        Optional<Usuario> usuarioOpt = this.usuarioService.findByEmail(usuarioLogado.getUsername());
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            UsuarioDto usuarioDto = new UsuarioDto();
            usuarioDto.setId(usuario.getId());
            usuarioDto.setNome(usuario.getNome());
            usuarioDto.setEmail(usuario.getEmail());
            usuarioDto.setFone(usuario.getFone());
            response.setData(usuarioDto);
            return ResponseEntity.ok(response);
        } else {
            response.setError("Usuário não localizado");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
