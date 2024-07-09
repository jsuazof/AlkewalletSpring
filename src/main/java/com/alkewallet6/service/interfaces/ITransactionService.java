package com.alkewallet6.service.interfaces;

import com.alkewallet6.model.entity.TransactionEntity;

import java.util.List;

public interface ITransactionService {

    List<TransactionEntity> getAllTransaction();
    List<TransactionEntity> getByUserEntityId(Long userId);
    void saveTransaction(TransactionEntity transaction);
    TransactionEntity getById(Long id);
}
