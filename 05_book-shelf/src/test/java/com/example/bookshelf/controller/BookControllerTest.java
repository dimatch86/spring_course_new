package com.example.bookshelf.controller;

import com.example.bookshelf.AbstractTest;
import com.example.bookshelf.dto.UpsertBookRequest;
import com.example.bookshelf.entity.Book;
import com.example.bookshelf.filter.AuthorAndTitleFilter;
import com.example.bookshelf.filter.CategoryFilter;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookControllerTest extends AbstractTest {

    @Test
    void whenGetAllBooks_thenReturnBookList() throws Exception {

        assertTrue(redisTemplate.keys("*").isEmpty());
        String actualResponse = mockMvc.perform(get("/book/category?category=hunting"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String expectedResponse = objectMapper.writeValueAsString(bookMapper.bookListToBookResponseList(bookService.filterByCategory(new CategoryFilter("hunting"))));
        assertFalse(redisTemplate.keys("*").isEmpty());
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    void whenGetBookByTitleAndAuthor_thenReturnsOneBook() throws Exception {
        assertTrue(redisTemplate.keys("*").isEmpty());
        String actualResponse = mockMvc.perform(get("/book/filter?title=Book1&author=Pushkin"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String expectedResponse = objectMapper.writeValueAsString(bookMapper.bookToResponse(bookService.filterByAuthorAndTitle(new AuthorAndTitleFilter("Book1", "Pushkin"))));
        assertFalse(redisTemplate.keys("*").isEmpty());
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    void whenCreateBook_thenCreateBookAndEvictCache() throws Exception {
        assertTrue(redisTemplate.keys("*").isEmpty());
        assertEquals(2, bookRepository.count());
        mockMvc.perform(get("/book/category?category=hunting"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertTrue(redisTemplate.persist("booksByCategory::hunting"));
        UpsertBookRequest request = new UpsertBookRequest("Book3", "Shishov", "hunting");
        String actualResponse = mockMvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Book book = bookMapper.requestToBook(request);
        String expectedResponse = objectMapper.writeValueAsString(bookMapper.bookToResponse(book));
        assertFalse(redisTemplate.persist("booksByCategory::hunting"));
        assertEquals(3, bookRepository.count());
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse, JsonAssert.whenIgnoringPaths("id"));
    }

    @Test
    void whenUpdateBook_thenUpdateBookAndEvictCache() throws Exception {
        assertTrue(redisTemplate.keys("*").isEmpty());

        mockMvc.perform(get("/book/category?category=hunting"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertTrue(redisTemplate.persist("booksByCategory::hunting"));
        UpsertBookRequest request = new UpsertBookRequest("Book11", "Pushkin", "hunting");
        String actualResponse = mockMvc.perform(put("/book/" + UPDATED_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Book book = bookMapper.requestToBook(request);
        String expectedResponse = objectMapper.writeValueAsString(bookMapper.bookToResponse(book));
        assertFalse(redisTemplate.persist("booksByCategory::hunting"));
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse, JsonAssert.whenIgnoringPaths("id"));
    }

    @Test
    void whenDeleteBookById_thenDeleteBookByIdAndEvictCache() throws Exception {
        assertTrue(redisTemplate.keys("*").isEmpty());
        assertEquals(2, bookRepository.count());
        mockMvc.perform(get("/book/category?category=hunting"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertTrue(redisTemplate.persist("booksByCategory::hunting"));
        mockMvc.perform(delete("/book/" + UPDATED_ID))
                .andExpect(status().isNoContent());
        assertFalse(redisTemplate.persist("booksByCategory::hunting"));
        assertEquals(1, bookRepository.count());
    }

}
