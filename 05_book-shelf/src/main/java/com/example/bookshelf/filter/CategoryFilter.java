package com.example.bookshelf.filter;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryFilter {

    @NotBlank(message = "Категория должна присутствовать в запросе")
    private String category;
}
