package com.example.tasktracker.repository;

import com.example.tasktracker.entity.Task;
import com.example.tasktracker.model.TaskResponse;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TaskRepository extends ReactiveMongoRepository<Task, String> {

    Mono<TaskResponse> findByName(String name);
}
