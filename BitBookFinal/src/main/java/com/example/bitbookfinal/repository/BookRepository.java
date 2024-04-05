package com.example.bitbookfinal.repository;

import com.example.bitbookfinal.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByTitle(String title);



}
