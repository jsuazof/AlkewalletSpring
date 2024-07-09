package com.alkewallet6.controller;

import com.alkewallet6.model.DTO.ContactDTO;
import com.alkewallet6.model.entity.ContactEntity;
import com.alkewallet6.service.interfaces.IContactService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactController {

    @Autowired
    private IContactService contactService;

    @PostMapping("/registerContact")
    public String createContact(@ModelAttribute("contactDTO")ContactDTO contactDTO, @RequestParam("userId") Long userId,RedirectAttributes redirectAttributes){
        try{
            contactService.save(contactDTO, userId);
            redirectAttributes.addFlashAttribute("message","Contacto Registrado");
        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("messageContact",e.getMessage());
        }
        redirectAttributes.addAttribute("id",userId);
        return "redirect:/transfer/{id}";
    }

    @PostMapping("/selectContact/{userId}")
    public String manageContact(@PathVariable Long userId, @RequestParam(value = "selectedContactId", required = false) Long selectedContactId, @RequestParam(value = "deleteContact", required = false) String deleteContact, RedirectAttributes redirectAttributes,HttpSession session) {
        if ("delete".equals(deleteContact)) {
            if (selectedContactId != null) {
                contactService.deleteContact(selectedContactId);
                redirectAttributes.addFlashAttribute("message", "Contacto eliminado correctamente");
            } else {
                redirectAttributes.addFlashAttribute("messageContact", "Debe seleccionar un contacto para eliminar");
            }
        } else {
            ContactEntity selectedContact = contactService.getById(selectedContactId);
            session.setAttribute("selectedContact", selectedContact);
            boolean needsRecharge = !selectedContact.isUser();
            redirectAttributes.addFlashAttribute("contactMessage",needsRecharge?"Se hará un recargo de $300 pesos por transferencia a este contacto.":"Pertenece a la familia Alke-Wallet");
        }
        redirectAttributes.addAttribute("id", userId);
        return "redirect:/transfer/{id}";
    }
}
