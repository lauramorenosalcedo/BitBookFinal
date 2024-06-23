package com.example.bitbookfinal.service;

import com.example.bitbookfinal.model.Review;
import com.example.bitbookfinal.service.BookService;
import com.example.bitbookfinal.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
@Service
public class ReviewService { //This service is dedicated to offer the necesary functionalitites to the review class, like the asignation of a unique id to each object of this class.
   /* private AtomicLong nextId = new AtomicLong(1L);
    private ConcurrentHashMap<Long, Review> mapreviews = new ConcurrentHashMap<>();*/
    @Autowired
   ReviewRepository reviewRepository;
    @Autowired
   BookService bookService;

    public void save(Review review) { // This function saves a review into the map of reviews.
       /* long id = nextId.getAndIncrement();
        review.setId(id);
        mapreviews.put(id, review);
        return review;*/
        reviewRepository.save(review);
    }


    public void deleteReviewsFromUser(String username) {
            List<Review>reviewList=reviewRepository.findAllByUser_Username(username);
            for (Review review:reviewList){
                Long bookid=review.getBookIdReview();
                Long reviewid=review.getId();
                bookService.deleteReview(bookid,reviewid);
            }
        }



}