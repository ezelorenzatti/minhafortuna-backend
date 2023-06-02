package br.com.lorenzatti.minhafortuna.backend.security.controllers;

import br.com.lorenzatti.minhafortuna.backend.security.JwtUser;
import br.com.lorenzatti.minhafortuna.backend.security.JwtUserFactory;
import br.com.lorenzatti.minhafortuna.backend.security.dto.JwtAuthenticationDto;
import br.com.lorenzatti.minhafortuna.backend.security.dto.SignUpDto;
import br.com.lorenzatti.minhafortuna.backend.security.dto.TokenDto;
import br.com.lorenzatti.minhafortuna.backend.security.utils.JwtTokenUtil;
import br.com.lorenzatti.minhafortuna.backend.shared.response.Response;
import br.com.lorenzatti.minhafortuna.backend.usuario.model.Usuario;
import br.com.lorenzatti.minhafortuna.backend.usuario.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private static final String TOKEN_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Response<TokenDto>> gerarTokenJwt(
            @RequestBody JwtAuthenticationDto authenticationDto, BindingResult result)
            throws AuthenticationException {
        Response<TokenDto> response = new Response<TokenDto>();

        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(authenticationDto.getEmail());
        Usuario usuario = null;
        JwtUser userDetails = null;
        if (usuarioOpt.isPresent()) {
            usuario = usuarioOpt.get();
            userDetails = JwtUserFactory.create(usuario);
        } else {
            response.setError("Usuário não encontrado, acesso não permitido");
            return ResponseEntity.badRequest().body(response);
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationDto.getEmail(), authenticationDto.getSenha()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenUtil.obterToken(userDetails, usuario);
        response.setData(new TokenDto(token));

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<Response<TokenDto>> signUp(@RequestBody SignUpDto signUpDto) {
        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(signUpDto.getEmail());
        Response<TokenDto> response = new Response<TokenDto>();

        if (usuarioOpt.isPresent()) {
            response.setError("Não foi possível finalizar, email já cadastrado !");
            return ResponseEntity.badRequest().body(response);
        } else {
            if (!signUpDto.getSenha().equals(signUpDto.getConfirmarSenha())) {
                response.setError("Não foi possível finalizar, senha e confirmação de senha inválidas !");
                return ResponseEntity.badRequest().body(response);
            }

            Usuario usuario = new Usuario();
            usuario.setNome(signUpDto.getNome());
            usuario.setEmail(signUpDto.getEmail());
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            usuario.setSenha(passwordEncoder.encode(signUpDto.getSenha()));

            usuarioService.salvar(usuario);

            JwtUser userDetails = JwtUserFactory.create(usuario);

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    signUpDto.getEmail(), signUpDto.getSenha()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenUtil.obterToken(userDetails, usuario);
            response.setData(new TokenDto(token));
            return ResponseEntity.ok(response);
        }
    }


    @PostMapping(value = "/refresh")
    public ResponseEntity<Response<TokenDto>> gerarRefreshTokenJwt(HttpServletRequest request) {
        log.info("Gerando refresh token JWT.");
        Response<TokenDto> response = new Response<TokenDto>();
        Optional<String> token = Optional.ofNullable(request.getHeader(TOKEN_HEADER));

        if (token.isPresent() && token.get().startsWith(BEARER_PREFIX)) {
            token = Optional.of(token.get().substring(7));
        }

        if (!token.isPresent()) {
            response.setError("Token não informado");
            return ResponseEntity.badRequest().body(response);
        } else if (!jwtTokenUtil.tokenValido(token.get())) {
            response.setError("Token inválido ou expirado.");
            return ResponseEntity.badRequest().body(response);
        }

        String refreshedToken = jwtTokenUtil.refreshToken(token.get());
        response.setData(new TokenDto(refreshedToken));
        return ResponseEntity.ok(response);
    }

}
