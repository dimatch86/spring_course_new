package com.example.tasktracker.model;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpsertUserRequest {
    private String id;

    private String userName;

    @Email
    private String email;
}
