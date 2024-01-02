package com.expandapis.productcatalog;

import com.expandapis.productcatalog.controllers.LogInController;
import com.expandapis.productcatalog.dto.AuthResponseDTO;
import com.expandapis.productcatalog.dto.UserDTO;
import com.expandapis.productcatalog.entity.Role;
import com.expandapis.productcatalog.entity.User;
import com.expandapis.productcatalog.security.AuthenticationProviderImpl;
import com.expandapis.productcatalog.security.JWTGenerator;
import com.expandapis.productcatalog.services.UserServiceImp;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class LogInControllerTest {

    @Mock
    private AuthenticationProviderImpl authenticationProvider;

    @Mock
    private JWTGenerator tokenGenerator;

    @Mock
    private UserServiceImp userService;

    @InjectMocks
    private LogInController logInController;

    @Test
    void testAuthenticate() {
        // Arrange
        UserDTO userSignUpRequest = new UserDTO();
        userSignUpRequest.setUsername("testUser");
        userSignUpRequest.setPassword("testPassword");
        userSignUpRequest.setRole(Role.ROLE_USER);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userSignUpRequest.getUsername(),
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
    void testAdd() {
        // Arrange
        UserDTO userSignUpRequest = new UserDTO();
        userSignUpRequest.setUsername("testUser");
        userSignUpRequest.setPassword("testPassword");
        userSignUpRequest.setRole(Role.ROLE_USER);

        // Act
        ResponseEntity<String> responseEntity = logInController.add(userSignUpRequest);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testGetList() {
        // Arrange
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setRole(Role.ROLE_USER);

        User user2 = new User();
        user1.setUsername("user2");
        user1.setPassword("password2");
        user1.setRole(Role.ROLE_USER);

        List<User> userList = Arrays.asList(user1, user2);
        Mockito.when(userService.getList()).thenReturn(userList);

        // Act
        ResponseEntity<List<User>> responseEntity = logInController.getList();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userList, responseEntity.getBody());
    }
}
