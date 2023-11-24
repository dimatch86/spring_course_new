package com.example.newsservice.service;

import com.example.newsservice.model.entity.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findAllByNews(Long id);
    Comment findById(Long id);

    Comment saveComment(Comment comment);

    void deleteById(Long id);

    Comment updateComment(Comment comment);
}
