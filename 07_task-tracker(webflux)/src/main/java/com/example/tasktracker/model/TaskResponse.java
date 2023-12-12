package com.example.tasktracker.model;

import com.example.tasktracker.entity.TaskStatus;
import com.example.tasktracker.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskResponse {

    private String id;
    private String name;
    private String description;
    private TaskStatus status;
    private User author;
    private User assignee;
    private Set<User> observers;
}
