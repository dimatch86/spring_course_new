package com.example.newsservice.model.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CommentListResponse {

    private List<CommentResponse> commentResponseList = new ArrayList<>();
}
