package com.example.newsservice.service;

import com.example.newsservice.exception.EntityNotFoundException;
import com.example.newsservice.model.dto.pagination.PageParameter;
import com.example.newsservice.model.entity.Category;
import com.example.newsservice.repository.CategoryRepository;
import com.example.newsservice.utils.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    @Override
    public List<Category> findAll(PageParameter pageParameter) {
        return categoryRepository.findAll(PageRequest.of(pageParameter.getPageNumber(), pageParameter.getPageSize())).getContent();
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Category with id {0} not found", id)));
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category updateCategory(Category category) {
        Category existedCategory = findById(category.getId());
        BeanUtil.copyNonNullProperties(category, existedCategory);
        return categoryRepository.save(existedCategory);
    }

    @Override
    public Category findByTag(String tag) {
        return categoryRepository.findCategoryByTag(tag);
    }
}
