package br.com.lorenzatti.minhafortuna.backend.history.service.impl;

import br.com.lorenzatti.minhafortuna.backend.history.model.History;
import br.com.lorenzatti.minhafortuna.backend.history.repository.HistoryRepository;
import br.com.lorenzatti.minhafortuna.backend.history.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private HistoryRepository historyRespository;

    @Override
    public List<History> getHistory(String sigla, Date start, Date end) {
        return historyRespository.findByCodeAndDateBetween(sigla, start, start);
    }

    @Override
    public History findLatestByCode(String code) {
        return historyRespository.findLatestByCode(code);
    }

    @Override
    public Optional<History> findByDateEqualsAndCode(Date date, String code) {
        return Optional.ofNullable(historyRespository.findByDateEqualsAndCode(date, code));
    }

}
