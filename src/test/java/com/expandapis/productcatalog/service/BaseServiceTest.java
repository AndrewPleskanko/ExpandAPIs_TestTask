package com.expandapis.productcatalog.service;

import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

import java.time.Duration;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Testcontainers
public class BaseServiceTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer(DockerImageName.parse("postgres:16-alpine"));

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.jpa.generate-ddl", () -> true);
    }

    @BeforeAll
    static void startContainers() {
        log.info("Starting PostgreSQL container...");
        postgresContainer.start();
        await().atMost(Duration.ofSeconds(60))
                .until(postgresContainer::isRunning);
        log.info("PostgreSQL container started.");
    }
}
