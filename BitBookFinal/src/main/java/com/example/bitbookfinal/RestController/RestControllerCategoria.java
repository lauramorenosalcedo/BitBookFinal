package com.example.bitbookfinal.RestController;
import com.example.bitbookfinal.model.Book;
import com.example.bitbookfinal.model.Category;

import com.example.bitbookfinal.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonView;

import java.net.URI;
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



    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editCategory(@RequestBody Category category, @PathVariable long id) {
        categoryService.editById(category, id);
        return ResponseEntity.status(HttpStatus.OK).body("Category updated successfully");
    }

    @PostMapping("/newcategory")
    public ResponseEntity<Category> newCategory(@RequestBody Category category) {
        Category newCategory = categoryService.save(category);
        return ResponseEntity.created(URI.create("/categories/" + newCategory.getId())).body(newCategory);
    }
}



