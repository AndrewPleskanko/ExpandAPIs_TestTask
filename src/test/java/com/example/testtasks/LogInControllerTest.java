package com.example.testtasks;

import com.example.testtasks.controllers.LogInController;
import com.example.testtasks.dto.AuthResponseDTO;
import com.example.testtasks.dto.UserDTO;
import com.example.testtasks.security.AuthenticationProviderImplementation;
import com.example.testtasks.security.JWTGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class LogInControllerTest {

    @Mock
    private AuthenticationProviderImplementation authenticationProvider;

    @Mock
    private JWTGenerator tokenGenerator;

    @InjectMocks
    private LogInController logInController;

    @Test
    public void testLogin() {
        // Arrange
        UserDTO userSignUpRequest = new UserDTO();
        userSignUpRequest.setUsername("testUser");
        userSignUpRequest.setPassword("testPassword");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userSignUpRequest.getUsername(),
                userSignUpRequest.getPassword());
        Mockito.when(authenticationProvider.authenticate(authenticationToken)).thenReturn(authenticationToken);

        Mockito.when(tokenGenerator.generateToken(authenticationToken)).thenReturn("testToken");

        // Act
        ResponseEntity<AuthResponseDTO> responseEntity = logInController.authenticate(userSignUpRequest);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("testToken", responseEntity.getBody().getAccessToken());
    }

    @Test
    public void testAdd() {
        // Arrange
        UserDTO userSignUpRequest = new UserDTO();
        userSignUpRequest.setUsername("testUser");
        userSignUpRequest.setPassword("testPassword");

        // Act
        ResponseEntity<String> responseEntity = logInController.add(userSignUpRequest);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
