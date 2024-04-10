package com.example.bitbookfinal.RestController;

import com.example.bitbookfinal.model.Book;
import com.example.bitbookfinal.model.Category;
import com.example.bitbookfinal.model.Review;
import com.example.bitbookfinal.service.BookService;

import com.example.bitbookfinal.service.CategoryService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/books")  //This is how the urls of this restcontroller begin
public class RestControllerBook {
    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryService categoryService;
    @JsonView(Book.Basic.class)
    @GetMapping("/")
    public Collection<Book> getBooks() {
        return bookService.findAll();
    }  //show all books

    interface BookDetail extends Book.Categories, Category.Basic, Book.Basic, Review.Basic{}  // interface that contains those necessary to display all the elements of a book without a circular reference

    @JsonView(BookDetail.class)
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Book>> getBook(@PathVariable long id) {  //shows if the book that has been requested by the url exists

        Optional<Book> book = bookService.findById(id);  //search the book in the map of the bookservice by it's id

        if (book.isPresent()) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @JsonView(BookDetail.class)
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Optional<Book>> deleteBook(@PathVariable long id) {  //deletes the book if it exists, to do so its id is passed

        Optional<Book> book = bookService.findById(id); //search for the book in the bookservice book map by its id

        if (book.isPresent()) {
            bookService.deleteById(id);  //deletes the book in the bookservice book map by its id
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/newbook")
    public ResponseEntity<Book> newBook(@RequestBody Book book, @RequestParam(required = false) List<Long> selectedCategories, MultipartFile imageFile) throws SQLException, IOException {  //adds a book, passes a book, accepts several categories and an image
        if (bookService.exist(book.getTitle())) {  //If the title already exists, the book is not created
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            if (selectedCategories != null) {
                List<Category> categories = categoryService.findByIds(selectedCategories);
                book.setCategories(categories); //Add the selected categories to the book
                for (Category category : categories) {
                    category.getBooks().add(book);  //Add that book to the selected categories
                }
            }

            Book newBook = bookService.save(book, imageFile);  //the book is saved with its corresponding image

            return ResponseEntity.ok(newBook);
        }
    }

   @PostMapping("/{id}/addreview")
    public ResponseEntity<Void> newReview(@RequestBody Review review, @PathVariable long id) { //adds a review to a specific book whose id is passed
        bookService.addReview(review, id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/review/{reviewid}")
    public ResponseEntity<String> deleteReview(@PathVariable("id") long id, @PathVariable("reviewid") long reviewid) {  //deletes a certain review of a certain book, to do so the id of each one is passed
        Optional<Book> book = bookService.findById(id);
        if (book.isPresent()) {
            bookService.deleteReviewById(id, reviewid);
            return ResponseEntity.ok("Review deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
