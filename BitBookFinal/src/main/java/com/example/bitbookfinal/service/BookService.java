package com.example.bitbookfinal.service;

import com.example.bitbookfinal.model.Book;
import com.example.bitbookfinal.model.Category;
import com.example.bitbookfinal.model.Review;
import com.example.bitbookfinal.repository.BookRepository;
import com.example.bitbookfinal.repository.CategoryRepository;
import com.example.bitbookfinal.repository.ReviewRepository;
import jakarta.persistence.Entity;
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

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

@Service

public class BookService { //This service is dedicated to offer the necesary funcentityManagertionalitites to the book class.

    //Creation of map tha contains the books, set with their ids.

    @Autowired
    private ImageService imageService;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private EntityManager entityManager;


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
    /*public boolean exist(String title) { //Function used to see if a title is already assigned to a book.
        return bookRepository.findByTitle(title);
    }*/
    @SuppressWarnings("unchecked")
    public List<Book> findAll(Integer from, Integer to, String author) {

        String query = "SELECT * FROM Book";
        if( (from != null && to != null) || isNotEmptyField(author)) {
            query+=" WHERE";
        }
        if(from != null && to != null) {
            query+=" price BETWEEN "+from+" AND "+to;
        }
        if( from != null && to != null && isNotEmptyField(author)) {
            query+=" AND";
        }
        if(isNotEmptyField(author)) {
            query+=" author='"+author+"'";
        }
        return (List<Book>) entityManager.createNativeQuery(query, Book.class).getResultList();
    }

    private boolean isNotEmptyField(String field) {
        return field != null && !field.isEmpty();
    }


    public boolean exist(String title) {
        // Busca el libro por título
        Book existingBook = bookRepository.findByTitle(title);

        // Verifica si se encontró un libro con el título especificado
        return existingBook != null;
    }


    public Book save(Book book, MultipartFile imageField){ //Function used to save a book in the book map.
        if (imageField != null && !imageField.isEmpty()){
            String path = imageService.createImage(imageField);
            book.setImage(path);
        }

        if(book.getImage() == null || book.getImage().isEmpty()) book.setImage("no-image.jpg");

        //convertir la imagen a hexadecimal

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
       /* Book book = bookRepository.findBookById(id);
       // Book book= this.mapbooks.get(id);
        List<Category>categorias=book.getCategories();
        for(Category categoria: categorias){
            categoria.deleteBook(book);
        }*/
        bookRepository.deleteById(id);;
       // this.mapbooks.remove(id);
    }
    public void addReview(Review review, long bookId) {
        //review.setId(null);
        Optional<Book> optionalBook = bookRepository.findById(bookId);

        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            // Codifica la descripción enriquecida antes de guardarla
            String cleanedDescripion = cleanedHTML(review.getDescription());
            review.setDescription(cleanedDescripion);
            // No es necesario establecer el ID de la reseña, se generará automáticamente en la base de datos
            // Agrega la reseña al libro
            book.getReviews().add(review);
          //  reviewRepository.save(review);
            // Guarda el libro actualizado en la base de datos
            bookRepository.save(book);
        } else {
            // Maneja el caso en el que no se encuentre el libro
            throw new IllegalArgumentException("Book not found with id: " + bookId);
        }
    }


    private String cleanedHTML(String inputDescription) {

        //Creamos una whitelist que solo permite las siguientes etiquetas (las demás las borras)
        Whitelist whitelist = new Whitelist().addTags("p", "strong", "em", "u", "h1", "h2", "h3", "ol", "ul", "li", "br");
        // Limpiamos la cadena de entrada de elementos no permitidos
        String cleanedHTML = Jsoup.clean(inputDescription, whitelist); //Si hay una etiqueta no permitida en el input se va a borrar la etiqueta y lo que contenga dentro

        return cleanedHTML;
    }




   /* public void addReview(Review review, long bookid) { // Function used to add a review to a specific book.
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