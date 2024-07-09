package com.alkewallet6.service.interfaces;


import com.alkewallet6.model.entity.AccountEntity;

public interface IAccountService {

    void createAccount(AccountEntity account);
    void updateAccount(AccountEntity account);
    AccountEntity getById(Long id);
    AccountEntity getByUserId(Long userId);
    void currencyConversion(AccountEntity account);
    void deposit(Long id,double amount);
    void withdraw(Long id,double amount);
    void executeTransfer(Long id,Long contactId,double amount);
    void addIncomingBalance(Long id,double amount);
    void subtractOutgoingBalance(Long id,double amount);
    void subtractOutgoingBalanceRecharge(Long id, double amount);
}
