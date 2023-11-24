package com.example.newsservice.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateNewsRequest {
    @NotNull(message = "UserId должен присутствовать!")
    private Long userId;
    private List<String> categories;
    @NotBlank(message = "Новость должна быть заполнена!")
    private String text;
}
