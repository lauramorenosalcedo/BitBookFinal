package com.example.bitbookfinal.service;

import com.example.bitbookfinal.model.Category;

import java.util.concurrent.atomic.AtomicLong;

public class ReviewService {
    private AtomicLong nextId = new AtomicLong(1L);
    public Category save(Category category) {
        long id = nextId.getAndIncrement();
        category.setId(id);
        mapcategories.put(id, category);
        return category;
    }
}
