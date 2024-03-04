package com.example.bitbookfinal.service;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.bitbookfinal.model.Book;
import com.example.bitbookfinal.model.Category;
import jakarta.annotation.PostConstruct;
@Component
public class Inicializer {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BookService bookService;

    @PostConstruct
    public void init(){
        Book quijote= new Book("Quijote", "Miguel Cervantes");
        Book campos=new Book("Campos de Castilla", "Antonio Machada");
        Book znati=new Book("Znati en la escuela", "Dbid Rey");

        Category literatura= new Category("Literatura");
        Category aventura= new Category("Aventura");
        Category ficcion= new Category("Ficcion");

        quijote.addCategory(aventura);
        quijote.addCategory(ficcion);
        aventura.getBooks().add(quijote);
        ficcion.getBooks().add(quijote);

        campos.addCategory(literatura);
        literatura.getBooks().add(campos);

        znati.addCategory(ficcion);
        ficcion.getBooks().add(znati);

        bookService.save(quijote);
        bookService.save(znati);
        bookService.save(campos);

        categoryService.save(aventura);
        categoryService.save(ficcion);
        categoryService.save(literatura);

    }
}
