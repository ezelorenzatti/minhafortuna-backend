package br.com.lorenzatti.minhafortuna.backend.operacao.controller;

import br.com.lorenzatti.minhafortuna.backend.ativo.model.Ativo;
import br.com.lorenzatti.minhafortuna.backend.operacao.model.Operacao;
import br.com.lorenzatti.minhafortuna.backend.operacao.service.OperacaoService;
import br.com.lorenzatti.minhafortuna.backend.shared.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("operacao")
public class OperacaoRestController {

    @Autowired
    private OperacaoService operacaoService;

    private ResponseEntity<Response> salvar(@RequestBody Operacao operacao, BindingResult result) {
        Response<Operacao> response = new Response<>();
        response.setSucesso(true);
        response.setData(operacaoService.salvar(operacao));
        return ResponseEntity.ok(response);
    }
}
