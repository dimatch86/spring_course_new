package com.example.newsservice.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class CreateNewsRequest {
    private List<String> categories;
    @NotBlank(message = "Новость должна быть заполнена!")
    private String text;
}
