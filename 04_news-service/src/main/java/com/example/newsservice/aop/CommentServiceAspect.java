package com.example.newsservice.aop;

import com.example.newsservice.exception.UserNotEqualsAuthorException;
import com.example.newsservice.model.entity.Comment;
import com.example.newsservice.model.entity.RoleType;
import com.example.newsservice.model.entity.User;
import com.example.newsservice.service.CommentService;
import com.example.newsservice.service.UserService;
import com.example.newsservice.utils.RequestParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class CommentServiceAspect {

    private final UserService userService;
    private final CommentService commentService;

    @Pointcut("execution(* com.example.newsservice.service.CommentServiceImpl.updateComment(com.example.newsservice.model.entity.Comment))")
    public void updateCommentPointcut(){}

    @Before(value = "updateCommentPointcut()")
    public void updateCommentAdvice() {
        String currentUserName = RequestParser.getCurrentUserName();
        User currentUser = userService.findUserByName(currentUserName);
        long targetCommentId = RequestParser.getIdFromRequest();
        Comment targetComment = commentService.findById(targetCommentId);
        if (!targetComment.getUser().getId().equals(currentUser.getId())) {
            throw new UserNotEqualsAuthorException("Вы не можете изменять комментарий другого пользователя!");
        }
    }

    @Pointcut("execution(* com.example.newsservice.service.CommentServiceImpl.deleteById(Long))")
    public void deleteCommentPointcut(){}

    @Before(value = "deleteCommentPointcut()")
    public void deleteCommentAdvice() {
        String currentUserName = RequestParser.getCurrentUserName();
        User currentUser = userService.findUserByName(currentUserName);
        long targetCommentId = RequestParser.getIdFromRequest();
        Comment targetComment = commentService.findById(targetCommentId);
        if (!targetComment.getUser().getId().equals(currentUser.getId()) && currentUser.getRoles().stream()
                .noneMatch(role -> role.getAuthority().equals(RoleType.ROLE_ADMIN) || role.getAuthority().equals(RoleType.ROLE_MODERATOR))) {
            throw new UserNotEqualsAuthorException("Вы не можете удалять новость другого пользователя!");
        }
    }
}
