package com.example.bitbookfinal.RestController;

import com.example.bitbookfinal.model.Book;
import com.example.bitbookfinal.model.Category;
import com.example.bitbookfinal.model.Review;
import com.example.bitbookfinal.service.BookService;

import com.example.bitbookfinal.service.CategoryService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/books")
public class RestControllerBook {
    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryService categoryService;
    @JsonView(Book.Basic.class)
    @GetMapping("/")   //
    public Collection<Book> getBooks() {
        return bookService.findAll();
    }

    interface BookDetail extends Book.Categories, Category.Basic, Book.Basic, Review.Basic{}

    @JsonView(BookDetail.class)
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Book>> getBook(@PathVariable long id) {

        Optional<Book> book = bookService.findById(id);

        if (book.isPresent()) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @JsonView(BookDetail.class)
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Optional<Book>> deleteBook(@PathVariable long id) {

        Optional<Book> book = bookService.findById(id);

        if (book.isPresent()) {
            bookService.deleteById(id);
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/newbook")
    public ResponseEntity<Book> newBook(@RequestBody Book book, @RequestParam(required = false) List<Long> selectedCategories, MultipartFile imageFile){
        if (bookService.exist(book.getTitle())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {

            if (selectedCategories != null) {
                List<Category> categories = categoryService.findByIds(selectedCategories);
                book.setCategories(categories);
                for (Category category : categories) {
                    category.getBooks().add(book);
                }
            }

            Book newBook = bookService.save(book, imageFile);

            return ResponseEntity.ok(newBook);
        }
    }

    @PostMapping("/{id}/addreview")
    public ResponseEntity<Void> newReview(@RequestBody Review review, @PathVariable long id) {
        bookService.addReview(review, id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/review/{reviewid}")
    public ResponseEntity<String> deleteReview(@PathVariable("id") long id, @PathVariable("reviewid") long reviewid) {
        Optional<Book> book = bookService.findById(id);
        if (book.isPresent()) {
            bookService.deleteReviewById(id, reviewid);
            return ResponseEntity.ok("Review deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }








}
