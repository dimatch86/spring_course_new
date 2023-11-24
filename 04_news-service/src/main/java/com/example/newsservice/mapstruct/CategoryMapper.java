package com.example.newsservice.mapstruct;

import com.example.newsservice.model.dto.*;
import com.example.newsservice.model.entity.Category;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {NewsMapper.class, CommentMapper.class})
@DecoratedWith(CategoryMapperDelegate.class)
public interface CategoryMapper {

    Category requestToCategory(UpsertCategoryRequest request);

    @Mapping(source = "categoryId", target = "id")
    Category requestToCategory(Long categoryId, UpsertCategoryRequest request);

    CategoryResponse categoryToResponse(Category category);

    List<CategoryResponse> categoryListToResponseList(List<Category> categoryList);

    default CategoryListResponse categoryListToCategoryListResponse(List<Category> categoryList) {
        CategoryListResponse response = new CategoryListResponse();
        response.setCategoryResponseList(categoryListToResponseList(categoryList));
        return response;
    }


}
