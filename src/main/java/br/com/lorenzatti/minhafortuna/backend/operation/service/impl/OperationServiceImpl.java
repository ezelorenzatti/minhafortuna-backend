package br.com.lorenzatti.minhafortuna.backend.operation.service.impl;

import br.com.lorenzatti.minhafortuna.backend.operation.enums.EnumOperationType;
import br.com.lorenzatti.minhafortuna.backend.operation.model.Operation;
import br.com.lorenzatti.minhafortuna.backend.operation.repository.OperationRepository;
import br.com.lorenzatti.minhafortuna.backend.operation.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OperationServiceImpl implements OperationService {

    @Autowired
    private OperationRepository operationRepository;

    @Override
    public Operation save(Operation operation) {
        return operationRepository.save(operation);
    }

    @Override
    public List<Operation> findAllByUserIdAndOperationTypeInAndDateBetween(Long userId, List<EnumOperationType> type, Date startDate, Date endDate) {
        return operationRepository.findAllByUserIdAndOperationTypeInAndDateBetweenOrderByDateDesc(userId, type, startDate, endDate);
    }

    @Override
    public Optional<Operation> findById(Long id) {
        return operationRepository.findById(id);
    }

    @Override
    public void delete(Operation operation) {
        operationRepository.delete(operation);
    }

}
