package com.example.newsservice.aop;

import com.example.newsservice.exception.EntityNotFoundException;
import com.example.newsservice.exception.UserNotEqualsAuthorException;
import com.example.newsservice.model.entity.RoleType;
import com.example.newsservice.model.entity.User;
import com.example.newsservice.repository.UserRepository;
import com.example.newsservice.utils.RequestParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class UserServiceAspect {
    private final UserRepository userRepository;

    @Before("@annotation(Authorizable)")
    public void userServiceAdvice() {
        String currentUserName = RequestParser.getCurrentUserName();
        User currentUser = userRepository.findUserByName(currentUserName).orElseThrow();
        long targetUserId = RequestParser.getIdFromRequest();
        User targetUser = userRepository.findById(targetUserId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (!currentUserName.equals(targetUser.getName()) && currentUser.getRoles().stream()
                .noneMatch(role -> role.getAuthority().equals(RoleType.ROLE_ADMIN) || role.getAuthority().equals(RoleType.ROLE_MODERATOR))) {
            throw new UserNotEqualsAuthorException("Вы не можете изменять или удалять данные другого пользователя!");
        }
    }
}
