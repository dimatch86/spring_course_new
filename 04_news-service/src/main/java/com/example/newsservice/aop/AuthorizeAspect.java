package com.example.newsservice.aop;

import com.example.newsservice.controller.NewsController;
import com.example.newsservice.exception.UserNotEqualsAuthorException;
import com.example.newsservice.filter.AuthorFilter;
import com.example.newsservice.model.entity.Comment;
import com.example.newsservice.model.entity.News;
import com.example.newsservice.service.CommentService;
import com.example.newsservice.service.NewsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;
import java.util.Objects;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthorizeAspect {

    private final NewsService newsService;
    private final CommentService commentService;

    @Before("@annotation(Authorizable)")
    @SuppressWarnings("unchecked")
    public void checkAuthorizationAdvice(JoinPoint joinPoint) {

        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            var pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

            long authorId = Long.parseLong(request.getParameter(AuthorFilter.Fields.authorId));

            if (className.equals(NewsController.class.getSimpleName())) {
                checkNewsUpdating(authorId, pathVariables);
            } else {
                checkCommentUpdating(authorId, pathVariables);
            }
        }
    }

    private void checkNewsUpdating(Long authorId, Map<String, String> pathVariables) {
        long newsId = Long.parseLong(pathVariables.get(News.Fields.id));
        News news = newsService.findById(newsId);
        if (!Objects.equals(authorId, news.getUser().getId())) {
            throw new UserNotEqualsAuthorException("Пользователь не является автором этого поста!");
        }
    }

    private void checkCommentUpdating(Long authorId, Map<String, String> pathVariables) {
        long commentId = Long.parseLong(pathVariables.get(Comment.Fields.id));
        Comment comment = commentService.findById(commentId);
        if (!Objects.equals(authorId, comment.getUser().getId())) {
            throw new UserNotEqualsAuthorException("Пользователь не является автором этого комментария!");
        }
    }
}
