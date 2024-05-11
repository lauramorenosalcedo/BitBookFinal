package com.example.bitbookfinal.RestController;

import com.example.bitbookfinal.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
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
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User userRequest) {
        if (userService.findByUsername(userRequest.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El usuario ya existe");
        }

        User user = new User(
                userRequest.getUsername(),
                userRequest.getEmail(),
                new BCryptPasswordEncoder().encode(userRequest.getPassword()),
                "USER" // O cualquier rol que necesites
        );
        userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        Optional<User> user= userService.findById(id);
        if (user.isPresent()){
            User user1=user.get();
            String username= user1.getUsername();
            reviewService.deleteReviewsFromUser(username);
        }
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/myperfil")
    public ResponseEntity<User> getMyProfile(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        Optional<User> user = userService.findByUsername(username);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /*@PutMapping("/myperfil/email")
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
    }*/
    @PutMapping("/myperfil/email")
    public ResponseEntity<Void> updateEmail(HttpServletRequest request, @RequestBody User userrequest) {
        String email = userrequest.getEmail();
        if (email == null) {
            return ResponseEntity.badRequest().build();
        }

        String username = request.getUserPrincipal().getName();
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            User currentUser = user.get();
            currentUser.setEmail(email);
            userService.save(currentUser);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/myperfil/deleteAccount")
    public ResponseEntity<?> deleteAccount(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        Optional<User> userOptional = userService.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            reviewService.deleteReviewsFromUser(username);
            userService.deleteUser(user);
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}