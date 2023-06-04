package br.com.lorenzatti.minhafortuna.backend.plataform.controller;

import br.com.lorenzatti.minhafortuna.backend.plataform.model.Plataform;
import br.com.lorenzatti.minhafortuna.backend.plataform.service.PlataformService;
import br.com.lorenzatti.minhafortuna.backend.shared.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("plataform")
public class PlataformRestController {

    @Autowired
    private PlataformService plataformService;

    @PostMapping
    public ResponseEntity<Response> save(@RequestBody Plataform plataformDto, BindingResult result) {
        Response<Plataform> response = new Response<>();
        response.setSuccess(true);
        response.setData(plataformService.save(plataformDto));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Response> plataformas() {
        Response<List<Plataform>> response = new Response<>();
        response.setSuccess(true);
        response.setData(plataformService.plataformas());
        return ResponseEntity.ok(response);
    }
}
