package com.example.newsservice.repository;

import com.example.newsservice.filter.NewsFilter;
import com.example.newsservice.model.entity.Category;
import com.example.newsservice.model.entity.News;
import com.example.newsservice.model.entity.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public interface NewsSpecification {

    static Specification<News> withFilter(NewsFilter newsFilter) {
        return Specification.where(byAuthor(newsFilter.getAuthor()))
                .and(byCategory(newsFilter.getCategory()));
    }

    static Specification<News> byAuthor(String author) {
        return ((root, query, criteriaBuilder) -> {
            if (author == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get(News.Fields.user).get(User.Fields.name), author);
        });
    }

    static Specification<News> byCategory(String category) {
        return ((root, query, criteriaBuilder) -> {
            if (category == null) {
                return null;
            }
            Join<News, Category> news2CategoryJoin = root.join(News.Fields.categories);
            return criteriaBuilder.equal(news2CategoryJoin.get(Category.Fields.tag), category);
        });
    }
}
