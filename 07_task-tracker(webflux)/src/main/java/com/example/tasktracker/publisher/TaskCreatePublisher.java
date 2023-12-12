package com.example.tasktracker.publisher;

import com.example.tasktracker.model.TaskResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

@Component
public class TaskCreatePublisher {

    private final Sinks.Many<TaskResponse> taskDtoUpdatesSink;

    public TaskCreatePublisher() {
        this.taskDtoUpdatesSink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public void publish(TaskResponse taskResponse) {
        taskDtoUpdatesSink.tryEmitNext(taskResponse);
    }

    public Sinks.Many<TaskResponse> getUpdatesSink() {
        return taskDtoUpdatesSink;
    }
}
