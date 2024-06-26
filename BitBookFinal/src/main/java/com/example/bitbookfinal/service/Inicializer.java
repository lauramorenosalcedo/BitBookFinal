package com.example.bitbookfinal.service;

import com.example.bitbookfinal.model.Review;
import com.example.bitbookfinal.model.User;
import com.example.bitbookfinal.repository.ReviewRepository;
import com.example.bitbookfinal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import com.example.bitbookfinal.model.Book;
import com.example.bitbookfinal.model.Category;
import jakarta.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;


import java.io.IOException;
import java.sql.SQLException;



import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collections;


@Component

public class Inicializer { //The incializer is used to create varius objects of our different entities to load before the user can intercat with the page.
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @PostConstruct
    public void init() throws SQLException, IOException {
        // Load image files
        byte[] quijoteImageBytes = loadImage("quijote.jpeg");
        byte[] camposImageBytes = loadImage("campos.jpeg");
        byte[] znatiImageBytes = loadImage("znati.jpeg");

        // Convert image bytes to Blob objects
        Blob quijoteImageBlob = createBlob(quijoteImageBytes);
        Blob camposImageBlob = createBlob(camposImageBytes);
        Blob znatiImageBlob = createBlob(znatiImageBytes);

        Category literatura= new Category("Literatura");
        Category aventura= new Category("Aventura");
        Category ficcion= new Category("Ficcion");
        categoryService.save(aventura);
        categoryService.save(ficcion);
        categoryService.save(literatura);

        // Create books and set their images
        Book quijote = new Book("Quijote", "Miguel Cervantes", 8);
        quijote.setImageFile(quijoteImageBlob);
        quijote.setFilename("quijote.pdf");
        quijote.getCategories().add(aventura);
        quijote.getCategories().add(ficcion);

        Book campos = new Book("Campos de Castilla", "Antonio Machado", 0);
        campos.setImageFile(camposImageBlob);
        campos.setFilename("camposdecastilla.pdf");
        campos.getCategories().add(literatura);

        Book znati = new Book("Znati en la escuela", "Dbid Rey", 999);
        znati.setImageFile(znatiImageBlob);
        znati.setFilename("znati.pdf");
        znati.getCategories().add(aventura);

        // Save books
        bookService.save(quijote);
        bookService.save(campos);
        bookService.save(znati);

        //Create user
        /*ArrayList<String> roles=new ArrayList<>();
        roles.add("USER");
        roles.add("ADMIN");
        User user =new User("admin",new BCryptPasswordEncoder().encode("admin"),roles);
        userService.save(user);*/
       // User user1 = new User("Paco", "paco@gmail.com", new BCryptPasswordEncoder().encode("Paco.123_pass"),
        //        "ADMIN", "USER");
       // userService.save(user1);
        userService.save(new User("Paco","paco@gmail.com", new BCryptPasswordEncoder().encode("Paco.123_pass"), "ADMIN", "USER"));
        userService.save(new User("Juan", "juan@gmail.com",new BCryptPasswordEncoder().encode("Juan.123_pass"), "USER"));
        userService.save(new User("Maria","maria@gmail.com", new BCryptPasswordEncoder().encode("Maria.123_pass"), "USER"));

       /* Review review1 = new Review("XXX", "Buen libro");
        review1.setUser(user1);
        reviewRepository.save(review1);*/
    }

        private byte[] loadImage(String imageName) {
            try {
                return new ClassPathResource("images/" + imageName).getInputStream().readAllBytes();
            } catch (IOException e) {
                throw new RuntimeException("Failed to load image: " + imageName, e);
            }
        }

        private Blob createBlob(byte[] data) {
            try {
                return new javax.sql.rowset.serial.SerialBlob(data);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create Blob", e);
            }
        }






        /*Category literatura= new Category("Literatura");
        Category aventura= new Category("Aventura");
        Category ficcion= new Category("Ficcion");
        categoryService.save(aventura);
        categoryService.save(ficcion);
        categoryService.save(literatura);

        //Create some books
        Book quijote= new Book("Quijote", "Miguel Cervantes", 8);
        quijote.setImage("quijote.jpeg");
        quijote.getCategories().add(aventura);
        quijote.getCategories().add(ficcion);

        Book campos=new Book("Campos de Castilla", "Antonio Machado", 0);
        campos.setImage("campos.jpeg");
        campos.getCategories().add(literatura);

        Book znati=new Book("Znati en la escuela", "Dbid Rey", 999);
        znati.setImage("znati.jpeg");
        znati.getCategories().add(aventura);

        /*Category literatura= new Category("Literatura");
        Category aventura= new Category("Aventura");
        Category ficcion= new Category("Ficcion");*/


        //Add the categories to the category list of the book.
        /*quijote.addCategory(aventura);
        quijote.addCategory(ficcion);
        //Add the book to the book list of the category.
        aventura.getBooks().add(quijote);
        ficcion.getBooks().add(quijote);

        campos.addCategory(literatura);
        literatura.getBooks().add(campos);

        znati.addCategory(ficcion);
        ficcion.getBooks().add(znati);*/

        //Save both the book and the categories.
       /* bookService.save(quijote,null);
        bookService.save(znati,null);
        bookService.save(campos,null);*/

        /*categoryService.save(aventura);
        categoryService.save(ficcion);
        categoryService.save(literatura);*/

    }