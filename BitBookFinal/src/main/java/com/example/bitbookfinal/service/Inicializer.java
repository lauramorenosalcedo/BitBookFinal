package com.example.bitbookfinal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.bitbookfinal.model.Book;
import com.example.bitbookfinal.model.Category;
import jakarta.annotation.PostConstruct;
@Component

public class Inicializer { //The incializer is used to create varius objects of our different entities to load before the user can intercat with the page.
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BookService bookService;

    @PostConstruct
    public void init(){
        Category literatura= new Category("Literatura");
        Category aventura= new Category("Aventura");
        Category ficcion= new Category("Ficcion");
        categoryService.save(aventura);
        categoryService.save(ficcion);
        categoryService.save(literatura);

        //Create some books
        Book quijote= new Book("Quijote", "Miguel Cervantes", 8.99F);
        quijote.setImage("quijote.jpeg");
        quijote.getCategories().add(aventura);
        quijote.getCategories().add(ficcion);

        Book campos=new Book("Campos de Castilla", "Antonio Machado", 0.01F);
        campos.setImage("campos.jpeg");
        campos.getCategories().add(literatura);

        Book znati=new Book("Znati en la escuela", "Dbid Rey", 999.99F);
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
        bookService.save(quijote,null);
        bookService.save(znati,null);
        bookService.save(campos,null);

        /*categoryService.save(aventura);
        categoryService.save(ficcion);
        categoryService.save(literatura);*/

    }
}