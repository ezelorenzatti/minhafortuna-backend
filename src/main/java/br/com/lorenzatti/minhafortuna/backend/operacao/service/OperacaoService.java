package br.com.lorenzatti.minhafortuna.backend.operacao.service;

import br.com.lorenzatti.minhafortuna.backend.operacao.model.Operacao;

import java.util.List;

public interface OperacaoService {

    Operacao salvar(Operacao operacao);

    List<Operacao> operacoes();
}
