package com.example.bitbookfinal.service;

import com.example.bitbookfinal.model.Book;
import com.example.bitbookfinal.model.Review;
import com.example.bitbookfinal.model.User;
import com.example.bitbookfinal.repository.BookRepository;
import com.example.bitbookfinal.repository.ReviewRepository;
import com.example.bitbookfinal.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import javax.sql.rowset.serial.SerialBlob;

@Service

public class BookService { //This service is dedicated to offer the necesary funcentityManagertionalitites to the book class.

    //Creation of map tha contains the books, set with their ids.

    @Autowired
    private ImageService imageService;
    @Autowired
    private FileService pdfService;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CategoryService categoryService;

    private NamedParameterJdbcTemplate jdbcTemplate;

    public BookService(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> findAll(Integer from, Integer to, String author) {
        String query = "SELECT * FROM book WHERE true "; // Consulta base

        Map<String, Object> paramMap = new HashMap<>(); // Mapa de parámetros

        if (from != null && to != null) {
            query += " AND price BETWEEN :fromPrice AND :toPrice"; // Agregar condición para precio
            paramMap.put("fromPrice", from);
            paramMap.put("toPrice", to);
        }
        if (isNotEmptyField(author)) {
            query += " AND author = :author"; // Agregar condición para autor
            paramMap.put("author", author);
        }

        return jdbcTemplate.query(query, paramMap, (rs, rowNum) -> {
            // Mapeo de resultados a objetos Book
            Book book = new Book();
            // Mapea las columnas del ResultSet a las propiedades del objeto Book
            book.setId(rs.getLong("id"));
            book.setTitle(rs.getString("title"));
            book.setPrice(rs.getInt("price"));
            book.setAuthor(rs.getString("author"));
            return book;
        });
    }


    private boolean isNotEmptyField(String field) {
        return field != null && !field.isEmpty();
    }


    //The next two functions used to search books, either all of them, or one identified by it´s id.
    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }



    public boolean exist(String title) {
        // Busca el libro por título
        Book existingBook = bookRepository.findByTitle(title);

        // Verifica si se encontró un libro con el título especificado
        return existingBook != null;
    }


    public Book save(Book book, MultipartFile imageField, MultipartFile pdfField) throws IOException, SQLException { //Function used to save a book in the book map.
        if (imageField != null && !imageField.isEmpty()){
            //Convertimos el contenido del archivo a un tipo Blob
            String path = imageField.getOriginalFilename();
            book.setImage(path);
            byte[] imageBytes = imageField.getBytes();
            Blob imageBlob = new SerialBlob(imageBytes);
            book.setImageFile(imageBlob);
        }else{
            Path defaultImagePath = Paths.get("images", "no-image.jpg");
            byte[] defaultImageBytes = Files.readAllBytes(defaultImagePath);
            Blob defaultImageBlob = new SerialBlob(defaultImageBytes);
            book.setImageFile(defaultImageBlob);
            //book.setImage("no-image.jpg");
        }
        if (pdfField != null && !pdfField.isEmpty()){
            String path = pdfService.createPDF(pdfField);
            book.setFilename(path);
        }

        return bookRepository.save(book);
    }


    public Book save(Book book){ //Function used to save a book in the book map, without an image.
        return bookRepository.save(book);
    }
    public Book savebook(Book book, MultipartFile pdfField)throws IOException, SQLException{ //Function used to save a book in the book map, without an image.
        if (pdfField != null && !pdfField.isEmpty()){
            String path = pdfService.createPDF(pdfField);
            book.setFilename(path);
        }
        return bookRepository.save(book);


    }



    public void deleteById(long id) { // This function can identify a book by its id and remove it from the map of books, and from the categories it belongs to.

        bookRepository.deleteById(id);;

    }
    public void addReview(Review review, long bookId, User user) {

        Optional<Book> optionalBook = bookRepository.findById(bookId);

        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            String cleanedDescripion = cleanedHTML(review.getDescription());
            review.setDescription(cleanedDescripion);
            // A la review le añadimos el usuario
            review.setUser(user);
            review.setBookIdReview(bookId);
            // Añadimos la review al libro
            book.getReviews().add(review);
            // Guardamos el libro
            bookRepository.save(book);
        } else {
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

    public int deleteReviewById(long bookId, long reviewId, String username, Boolean useradmin) {
        User user=userRepository.findByUsername(username).orElseThrow();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + bookId));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with id: " + reviewId));

        if (!book.getReviews().contains(review)) {
            throw new IllegalArgumentException("Review not associated with book");
        }
        if(Objects.equals(review.getUser().getId(), user.getId()) || useradmin){
            book.deleteReview(review);
            bookRepository.save(book);
            return 1;
        }
        return 0;
    }


    public void deleteReview(Long bookid, Long reviewid) {
        Book book = bookRepository.findById(bookid)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + bookid));

        Review review = reviewRepository.findById(reviewid)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with id: " + reviewid));
        book.deleteReview(review);
        bookRepository.save(book);
    }
}