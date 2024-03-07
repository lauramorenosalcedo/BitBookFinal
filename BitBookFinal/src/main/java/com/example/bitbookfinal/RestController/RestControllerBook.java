package com.example.bitbookfinal.RestController;

import com.example.bitbookfinal.model.Book;
import com.example.bitbookfinal.service.BookService;

import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/books")
public class RestControllerBook {
    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public Collection<Book> getBooks() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Book>> getBook(@PathVariable long id) {

        Optional<Book> book = bookService.findById(id);

        if (book.isPresent()) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

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





}
