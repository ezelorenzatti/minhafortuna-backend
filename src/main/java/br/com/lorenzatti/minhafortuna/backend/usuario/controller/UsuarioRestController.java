package br.com.lorenzatti.minhafortuna.backend.usuario.controller;

import br.com.lorenzatti.minhafortuna.backend.shared.Response;
import br.com.lorenzatti.minhafortuna.backend.usuario.model.Usuario;
import br.com.lorenzatti.minhafortuna.backend.usuario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
