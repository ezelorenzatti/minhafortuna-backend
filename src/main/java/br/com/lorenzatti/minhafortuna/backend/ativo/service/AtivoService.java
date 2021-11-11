package br.com.lorenzatti.minhafortuna.backend.ativo.service;

import br.com.lorenzatti.minhafortuna.backend.ativo.model.Ativo;

import java.util.List;

public interface AtivoService {

    List<Ativo> getAtivos();

    Ativo salvar(Ativo ativo);
}
