package br.com.lorenzatti.minhafortuna.backend.history.repository;

import br.com.lorenzatti.minhafortuna.backend.history.model.History;
import br.com.lorenzatti.minhafortuna.backend.history.model.HistoryPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, HistoryPrimaryKey> {

    List<History> findByCodeAndDateBetween(String code, Date start, Date end);

    @Query("SELECT h FROM History h WHERE h.code = :code AND h.date = (SELECT MAX(h2.date) FROM History h2 WHERE h2.code = :code)")
    History findLatestByCode(@Param("code") String code);

    History findByDateEqualsAndCode(Date date, String code);
}
