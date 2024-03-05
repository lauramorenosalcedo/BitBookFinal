package com.example.bitbookfinal.service;

import com.example.bitbookfinal.model.Book;
import com.example.bitbookfinal.model.Category;
import com.example.bitbookfinal.model.Review;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
@Service

public class BookService {

    //Creation of map tha contains the books, set with their ids.
    private AtomicLong nextId = new AtomicLong(1L);
    private ConcurrentHashMap<Long, Book> mapbooks = new ConcurrentHashMap<>();


    //Functions used to search books, either all of them, or one identified by it´s id.
    public Optional<Book> findById(long id) {
        if(this.mapbooks.containsKey(id)) {
            return Optional.of(this.mapbooks.get(id));
        }
        return Optional.empty();
    }

    public List<Book> findAll() {
        return this.mapbooks.values().stream().toList();
    }

    //Function used to save a book in the book map.
    public Book save(Book book){
        long id = nextId.getAndIncrement();
        book.setId(id);
        mapbooks.put(id, book);
        return book;
    }

   /* public Review saveReview(Review review){
        long id = nextId.getAndIncrement();
        review.setId(id);

    }*/

   public void deleteById(long id) {
        this.mapbooks.remove(id);
        /*Book book= this.mapbooks.get(id);
        List<Category>categorias=book.getCategories();
        for(Category categoria: categorias){
            categoria.deleteBook(book);
        }*/

    }

}
