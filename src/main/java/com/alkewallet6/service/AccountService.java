package com.alkewallet6.service;

import com.alkewallet6.model.Account;
import com.alkewallet6.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class AccountService implements IAccountService {

    private static final double surcharge = 300.00;
    private static final double usdValue = 908.79;
    @Autowired
    private AccountRepository accountRepository;

    public Optional<Account> findByUserId(int userId){
        return accountRepository.findById(userId);
    }

    @Override
    @Transactional
    public void deposit(int userId,double amount){
        Optional<Account> optionalAccount = accountRepository.findById(userId);
        if (optionalAccount.isPresent()) {
            Account updateAccount = optionalAccount.get();
            updateAccount.setBalance(updateAccount.getBalance() + amount);
            updateAccount.setBalanceForeign((updateAccount.getBalance() + amount)/ usdValue);
            accountRepository.save(updateAccount);
        }
    }

    @Override
    @Transactional
    public void withdraw(int userId, double amount) {
        Optional<Account> optionalAccount = accountRepository.findById(userId);
        if (optionalAccount.isPresent()) {
            Account updateAccount = optionalAccount.get();
            if (updateAccount.getBalance() >= amount){
                updateAccount.setBalance(updateAccount.getBalance() - amount);
                updateAccount.setBalanceForeign((updateAccount.getBalance() - amount)/ usdValue);
                accountRepository.save(updateAccount);
            }else {
                throw new IllegalArgumentException("Saldo insuficiente en su cuenta");
            }
        }
    }

    @Override
    @Transactional
    public void transfer(int senderUserId, int receiverUserId, double amount) {
        Optional<Account> senderAccount = accountRepository.findById(senderUserId);
        Optional<Account> receiverAccount = accountRepository.findById(receiverUserId);

        if (senderAccount.isPresent()) {
            Account updateSenderAccount = senderAccount.get();
            if (receiverAccount.isPresent()) {
                Account updateReceiverAccount = receiverAccount.get();
                if (updateSenderAccount.getBalance() >= amount) {
                    updateSenderAccount.setBalance(updateSenderAccount.getBalance() - amount);
                    updateSenderAccount.setBalanceForeign((updateSenderAccount.getBalance() - amount)/ usdValue);
                    updateReceiverAccount.setBalance(updateReceiverAccount.getBalance() + amount);
                    updateReceiverAccount.setBalanceForeign((updateReceiverAccount.getBalance() + amount)/ usdValue);

                    accountRepository.save(updateSenderAccount);
                    accountRepository.save(updateReceiverAccount);
                } else {
                    throw new IllegalArgumentException("Saldo insuficiente para transferir dinero a otra cuenta");
                }
            } else {
                double newBalance = amount + surcharge;
                if (updateSenderAccount.getBalance() >= newBalance) {
                    updateSenderAccount.setBalance(updateSenderAccount.getBalance() - newBalance);
                    updateSenderAccount.setBalanceForeign(updateSenderAccount.getBalance() / usdValue);

                    accountRepository.save(updateSenderAccount);
                } else {
                    throw new IllegalArgumentException("Saldo insuficiente para transferir dinero a otra cuenta");
                }
            }
        }else {
            throw new IllegalArgumentException("Cuenta remitente no es correcta");
        }
    }
}