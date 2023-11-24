package com.example.newsservice.controller;

import com.example.newsservice.aop.Authorizable;
import com.example.newsservice.mapstruct.CommentMapper;
import com.example.newsservice.model.dto.*;
import com.example.newsservice.filter.AuthorFilter;
import com.example.newsservice.model.entity.Comment;
import com.example.newsservice.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @GetMapping("/{id}")
    public ResponseEntity<CommentListResponse> findAllByNews(@PathVariable Long id) {
        return ResponseEntity.ok(
                commentMapper.commentListToCommentListResponse(commentService.findAllByNews(id))
        );
    }

    @PostMapping
    public ResponseEntity<CommentResponse> create(@Valid @RequestBody UpsertCommentRequest request) {
        Comment comment = commentService.saveComment(commentMapper.requestToComment(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(commentMapper.commentToResponse(comment));
    }

    @PostMapping("/{id}")
    @Authorizable
    public ResponseEntity<CommentResponse> update(@PathVariable Long id, @Valid @RequestBody CommentUpdateRequest request, @Valid AuthorFilter authorFilter) {
        Comment updatedComment = commentService.updateComment(commentMapper.requestToComment(id, request));
        return ResponseEntity.status(HttpStatus.CREATED).body(commentMapper.commentToResponse(updatedComment));
    }

    @DeleteMapping("/{id}")
    @Authorizable
    public ResponseEntity<Void> deleteById(@PathVariable Long id, @Valid AuthorFilter authorFilter) {
        commentService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
