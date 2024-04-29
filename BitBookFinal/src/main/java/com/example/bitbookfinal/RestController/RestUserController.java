package com.example.bitbookfinal.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import com.example.bitbookfinal.model.User;
import com.example.bitbookfinal.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RestController
@RequestMapping("/api/users")
public class RestUserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody User userRequest) {
        // Crear una nueva instancia de User con la información proporcionada
        User user = new User(
                userRequest.getUsername(),
                userRequest.getEmail(),
                new BCryptPasswordEncoder().encode(userRequest.getPassword()),
                "USER" // O cualquier rol que necesites
        );

        // Guardar el nuevo usuario
        userService.save(user);

        // Retornar una respuesta con código 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/myperfil")
    public ResponseEntity<User> getMyProfile(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        Optional<User> user = userService.findByUsername(username);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/myperfil/email")
    public ResponseEntity<Void> updateEmail(HttpServletRequest request, @RequestParam("email") String email) {
        String username = request.getUserPrincipal().getName();
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            User currentUser = user.get();
            userService.editEmail(currentUser, email);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}