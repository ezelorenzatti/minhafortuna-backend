package br.com.lorenzatti.minhafortuna.backend.usuario.controller;

import br.com.lorenzatti.minhafortuna.backend.shared.response.Response;
import br.com.lorenzatti.minhafortuna.backend.usuario.model.Usuario;
import br.com.lorenzatti.minhafortuna.backend.usuario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("usuario")
public class UsuarioRestController {


    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Response> salvar(@RequestBody Usuario usuario, BindingResult result) {
        Response<Usuario> response = new Response<>();
        response.setSucesso(true);
        usuario = usuarioService.salvar(usuario);
        response.setData(usuario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Response> getUsuarioById(@PathVariable(value = "id") Long id) {
        Response<Usuario> response = new Response<>();
        response.setSucesso(true);
        Optional<Usuario> usuario = usuarioService.getUsuarioById(id);
        response.setData(usuario.get());
        return ResponseEntity.ok(response);
    }
}
