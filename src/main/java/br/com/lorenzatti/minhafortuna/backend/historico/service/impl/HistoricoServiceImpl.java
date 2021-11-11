package br.com.lorenzatti.minhafortuna.backend.historico.service.impl;

import br.com.lorenzatti.minhafortuna.backend.historico.model.Historico;
import br.com.lorenzatti.minhafortuna.backend.historico.repository.HistoricoRepository;
import br.com.lorenzatti.minhafortuna.backend.historico.service.HistoricoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class HistoricoServiceImpl implements HistoricoService {

    @Autowired
    private HistoricoRepository historicoRepository;

    @Override
    public List<Historico> getHistorico(String sigla, Date inicio, Date fim) {
        return historicoRepository.findBySiglaAndDataBetween(sigla, inicio, fim);
    }

}
