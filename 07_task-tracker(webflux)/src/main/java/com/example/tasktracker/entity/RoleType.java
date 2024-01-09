package com.example.tasktracker.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RoleType {

    ROLE_USER("USER"), ROLE_MANAGER("MANAGER");

    private final String role;
}
