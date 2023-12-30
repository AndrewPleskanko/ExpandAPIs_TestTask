package com.expandapis.productcatalog;

import com.expandapis.productcatalog.security.JWTGenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Key;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class JWTGeneratorTest {

    @Mock
    private Key key;

    @InjectMocks
    private JWTGenerator jwtGenerator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @WithMockUser(username = "testuser", password = "testpassword")
    void testGenerateToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = jwtGenerator.generateToken(authentication);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @WithMockUser(username = "testuser", password = "testpassword")
    void testGetUsernameFromJWT() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = jwtGenerator.generateToken(authentication);
        String username = jwtGenerator.getUsernameFromJWT(token);
        assertEquals("testuser", username);
    }

    @Test
    void testValidateToken() {
        String validToken = jwtGenerator.generateToken(mock(Authentication.class));
        String invalidToken = "invalid_token";

        assertTrue(jwtGenerator.validateToken(validToken));
        assertFalse(jwtGenerator.validateToken(invalidToken));
    }
}
