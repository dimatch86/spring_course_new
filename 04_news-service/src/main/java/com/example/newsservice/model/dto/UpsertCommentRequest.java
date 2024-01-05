package com.example.newsservice.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpsertCommentRequest {
    @NotNull(message = "NewsId должен присутствовать!")
    private Long newsId;
    @NotBlank(message = "Комментарий должен быть заполнен!")
    private String text;
}
