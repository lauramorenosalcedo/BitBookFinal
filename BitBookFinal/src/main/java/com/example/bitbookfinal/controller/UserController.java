package com.example.bitbookfinal.controller;

import com.example.bitbookfinal.model.Book;
import com.example.bitbookfinal.model.Category;
import com.example.bitbookfinal.model.User;
import com.example.bitbookfinal.service.UserService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("/login")
    public String login() {
        return "login_form";
    }


    @GetMapping("/register")
    public String register() {
        return "register_form";
    }

    @PostMapping("/register")
    public String registerProcess(@RequestParam("username") String username,@RequestParam("email") String email, @RequestParam("password") String password){
        User user=new User(username,email,new BCryptPasswordEncoder().encode(password), "USER");
        userService.save(user);
        return "redirect:/";
    }

    @GetMapping("/loginerror")
    public String loginerror() {
        return "loginerror_form";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.findAll(); // Un método para obtener todos los usuarios
        model.addAttribute("users", users); // Añadir la lista de usuarios al modelo
        return "show_users"; // Nombre de la vista para mostrar los usuarios
    }
    @GetMapping("/users/{id}/delete")
    public String deletePost(@PathVariable long id) {

        userService.deleteById(id);

        return "deleted_user";
    }



}


