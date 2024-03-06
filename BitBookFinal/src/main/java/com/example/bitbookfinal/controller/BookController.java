package com.example.bitbookfinal.controller;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.example.bitbookfinal.model.Book;
import com.example.bitbookfinal.model.Category;
import com.example.bitbookfinal.model.Review;
import com.example.bitbookfinal.service.BookService;
import com.example.bitbookfinal.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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

    /*@PostMapping("/book/{id}")
    public String newReview(Book book,Review review) throws IOException  {

        book.setCategories(categories);

        Review newReview = bookService.savereview(review);


        model.addAttribute("bookId", newBook.getId());

        return "redirect:/books/"+newBook.getId();
    }*/




}
