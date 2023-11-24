package com.example.newsservice.mapstruct;

import com.example.newsservice.model.dto.CategoryListResponse;
import com.example.newsservice.model.dto.CategoryResponse;
import com.example.newsservice.model.entity.Category;

import java.util.List;

public abstract class CategoryMapperDelegate implements CategoryMapper {

    @Override
    public List<CategoryResponse> categoryListToResponseList(List<Category> categoryList) {
        return categoryList.stream()
                .map(category -> CategoryResponse.builder()
                .id(category.getId())
                .tag(category.getTag())
                .newsCount(category.getNewsList().size())
                .build()).toList();
    }

    @Override
    public CategoryListResponse categoryListToCategoryListResponse(List<Category> categoryList) {
        return new CategoryListResponse(categoryListToResponseList(categoryList));
    }
}
