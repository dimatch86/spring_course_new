package com.example.bookshelf;

import com.example.bookshelf.dto.BookResponse;
import com.example.bookshelf.dto.UpsertBookRequest;
import com.example.bookshelf.mapstruct.BookMapper;
import com.example.bookshelf.repository.BookRepository;
import com.example.bookshelf.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest
@Sql("classpath:db/init.sql")
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
public abstract class AbstractTest {

    public static final Long UPDATED_ID = 1L;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    protected ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    protected BookService bookService;
    @Autowired
    protected BookRepository bookRepository;

    @Autowired
    protected BookMapper bookMapper;
    @RegisterExtension
    protected static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort()).build();

    protected static PostgreSQLContainer postgreSQLContainer;

    @Container
    protected static final RedisContainer REDIS_CONTAINER =
            new RedisContainer(DockerImageName.parse("redis:7.0.12")).withExposedPorts(6379).withReuse(true);

    static {
        DockerImageName postgres = DockerImageName.parse("postgres:12.3");
        postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer(postgres)
                .withReuse(true);
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    public static void registerProperties(DynamicPropertyRegistry registry) {
        String jdbcUrl = postgreSQLContainer.getJdbcUrl();
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.url", () -> jdbcUrl);
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());
    }

    @BeforeEach
    public void before() throws Exception {
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
        stubClient();
    }

    @AfterEach
    public void afterEach() {
        wireMockServer.resetAll();
    }

    private void stubClient() throws Exception {
        //objectMapper.registerModule(new JavaTimeModule());
        List<BookResponse> findAllResponse = new ArrayList<>();
        findAllResponse.add(new BookResponse(1L, "Book1", "Pushkin", "hunting"));
        findAllResponse.add(new BookResponse(2L, "Book2", "Gogol", "hunting"));

        wireMockServer.stubFor(WireMock.get("/book/category?category=hunting").willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(findAllResponse)).withStatus(200)));

        BookResponse findByTitleResponse = new BookResponse(1L, "Book1", "Pushkin", "fairy");
        wireMockServer.stubFor(WireMock.get("/book/filter?title=" + findByTitleResponse.getTitle() + "&author=" + findByTitleResponse.getAuthor())
                .willReturn(aResponse()
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(objectMapper.writeValueAsString(findByTitleResponse))
                .withStatus(200)));

        UpsertBookRequest request = new UpsertBookRequest("Book3", "Shishov", "hunting");

        BookResponse createResponseBody = new BookResponse(3L, "Book3", "Shishov", "mistery");

        wireMockServer.stubFor(WireMock.post("/book")
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(request)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(createResponseBody))
                        .withStatus(201)));

        UpsertBookRequest updateRequest = new UpsertBookRequest();
        updateRequest.setTitle("Book667");
        updateRequest.setAuthor("Gregor");
        updateRequest.setCategory("hunting");
        BookResponse updateResponseBody = new BookResponse(3L, "Book667", "Gregor", "hunting");

        wireMockServer.stubFor(WireMock.put("/book/" + UPDATED_ID)
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(updateRequest)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(updateResponseBody))
                        .withStatus(200)));

        wireMockServer.stubFor(WireMock.delete("/book/" + UPDATED_ID)
                .willReturn(aResponse().withStatus(204)));
    }

}
