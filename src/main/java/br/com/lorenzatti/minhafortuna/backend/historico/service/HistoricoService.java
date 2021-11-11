package br.com.lorenzatti.minhafortuna.backend.historico.service;

import br.com.lorenzatti.minhafortuna.backend.historico.model.Historico;

import java.util.Date;
import java.util.List;

public interface HistoricoService {

    List<Historico> getHistorico(String sigla, Date inicio, Date fim);

}
