package com.example.bookshelf.service;

import com.example.bookshelf.configuration.properties.AppCacheProperties;
import com.example.bookshelf.entity.Book;
import com.example.bookshelf.entity.Category;
import com.example.bookshelf.exception.BookNotFoundException;
import com.example.bookshelf.filter.AuthorAndTitleFilter;
import com.example.bookshelf.filter.CategoryFilter;
import com.example.bookshelf.repository.BookRepository;
import com.example.bookshelf.repository.BookSpecification;
import com.example.bookshelf.repository.CategoryRepository;
import com.example.bookshelf.utils.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheManager = "redisCacheManager")
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final CacheManager cacheManager;

    @Override
    @Cacheable(cacheNames = AppCacheProperties.CacheNames.BOOK_BY_TITLE_AND_AUTHOR, key = "#filter.title + #filter.author")
    public Book filterByAuthorAndTitle(AuthorAndTitleFilter filter) {
        return bookRepository.findBookByTitleAndAuthor(filter.getTitle(), filter.getAuthor())
                .orElseThrow(() -> new BookNotFoundException("Книга не найдена!"));
    }

    @Override
    @Cacheable(cacheNames = AppCacheProperties.CacheNames.BOOKS_BY_CATEGORY, key = "#categoryFilter.category")
    public List<Book> filterByCategory(CategoryFilter categoryFilter) {
        return bookRepository.findAll(BookSpecification.withFilterByCategory(categoryFilter));
    }


    @Override
    @Transactional
    @CacheEvict(value = AppCacheProperties.CacheNames.BOOKS_BY_CATEGORY, key = "#book.category")
    public Book saveBook(Book book) {
        Category category = categoryRepository.findCategoryByName(book.getCategory().getName());
        if (category == null) {
            category = categoryRepository.save(book.getCategory());
        }

        book.setCategory(category);
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book updateBook(Book book) {
        Category category = categoryRepository.findCategoryByName(book.getCategory().getName());
        if (category == null) {
            category = categoryRepository.save(book.getCategory());
        }

        Book existedBook = bookRepository.findById(book.getId()).orElseThrow(() ->
                new BookNotFoundException("Книга не найдена"));
        evictSingleCacheValue(AppCacheProperties.CacheNames.BOOK_BY_TITLE_AND_AUTHOR, existedBook.getTitle().concat(existedBook.getAuthor()));
        evictSingleCacheValue(AppCacheProperties.CacheNames.BOOKS_BY_CATEGORY, existedBook.getCategory().getName());


        BeanUtil.copyNonNullProperties(book, existedBook);
        existedBook.setCategory(category);
        return bookRepository.save(existedBook);
    }

    @Override
    public void deleteById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new BookNotFoundException("Книга не найдена"));
        evictSingleCacheValue(AppCacheProperties.CacheNames.BOOK_BY_TITLE_AND_AUTHOR, book.getTitle().concat(book.getAuthor()));
        evictSingleCacheValue(AppCacheProperties.CacheNames.BOOKS_BY_CATEGORY, book.getCategory().getName());

        bookRepository.deleteById(id);
    }

    public void evictSingleCacheValue(String cacheName, String cacheKey) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.evict(cacheKey);
        }
    }
}
