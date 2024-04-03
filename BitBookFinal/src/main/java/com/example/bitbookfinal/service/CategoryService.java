package com.example.bitbookfinal.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.example.bitbookfinal.model.Book;
import com.example.bitbookfinal.model.Category;
import com.example.bitbookfinal.repository.BookRepository;
import com.example.bitbookfinal.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class CategoryService { //This service is dedicated to offer the necesary functionalitites to the category class.

    @Autowired
    private CategoryRepository categoryRepository;

    /*private AtomicLong nextId = new AtomicLong(1L); // This attribute is used to assing a unique id to each object of this class.
    private ConcurrentHashMap<Long, Category> mapcategories = new ConcurrentHashMap<>();*/

    //The next three functions used to search categories, either all of them, only some, or one identified by it´s id.
    public Optional<Category> findById(long id) {
        return categoryRepository.findById(id);
        /*if(this.mapcategories.containsKey(id)) {
            return Optional.of(this.mapcategories.get(id));
        }
        return Optional.empty();*/
    }

    public List<Category> findAll() {
        return this.categoryRepository.findAll();
    }

    public List<Category> findByIds(List<Long> ids){

        List<Category> categories = categoryRepository.findAllById(ids);
        /*List<Category> categories = new ArrayList<>();
        for(long id : ids){
            categories.add(this.mapcategories.get(id));
        }*/
        return categories;
    }

    public Category save(Category category) {//Function used to save a category into the category map.
        return categoryRepository.save(category);
        /*long id = nextId.getAndIncrement();
        category.setId(id);
        mapcategories.put(id, category);
        return category;*/
    }


    public void deleteById(long id) { // Function used to delete a specific category identified by it´s id.
        Category category = this.categoryRepository.findCategoryById(id);
        //Optional category= this.mapcategories.get(id);
        List<Book>books=category.getBooks();
        for(Book book: books){
            book.deleteCategory(category);
        }
        //this.mapcategories.remove(id);
        this.categoryRepository.deleteById(id);
    }

    /*public void editById(Category category, long id){ // This function is used to retrieve the name of a category to edit it and save it again.
        this.mapcategories.get(id).setName(category.getName());
    }*/


    public void editById(Category category, long id) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));

        existingCategory.setName(category.getName());
        categoryRepository.save(existingCategory);
    }


}