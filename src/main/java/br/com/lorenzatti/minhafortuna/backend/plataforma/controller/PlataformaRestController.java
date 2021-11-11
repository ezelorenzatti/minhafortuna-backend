package br.com.lorenzatti.minhafortuna.backend.plataforma.controller;

import br.com.lorenzatti.minhafortuna.backend.plataforma.model.Plataforma;
import br.com.lorenzatti.minhafortuna.backend.plataforma.service.PlataformaService;
import br.com.lorenzatti.minhafortuna.backend.shared.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("plataforma")
public class PlataformaRestController {

    @Autowired
    private PlataformaService plataformaService;

    @PostMapping
    public ResponseEntity<Response> salvar(@RequestBody Plataforma plataforma, BindingResult result) {
        Response<Plataforma> response = new Response<>();
        response.setSucesso(true);
        response.setData(plataformaService.salvar(plataforma));
        return ResponseEntity.ok(response);
    }
}
