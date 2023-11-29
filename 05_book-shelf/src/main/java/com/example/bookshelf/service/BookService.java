package com.example.bookshelf.service;

import com.example.bookshelf.entity.Book;
import com.example.bookshelf.filter.CategoryFilter;
import com.example.bookshelf.filter.AuthorAndTitleFilter;

import java.util.List;

public interface BookService {

    Book filterByAuthorAndTitle(AuthorAndTitleFilter authorAndTitleFilter);

    List<Book> filterByCategory(CategoryFilter categoryFilter);
    Book saveBook(Book book);
    Book updateBook(Book book);
    void deleteById(Long id);
}
