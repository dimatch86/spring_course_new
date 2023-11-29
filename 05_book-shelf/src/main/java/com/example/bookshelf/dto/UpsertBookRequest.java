package com.example.bookshelf.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpsertBookRequest {

    @NotBlank(message = "Название должно быть заполнено")
    private String title;
    @NotBlank(message = "Автор должен быть заполнен")
    private String author;
    @NotBlank(message = "Категория должна быть заполнена")
    private String category;
}
