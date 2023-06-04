package br.com.lorenzatti.minhafortuna.backend.history.service;

import br.com.lorenzatti.minhafortuna.backend.history.model.History;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface HistoryService {

    List<History> getHistory(String code, Date start, Date end);

    History findLatestByCode(String code);

    Optional<History> findByDateEqualsAndCode(Date date, String code);
}
