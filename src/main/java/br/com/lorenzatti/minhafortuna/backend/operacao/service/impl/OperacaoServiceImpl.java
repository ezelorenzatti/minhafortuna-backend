package br.com.lorenzatti.minhafortuna.backend.operacao.service.impl;

import br.com.lorenzatti.minhafortuna.backend.operacao.model.Operacao;
import br.com.lorenzatti.minhafortuna.backend.operacao.repository.OperacaoRepository;
import br.com.lorenzatti.minhafortuna.backend.operacao.service.OperacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperacaoServiceImpl implements OperacaoService {

    @Autowired
    private OperacaoRepository operacaoRepository;

    @Override
    public Operacao salvar(Operacao operacao) {
        return operacaoRepository.save(operacao);
    }

    @Override
    public List<Operacao> operacoes() {
        return operacaoRepository.findAll();
    }
}
