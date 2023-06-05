package br.com.lorenzatti.minhafortuna.backend.operation.repository;

import br.com.lorenzatti.minhafortuna.backend.operation.enums.EnumOperationType;
import br.com.lorenzatti.minhafortuna.backend.operation.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {

    List<Operation> findAllByUserIdAndOperationTypeInAndDateBetweenOrderByDateDesc(Long userId, List<EnumOperationType> type, Date startDate, Date endDate);

    Integer countByExchangeId(Long id);

    Integer countByCurrencyCode(String code);
}
