package br.com.lorenzatti.minhafortuna.backend.ativo.service.impl;

import br.com.lorenzatti.minhafortuna.backend.ativo.model.Ativo;
import br.com.lorenzatti.minhafortuna.backend.ativo.repository.AtivoRepository;
import br.com.lorenzatti.minhafortuna.backend.ativo.service.AtivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AtivoServiceImpl implements AtivoService {

    @Autowired
    private AtivoRepository ativoRepository;

    @Override
    public List<Ativo> getAtivos() {
        List<Ativo> ativos = ativoRepository.findAll();
        return ativos;
    }

    @Override
    public Ativo salvar(Ativo ativo) {
        return ativoRepository.save(ativo);
    }
}
