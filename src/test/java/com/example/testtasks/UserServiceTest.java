package com.example.testtasks;

import com.example.testtasks.dto.UserDTO;
import com.example.testtasks.entity.User;
import com.example.testtasks.repositories.UserRepository;
import com.example.testtasks.security.JWTGenerator;
import com.example.testtasks.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Key;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

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

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // Act
        User result = userService.get(id);

        // Assert
        assertEquals(user, result);
    }

    @Test
    void testSaveUser() {
        // Arrange
        UserDTO request = new UserDTO();
        request.setUsername("test");
        request.setPassword("test");

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());

        when(passwordEncoder.encode(request.getPassword())).thenReturn(request.getPassword());
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        userService.saveUser(request);

        // Assert
        verify(userRepository).save(user);
        assertEquals(request.getUsername(), user.getUsername());
        assertEquals(request.getPassword(), user.getPassword());
    }
}
