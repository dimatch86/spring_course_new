package com.example.newsservice.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentUpdateRequest {

    @NotNull(message = "Id комментария должен быть заполнен!")
    private Long commentId;

    @NotBlank(message = "Комментарий должен быть заполнен!")
    private String text;
}
