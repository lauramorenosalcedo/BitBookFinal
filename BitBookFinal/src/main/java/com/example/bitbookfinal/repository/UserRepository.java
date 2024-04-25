package com.example.bitbookfinal.repository;

import com.example.bitbookfinal.model.Book;
import com.example.bitbookfinal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

}
