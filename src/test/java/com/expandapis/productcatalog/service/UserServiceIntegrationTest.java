package com.expandapis.productcatalog.service;

import com.expandapis.productcatalog.entity.Role;
import com.expandapis.productcatalog.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import com.expandapis.productcatalog.repositories.UserRepository;
import com.expandapis.productcatalog.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Testcontainers
public class UserServiceIntegrationTest {

    private List<User> userList;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

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

    @BeforeEach
    void setUp() {
        //Given
        User user1 = new User(1L, "test1", "test123", Role.ROLE_USER);
        User user2 = new User(2L, "test2", "test234", Role.ROLE_ADMIN);
        userList = Arrays.asList(user1, user2);
    }

    @Test
    @DisplayName("Get User by ID")
    void getUserById_returnsUser_existingId() {
        // Given
        Long id = 1L;
        userRepository.save(userList.get(0));

        // When
        User result = userService.get(id);

        // Then
        assertEquals(userList.get(0), result);
    }

    @Test
    @DisplayName("Save User with Valid UserDTO")
    void saveUser_WithValidUserDTO_SavesUserToRepository() {
        // Given
        User savedUser = userList.get(0);

        // When
        userRepository.save(savedUser);

        // Then
        List<User> userListFromRepository  = userRepository.findAll();
        assertEquals(1, userListFromRepository .size());

        assertNotNull(userListFromRepository .get(0).getId());
        assertEquals("test1", userListFromRepository .get(0).getUsername());
        assertEquals(Role.ROLE_USER, userListFromRepository .get(0).getRole());
        assertEquals("test123", userListFromRepository .get(0).getPassword());
    }

    @Test
    @DisplayName("Get List of Users")
    void getList_withMultipleUsers_returnsUserList() {
        // Given
        userRepository.save(userList.get(0));
        userRepository.save(userList.get(1));

        // When
        List<User> usersInService = userService.getList();

        // Then
        assertEquals(2, usersInService.size());
        assertTrue(usersInService.contains(userList.get(0)));
        assertTrue(usersInService.contains(userList.get(1)));
    }

    @Test
    @DisplayName("Find User by Username")
    void findByUsername_withExistingUsername_returnsUser() {
        // Given
        userRepository.save(userList.get(0));

        // When
        User result = userService.findByUsername(userList.get(0).getUsername());

        // Then
        assertEquals(userList.get(0), result);
    }
}