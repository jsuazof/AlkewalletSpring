package com.alkewallet6.service;

import com.alkewallet6.model.Account;

import java.util.Optional;

public interface IAccountService {

    Optional<Account> findByUserId(int userId);
    void deposit(int userId,double amount);
    void withdraw(int userId,double amount);
    void transfer(int senderUserid,int receiverUserId,double amount);
}
