package com.example.bitbookfinal.repository;

import com.example.bitbookfinal.model.Book;
import com.example.bitbookfinal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
