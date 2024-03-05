package com.example.bitbookfinal.service;

import com.example.bitbookfinal.model.Book;
import com.example.bitbookfinal.model.Category;
import com.example.bitbookfinal.model.Review;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ReviewService {
    private AtomicLong nextId = new AtomicLong(1L);
    private ConcurrentHashMap<Long, Review> mapreviews = new ConcurrentHashMap<>();
    public Review save(Review review) {
        long id = nextId.getAndIncrement();
        review.setId(id);
        mapreviews.put(id, review);
        return review;
    }
}
