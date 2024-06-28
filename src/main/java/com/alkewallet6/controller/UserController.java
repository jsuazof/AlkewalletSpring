package com.alkewallet6.controller;

import com.alkewallet6.model.UserEntity;
import com.alkewallet6.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final IUserService userService;

    /*@GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable int id) {
        UserEntity userEntity = userService.findByUserId(id);
        if (userEntity != null) {
            return ResponseEntity.ok(userEntity);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/

    @PostMapping("/login")
    public ResponseEntity<UserEntity> loginUser(@RequestBody UserEntity userEntity) {
        String email = userEntity.getEmail();
        String password = userEntity.getPassword();

        UserEntity userEntityFromDB = userService.findByUser(email, password);
        if (userEntityFromDB != null) {
            return ResponseEntity.ok(userEntityFromDB);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
     public ResponseEntity<UserEntity> registerUser(@RequestBody UserEntity userEntity) {
         try {
             UserEntity newUSer = userService.saveUser(userEntity);
             return new ResponseEntity<>(newUSer,HttpStatus.CREATED);
         } catch (IllegalArgumentException e) {
             return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
         }
     }

//    @GetMapping("/login")
//    public String showLoginForm(Model model) {
//        return "/index";
//    }
//
//    @PostMapping("/login")
//    public String loginUser(@RequestParam String email,@RequestParam String password, RedirectAttributes redirectAttributes) {
//        UserEntity userEntity = userService.findByUser(email, password);
//
//        if (userEntity != null) {
//            return "redirect:/home";
//        } else {
//            redirectAttributes.addFlashAttribute("error", "Credenciales incorrectas");
//            return "redirect:/login";
//        }
//    }
//
//    @PostMapping("/register")
//    public String register(@RequestParam String name, @RequestParam String surname, @RequestParam String email, @RequestParam String password, RedirectAttributes redirectAttributes) {
//        try {
//            UserEntity newUserEntity = new UserEntity();
//            newUserEntity.setUserName(name);
//            newUserEntity.setUserSurname(surname);
//            newUserEntity.setEmail(email);
//            newUserEntity.setPassword(password);
//            userService.saveUser(newUserEntity);
//
//            return "redirect:/login";
//        } catch (IllegalArgumentException e) {
//            redirectAttributes.addFlashAttribute("error", e.getMessage());
//            return "redirect:/register";
//        }
//    }
}
