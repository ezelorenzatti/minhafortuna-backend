package br.com.lorenzatti.minhafortuna.backend.plataform.service;

import br.com.lorenzatti.minhafortuna.backend.plataform.model.Plataform;

import java.util.List;
import java.util.Optional;

public interface PlataformService {

    Plataform save(Plataform plataforma);

    List<Plataform> plataformas();

    Optional<Plataform> getPlataformById(Long plataformId);
}
