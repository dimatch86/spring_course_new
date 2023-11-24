package com.example.newsservice.controller;

import com.example.newsservice.mapstruct.CategoryMapper;

import com.example.newsservice.model.dto.*;
import com.example.newsservice.model.dto.pagination.PageParameter;
import com.example.newsservice.model.entity.Category;
import com.example.newsservice.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping()
    public ResponseEntity<CategoryListResponse> findAll(@Valid PageParameter pageParameter) {
        return ResponseEntity.ok(
                categoryMapper.categoryListToCategoryListResponse(categoryService.findAll(pageParameter))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryMapper.categoryToResponse(categoryService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody UpsertCategoryRequest request) {
        Category category = categoryService.saveCategory(categoryMapper.requestToCategory(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryMapper.categoryToResponse(category));
    }

    @PostMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id, @Valid @RequestBody UpsertCategoryRequest request) {
        Category updatedCategory = categoryService.updateCategory(categoryMapper.requestToCategory(id, request));
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryMapper.categoryToResponse(updatedCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
