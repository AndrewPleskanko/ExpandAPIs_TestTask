package com.expandapis.productcatalog.controller;

import com.expandapis.productcatalog.controllers.LogInController;
import com.expandapis.productcatalog.dto.UserDTO;
import com.expandapis.productcatalog.entity.Role;
import com.expandapis.productcatalog.entity.User;
import com.expandapis.productcatalog.security.AuthenticationProviderImpl;
import com.expandapis.productcatalog.security.JWTGenerator;
import com.expandapis.productcatalog.services.UserServiceImp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.testcontainers.junit.jupiter.Container;

@Testcontainers
@SpringBootTest
public class LogInControllerIntegrationTest {

    @Mock
    private AuthenticationProviderImpl authenticationProvider;
    @Mock
    private JWTGenerator tokenGenerator;
    @Mock
    private UserServiceImp userService;
    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer(DockerImageName.parse("postgres:16-alpine"));
    @InjectMocks
    private LogInController logInController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(logInController).build();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.jpa.generate-ddl", () -> true);
    }

    @Test
    public void authenticateTest() throws Exception {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test");
        userDTO.setPassword("test");
        // Act
        when(authenticationProvider.authenticate(any())).thenReturn(null);
        when(tokenGenerator.generateToken(any())).thenReturn("test-token");
        // Assert
        mockMvc.perform(post("/users/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", is("test-token")));
    }

    @Test
    public void addNewUserTest() throws Exception {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("newUser");
        userDTO.setPassword("password");
        userDTO.setRole(Role.ROLE_USER);
        // Act
        mockMvc.perform(post("/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void getListTest() throws Exception {
        // Arrange
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setRole(Role.ROLE_USER);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword("password2");
        user2.setRole(Role.ROLE_USER);

        User user3 = new User();
        user3.setUsername("user3");
        user3.setPassword("password3");
        user3.setRole(Role.ROLE_ADMIN);

        List<User> userList = Arrays.asList(user1, user2, user3);
        // Act
        when(userService.getList()).thenReturn(userList);
        // Assert
        mockMvc.perform(get("/users/all"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[*].username", hasItems("user1", "user2", "user3")));
    }

    private String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
