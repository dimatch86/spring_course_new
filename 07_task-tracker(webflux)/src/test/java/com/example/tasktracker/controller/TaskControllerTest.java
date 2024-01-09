package com.example.tasktracker.controller;

import com.example.tasktracker.AbstractTest;
import com.example.tasktracker.entity.RoleType;
import com.example.tasktracker.entity.TaskStatus;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.model.TaskResponse;
import com.example.tasktracker.model.UpdateTaskRequest;
import com.example.tasktracker.model.UpsertTaskRequest;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class TaskControllerTest extends AbstractTest {

    @Test
    @WithMockUser
    void whenGetAllTasks_thenReturnsListOfTasksFromDatabase() {
        var expectedData = List.of(
                new TaskResponse(FIRST_TASK_ID, "Task 1", "Test Task 1", TaskStatus.TODO,
                        new User(AUTHOR_ID, "Author", "goldens", "authorEmail@mail.ru", Set.of(RoleType.ROLE_MANAGER)),
                        new User(ASSIGNEE_ID, "Assignee", "passwore", "assigneeEmail@mail.ru", Set.of(RoleType.ROLE_USER)), Set.of()),
                new TaskResponse(SECOND_TASK_ID, "Task 2", "Test Task 2", TaskStatus.TODO,
                        new User(AUTHOR_ID, "Author", "goldens", "authorEmail@mail.ru", Set.of(RoleType.ROLE_MANAGER)),
                        new User(ASSIGNEE_ID, "Assignee", "passwore", "assigneeEmail@mail.ru", Set.of(RoleType.ROLE_USER)), Set.of())
        );
        webTestClient.get().uri("/api/v1/tasks")
                .exchange().expectStatus().isOk()
                .expectBodyList(TaskResponse.class)
                .hasSize(2)
                .contains(expectedData.toArray(TaskResponse[]::new));
    }

    @Test
    @WithMockUser
    void whenGetTaskById_thenReturnsTaskById() {
        var expectedData = new TaskResponse(FIRST_TASK_ID, "Task 1", "Test Task 1", TaskStatus.TODO,
                new User(AUTHOR_ID, "Author", "goldens", "authorEmail@mail.ru", Set.of(RoleType.ROLE_MANAGER)),
                new User(ASSIGNEE_ID, "Assignee", "passwore", "assigneeEmail@mail.ru", Set.of(RoleType.ROLE_USER)), Set.of());
        webTestClient.get().uri("/api/v1/tasks/" + FIRST_TASK_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .isEqualTo(expectedData);
    }

    @Test
    @WithMockUser(username = "Author", roles = {"MANAGER"})
    void whenCreateTask_thenReturnsNewTaskWithIdAndPublishEvent() {
        StepVerifier.create(taskRepository.count())
                .expectNext(2L)
                .expectComplete()
                .verify();
        UpsertTaskRequest request = new UpsertTaskRequest("Task 3", "Test task 3", ASSIGNEE_ID);
        webTestClient.post().uri("/api/v1/tasks")
                .body(Mono.just(request), UpsertTaskRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .value(taskResponse -> assertNotNull(taskResponse.getId()));

        StepVerifier.create(taskRepository.count())
                .expectNext(3L)
                .expectComplete()
                .verify();

        webTestClient.get().uri("/api/v1/tasks/stream")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .returnResult(new ParameterizedTypeReference<ServerSentEvent<TaskResponse>>() {
                })
                .getResponseBody()
                .take(1)
                .as(StepVerifier::create)
                .consumeNextWith(taskResponseServerSentEvent -> {
                    var data = taskResponseServerSentEvent.data();
                    assertNotNull(data);
                    assertNotNull(data.getId());
                    assertEquals("Task 3", data.getName());
                })
                .thenCancel()
                .verify();
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    void whenUpdateTask_thenReturnsUpdatedTask() {
        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setName("New task name");
        request.setDescription("New description");
        request.setTaskStatus("IN_PROGRESS");
        request.setAssigneeId(ASSIGNEE_ID);

        webTestClient.put().uri("/api/v1/tasks/{id}", FIRST_TASK_ID)
                .body(Mono.just(request), UpdateTaskRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .value(taskResponse -> assertEquals("New task name", taskResponse.getName()));

        StepVerifier.create(taskRepository.findByName("Task 1"))
                .expectNextCount(0L)
                .verifyComplete();

        StepVerifier.create(taskRepository.findByName("New task name"))
                .expectNextCount(1L)
                .verifyComplete();
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    void whenDeleteById_thenRemoveTaskFromDatabase() {
        webTestClient.delete().uri("/api/v1/tasks/{id}", FIRST_TASK_ID)
                .exchange()
                .expectStatus().isNoContent();

        StepVerifier.create(taskRepository.count())
                .expectNext(1L)
                .expectComplete()
                .verify();
    }
}
