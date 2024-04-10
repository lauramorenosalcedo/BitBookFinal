package com.example.bitbookfinal.controller;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.example.bitbookfinal.model.Book;
import com.example.bitbookfinal.model.Category;
import com.example.bitbookfinal.model.Review;
import com.example.bitbookfinal.service.BookService;
import com.example.bitbookfinal.service.CategoryService;
import com.example.bitbookfinal.service.ImageService;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;



@Controller
public class BookController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BookService bookService;


    @GetMapping("/")
    //This get mapping returns the home page, showing the index.html, from where you can access both the books and the categories.
    public String showHome() {
        return "index";
    }

    @GetMapping("/books")
    // Passes all the books with a book service function, to the show_books html, where the user can view all the books.
    public String showBooks(Model model, @RequestParam(required = false) Integer from, @RequestParam(required = false) Integer to, @RequestParam(required = false) String author) {
        /*model.addAttribute("books", bookService.findAll());
        return "show_books";*/

        model.addAttribute("books", bookService.findAll(from, to, author));

        return "show_books";

    }

    @GetMapping("/books/{id}") // Shows a single book identified by it´s id.
    public String showBook(Model model, @PathVariable long id) {
        Optional<Book> optionalBook = bookService.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            model.addAttribute("book", book);
            return "show_book";
        } else {
            return "show_books";
        }
    }
/*
    @GetMapping(value = "/books/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] showBookImage(@PathVariable long id) throws IOException, SQLException {
        Optional<Book> optionalBook = bookService.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            return book.getImageFile().getBytes(1, (int) book.getImageFile().length());
        } else {
            return null;
        }
    }*/

    @GetMapping("/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id)
            throws SQLException {  Post post = posts.findById(id).orElseThrow();  if (post.getImageFile() != null) {    Resource file = new InputStreamResource(      post.getImageFile().getBinaryStream());    return ResponseEntity.ok()      .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")      .contentLength(post.getImageFile().length())      .body(file);  } else {    return ResponseEntity.notFound().build();  } }



    @GetMapping("/newbook")
    //This function handles the GET method part of creating a new book. This is linked to the button that shows up on the show_books html, and redirects to the form used to create a new book.
    public String newBook(Model model) {

        model.addAttribute("availableCategories", categoryService.findAll());

        return "newbookform";
    }

    @PostMapping("/newbook")
    // Receives data from the newbookform, and saves it into the database as a new book using the BookService.
    public String newBookProcess(Model model, Book book, @RequestParam(required = false) List<Long> selectedCategories, MultipartFile imageFile) throws SQLException, IOException {
        if (bookService.exist(book.getTitle())) {
            return "error_book";
        } else {

            if (selectedCategories != null) {
                List<Category> categories = categoryService.findByIds(selectedCategories);
                book.setCategories(categories);
                for (Category category : categories) {
                    category.getBooks().add(book);

                }
            }

            Book newBook = bookService.save(book, imageFile);


            model.addAttribute("bookId", newBook.getId());

            return "redirect:/books/" + newBook.getId();
        }
    }

    @GetMapping("/book/{id}/delete") //Function used to remove a book using the id embeded in the url.
    public String deletePost(@PathVariable long id) {

        bookService.deleteById(id);

        return "deleted_post";
    }

    @PostMapping("/books/{bookId}/addreview")
    // This function recieves data from the form embeded in the show_books html, and saves it in the review list of the specified book.
    public String newReview(Review review, @PathVariable long bookId) {
        bookService.addReview(review, bookId);

        return "redirect:/books/" + bookId;
    }

    @GetMapping("/book/{id}/review/{reviewid}")
    //This function is used to remove a specific review of a specific book, they are both identified by their id, embeded in the url.
    public String deleteReview(@PathVariable("id") long id, @PathVariable("reviewid") long reviewid) {

        Optional<Book> book = bookService.findById(id);
        if (book.isPresent()) {
            bookService.deleteReviewById(id, reviewid);
        }

        return "deleted_review";

    }



   /* @GetMapping("/books/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id){

        Optional<Book> op = bookService.findById(id);

        if(op.isPresent()) {
            Book book = op.get();
            Resource poster = ImageService.getImage(book.getImage());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").body(poster);
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
    }
    */

   /* @GetMapping("/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {
        Book book = bookService.findById(id).orElseThrow(); if (book.getImageFile() != null) {
            Resource file = new InputStreamResource( book.getImageFile().getBinaryStream());
            return ResponseEntity.ok() .header(HttpHeaders.CONTENT_TYPE, "image/jpeg") .contentLength(book.getImageFile().length()) .body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/

}