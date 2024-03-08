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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class BookController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public String showHome(){
       return "index";
    }

    @GetMapping("/books")
    public String showBooks(Model model){
        model.addAttribute("books", bookService.findAll());
        return "show_books";
    }

    @GetMapping("/books/{id}")
    public String showBook(Model model, @PathVariable long id) {

        Optional<Book> book = bookService.findById(id);
        if (book.isPresent()) {
            model.addAttribute("book", book.get());
            return "show_book";
        } else {
            return "show_books";
        }

    }

    @GetMapping("/newbook")
    public String newBook(Model model) {

        model.addAttribute("availableCategories", categoryService.findAll());

        return "newbookform";
    }

    @PostMapping("/newbook")
    public String newBookProcess(Model model, Book book, @RequestParam(required = false) List<Long> selectedCategories, MultipartFile multipartFile) {
        if (bookService.exist(book.getTitle())) {
            return "error_book";
        } else {

            if (selectedCategories != null) {
                List<Category> categories = categoryService.findByIds(selectedCategories);
                book.setCategories(categories);
                for (Category category : categories) {
                    category.getBooks().add(book);

                }
//book.addCategory(category);
            }

            Book newBook = bookService.save(book, multipartFile);


            model.addAttribute("bookId", newBook.getId());

            return "redirect:/books/" + newBook.getId();
        }
    }

    @PostMapping("/books/{id}/addreview")
    public String newReview(Review review,  @PathVariable long id ) {
        bookService.addReview(review, id);

        return "redirect:/books/{id}";
    }

    @GetMapping("/book/{id}/delete")
    public String deletePost(Model model, @PathVariable long id) throws IOException {

        bookService.deleteById(id);

        return "deleted_post";
    }



    @GetMapping("/books/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {

        Optional<Book> op = bookService.findById(id);

        if(op.isPresent()) {
            Book book = op.get();
            Resource poster = ImageService.getImage(book.getImage());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").body(poster);
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Film not found");
        }
    }

    @GetMapping("/book/{id}/review/{reviewid}/delete")
    public String deleteReview(Model model, @PathVariable("id") long id, @PathVariable("reviewid") long reviewid) throws IOException {

        bookService.deleteReviewById(id, reviewid);

        return "deleted_review";
    }



}
