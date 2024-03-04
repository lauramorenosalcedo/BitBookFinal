package com.example.bitbookfinal.controller;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.example.bitbookfinal.model.Book;
import com.example.bitbookfinal.model.Category;
import com.example.bitbookfinal.service.BookService;
import com.example.bitbookfinal.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BookService bookService;

    @GetMapping("/categories")
    public String showCategories(Model model) {

        model.addAttribute("categories", categoryService.findAll());

        return "show_categories";
    }

    @GetMapping("/categories/{id}")
    public String showCategory(Model model, @PathVariable long id) {
        Optional<Category> category = categoryService.findById(id);
        if (category.isPresent()) {
            model.addAttribute("categoryName", category.get().getName()); // Añadir el nombre de la categoría al modelo
            model.addAttribute("books", category.get().getBooks()); // Añadir la lista de libros al modelo
            return "show_category";
        } else {
            return "show_categories";
        }
    }

    @GetMapping("/newcategory")
    public String newCategory() {

        return "newcategoryform";
    }

    //Save a new category in CategoryService
    @PostMapping("/newcategory")
    public String newBookProcess(Model model, Category category){


        Category newCategory = categoryService.save(category);

        //model.addAttribute("bookId", newBook.getId());

        return "redirect:/categories";
    }

}
