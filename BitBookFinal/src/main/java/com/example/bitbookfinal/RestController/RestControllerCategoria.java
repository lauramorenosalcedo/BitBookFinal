package com.example.bitbookfinal.RestController;
import com.example.bitbookfinal.model.Book;
import com.example.bitbookfinal.model.Category;
import com.example.bitbookfinal.service.BookService;

import com.example.bitbookfinal.service.CategoryService;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/categories")

public class RestControllerCategoria {
    @Autowired
    private CategoryService categoryService;
    @JsonView(Category.Basic.class)
    @GetMapping("/")
    public Collection<Category> getCategories() {
        return categoryService.findAll();
    }

    interface CategoryDetail extends Book.Basic,  Category.Basic, Category.Books{};
    @JsonView(CategoryDetail.class)
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Category>> getCategory(@PathVariable long id) {

        Optional<Category> category = categoryService.findById(id);

        if (category.isPresent()) {
            return ResponseEntity.ok(category);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @JsonView(CategoryDetail.class)
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Optional<Category>> deleteCategory(@PathVariable long id) {

        Optional<Category> category = categoryService.findById(id);

        if (category.isPresent()) {
            categoryService.deleteById(id);
            return ResponseEntity.ok(category);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
