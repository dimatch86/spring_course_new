package com.example.newsservice.mapstruct;

import com.example.newsservice.model.dto.CreateNewsRequest;
import com.example.newsservice.model.dto.NewsListResponse;
import com.example.newsservice.model.dto.NewsResponse;
import com.example.newsservice.model.entity.Category;
import com.example.newsservice.model.entity.News;
import com.example.newsservice.service.CategoryService;
import com.example.newsservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public abstract class NewsMapperDelegate implements NewsMapper {

    @Autowired
    private UserService userService;

    @Autowired
    CategoryService categoryService;

    @Override
    public News requestToNews(CreateNewsRequest request) {

        List<Category> categoryList = request.getCategories().stream()
                .filter(c -> !c.isEmpty())
                .map(Category::new)
                .toList();

        return News.builder()
                .text(request.getText())
                .user(userService.findById(request.getUserId()))
                .categories(categoryList)
                .build();
    }

    @Override
    public News requestToNews(Long newsId, CreateNewsRequest request) {
        News news = requestToNews(request);
        news.setId(newsId);
        return news;
    }

    @Override
    public List<NewsResponse> newsListToResponseList(List<News> newsList) {

        if (newsList == null) {
            return new ArrayList<>();
        }

        return newsList.stream()
                .map(news -> NewsResponse.builder()
                        .id(news.getId())
                        .text(news.getText())
                        .userId(news.getUser().getId())
                        .commentsCount(news.getCommentList().size())
                        .build())
                .toList();
    }

    @Override
    public NewsListResponse newsListToNewsListResponse(List<News> newsList) {

        return new NewsListResponse(newsListToResponseList(newsList));
    }
}
