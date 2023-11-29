package com.example.bookshelf.mapstruct;

import com.example.bookshelf.dto.BookListResponse;
import com.example.bookshelf.dto.BookResponse;
import com.example.bookshelf.dto.UpsertBookRequest;
import com.example.bookshelf.entity.Book;
import com.example.bookshelf.entity.Category;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(BookMapperDelegate.class)
public interface BookMapper {

    Category mapToCategory(String value);
    String mapToResponse(Category value);
    Book requestToBook(UpsertBookRequest bookRequest);
    @Mapping(source = "bookId", target = "id")
    Book requestToBook(Long bookId, UpsertBookRequest bookRequest);

    BookResponse bookToResponse(Book book);

    default BookListResponse bookListToBookResponseList(List<Book> books) {
        BookListResponse response = new BookListResponse();
        response.setBookResponses(books.stream().map(this::bookToResponse).toList());
        return response;
    }
}
