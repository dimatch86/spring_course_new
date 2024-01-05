package com.example.newsservice.mapstruct;

import com.example.newsservice.model.dto.CommentUpdateRequest;
import com.example.newsservice.model.dto.UpsertCommentRequest;
import com.example.newsservice.model.entity.Comment;
import com.example.newsservice.service.CommentService;
import com.example.newsservice.service.NewsService;
import com.example.newsservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CommentMapperDelegate implements CommentMapper {

    @Autowired
    private NewsService newsService;
    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Override
    public Comment requestToComment(UpsertCommentRequest request, String userName) {
        Comment comment = new Comment();
        comment.setText(request.getText());
        comment.setNews(newsService.findById(request.getNewsId()));
        comment.setUser(userService.findUserByName(userName));
        return comment;
    }

    @Override
    public Comment requestToComment(CommentUpdateRequest request) {
        Comment comment = commentService.findById(request.getCommentId());
        comment.setText(request.getText());
        return comment;
    }


    @Override
    public Comment requestToComment(Long commentId, CommentUpdateRequest request) {
        Comment comment = requestToComment(request);
        comment.setId(commentId);
        return comment;
    }
}
