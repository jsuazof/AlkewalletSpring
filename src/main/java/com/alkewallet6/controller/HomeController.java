package com.alkewallet6.controller;

import com.alkewallet6.model.DTO.ContactDTO;
import com.alkewallet6.model.entity.ContactEntity;
import com.alkewallet6.model.entity.TransactionEntity;
import com.alkewallet6.model.entity.UserEntity;
import com.alkewallet6.model.enums.TransactionType;
import com.alkewallet6.service.interfaces.IContactService;
import com.alkewallet6.service.interfaces.ITransactionService;
import com.alkewallet6.service.interfaces.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private IUserService userService;
    @Autowired
    private ITransactionService transactionService;
    @Autowired
    private IContactService contactService;

    @GetMapping("/deposit/{id}")
    public String depositView(@PathVariable Long id, Model model) {
        UserEntity user = userService.getById(id);

        if (user == null){
            return "redirect:/?error=true";
        }

        System.out.println("User ID: " + id);
        model.addAttribute("user",user);
        model.addAttribute("userId", id);
        return "deposit";
    }

    @GetMapping("/withdraw/{id}")
    public String withdrawView(@PathVariable Long id, Model model) {
        UserEntity user = userService.getById(id);
        if (user == null){
            return "redirect:/?error=true";
        }

        model.addAttribute("user",user);
        model.addAttribute("userId", id);
        return "withdraw";
    }

    @GetMapping("/transfer/{id}")
    String transferView(@PathVariable Long id, Model model, HttpSession session) {
        UserEntity user = userService.getById(id);
        if (user == null){
            return "redirect:/?error=true";
        }

        ContactDTO contactDTO = new ContactDTO();
        List<ContactEntity> contacts = contactService.getByUserEntityId(id).stream()
                .filter(ContactEntity::isActive)
                .collect(Collectors.toList());

        ContactEntity selectedContact = (ContactEntity) session.getAttribute("selectedContact");

        model.addAttribute("user",user);
        model.addAttribute("userId", user.getId());
        model.addAttribute("contactDTO", contactDTO);
        model.addAttribute("contacts", contacts);

        if (selectedContact != null) {
            model.addAttribute("selectedContact", selectedContact);
        }

        return "transfer";
    }

    @GetMapping("/transactionList")
    public String transactionView(@RequestParam(value = "transactionType", required = false) TransactionType transactionType, Model model, HttpSession session){
        Long userId = (Long) session.getAttribute("userId");
        UserEntity user = userService.getById(userId);
        if (user == null){
            return "redirect:/?error=true";
        }

        List<TransactionEntity> transactionEntityList = transactionService.getByUserEntityId(userId);
        List<TransactionEntity> filteredTransactionList;
        if (transactionType == null) {
            filteredTransactionList = transactionEntityList;
        } else {
            filteredTransactionList = transactionEntityList.stream()
                    .filter(t -> t.getType().equals(transactionType))
                    .collect(Collectors.toList());
        }

        model.addAttribute("user", user);
        model.addAttribute("filteredTransactionList", filteredTransactionList);
        return  "transaction";
    }
}
