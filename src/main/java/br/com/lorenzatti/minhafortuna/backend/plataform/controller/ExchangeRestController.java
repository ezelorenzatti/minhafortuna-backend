package br.com.lorenzatti.minhafortuna.backend.plataform.controller;

import br.com.lorenzatti.minhafortuna.backend.plataform.dto.ExchangeDto;
import br.com.lorenzatti.minhafortuna.backend.plataform.model.Exchange;
import br.com.lorenzatti.minhafortuna.backend.plataform.service.ExchangeService;
import br.com.lorenzatti.minhafortuna.backend.shared.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("exchange")
public class ExchangeRestController {

    @Autowired
    private ExchangeService exchangeService;

    @PostMapping
    public ResponseEntity<Response> save(@RequestBody ExchangeDto exchangeDto, BindingResult result) {
        Response<Exchange> response = new Response<>();
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Response> exchanges() {
        Response<List<ExchangeDto>> response = new Response<>();
        List<Exchange> exchanges = exchangeService.exchanges();
        List<ExchangeDto> exchangeDtos = new ArrayList<>();
        exchanges.forEach(exchange -> {
            ExchangeDto exchangeDto = new ExchangeDto();
            exchangeDto.setId(exchange.getId());
            exchangeDto.setName(exchange.getName());
            exchangeDto.setUrl(exchange.getUrl());
            exchangeDtos.add(exchangeDto);
        });
        response.setData(exchangeDtos);
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }
}
