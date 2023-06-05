package br.com.lorenzatti.minhafortuna.backend.user.controller;

import br.com.lorenzatti.minhafortuna.backend.currency.service.CurrencyService;
import br.com.lorenzatti.minhafortuna.backend.exchange.service.ExchangeService;
import br.com.lorenzatti.minhafortuna.backend.operation.service.OperationService;
import br.com.lorenzatti.minhafortuna.backend.security.JwtUser;
import br.com.lorenzatti.minhafortuna.backend.shared.response.Response;
import br.com.lorenzatti.minhafortuna.backend.user.dto.UserDto;
import br.com.lorenzatti.minhafortuna.backend.user.model.User;
import br.com.lorenzatti.minhafortuna.backend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Optional;

@RestController
@RequestMapping("user")
@Transactional
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private OperationService operationService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private ExchangeService exchangeService;

    @PostMapping
    public ResponseEntity<Response> save(@RequestBody UserDto userDto, Authentication authentication) {
        Response<String> response = new Response<>();
        JwtUser loggedUser = (JwtUser) authentication.getPrincipal();
        if (loggedUser.getId().equals(userDto.getId())) {
            User user = new User();
            if (StringUtils.hasText(userDto.getPassword()) && StringUtils.hasText(userDto.getConfirmPassword())) {
                if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
                    response.setError("Senha e Confirmação de senha devem ser iguais!");
                    return ResponseEntity.badRequest().body(response);
                } else {
                    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
                }
            } else {
                Optional<User> userOpt = userService.getUserById(userDto.getId());
                if (userOpt.isPresent()) {
                    user = userOpt.get();
                }
            }

            user.setId(userDto.getId());
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());
            user.setPhone(userDto.getPhone());

            userService.save(user);
            response.setSuccess(true);
            response.setData("Cadastro salvo!");
            return ResponseEntity.ok(response);
        } else {
            response.setError("Operação não autorizada!");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping(value = "/profile")
    public ResponseEntity<Response> getLoggedUser(Authentication authentication) {
        Response<UserDto> response = new Response<>();
        response.setSuccess(true);
        JwtUser loggedUser = (JwtUser) authentication.getPrincipal();
        Optional<User> userOpt = this.userService.findByEmail(loggedUser.getUsername());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setName(user.getName());
            userDto.setEmail(user.getEmail());
            userDto.setPhone(user.getPhone());
            response.setData(userDto);
            return ResponseEntity.ok(response);
        } else {
            response.setError("Cadastro não localizado!");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping(value = "/delete")
    public ResponseEntity<Response> getDeleteUser(Authentication authentication) {
        Response<String> response = new Response<>();
        JwtUser loggedUser = (JwtUser) authentication.getPrincipal();
        Optional<User> userOpt = this.userService.findByEmail(loggedUser.getUsername());
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            operationService.deleteByUserId(user.getId());
            currencyService.deleteByUserId(user.getId());
            exchangeService.deleteByUserId(user.getId());
            userService.delete(user);

            response.setData("Cadastro removido!");
            response.setSuccess(true);
            return ResponseEntity.ok(response);
        } else {
            response.setError("Cadastro não localizado!");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
