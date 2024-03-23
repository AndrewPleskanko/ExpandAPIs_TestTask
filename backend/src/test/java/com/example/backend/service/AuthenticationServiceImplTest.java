package com.example.backend.service;

import static com.example.backend.utils.TokenGenerationUtils.extractTokenFromResponse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.dto.AuthResponseDto;
import com.example.backend.dto.UserDto;
import com.example.backend.entity.Role;
import com.example.backend.services.UserServiceImpl;
import com.example.backend.services.interfaces.AuthenticationService;
import com.example.backend.utils.UserTestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class AuthenticationServiceImplTest extends BaseServiceTest {
    private UserDto userDtoUser;
    private UserDto userDtoAdmin;
    private ObjectMapper mapper;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setup() {
        // Given
        userDtoUser = UserTestUtils.createUserDto("john", "123", Role.ROLE_USER, "testUser@gmail.com");
        userDtoAdmin = UserTestUtils.createUserDto("admin", "admin", Role.ROLE_ADMIN,"testAdmin@gmail.com");
        mapper = new ObjectMapper();
    }

    @Test
    @Transactional
    public void testAuthenticateUser_ReturnsValidToken() throws IOException {
        userService.saveUser(userDtoUser);
        userService.saveUser(userDtoAdmin);
        // When
        AuthResponseDto result1 = authenticationService.authenticateUser(userDtoUser);

        AuthResponseDto result2 = authenticationService.authenticateUser(userDtoAdmin);
        String token1 = extractTokenFromResponse(mapper.writeValueAsString(result1));
        String token2 = extractTokenFromResponse(mapper.writeValueAsString(result2));

        // Then
        assertNotNull(token1);
        assertNotNull(token2);
        assertNotEquals(token1, token2);
    }
}