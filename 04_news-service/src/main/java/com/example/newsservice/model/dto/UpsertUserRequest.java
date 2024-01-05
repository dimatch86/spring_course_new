package com.example.newsservice.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpsertUserRequest {

    @NotBlank(message = "Имя должно быть заполнено!")
    private String name;

    @NotBlank(message = "Пароль должен быть заполнен!")
    private String password;
}
