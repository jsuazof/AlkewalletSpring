
package com.alkewallet6.service;


import com.alkewallet6.model.entity.AccountEntity;
import com.alkewallet6.model.entity.ContactEntity;
import com.alkewallet6.repository.AccountRepository;
import com.alkewallet6.repository.ContactRepository;
import com.alkewallet6.service.interfaces.IAccountService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ContactRepository contactRepository;


    private static final double usdValue = 908.79;
    private static final double recharge = 300.00;

    @Override
    public void createAccount(AccountEntity account) {
        account.setBalance(5000.00);
        accountRepository.save(account);
    }

    @Override
    public void updateAccount(AccountEntity account){
        accountRepository.save(account);
    }

    @Override
    public AccountEntity getById(Long id) {
        Optional<AccountEntity> optional = accountRepository.findById(id);
        AccountEntity account;

        if (optional.isPresent()){
            account = optional.get();
        }else{
            throw new RuntimeException("Usuario no es encontrado: " + id);
        }
        return account;
    }

    @Override
    public AccountEntity getByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("El ID del usuario no debe ser nulo");
        }
        return accountRepository.findByUserEntityId(userId).orElseThrow(
                ()-> new RuntimeException("Cuenta no encontrada para el ID de usuarios" + userId));
    }

    @Override
    @Transactional
    public void currencyConversion(AccountEntity account){
        double newAmount = account.getBalance() / usdValue;
        account.setForeignBalance(newAmount);
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void deposit(Long id, double amount){
        if (amount > 0){
            AccountEntity account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Cuenta no encontrada: " + id));
            account.setBalance(amount + account.getBalance());
            accountRepository.save(account);
        } else {
            throw new IllegalArgumentException("El monto a depositar debe ser mayor a 0. Intente de nuevo");
        }
    }

    @Override
    @Transactional
    public void withdraw(Long id, double amount) {
        AccountEntity account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Cuenta no encontrada: " + id));
        double currentAmount = account.getBalance();
        double newAmount;

        if (amount > 0 && amount <= currentAmount) {
            newAmount = currentAmount - amount;
            account.setBalance(newAmount);
            accountRepository.save(account);
        } else {
            throw new IllegalArgumentException("El monto a retirar debe ser menor a su saldo actual $" + (int)currentAmount);
        }
    }

    @Override
    @Transactional
    public void executeTransfer(Long id, Long contactId, double amount) {
        AccountEntity account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Cuenta no encontrada: " + id));
        double currentAmount = account.getBalance();

        ContactEntity contact = contactRepository.findById(contactId).orElseThrow(() -> new RuntimeException("Contacto no encontrado: " + id));
        System.out.println("Contact isUser: " + contact.isUser());
        if (amount > 0 && amount <= currentAmount) {
            double newAmount;
            if(contact.isUser()){
                newAmount = currentAmount - amount;
            }
            else {
                newAmount = currentAmount - (amount + recharge);
            }
            account.setBalance(newAmount);
            accountRepository.save(account);
        } else {
            throw new IllegalArgumentException("El monto a retirar debe ser menor a su saldo actual $" + (int)currentAmount);
        }
    }

    @Override
    @Transactional
    public void addIncomingBalance(Long id, double amount) {
        AccountEntity account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Cuenta no encontrada: " + id));
        account.setTotalIncomingBalance(account.getTotalIncomingBalance() + amount);
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void subtractOutgoingBalance(Long id, double amount) {
        AccountEntity account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Cuenta no encontrada: " + id));
        account.setTotalOutgoingBalance(account.getTotalOutgoingBalance() + amount);
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void subtractOutgoingBalanceRecharge(Long id, double amount) {
        AccountEntity account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Cuenta no encontrada: " + id));
        account.setTotalOutgoingBalance(account.getTotalOutgoingBalance() + amount + recharge);
        accountRepository.save(account);
    }
}
