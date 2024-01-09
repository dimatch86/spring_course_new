package com.example.tasktracker.mapstruct;

import com.example.tasktracker.entity.Task;
import com.example.tasktracker.model.TaskResponse;
import com.example.tasktracker.model.UpdateTaskRequest;
import com.example.tasktracker.model.UpsertTaskRequest;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(TaskMapperDelegate.class)
public interface TaskMapper {

    Task requestToTask(UpsertTaskRequest request, String userId);
    Task requestToTask(UpdateTaskRequest request);

    TaskResponse taskToResponse(Task task);
}
