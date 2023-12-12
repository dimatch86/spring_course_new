package com.example.tasktracker.controller;

import com.example.tasktracker.mapstruct.TaskMapper;
import com.example.tasktracker.model.*;
import com.example.tasktracker.publisher.TaskCreatePublisher;
import com.example.tasktracker.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final TaskCreatePublisher publisher;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TaskResponse>> getById(@PathVariable String id) {
        return taskService.findById(id).map(taskMapper::taskToResponse).map(ResponseEntity::ok);
    }

    @GetMapping
    public Flux<TaskResponse> getAllTasks() {
        return taskService.findAll().map(taskMapper::taskToResponse);
    }

    @PostMapping
    public Mono<ResponseEntity<TaskResponse>> createTask(@RequestBody UpsertTaskRequest taskRequest) {
        return taskService.saveTask(taskMapper.requestToTask(taskRequest))
                .map(taskMapper::taskToResponse)
                .doOnSuccess(publisher::publish)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<TaskResponse>> updateTask(@PathVariable String id, @RequestBody @Valid UpdateTaskRequest request) {
        return taskService.update(id, taskMapper.requestToTask(request))
                .map(taskMapper::taskToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/observer/{id}")
    public Mono<ResponseEntity<TaskResponse>> addObserverToTask(@PathVariable String id, @RequestBody ObserverDto observerDto) {
        return taskService.addObserver(id, observerDto.getObserverId())
                .map(taskMapper::taskToResponse)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id) {
        return taskService.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<TaskResponse>> getTaskCreates() {
        return publisher.getUpdatesSink()
                .asFlux()
                .map(taskResponse -> ServerSentEvent.builder(taskResponse)
                        .build());
    }
}
