package com.example.newsservice.controller;

import com.example.newsservice.mapstruct.CommentMapper;
import com.example.newsservice.model.dto.CommentListResponse;
import com.example.newsservice.model.dto.CommentResponse;
import com.example.newsservice.model.dto.CommentUpdateRequest;
import com.example.newsservice.model.dto.UpsertCommentRequest;
import com.example.newsservice.model.entity.Comment;
import com.example.newsservice.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<CommentResponse> create(@Valid @RequestBody UpsertCommentRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        Comment comment = commentService.saveComment(commentMapper.requestToComment(request, userDetails.getUsername()));
        return ResponseEntity.status(HttpStatus.CREATED).body(commentMapper.commentToResponse(comment));
    }

    @PostMapping("/{id}")
    public ResponseEntity<CommentResponse> update(@PathVariable Long id, @Valid @RequestBody CommentUpdateRequest request) {
        Comment updatedComment = commentService.updateComment(commentMapper.requestToComment(id, request));
        return ResponseEntity.status(HttpStatus.CREATED).body(commentMapper.commentToResponse(updatedComment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        commentService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
