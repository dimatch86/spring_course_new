package com.example.bookshelf.repository;

import com.example.bookshelf.entity.Book;
import com.example.bookshelf.entity.Category;
import com.example.bookshelf.filter.CategoryFilter;
import org.springframework.data.jpa.domain.Specification;

public interface BookSpecification {

    static Specification<Book> withFilterByCategory(CategoryFilter categoryFilter) {
        return Specification.where(byCategory(categoryFilter.getCategory()));
    }

    static Specification<Book> byCategory(String category) {
        return ((root, query, criteriaBuilder) -> {
            if (category == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get(Book.Fields.category).get(Category.Fields.name), category);
        });
    }
}
