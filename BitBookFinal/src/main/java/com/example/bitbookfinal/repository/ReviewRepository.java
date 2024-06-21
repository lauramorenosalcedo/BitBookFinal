package com.example.bitbookfinal.repository;

import com.example.bitbookfinal.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
