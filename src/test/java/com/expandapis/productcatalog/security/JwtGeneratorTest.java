package com.expandapis.productcatalog.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.expandapis.productcatalog.service.BaseServiceTest;

@SpringBootTest
public class JwtGeneratorTest extends BaseServiceTest {
    @Autowired
    private JwtGenerator jwtGenerator;

    private Authentication authentication;

    @BeforeEach
    public void setup() {
        // Given
        authentication = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return "testUser";
            }
        };
    }

    @Test
    @DisplayName("Should generate a valid token for a given authentication")
    void testGenerateToken_returnsValidToken() {
        // When
        String token = jwtGenerator.generateToken(authentication);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Should extract username from JWT")
    void testGetUsernameFromJwt() {
        // Given
        String token = jwtGenerator.generateToken(authentication);

        // When
        String username = jwtGenerator.getUsernameFromJwt(token);

        // Then
        assertEquals("testUser", username);
    }

    @Test
    @DisplayName("Should validate a valid token")
    void testValidateToken() {
        // Given
        String token = jwtGenerator.generateToken(authentication);

        // When
        boolean isValid = jwtGenerator.validateToken(token);

        // Then
        assertTrue(isValid);
    }
}
