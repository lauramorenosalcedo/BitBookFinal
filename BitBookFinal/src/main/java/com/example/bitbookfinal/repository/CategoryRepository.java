package com.example.bitbookfinal.repository;

import com.example.bitbookfinal.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findCategoryById(long id);
}
