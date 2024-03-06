package com.example.bitbookfinal.service;

import com.example.bitbookfinal.model.Book;
import com.example.bitbookfinal.model.Category;
import com.example.bitbookfinal.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
@Service

public class BookService {

    //Creation of map tha contains the books, set with their ids.

    @Autowired
    private ImageService imageService;
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
    public Book save(Book book, MultipartFile imageField){
        if (imageField != null && !imageField.isEmpty()){
            String path = imageService.createImage(imageField);
            book.setImage(path);
        }

        if(book.getImage() == null || book.getImage().isEmpty()) book.setImage("ListBookCard.jpg");

        long id = nextId.getAndIncrement();
        book.setId(id);
        mapbooks.put(id, book);
        return book;
    }

    public void deleteById(long id) {
        Book book= this.mapbooks.get(id);
        List<Category>categorias=book.getCategories();
        for(Category categoria: categorias){
            categoria.deleteBook(book);
        }
        this.mapbooks.remove(id);
    }

    /*public boolean exist(String title) {
        return this.mapbooks.contains(book.ge);
    }*/

    public void addReview(Review review, long bookid) {

    }
}
