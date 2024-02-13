package com.example.backend.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Testcontainers
public abstract class BaseServiceTest {

    public static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"))
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa")
            .withReuse(true);

    @BeforeAll
    public static void startContainer() {
        postgresContainer.start();
    }

    @AfterAll
    public static void stopContainer() {
        postgresContainer.stop();
    }
}

