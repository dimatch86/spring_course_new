package com.example.bookshelf.mapstruct;

import com.example.bookshelf.dto.BookResponse;
import com.example.bookshelf.dto.UpsertBookRequest;
import com.example.bookshelf.entity.Book;
import com.example.bookshelf.entity.Category;

public abstract class BookMapperDelegate implements BookMapper {

    @Override
    public Book requestToBook(UpsertBookRequest bookRequest) {
        return Book.builder()
                .title(bookRequest.getTitle())
                .author(bookRequest.getAuthor())
                .category(mapToCategory(bookRequest.getCategory()))
                .build();
    }

    @Override
    public Book requestToBook(Long bookId, UpsertBookRequest bookRequest) {
        Book book = requestToBook(bookRequest);
        book.setId(bookId);
        return book;
    }

    @Override
    public String mapToResponse(Category value) {
        return value.getName();
    }

    @Override
    public Category mapToCategory(String value) {
        return new Category(value);
    }

    @Override
    public BookResponse bookToResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .category(mapToResponse(book.getCategory()))
                .build();
    }
}
