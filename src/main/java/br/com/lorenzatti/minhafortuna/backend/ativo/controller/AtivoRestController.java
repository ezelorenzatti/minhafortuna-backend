package br.com.lorenzatti.minhafortuna.backend.ativo.controller;

import br.com.lorenzatti.minhafortuna.backend.ativo.model.Ativo;
import br.com.lorenzatti.minhafortuna.backend.ativo.service.impl.AtivoServiceImpl;
import br.com.lorenzatti.minhafortuna.backend.shared.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("ativo")
public class AtivoRestController {

    @Autowired
    private AtivoServiceImpl ativoService;

    @GetMapping()
    public ResponseEntity<Response> getAtivos() {
        Response<List<Ativo>> response = new Response<>();
        response.setSucesso(true);
        response.setData(ativoService.getAtivos());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Response> salvar(@RequestBody Ativo ativo, BindingResult result) {
        Response<Ativo> response = new Response<>();
        response.setSucesso(true);
        response.setData(ativoService.salvar(ativo));
        return ResponseEntity.ok(response);
    }

}
