package com.alkewallet6.controller;

import com.alkewallet6.model.DTO.ContactDTO;
import com.alkewallet6.model.entity.AccountEntity;
import com.alkewallet6.model.entity.ContactEntity;
import com.alkewallet6.model.entity.TransactionEntity;
import com.alkewallet6.model.entity.UserEntity;
import com.alkewallet6.model.enums.TransactionStatus;
import com.alkewallet6.model.enums.TransactionType;
import com.alkewallet6.service.interfaces.IAccountService;
import com.alkewallet6.service.interfaces.IContactService;
import com.alkewallet6.service.interfaces.ITransactionService;
import com.alkewallet6.service.interfaces.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class TransactionController {

    private final ITransactionService transactionService;
    private final IAccountService accountService;
    private final IUserService userService;
    private final IContactService contactService;

    public TransactionController(ITransactionService transactionService, IAccountService accountService, IUserService userService, IContactService contactService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
        this.userService = userService;
        this.contactService = contactService;
    }

    @GetMapping("/transactionList/{id}")
    public String transactionList(@PathVariable(value = "id") Long id, HttpSession session) {
        session.setAttribute("userId", id);
        return "redirect:/";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam("userId") Long userId, @RequestParam("depositAmount") double amount, RedirectAttributes redirectAttributes, Model model){
        try {
            accountService.deposit(userId, amount);
            AccountEntity account = accountService.getById(userId);
            accountService.currencyConversion(account);

            TransactionEntity transaction = new TransactionEntity();
            transaction.setType(TransactionType.DEPOSITO);
            transaction.setAmount(amount);
            transaction.setMessage("Deposito automático");
            transaction.setDate(LocalDate.now());
            UserEntity sender = userService.getById(userId);
            transaction.setUserSender(sender);
            transaction.setUserReceiver(null);
            transaction.setStatus(TransactionStatus.EXITOSO);
            transactionService.saveTransaction(transaction);

            accountService.addIncomingBalance(userId,amount);
            redirectAttributes.addFlashAttribute("message", "Depósito realizado con éxito");

            return "redirect:/home";
        } catch (IllegalArgumentException e) {
            UserEntity user = userService.getById(userId);
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", e.getMessage());
            return "deposit";
        }

    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam("userId") Long userId, @RequestParam("withdrawAmount") double amount, RedirectAttributes redirectAttributes,Model model){
        try {
            accountService.withdraw(userId,amount);
            AccountEntity account = accountService.getById(userId);
            accountService.currencyConversion(account);

            TransactionEntity transaction = new TransactionEntity();
            transaction.setType(TransactionType.RETIRO);
            transaction.setAmount(amount);
            transaction.setMessage("Retiro Voluntario");
            transaction.setDate(LocalDate.now());
            UserEntity sender = userService.getById(userId);
            transaction.setUserSender(sender);
            transaction.setUserReceiver(null);
            transaction.setStatus(TransactionStatus.EXITOSO);
            transactionService.saveTransaction(transaction);

            accountService.subtractOutgoingBalance(userId,amount);
            redirectAttributes.addFlashAttribute("message", "Retiro de Dinero realizado con éxito");
            return "redirect:/home";
        } catch (IllegalArgumentException e) {
            UserEntity user = userService.getById(userId);
            model.addAttribute("user",user);
            model.addAttribute("errorMessage", e.getMessage());
            return "withdraw";
        }
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam("userId") Long userId, @RequestParam("contactId") Long contactId, @RequestParam("transferAmount") double amount, @RequestParam("description") String description, RedirectAttributes redirectAttributes,Model model){
        try {
            ContactEntity receiverContact = contactService.getById(contactId);
            System.out.println("receiverContact " + receiverContact);
            AccountEntity senderAccount = accountService.getById(userId);
            System.out.println("senderAccount ID " + senderAccount);
            accountService.executeTransfer(userId,contactId, amount);
            accountService.currencyConversion(senderAccount);

            UserEntity receiver = null;
            Long receiverUserId = null;
            AccountEntity receiverAccount = null;
            if (receiverContact.isUser()) {
                receiver = userService.getReceiverAccount(receiverContact.getName(),receiverContact.getEmail());
                System.out.println("receiver " + receiver);
                receiverUserId = receiver.getId();
                System.out.println("receiverUserId  " + receiverUserId);
                receiverAccount = accountService.getByUserId(receiverUserId);
                System.out.println("receiverAccount ID " + receiverAccount);
                accountService.deposit(receiverUserId,amount);
                accountService.currencyConversion(receiverAccount);
                accountService.addIncomingBalance(receiverUserId,amount);
            }

            TransactionEntity transaction = new TransactionEntity();
            transaction.setType(TransactionType.TRANSFERENCIA);
            transaction.setAmount(amount);
            transaction.setMessage(description);
            transaction.setDate(LocalDate.now());
            UserEntity sender = userService.getById(userId);
            System.out.println("sender ID " + sender);
            transaction.setUserSender(sender);
            if (receiverContact.isUser() ) {
                transaction.setUserReceiver(receiver);
                accountService.subtractOutgoingBalance(userId,amount);
            } else {
                transaction.setContactReceiver(receiverContact);
                accountService.subtractOutgoingBalanceRecharge(userId,amount);
            }
            transaction.setStatus(TransactionStatus.EXITOSO);
            transactionService.saveTransaction(transaction);

            redirectAttributes.addFlashAttribute("message", "Transferencia realizada con éxito");
            return "redirect:/home";
        } catch (IllegalArgumentException e) {
            UserEntity user = userService.getById(userId);
            ContactEntity contact = contactService.getById(contactId);
            List<ContactEntity> contacts = contactService.getByUserEntityId(userId);
            ContactDTO contactDTO = new ContactDTO();
            model.addAttribute("user",user);
            model.addAttribute("contactDTO",contactDTO);
            model.addAttribute("selectedContact", contact);
            model.addAttribute("contacts",contacts);
            model.addAttribute("errorMessage", e.getMessage());
            return "transfer";
        }
    }
}
