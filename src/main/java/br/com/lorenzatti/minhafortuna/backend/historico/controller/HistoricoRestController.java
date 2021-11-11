package br.com.lorenzatti.minhafortuna.backend.historico.controller;

import br.com.lorenzatti.minhafortuna.backend.historico.model.Historico;
import br.com.lorenzatti.minhafortuna.backend.historico.service.HistoricoService;
import br.com.lorenzatti.minhafortuna.backend.shared.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("historico")
public class HistoricoRestController {

    @Autowired
    private HistoricoService historicoService;

    @GetMapping
    public ResponseEntity<Response> historico(@RequestParam(name = "sigla") String sigla, @RequestParam(name = "inicio") String inicio, @RequestParam(name = "fim") String fim) {
        Response<List<Historico>> response = new Response<>();
        response.setSucesso(true);
        Date dataInicio = null;
        Date dataFim = null;
        List<Historico> historico = historicoService.getHistorico(sigla, dataInicio, dataFim);
        response.setData(historico);
        return ResponseEntity.ok(response);
    }

}
