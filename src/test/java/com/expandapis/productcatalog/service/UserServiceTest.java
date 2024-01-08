package com.expandapis.productcatalog.service;

import com.expandapis.productcatalog.entity.Role;
import com.expandapis.productcatalog.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.expandapis.productcatalog.dto.UserDTO;
import com.expandapis.productcatalog.repositories.UserRepository;
import com.expandapis.productcatalog.services.UserServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImp userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserById() {
        // Arrange
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setUsername("test");
        user.setPassword("test");
        user.setRole(Role.ROLE_USER);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // Act
        User result = userService.get(id);

        // Assert
        assertEquals(user, result);
    }

    @Test
    void testSaveUser() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setPassword("testPassword");
        userDTO.setRole(Role.ROLE_USER);

        // When
        userService.saveUser(userDTO);

        // Then
        List<User> userList = userRepository.findAll();
        assertEquals(1, userList.size());

        User savedUser = userList.get(0);
        assertNotNull(savedUser.getId());
        assertEquals("testUser", savedUser.getUsername());
        assertEquals("ROLE_USER", savedUser.getRole());
        assertTrue(passwordEncoder.matches("testPassword", savedUser.getPassword()));
    }
}