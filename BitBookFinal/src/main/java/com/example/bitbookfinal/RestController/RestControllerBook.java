package com.example.bitbookfinal.RestController;

import com.example.bitbookfinal.model.Book;
import com.example.bitbookfinal.model.Category;
import com.example.bitbookfinal.model.Review;
import com.example.bitbookfinal.model.User;
import com.example.bitbookfinal.service.BookService;

import com.example.bitbookfinal.service.CategoryService;
import com.example.bitbookfinal.service.FileService;
import com.example.bitbookfinal.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/books")  //This is how the urls of this restcontroller begin
public class RestControllerBook {
    @Autowired
    private BookService bookService;
    @Autowired
    private FileService fileService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FileService pdfService;
    @Autowired
    private UserService userService;
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
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable long id) {  //deletes the book if it exists, to do so its id is passed

        Optional<Book> book = bookService.findById(id); //search for the book in the bookservice book map by its id

        if (book.isPresent()) {
            bookService.deleteById(id);  //deletes the book in the bookservice book map by its id
            return ResponseEntity.ok("Book deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<?>  newBook(@RequestBody Book book, @RequestParam(required = false) List<Long> selectedCategories, MultipartFile imageFile, MultipartFile pdfFile) throws SQLException, IOException {  //adds a book, passes a book, accepts several categories and an image
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

            Book newBook = bookService.save(book, imageFile, pdfFile);  //the book is saved with its corresponding image

            return ResponseEntity.status(201).body("book creado");
        }
    }

    @PostMapping("/{id}/image") public ResponseEntity<Object> uploadImage(@PathVariable long id, @RequestParam MultipartFile imageFile) throws IOException {
        Book book = bookService.findById(id).orElseThrow();
        URI location = fromCurrentRequest().build().toUri();
        book.setImage(location.toString());
        book.setImageFile(BlobProxy.generateProxy(
                imageFile.getInputStream(), imageFile.getSize()));
        bookService.save(book);
        return ResponseEntity.created(location).build();
    }


    @PostMapping("/{id}/pdf")
    public ResponseEntity<?> uploadPDF(@PathVariable("id") Long id, @RequestParam("pdfFile") MultipartFile pdfFile) {
        // Verificar si el libro existe
        Book book = bookService.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado")
        );

        // Guardar el archivo PDF usando el servicio de archivos
        String fileName;
        try {
            fileName = fileService.createPDF(pdfFile); // Guarda el archivo y obtiene el nombre del archivo
        } catch (ResponseStatusException e) {
            // Si hay un error (por ejemplo, no es un PDF), devuelve una respuesta con el código de error y el mensaje
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }

        // Asociar el nombre del archivo con el libro
        book.setFilename(fileName);
        bookService.save(book); // Guardar el libro actualizado

        // Construir URI para el archivo guardado
        URI location = fromCurrentRequest()
                .replacePath("/files/" + fileName) // Ruta donde se guardan los PDF
                .build()
                .toUri();

        // Devolver respuesta HTTP con estado 201 Created y la ubicación del archivo
        return ResponseEntity.created(location).body("PDF subido exitosamente");
    }



    private Blob createBlob(byte[] data) throws SQLException {
        try {
            return new javax.sql.rowset.serial.SerialBlob(data);
        } catch (SQLException e) {
            throw new SQLException("Failed to create Blob", e);
        }
    }


   @PostMapping("/{bookId}/review")
    public ResponseEntity<?> addReview(@RequestBody Review review, @PathVariable("bookId") long bookId, HttpServletRequest request) {
       String username =request.getUserPrincipal().getName();
       User user=userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        // Intentar añadir la reseña al libro especificado
        try {
            bookService.addReview(review, bookId,user);
            return ResponseEntity.status(201).body("reseña creada");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al añadir la reseña: " + e.getMessage());
        }
    }


    @DeleteMapping("/{id}/review/{reviewid}")
    public ResponseEntity<String> deleteReview(@PathVariable("id") long bookId, @PathVariable("reviewid") long reviewId, HttpServletRequest request) {
        // Obtener datos del usuario y roles
        boolean isAdmin = request.isUserInRole("ADMIN");
        String username = request.getUserPrincipal().getName();

        // Buscar el libro por su ID
        Optional<Book> book = bookService.findById(bookId);

        if (book.isPresent()) {
            // Intentar eliminar la reseña
            int result = bookService.deleteReviewById(bookId, reviewId, username, isAdmin);

            if (result == 1) {
                return ResponseEntity.ok("Reseña eliminada con éxito");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tiene permiso para eliminar esta reseña");
            }
        } else {
            // Si el libro no se encuentra
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Libro no encontrado");
        }
    }
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> descargarPDF(@PathVariable Long id) throws IOException {
        Optional<Book> book = bookService.findById(id);

        if (book.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String pdfName = book.get().getFilename();
        if (pdfName == null || pdfName.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }

        File archivoPDF = pdfService.getPDF(pdfName);
        if (archivoPDF == null || !archivoPDF.exists()) {
            return ResponseEntity.notFound().build();
        }

        byte[] contenido = Files.readAllBytes(archivoPDF.toPath());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename(pdfName).build());

        return new ResponseEntity<>(contenido, headers, HttpStatus.OK);
    }


}
