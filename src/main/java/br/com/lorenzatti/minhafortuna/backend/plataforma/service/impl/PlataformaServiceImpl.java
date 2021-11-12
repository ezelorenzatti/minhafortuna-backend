package br.com.lorenzatti.minhafortuna.backend.plataforma.service.impl;

import br.com.lorenzatti.minhafortuna.backend.plataforma.model.Plataforma;
import br.com.lorenzatti.minhafortuna.backend.plataforma.repository.PlataformaRepository;
import br.com.lorenzatti.minhafortuna.backend.plataforma.service.PlataformaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlataformaServiceImpl implements PlataformaService {

    @Autowired
    private PlataformaRepository plataformaRepository;

    @Override
    public Plataforma salvar(Plataforma plataforma) {
        return plataformaRepository.save(plataforma);
    }

    @Override
    public List<Plataforma> plataformas() {
        return plataformaRepository.findAll();
    }
}
