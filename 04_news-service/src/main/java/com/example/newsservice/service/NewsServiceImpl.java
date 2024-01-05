package com.example.newsservice.service;

import com.example.newsservice.exception.EntityNotFoundException;
import com.example.newsservice.filter.NewsFilter;
import com.example.newsservice.model.dto.pagination.PageParameter;
import com.example.newsservice.model.entity.Category;
import com.example.newsservice.model.entity.News;
import com.example.newsservice.model.entity.User;
import com.example.newsservice.repository.NewsRepository;
import com.example.newsservice.repository.NewsSpecification;
import com.example.newsservice.utils.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    @Override
    public List<News> filterBy(NewsFilter newsFilter, PageParameter pageParameter) {
        return newsRepository.findAll(NewsSpecification.withFilter(newsFilter),
                PageRequest.of(pageParameter.getPageNumber(), pageParameter.getPageSize())).getContent();

    }

    @Override
    public List<News> findAll(PageParameter pageParameter) {
        return newsRepository.findAll(PageRequest.of(pageParameter.getPageNumber(), pageParameter.getPageSize()))
                .getContent();
    }

    @Override
    public News findById(Long id) {
        return newsRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("News with id {0} not found", id)));
    }

    @Override
    @Transactional
    public News saveWithCategory(News news) {

        List<Category> existedCategories = prepareCategories(news);
        news.setCategories(existedCategories);
        return newsRepository.save(news);
    }

    @Override
    @Transactional
    public News updateNews(News news) {

        List<Category> existedCategories = prepareCategories(news);
        User user = userService.findUserByName(news.getUser().getName());
        News existedNews = findById(news.getId());
        BeanUtil.copyNonNullProperties(news, existedNews);
        existedNews.setUser(user);
        existedNews.setCategories(existedCategories);
        return newsRepository.save(existedNews);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        newsRepository.deleteById(id);
    }

    private List<Category> prepareCategories(News news) {
        List<Category> preparedCategories = new ArrayList<>();
        news.getCategories().forEach(category -> {
            Category existed = categoryService.findByTag(category.getTag());
            if (existed == null) {
                preparedCategories.add(categoryService.saveCategory(category));
            } else {
                preparedCategories.add(existed);
            }
        });
        return preparedCategories;
    }
}
