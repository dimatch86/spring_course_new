package com.example.newsservice.model.dto;

import lombok.Data;

@Data
public class CommentResponse {

    private Long id;
    private String text;
    private Long newsId;
}
