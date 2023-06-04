package br.com.lorenzatti.minhafortuna.backend.currency.controller;

import br.com.lorenzatti.minhafortuna.backend.currency.dto.CurrencyDto;
import br.com.lorenzatti.minhafortuna.backend.currency.model.Currency;
import br.com.lorenzatti.minhafortuna.backend.currency.service.impl.CurrencyServiceImpl;
import br.com.lorenzatti.minhafortuna.backend.restcountries.service.RestCountriesApi;
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
@RequestMapping("currency")
public class CurrencyRestController {

    @Autowired
    private CurrencyServiceImpl currencyService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestCountriesApi restCountriesApi;

    @GetMapping()
    public ResponseEntity<Response> getCurrencies(Authentication authentication) {
        Response<List<CurrencyDto>> response = new Response<>();
        JwtUser loggedUser = (JwtUser) authentication.getPrincipal();
        Optional<User> userOpt = userService.getUserById(loggedUser.getId());
        if (userOpt.isPresent()) {
            response.setSuccess(true);
            List<CurrencyDto> currenciesDto = new ArrayList<>();
            List<Currency> defaultCurrencies = currencyService.getCurrenciesDefaultCurrencies();
            List<Currency> customCurrencies = currencyService.getCurrenciesByUserId(loggedUser.getId());
            defaultCurrencies.addAll(customCurrencies);
            defaultCurrencies.forEach(currency -> {
                CurrencyDto currencyDto = new CurrencyDto();
                currencyDto.setCode(currency.getCode());
                currencyDto.setName(currency.getName());
                currencyDto.setCustom(currency.getCustom());
                currenciesDto.add(currencyDto);
            });
            response.setData(currenciesDto);
            return ResponseEntity.ok(response);
        } else {
            response.setError("Operação não autorizada!");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Response> save(@RequestBody CurrencyDto currencyDto, Authentication authentication) {
        JwtUser loggedUser = (JwtUser) authentication.getPrincipal();
        Optional<User> userOpt = userService.getUserById(loggedUser.getId());
        Response<String> response = new Response<>();
        if (userOpt.isPresent()) {
            Currency currency = new Currency();
            currency.setName(currencyDto.getName());
            currency.setCode(currencyDto.getCode());
            currency.setCustom(true);
            currency.setUser(userOpt.get());
            currencyService.save(currency);

            response.setSuccess(true);
            response.setMessage("Cadastro salvo!");
            return ResponseEntity.ok(response);
        } else {
            response.setError("Operação não autorizada!");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping(value = "/fill")
    public ResponseEntity<Response> fill() {
        Response<List<String>> response = new Response<>();
        try {
            restCountriesApi.fill();
            response.setSuccess(true);
            response.setMessage("Moedas atualizadas!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.setError("Falha ao obter moedas");
            return ResponseEntity.badRequest().body(response);
        }
    }

}
