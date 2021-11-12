package br.com.lorenzatti.minhafortuna.backend.plataforma.service;

import br.com.lorenzatti.minhafortuna.backend.plataforma.model.Plataforma;

import java.util.List;

public interface PlataformaService {

    Plataforma salvar(Plataforma plataforma);

    List<Plataforma> plataformas();
}
