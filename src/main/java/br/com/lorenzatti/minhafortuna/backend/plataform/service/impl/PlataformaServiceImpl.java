package br.com.lorenzatti.minhafortuna.backend.plataform.service.impl;

import br.com.lorenzatti.minhafortuna.backend.plataform.model.Plataform;
import br.com.lorenzatti.minhafortuna.backend.plataform.repository.PlataformRepository;
import br.com.lorenzatti.minhafortuna.backend.plataform.service.PlataformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlataformaServiceImpl implements PlataformService {

    @Autowired
    private PlataformRepository plataformRepository;

    @Override
    public Optional<Plataform> getPlataformById(Long plataformId) {
        return Optional.ofNullable(plataformRepository.getById(plataformId));
    }

    @Override
    public Plataform save(Plataform plataforma) {
        return plataformRepository.save(plataforma);
    }

    @Override
    public List<Plataform> plataformas() {
        return plataformRepository.findAll();
    }
}
