package com.example.bitbookfinal.service;

import com.example.bitbookfinal.model.Book;
import com.example.bitbookfinal.model.Category;
import com.example.bitbookfinal.model.Review;
import com.example.bitbookfinal.repository.BookRepository;
import com.example.bitbookfinal.repository.ReviewRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
@Service

public class BookService { //This service is dedicated to offer the necesary functionalitites to the book class.

    //Creation of map tha contains the books, set with their ids.

    @Autowired
    private ImageService imageService;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ReviewRepository reviewRepository;


    @Autowired
    private CategoryService categoryService;

    //private AtomicLong nextId = new AtomicLong(1L); // This attribute is used to assing a unique id to each object of this class.
    //private ConcurrentHashMap<Long, Book> mapbooks = new ConcurrentHashMap<>();
   // private AtomicLong nextReviewId = new AtomicLong(1L);


    //The next two functions used to search books, either all of them, or one identified by it´s id.
    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }
    public boolean exist(String title) { //Function used to see if a title is already assigned to a book.
        return bookRepository.findByTitle(title);
    }


    public Book save(Book book, MultipartFile imageField){ //Function used to save a book in the book map.
        if (imageField != null && !imageField.isEmpty()){
            String path = imageService.createImage(imageField);
            book.setImage(path);
        }

        if(book.getImage() == null || book.getImage().isEmpty()) book.setImage("no-image.jpg");

       /* long id = nextId.getAndIncrement();
        book.setId(id);
        mapbooks.put(id, book);
        return book;*/
        return bookRepository.save(book);
    }

    public Book save(Book book){ //Function used to save a book in the book map, without an image.
        return bookRepository.save(book);
        /*long id = nextId.getAndIncrement();
        book.setId(id);
        mapbooks.put(id, book);
        return book;*/
    }


    public void deleteById(long id) { // This function can identify a book by its id and remove it from the map of books, and from the categories it belongs to.
        Book book = bookRepository.findBookById(id);
       // Book book= this.mapbooks.get(id);
        List<Category>categorias=book.getCategories();
        for(Category categoria: categorias){
            categoria.deleteBook(book);
        }
        bookRepository.deleteById(id);;
       // this.mapbooks.remove(id);
    }



    public void addReview(Review review, long bookid) { // Function used to add a review to a specific book.
        Collection<Book> books = mapbooks.values();
        for (Book book : books) {
            if (Objects.equals(book.getId(), bookid)) {
                long id = nextReviewId.getAndIncrement();
                review.setId(id);
                List<Review> bookReviews = book.getReviews();
                bookReviews.add(review);
                book.setReviews(bookReviews);
            }
        }
    }

    public void addReview(Review review, long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + bookId));
        review.setBook(book);
        ReviewRepository.save(review);
    }
/*
    public void deleteReviewById(long id, long reviewid) {// Function used to remove a specific review from a specific book.
        Book book = bookRepository.findBookById(id);
        //Book book= this.mapbooks.get(id);
        List<Review>reviews=book.getReviews();
        for(Review review: reviews){
            if(review.getId()== reviewid){
                reviews.remove(review);
                break;
            }
        }

        book.setReviews(reviews);
        this.mapbooks.put(id, book);

    }*/

    public void deleteReviewById(long bookId, long reviewId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + bookId));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with id: " + reviewId));

        if (!book.getReviews().contains(review)) {
            throw new IllegalArgumentException("Review not associated with book");
        }

        book.deleteReview(review);
        bookRepository.save(book);
    }
}