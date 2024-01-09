package com.example.tasktracker.mapstruct;

import com.example.tasktracker.entity.Task;
import com.example.tasktracker.entity.TaskStatus;
import com.example.tasktracker.model.TaskResponse;
import com.example.tasktracker.model.UpdateTaskRequest;
import com.example.tasktracker.model.UpsertTaskRequest;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public abstract class TaskMapperDelegate implements TaskMapper {

    @Override
    public Task requestToTask(UpsertTaskRequest request, String userId) {
        return Task.builder()
                .id(UUID.randomUUID().toString())
                .name(request.getName())
                .description(request.getDescription())
                .status(TaskStatus.TODO)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .authorId(userId)
                .assigneeId(request.getAssigneeId())
                .observerIds(Set.of())
                .build();
    }

    @Override
    public Task requestToTask(UpdateTaskRequest request) {

        return Task.builder()
                .name(request.getName())
                .description(request.getDescription())
                .status(TaskStatus.getTaskStatus(request.getTaskStatus()))
                .updatedAt(Instant.now())
                .assigneeId(request.getAssigneeId())
                .build();
    }

    @Override
    public TaskResponse taskToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .status(task.getStatus())
                .author(task.getAuthor())
                .assignee(task.getAssignee())
                .observers(task.getObservers())
                .build();
    }
}
