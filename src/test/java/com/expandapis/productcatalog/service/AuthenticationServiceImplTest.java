package com.expandapis.productcatalog.service;

import com.expandapis.productcatalog.dto.AuthResponseDto;
import com.expandapis.productcatalog.dto.UserDto;
import com.expandapis.productcatalog.entity.Role;
import com.expandapis.productcatalog.services.UserServiceImpl;
import com.expandapis.productcatalog.services.interfaces.AuthenticationService;
import com.expandapis.productcatalog.utils.UserTestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

import static com.expandapis.productcatalog.utils.TokenGenerationUtils.extractTokenFromResponse;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class AuthenticationServiceImplTest {

    private UserDto userDtoUser;
    private UserDto userDtoAdmin;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer(DockerImageName.parse("postgres:16-alpine"));

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.jpa.generate-ddl", () -> true);
    }

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setup() {
        // Given
        userDtoUser = UserTestUtils.createUserDto("john", "123", Role.ROLE_USER);
        userDtoAdmin = UserTestUtils.createUserDto("admin", "admin", Role.ROLE_ADMIN);
    }

    @Test
    public void testAuthenticateUser_ReturnsValidToken() throws IOException {
        // Given
        userService.saveUser(userDtoUser);
        userService.saveUser(userDtoAdmin);

        // When
        AuthResponseDto result1 = authenticationService.authenticateUser(userDtoUser);
        ObjectMapper mapper1 = new ObjectMapper();

        AuthResponseDto result2 = authenticationService.authenticateUser(userDtoAdmin);
        ObjectMapper mapper2 = new ObjectMapper();

        String token1 = extractTokenFromResponse(mapper1.writeValueAsString(result1));
        String token2 = extractTokenFromResponse(mapper2.writeValueAsString(result2));

        // Then
        assertNotNull(token1);
        assertNotNull(token2);
        assertNotEquals(token1,token2);
    }
}