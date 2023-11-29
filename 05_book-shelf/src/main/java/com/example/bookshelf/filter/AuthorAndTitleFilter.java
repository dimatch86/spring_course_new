package com.example.bookshelf.filter;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorAndTitleFilter {

    @NotBlank(message = "Название должно присутствовать в запросе")
    private String title;
    @NotBlank(message = "Автор должен присутствовать в запросе")
    private String author;
}
