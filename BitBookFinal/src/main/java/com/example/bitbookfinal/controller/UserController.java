package com.example.bitbookfinal.controller;

import com.example.bitbookfinal.model.Book;
import com.example.bitbookfinal.model.Category;
import com.example.bitbookfinal.model.User;
import com.example.bitbookfinal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String registerProcess(@RequestParam("username") String username, @RequestParam("password") String password){
        ArrayList<String> roles=new ArrayList<>();
       // roles.add("USER"); //Cada vez que una persona nueva se registra adquiere el rol "USER"
        User user=new User(username,new BCryptPasswordEncoder().encode(password), "USER");
        userService.save(user);
        return "redirect:/";
    }

    @GetMapping("/loginerror")
    public String loginerror() {
        return "loginerror_form";
    }

}
