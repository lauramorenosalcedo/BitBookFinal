package com.example.bitbookfinal.RestController;
import com.example.bitbookfinal.model.Book;
import com.example.bitbookfinal.model.Category;


import com.example.bitbookfinal.service.CategoryService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonView;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/categories")  //This is how the urls of this restcontroller begin

public class RestControllerCategory {
    @Autowired
    private CategoryService categoryService;

    @JsonView(Category.Basic.class)
    @GetMapping("/")
    public Collection<Category> getCategories() {
        return categoryService.findAll();
    }  //show all books

    interface CategoryDetail extends Book.Basic,  Category.Basic, Category.Books{}  // interface that contains those necessary to display all the elements of a category without a circular reference
    @JsonView(CategoryDetail.class)
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Category>> getCategory(@PathVariable long id) {  //A category is shown if it exists, to do so its id is passed

        Optional<Category> category = categoryService.findById(id);

        if (category.isPresent()) {
            return ResponseEntity.ok(category);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @JsonView(CategoryDetail.class)
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Optional<Category>> deleteCategory(@PathVariable long id) {  //A category is deleted if it exists, to do so its id is passed

        Optional<Category> category = categoryService.findById(id);  //search for the category that has that id

        if (category.isPresent()) {
            categoryService.deleteById(id);  //the category that has that id is deleted
            return ResponseEntity.ok(category);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editCategory(@RequestBody Category category, @PathVariable long id) {  //You edit a category to do this, you pass its id and the category you want to replace it with.
        categoryService.editById(category, id);
        return ResponseEntity.status(HttpStatus.OK).body("Category updated successfully");
    }

    @PostMapping("/newcategory")
    public ResponseEntity<Category> newCategory(@RequestBody Category category) {  //a new category is added
        Category newCategory = categoryService.save(category);
        return ResponseEntity.created(URI.create("/categories/" + newCategory.getId())).body(newCategory);
    }


}



