package com.example.newsservice.repository;

import com.example.newsservice.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Override
    Page<Category> findAll(Pageable pageable);
    Category findCategoryByTag(String tag);
}
