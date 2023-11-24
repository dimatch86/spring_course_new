package com.example.newsservice.controller;

import com.example.newsservice.aop.Authorizable;
import com.example.newsservice.mapstruct.NewsMapper;
import com.example.newsservice.model.dto.NewsListResponse;
import com.example.newsservice.model.dto.NewsResponse;
import com.example.newsservice.model.dto.CreateNewsRequest;
import com.example.newsservice.filter.AuthorFilter;
import com.example.newsservice.filter.NewsFilter;
import com.example.newsservice.model.dto.pagination.PageParameter;
import com.example.newsservice.model.entity.News;
import com.example.newsservice.service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;
    private final NewsMapper newsMapper;

    @GetMapping("/filter")
    public ResponseEntity<NewsListResponse> filterBy(NewsFilter newsFilter, @Valid PageParameter pageParameter) {

        return ResponseEntity.ok(
                newsMapper.newsListToNewsListResponse(newsService.filterBy(newsFilter, pageParameter))
        );
    }
    @GetMapping
    public ResponseEntity<NewsListResponse> findAll(@Valid PageParameter pageParameter) {
        return ResponseEntity.ok(
                newsMapper.newsListToNewsListResponse(newsService.findAll(pageParameter))
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<NewsResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(newsMapper.newsToResponse(newsService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<NewsResponse> create(@Valid @RequestBody CreateNewsRequest request) {
        News savedNews = newsService.saveWithCategory(newsMapper.requestToNews(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(newsMapper.newsToResponse(savedNews));
    }

    @PostMapping("/{id}")
    @Authorizable
    public ResponseEntity<NewsResponse> update(@PathVariable Long id, @Valid @RequestBody CreateNewsRequest request, @Valid AuthorFilter authorFilter) {
        News updatedNews = newsService.updateNews(newsMapper.requestToNews(id, request));
        return ResponseEntity.status(HttpStatus.CREATED).body(newsMapper.newsToResponse(updatedNews));
    }
    @DeleteMapping("/{id}")
    @Authorizable
    public ResponseEntity<Void> deleteById(@PathVariable Long id, @Valid AuthorFilter authorFilter) {
        newsService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
