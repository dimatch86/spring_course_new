package com.example.bookshelf.controller;

import com.example.bookshelf.dto.BookListResponse;
import com.example.bookshelf.dto.BookResponse;
import com.example.bookshelf.dto.UpsertBookRequest;
import com.example.bookshelf.entity.Book;
import com.example.bookshelf.filter.CategoryFilter;
import com.example.bookshelf.filter.AuthorAndTitleFilter;
import com.example.bookshelf.mapstruct.BookMapper;
import com.example.bookshelf.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @GetMapping("/filter")
    public ResponseEntity<BookResponse> filterBy(@Valid AuthorAndTitleFilter oneBookFilter) {
        Book book = bookService.filterByAuthorAndTitle(oneBookFilter);
        return ResponseEntity.ok(bookMapper.bookToResponse(book));
    }

    @GetMapping("/category")
    public ResponseEntity<BookListResponse> findBooksByCategory(@Valid CategoryFilter categoryFilter) {
        List<Book> books = bookService.filterByCategory(categoryFilter);
        return ResponseEntity.ok(bookMapper.bookListToBookResponseList(books));
    }

    @PostMapping
    public ResponseEntity<BookResponse> create(@Valid @RequestBody UpsertBookRequest request) {
        Book savedBook = bookService.saveBook(bookMapper.requestToBook(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(bookMapper.bookToResponse(savedBook));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> update(@PathVariable Long id, @RequestBody UpsertBookRequest request) {
        Book updatedBook = bookService.updateBook(bookMapper.requestToBook(id, request));
        return ResponseEntity.status(HttpStatus.CREATED).body(bookMapper.bookToResponse(updatedBook));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
