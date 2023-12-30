package com.expandapis.productcatalog;

import com.expandapis.productcatalog.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.expandapis.productcatalog.dto.UserDTO;
import com.expandapis.productcatalog.repositories.UserRepository;
import com.expandapis.productcatalog.services.UserServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

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