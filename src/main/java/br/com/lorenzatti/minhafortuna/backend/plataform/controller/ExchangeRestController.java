package br.com.lorenzatti.minhafortuna.backend.plataform.controller;

import br.com.lorenzatti.minhafortuna.backend.operation.service.OperationService;
import br.com.lorenzatti.minhafortuna.backend.plataform.dto.ExchangeDto;
import br.com.lorenzatti.minhafortuna.backend.plataform.model.Exchange;
import br.com.lorenzatti.minhafortuna.backend.plataform.service.ExchangeService;
import br.com.lorenzatti.minhafortuna.backend.security.JwtUser;
import br.com.lorenzatti.minhafortuna.backend.shared.response.Response;
import br.com.lorenzatti.minhafortuna.backend.user.model.User;
import br.com.lorenzatti.minhafortuna.backend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("exchange")
public class ExchangeRestController {

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private UserService userService;

    @Autowired
    private OperationService operationService;

    @PostMapping
    public ResponseEntity<Response> save(@RequestBody ExchangeDto exchangeDto, Authentication authentication) {
        Response<String> response = new Response<>();
        Optional<Authentication> authenticationOpt = Optional.ofNullable(authentication);
        if (authenticationOpt.isPresent()) {
            JwtUser loggedUser = (JwtUser) authenticationOpt.get().getPrincipal();
            Optional<User> userOpt = userService.getUserById(loggedUser.getId());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                Exchange exchange = new Exchange();

                if (Optional.ofNullable(exchangeDto.getId()).isPresent()) {
                    Optional<Exchange> exchangeOpt = exchangeService.findById(exchangeDto.getId());
                    if (exchangeOpt.isPresent()) {
                        exchange = exchangeOpt.get();
                        if (!user.getId().equals(exchange.getUser().getId())) {
                            response.setError("Operação não autorizada!");
                            return ResponseEntity.badRequest().body(response);
                        }
                    }
                } else {
                    exchange.setUser(userOpt.get());
                }
                exchange.setName(exchangeDto.getName());
                exchange.setUrl(exchangeDto.getUrl());

                exchangeService.save(exchange);

                response.setData("Cadastro salvo!");
                response.setSuccess(true);
                return ResponseEntity.ok(response);
            }
        }

        response.setError("Operação não autorizada!");
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping
    public ResponseEntity<Response> exchanges(Authentication authentication) {
        Response<List<ExchangeDto>> response = new Response<>();
        Optional<Authentication> authenticationOpt = Optional.ofNullable(authentication);
        if (authenticationOpt.isPresent()) {
            JwtUser loggedUser = (JwtUser) authenticationOpt.get().getPrincipal();
            Optional<User> userOpt = userService.getUserById(loggedUser.getId());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                List<Exchange> exchanges = exchangeService.findByUserId(user.getId());
                List<ExchangeDto> exchangeDtos = new ArrayList<>();
                exchanges.forEach(exchange -> {
                    ExchangeDto exchangeDto = new ExchangeDto();
                    exchangeDto.setId(exchange.getId());
                    exchangeDto.setName(exchange.getName());
                    exchangeDto.setUrl(exchange.getUrl());
                    exchangeDto.setAllowDelete(operationService.countByExchangeId(exchange.getId()) == 0);
                    exchangeDtos.add(exchangeDto);
                });
                response.setData(exchangeDtos);
                response.setSuccess(true);
                return ResponseEntity.ok(response);
            }
        }
        response.setError("Operação não autorizada!");
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Response> getById(@PathVariable Long id, Authentication authentication) {
        Response<ExchangeDto> response = new Response<>();
        JwtUser loggedUser = (JwtUser) authentication.getPrincipal();
        Optional<User> userOpt = userService.getUserById(loggedUser.getId());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Optional<Exchange> exchangeOpt = exchangeService.findById(id);
            if (exchangeOpt.isPresent()) {
                Exchange exchange = exchangeOpt.get();
                if (!exchange.getUser().getId().equals(user.getId())) {
                    response.setError("Operação não autorizada!");
                    return ResponseEntity.badRequest().body(response);
                }
                ExchangeDto exchangeDto = new ExchangeDto();
                exchangeDto.setId(exchange.getId());
                exchangeDto.setName(exchange.getName());
                exchangeDto.setUrl(exchange.getUrl());

                response.setData(exchangeDto);
                response.setSuccess(true);
                return ResponseEntity.ok(response);
            }
        }
        response.setError("Operação não autorizada!");
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping(value = "/delete/{id}")
    public ResponseEntity<Response> deleteById(@PathVariable Long id, Authentication authentication) {
        Response<String> response = new Response<>();

        JwtUser loggedUser = (JwtUser) authentication.getPrincipal();
        Optional<User> userOpt = userService.getUserById(loggedUser.getId());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Optional<Exchange> exchangeOpt = exchangeService.findById(id);
            if (exchangeOpt.isPresent()) {
                Exchange exchange = exchangeOpt.get();
                if (!exchange.getUser().getId().equals(user.getId())) {
                    response.setError("Operação não autorizada!");
                    return ResponseEntity.badRequest().body(response);
                }
                exchangeService.delete(exchange);
                return ResponseEntity.noContent().build();
            }
        }
        response.setError("Operação não autorizada!");
        return ResponseEntity.badRequest().body(response);
    }
}
