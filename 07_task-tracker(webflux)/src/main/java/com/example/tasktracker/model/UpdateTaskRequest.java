package com.example.tasktracker.model;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateTaskRequest {

    private String name;
    private String description;
    @Pattern(regexp = "TODO|IN_PROGRESS|DONE", message = "Field must be IN_PROGRESS or DONE")
    private String taskStatus;
    private String assigneeId;
}
