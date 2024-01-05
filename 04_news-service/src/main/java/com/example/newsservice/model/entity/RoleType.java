package com.example.newsservice.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RoleType {

    ROLE_USER("USER"), ROLE_ADMIN("ADMIN"), ROLE_MODERATOR("MODERATOR");

    private final String role;
}
