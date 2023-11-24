package com.example.newsservice.service;

import com.example.newsservice.model.dto.pagination.PageParameter;
import com.example.newsservice.model.entity.Category;

import java.util.List;

public interface CategoryService {

    List<Category> findAll(PageParameter pageParameter);
    Category findById(Long id);

    Category saveCategory(Category category);

    void deleteById(Long id);

    Category updateCategory(Category category);
    Category findByTag(String tag);
}
