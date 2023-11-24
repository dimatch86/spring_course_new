package com.example.newsservice.service;

import com.example.newsservice.filter.NewsFilter;
import com.example.newsservice.model.dto.pagination.PageParameter;
import com.example.newsservice.model.entity.News;

import java.util.List;

public interface NewsService {

    List<News> filterBy(NewsFilter newsFilter, PageParameter pageParameter);
    List<News> findAll(PageParameter pageParameter);
    News findById(Long id);

    void deleteById(Long id);

    News updateNews(News news);
    News saveWithCategory(News news);
}
