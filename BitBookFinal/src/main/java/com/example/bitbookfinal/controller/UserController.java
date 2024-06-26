package com.example.bitbookfinal.controller;

import com.example.bitbookfinal.model.Book;
import com.example.bitbookfinal.model.Category;
import com.example.bitbookfinal.model.User;
import com.example.bitbookfinal.service.ReviewService;
import com.example.bitbookfinal.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ReviewService reviewService;


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
        if (userService.findByUsername(username).isPresent()) {
            return "error_register";
        }
        User user=new User(username,email,new BCryptPasswordEncoder().encode(password), "USER");
        userService.save(user);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        String username =request.getUserPrincipal().getName();
        Optional<User> user = userService.findByUsername(username);

        if(user.isPresent()){
            HttpSession session = request.getSession(false);  // Obtiene la sesión actual
            if (session != null) {
                session.invalidate();  // Invalida la sesión para cerrar la sesión del usuario
            }
        }
        return "index";
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
    public String deleteUser(@PathVariable long id) {
        /*Optional<User> user= userService.findById(id);
        if (user.isPresent()){
            User user1=user.get();
            String username= user1.getUsername();
            reviewService.deleteReviewsFromUser(username);
        }*/
        userService.deleteById(id);
        return "deleted_user";
    }
    @GetMapping("/myperfil")
    public String showperfil(HttpServletRequest request, Model model) {
        String username =request.getUserPrincipal().getName();
        Optional<User> user = userService.findByUsername(username);
        if(user.isPresent()){
            User user1 = user.get();
            model.addAttribute("user", user1);
            return "myperfil";
        }else {
            return "index";
        }
    }


    @GetMapping("/myperfil/editemail")
    public String userEditemail() {

        return "edit_email_form";
    }

    @PostMapping("/myperfil/editemail") // Function used to edit a single category identified by it´s id embeded in the url.
    public String editUserEmail(HttpServletRequest request, @RequestParam ("email") String email) {

        String username =request.getUserPrincipal().getName();
        Optional<User> user = userService.findByUsername(username);
        if(user.isPresent()){
            User user1 = user.get();
            userService.editEmail(user1, email);
        }
        return "redirect:/myperfil";
    }

    @GetMapping("/myperfil/deleteAccount")
    public String deleteAccount(HttpServletRequest request) {

        String username =request.getUserPrincipal().getName();
        Optional<User> user = userService.findByUsername(username);

        if(user.isPresent()){
           // reviewService.deleteReviewsFromUser(username);
            User user1 = user.get();
            userService.deleteUser(user1);
            HttpSession session = request.getSession(false);  // Obtiene la sesión actual
            if (session != null) {
                session.invalidate();  // Invalida la sesión para cerrar la sesión del usuario
            }
        }
        return "redirect:/";
    }


}


