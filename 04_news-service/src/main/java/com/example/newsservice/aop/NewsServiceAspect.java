package com.example.newsservice.aop;

import com.example.newsservice.exception.UserNotEqualsAuthorException;
import com.example.newsservice.model.entity.News;
import com.example.newsservice.model.entity.RoleType;
import com.example.newsservice.model.entity.User;
import com.example.newsservice.service.NewsService;
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
public class NewsServiceAspect {
    private final UserService userService;
    private final NewsService newsService;

    @Pointcut("execution(* com.example.newsservice.service.NewsServiceImpl.updateNews(com.example.newsservice.model.entity.News))")
    public void updateNewsPointcut(){}

    @Before(value = "updateNewsPointcut()")
    public void updateNewsAdvice() {
        String currentUserName = RequestParser.getCurrentUserName();
        User currentUser = userService.findUserByName(currentUserName);
        long targetNewsId = RequestParser.getIdFromRequest();
        News targetNews = newsService.findById(targetNewsId);
        if (!targetNews.getUser().getId().equals(currentUser.getId())) {
            throw new UserNotEqualsAuthorException("Вы не можете изменять новость другого пользователя!");
        }
    }

    @Pointcut("execution(* com.example.newsservice.service.NewsServiceImpl.deleteById(Long))")
    public void deleteNewsPointcut(){}

    @Before(value = "deleteNewsPointcut()")
    public void deleteNewsAdvice() {
        String currentUserName = RequestParser.getCurrentUserName();
        User currentUser = userService.findUserByName(currentUserName);
        long targetNewsId = RequestParser.getIdFromRequest();
        News targetNews = newsService.findById(targetNewsId);
        if (!targetNews.getUser().getId().equals(currentUser.getId()) && currentUser.getRoles().stream()
                .noneMatch(role -> role.getAuthority().equals(RoleType.ROLE_ADMIN) || role.getAuthority().equals(RoleType.ROLE_MODERATOR))) {
            throw new UserNotEqualsAuthorException("Вы не можете удалять новость другого пользователя!");
        }
    }
}
