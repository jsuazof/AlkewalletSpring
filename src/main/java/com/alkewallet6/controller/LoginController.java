package com.alkewallet6.controller;

import com.alkewallet6.model.DTO.LoginDTO;
import com.alkewallet6.model.entity.AccountEntity;
import com.alkewallet6.model.entity.UserEntity;
import com.alkewallet6.service.interfaces.IAccountService;
import com.alkewallet6.service.interfaces.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController implements ErrorController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IAccountService accountService;

    @PostMapping("/login")
    public String login(@ModelAttribute("loginDTO") LoginDTO loginDTO, Model model, HttpSession session){
        try {
            UserEntity user = userService.loginUser(loginDTO.getUsernameLogin(), loginDTO.getPasswordLogin());
            if (user != null){
                session.setAttribute("userId",user.getId());
                System.out.println("Usuario autenticado. ID de usuario: " + user.getId());
                return "redirect:/home";
            }else{
                model.addAttribute("message","Usuario o contraseña incorrecta");
                return "redirect:/?error=true";
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("message","INGRESO INVALIDADO");
            return "redirect:/login";
        }
    }

    @GetMapping("/home")
    public String homeView(Model model,HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        System.out.println("Id del usuario en sesión: " + userId);
        if (userId == null) {
            System.out.println("Sesión no encontrada. Redirigiendo al inicio.");
            return "redirect:/?error=true";
        }
        try {
            UserEntity user = userService.getById(userId);
            AccountEntity account = accountService.getByUserId(userId);
            System.out.println("Usuario encontrado en sesión. ID de usuario: " + user.getId());
            model.addAttribute("user", user);
            model.addAttribute("account", account);
            return "home";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Usuario no encontrado.");
            return "redirect:/?error=true";
        }
    }

    @GetMapping("/access")
    public String access(HttpSession session) {
        UserEntity user = userService.getById(Long.parseLong(session.getAttribute("userId").toString()));

        if (user != null) {
            session.setAttribute("userId", user.getId());
            return "redirect:/home";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("userId");
        System.out.println("Sesión de usuario cerrada. Redirigiendo al inicio.");
        return "redirect:/";
    }

    @GetMapping("/error")
    public String handleError() {

        return "error_page";

    }
}
