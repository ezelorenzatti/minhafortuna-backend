package br.com.lorenzatti.minhafortuna.backend.operation.service;

import br.com.lorenzatti.minhafortuna.backend.operation.enums.EnumOperationType;
import br.com.lorenzatti.minhafortuna.backend.operation.model.Operation;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OperationService {

    Operation save(Operation operation);

    List<Operation> findAllByUserIdAndOperationTypeInAndDateBetween(Long userId, List<EnumOperationType> type, Date startDate, Date endDate);

    Optional<Operation> findById(Long id);

    void delete(Operation operation);
}
