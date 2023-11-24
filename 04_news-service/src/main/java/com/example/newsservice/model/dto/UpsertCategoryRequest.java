package com.example.newsservice.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpsertCategoryRequest {

    @NotBlank(message = "Категория должна быть заполнена!")
    private String tag;
}
