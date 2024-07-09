package com.alkewallet6.service;

import com.alkewallet6.model.entity.TransactionEntity;
import com.alkewallet6.repository.TransacctionRepository;
import com.alkewallet6.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService implements ITransactionService {

    @Autowired
    private TransacctionRepository transactionRepository;

    @Override
    public List<TransactionEntity> getAllTransaction() {
        return transactionRepository.findAll();
    }

    @Override
    public List<TransactionEntity> getByUserEntityId(Long userId) {
        List<TransactionEntity> transactionsAsSender = transactionRepository.findByUserSenderId(userId);
        List<TransactionEntity> transactionsAsReceiver = transactionRepository.findByUserReceiverId(userId);
        List<TransactionEntity> allTransactions = new ArrayList<>();
        allTransactions.addAll(transactionsAsSender);
        allTransactions.addAll(transactionsAsReceiver);
        return allTransactions;
    }

    @Override
    public void saveTransaction(TransactionEntity transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public TransactionEntity getById(Long id) {
        Optional<TransactionEntity> optional = transactionRepository.findById(id);
        TransactionEntity transaction = null;

        if (optional.isPresent()){
            transaction = optional.get();
        }else{
            throw new RuntimeException("transacción no es encontrado: " + id);
        }
        return transaction;
    }
}
