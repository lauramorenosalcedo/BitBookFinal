package com.example.bitbookfinal.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.example.bitbookfinal.model.Book;
import com.example.bitbookfinal.model.Category;
import org.springframework.stereotype.Service;
@Service
public class CategoryService {
    private AtomicLong nextId = new AtomicLong(1L);
    private ConcurrentHashMap<Long, Category> mapcategories = new ConcurrentHashMap<>();
    public Optional<Category> findById(long id) {
        if(this.mapcategories.containsKey(id)) {
            return Optional.of(this.mapcategories.get(id));
        }
        return Optional.empty();
    }

    public List<Category> findAll() {
        return this.mapcategories.values().stream().toList();
    }

    public Category save(Category category) {
        long id = nextId.getAndIncrement();
        category.setId(id);
        mapcategories.put(id, category);
        return category;
    }
    public List<Category> findByIds(List<Long> ids){
        List<Category> categories = new ArrayList<>();
        for(long id : ids){
            categories.add(this.mapcategories.get(id));
        }
        return categories;
    }

    public void deleteById(long id) {
        Category category= this.mapcategories.get(id);
        List<Book>books=category.getBooks();
        for(Book book: books){
            book.deleteCategory(category);
        }
        this.mapcategories.remove(id);
    }

    public void editById(Category category, long id){
        this.mapcategories.get(id).setName(category.getName());
    }


}
