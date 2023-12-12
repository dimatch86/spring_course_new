package com.example.tasktracker.service;

import com.example.tasktracker.entity.Task;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.repository.TaskRepository;
import com.example.tasktracker.util.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    public Mono<Task> findById(String id) {
        return zipTaskMono(taskRepository.findById(id));
    }

    public Flux<Task> findAll() {
        return taskRepository.findAll().flatMap(task -> zipTaskMono(Mono.just(task)));
    }

    public Mono<Task> saveTask(Task task) {
        return zipTaskMono(taskRepository.save(task));
    }

    public Mono<Task> update(String id, Task task) {
        return findById(id).flatMap(taskForUpdate -> {
            BeanUtil.copyNonNullProperties(task, taskForUpdate);
            return zipTaskMono(taskRepository.save(taskForUpdate));
        });
    }

    public Mono<Task> addObserver(String taskId, String observerId) {
        return taskRepository.findById(taskId)
        .flatMap(task -> {
            if (StringUtils.hasText(observerId)) {
                task.getObserverIds().add(observerId);
            }
            return taskRepository.save(task);
        });
    }

    public Mono<Void> deleteById(String id) {
        return taskRepository.deleteById(id);
    }

    private Mono<Task> zipTaskMono(Mono<Task> taskMono) {

        Mono<User> authorMono = taskMono.map(Task::getAuthorId).flatMap(userService::findById).defaultIfEmpty(new User());
        Mono<User> assigneeMono = taskMono.map(Task::getAssigneeId).flatMap(userService::findById).defaultIfEmpty(new User());
        Mono<Set<User>> observersSetMono = taskMono.map(Task::getObserverIds)
                .flatMap(userService::findAllById).defaultIfEmpty(Collections.emptySet());

        return Mono.zip(taskMono, authorMono, assigneeMono, observersSetMono).map(tuple -> {
            Task task = tuple.getT1();
            User author = tuple.getT2();
            User assignee = tuple.getT3();
            Set<User> observers = tuple.getT4();

            task.setAuthor(author);
            task.setAssignee(assignee);
            task.setObservers(observers);
            return task;
        });
    }
}
