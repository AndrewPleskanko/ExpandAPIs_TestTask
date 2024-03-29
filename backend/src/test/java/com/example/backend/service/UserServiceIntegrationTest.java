package com.example.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.dto.UserDto;
import com.example.backend.entity.Role;
import com.example.backend.entity.User;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.UserServiceImpl;
import com.example.backend.utils.UserTestUtils;

@SpringBootTest

public class UserServiceIntegrationTest extends BaseServiceTest {

    private List<User> userList;
    private User user1;
    private User user2;

    private UserDto userDtoUser;
    private UserDto userDtoAdmin;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        //Given
        user1 = new User(1L, "test1", "test123", "presF@gmail.com", Role.ROLE_USER);
        user2 = new User(2L, "test2", "test234", "test2@gmail.com", Role.ROLE_ADMIN);
        userDtoUser = UserTestUtils.createUserDto("john", "123", Role.ROLE_USER, "testUser@gmail.com");
        userDtoAdmin = UserTestUtils.createUserDto("admin", "admin", Role.ROLE_ADMIN, "testAdmin@gmail.com");
        userList = Arrays.asList(user1, user2);
    }

    @Test
    @DisplayName("Get User by ID")
    @Transactional
    void getUserById_returnsUser_expectGetUerId() {
        // Given
        userRepository.save(user1);
        Long id = user1.getId();

        // When
        User result = userService.get(id);

        // Then
        assertEquals(user1.getId(), result.getId());
        assertEquals(user1.getUsername(), result.getUsername());
        assertEquals(user1.getPassword(), result.getPassword());
        assertEquals(user1.getEmail(), result.getEmail());
        assertEquals(user1.getRole(), result.getRole());
    }

    @Test
    @DisplayName("Save User with Valid UserDTO")
    @Transactional
    void saveUser_WithValidUserDto_SavesUserToRepository() {
        //Given
        int expectedSize = 2;
        // When
        userService.saveUser(userDtoUser);
        userService.saveUser(userDtoAdmin);
        List<User> userListFromRepository = userService.getList();

        // Then
        assertEquals(expectedSize, userListFromRepository.size());
        User firstUser = userListFromRepository.get(0);
        assertNotNull(firstUser.getId());
        assertEquals(userDtoUser.getUsername(), firstUser.getUsername());
        assertEquals(userDtoUser.getRole(), firstUser.getRole());
        assertEquals(userDtoUser.getEmail(), firstUser.getEmail());
        assertTrue(passwordEncoder.matches(userDtoUser.getPassword(), firstUser.getPassword()));
    }

    @Test
    @DisplayName("Get List of Users")
    @Transactional
    void getList_withMultipleUsers_returnsUserList() {
        // Given
        userRepository.save(user1);
        userRepository.save(user2);

        // When
        List<User> usersInService = userService.getList();

        // Then
        assertEquals(2, usersInService.size());
        assertTrue(usersInService.contains(userList.get(0)));
        assertTrue(usersInService.contains(userList.get(1)));
    }

    @Test
    @DisplayName("Find User by Username")
    @Transactional
    void findByUsername_withExistingUsername_returnsUser() {
        // Given
        userRepository.save(user1);

        // When
        User firstUser = userService.findByUsername(user1.getUsername());

        // Then
        assertEquals(user1.getUsername(), firstUser.getUsername());
        assertEquals(user1.getRole(), firstUser.getRole());
        assertEquals(user1.getEmail(), firstUser.getEmail());
        assertEquals(user1.getPassword(), firstUser.getPassword());
    }
}