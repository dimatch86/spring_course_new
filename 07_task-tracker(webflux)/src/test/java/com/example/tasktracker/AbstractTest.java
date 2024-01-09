package com.example.tasktracker;

import com.example.tasktracker.entity.RoleType;
import com.example.tasktracker.entity.Task;
import com.example.tasktracker.entity.TaskStatus;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.repository.TaskRepository;
import com.example.tasktracker.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@SpringBootTest
@Testcontainers
@AutoConfigureWebTestClient
public abstract class AbstractTest {

    protected static String FIRST_TASK_ID = UUID.randomUUID().toString();
    protected static String SECOND_TASK_ID = UUID.randomUUID().toString();
    protected static String AUTHOR_ID = UUID.randomUUID().toString();
    protected static String ASSIGNEE_ID = UUID.randomUUID().toString();

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.8")
            .withReuse(true);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }
    @Autowired
    protected WebTestClient webTestClient;
    @Autowired
    protected TaskRepository taskRepository;
    @Autowired
    protected UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.saveAll(
                List.of(
                        new User(AUTHOR_ID, "Author", "goldens", "authorEmail@mail.ru", Set.of(RoleType.ROLE_MANAGER)),
                        new User(ASSIGNEE_ID, "Assignee", "passwore", "assigneeEmail@mail.ru", Set.of(RoleType.ROLE_USER))
                )).collectList().block();
        taskRepository.saveAll(
                List.of(
                        new Task(FIRST_TASK_ID, "Task 1", "Test Task 1", Instant.now(), Instant.now(), TaskStatus.TODO, AUTHOR_ID, ASSIGNEE_ID, Set.of()),
                        new Task(SECOND_TASK_ID, "Task 2", "Test Task 2", Instant.now(), Instant.now(), TaskStatus.TODO, AUTHOR_ID, ASSIGNEE_ID, Set.of())))
                .collectList().block();
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll().block();
        taskRepository.deleteAll().block();
    }
}
