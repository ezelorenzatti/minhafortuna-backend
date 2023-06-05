package br.com.lorenzatti.minhafortuna.backend.security.controllers;

import br.com.lorenzatti.minhafortuna.backend.operation.service.OperationService;
import br.com.lorenzatti.minhafortuna.backend.exchange.service.ExchangeService;
import br.com.lorenzatti.minhafortuna.backend.security.JwtUser;
import br.com.lorenzatti.minhafortuna.backend.security.JwtUserFactory;
import br.com.lorenzatti.minhafortuna.backend.security.dto.JwtAuthenticationDto;
import br.com.lorenzatti.minhafortuna.backend.security.dto.SignUpDto;
import br.com.lorenzatti.minhafortuna.backend.security.dto.TokenDto;
import br.com.lorenzatti.minhafortuna.backend.security.utils.JwtTokenUtil;
import br.com.lorenzatti.minhafortuna.backend.shared.response.Response;
import br.com.lorenzatti.minhafortuna.backend.shared.utils.Utils;
import br.com.lorenzatti.minhafortuna.backend.user.model.User;
import br.com.lorenzatti.minhafortuna.backend.user.service.UserService;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
    private UserService userService;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private OperationService operationService;

    @PostMapping(value = "/signin")
    public ResponseEntity<Response<TokenDto>> signIn(@RequestBody JwtAuthenticationDto authenticationDto) throws AuthenticationException {
        Response<TokenDto> response = new Response<TokenDto>();

        Optional<User> userOpt = userService.findByEmail(authenticationDto.getEmail());
        User user = null;
        JwtUser userDetails = null;
        if (userOpt.isPresent()) {
            user = userOpt.get();
            userDetails = JwtUserFactory.create(user);
        } else {
            response.setError("Usuário não encontrado, acesso não permitido");
            return ResponseEntity.badRequest().body(response);
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationDto.getEmail(), authenticationDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        if (authenticationDto.getSimulateData()) {
            try {
                exchangeService.simulate(user);
                Date startDate = Utils.toDate("2023-01-01");
                Date endDate = new Date();
                operationService.simulate(startDate, endDate, 100, user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String token = jwtTokenUtil.getToken(userDetails, user);
        response.setData(new TokenDto(token));

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<Response<TokenDto>> signUp(@RequestBody SignUpDto signUpDto) {
        Optional<User> userOpt = userService.findByEmail(signUpDto.getEmail());
        Response<TokenDto> response = new Response<TokenDto>();
        if (userOpt.isPresent()) {
            response.setError("Não foi possível cadastrar, email já utilizado!");
            return ResponseEntity.badRequest().body(response);
        } else {
            if (!signUpDto.getPassword().equals(signUpDto.getConfirmPassword())) {
                response.setError("Não foi possível cadastrar, senha e confirmação de senha inválidas!");
                return ResponseEntity.badRequest().body(response);
            }

            User user = new User();
            user.setName(signUpDto.getName());
            user.setEmail(signUpDto.getEmail());
            user.setPhone(signUpDto.getPhone());
            user.setCreateDate(new Date());
            user.setUpdateDate(new Date());
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

            userService.save(user);

            JwtUser userDetails = JwtUserFactory.create(user);

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    signUpDto.getEmail(), signUpDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenUtil.getToken(userDetails, user);
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
        } else if (!jwtTokenUtil.validateToken(token.get())) {
            response.setError("Token inválido ou expirado.");
            return ResponseEntity.badRequest().body(response);
        }

        String refreshedToken = jwtTokenUtil.refreshToken(token.get());
        response.setData(new TokenDto(refreshedToken));
        return ResponseEntity.ok(response);
    }

}
