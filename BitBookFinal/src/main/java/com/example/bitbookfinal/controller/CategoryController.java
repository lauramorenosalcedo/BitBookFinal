package com.example.bitbookfinal.controller;
import java.util.Optional;

import com.example.bitbookfinal.model.Category;
import com.example.bitbookfinal.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories") // This funtion passes every category to the show_categories html, to be displayed.
    public String showCategories(Model model) {

        model.addAttribute("categories", categoryService.findAll());

        return "show_categories";
    }

    @GetMapping("/categories/{id}")  // Shows a single category identified by it´s id.
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

    @GetMapping("/newcategory") //This function handles the GET method part of creating a new category. This is linked to the button that shows up on the show_categories html, and redirects to the form used to create a new category.
    public String newCategory() {

        return "newcategoryform";
    }


    @PostMapping("/newcategory") // Recieves data from the newcategoryform, and saves it into the database as a new category using the CategoryService.
    public String newBookProcess(Model model, Category category){

        Category newCategory=categoryService.save(category);

        model.addAttribute("categoryId", newCategory.getId());

        return "redirect:/categories/"+newCategory.getId();
    }

    @GetMapping("/categories/{id}/delete") // Function used to delete a single category identified by it´s id embeded in the url.
    public String deletePost(@PathVariable long id){

        categoryService.deleteById(id);

        return "deleted_category";
    }

    @GetMapping("/categories/{id}/editform") //This function handles the GET method part of editing a category. This is linked to the button that shows up on the show_category html, and redirects to the form used to edit a category.
    public String editForm(Model model, @PathVariable long id) {
        Optional<Category> category =categoryService.findById(id);
        model.addAttribute("category", category);
        return "editform";
    }

    @PostMapping("/categories/{id}/editform") // Function used to edit a single category identified by it´s id embeded in the url.
    public String editCategory(Category category, @PathVariable long id){

        categoryService.editById(category, id);

        return "redirect:/categories";
    }

}