package com.example.bitbookfinal.repository;

import com.example.bitbookfinal.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoorRepository extends JpaRepository<Book, Long> {
}
