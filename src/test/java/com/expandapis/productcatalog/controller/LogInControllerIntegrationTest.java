package com.expandapis.productcatalog.controller;

import com.expandapis.productcatalog.controllers.LogInController;
import com.expandapis.productcatalog.dto.UserDTO;
import com.expandapis.productcatalog.entity.Role;
import com.expandapis.productcatalog.entity.User;
import com.expandapis.productcatalog.security.AuthenticationProviderImpl;
import com.expandapis.productcatalog.security.JWTGenerator;
import com.expandapis.productcatalog.services.UserServiceImp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LogInController.class)
public class LogInControllerIntegrationTest {

    private UserDTO userDTO;
    private List<User> userList;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationProviderImpl authenticationProvider;

    @MockBean
    private JWTGenerator tokenGenerator;

    @MockBean
    private UserServiceImp userService;

    @BeforeEach
    public void setup() {
        userDTO = new UserDTO();
        userDTO.setUsername("test");
        userDTO.setPassword("test");
        userDTO.setRole(Role.ROLE_USER);
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setRole(Role.ROLE_USER);
        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword("password2");
        user2.setRole(Role.ROLE_USER);
        User user3 = new User();
        user3.setUsername("user3");
        user3.setPassword("password3");
        user3.setRole(Role.ROLE_ADMIN);
        userList = Arrays.asList(user1, user2, user3);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return a valid access token for a valid user")
    public void testAuthenticate_returnsValidToken() throws Exception {
        // Given
        when(authenticationProvider.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken("test", "test"));
        when(tokenGenerator.generateToken(any())).thenReturn("test-token");

        // When-Then
        mockMvc.perform(post("/users/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDTO)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", is("test-token")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should add a new user and return the user details")
    public void testAddNewUser_expandSuccess() throws Exception {
        // Given
        doNothing().when(userService).saveUser(any(UserDTO.class));

        // When-Then
        mockMvc.perform(post("/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDTO)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(userDTO.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(userDTO.getPassword()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value(userDTO.getRole().toString()));
        verify(userService, times(1)).saveUser(any(UserDTO.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return a list of all users")
    public void testGetList_returnsAllUsers() throws Exception {
        // Given
        when(userService.getList()).thenReturn(userList);

        // When-Then
        mockMvc.perform(get("/users/all"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[*].username", hasItems("user1", "user2", "user3")))
                .andExpect(jsonPath("$.[*].password", hasItems("password1", "password2", "password3")))
                .andExpect(jsonPath("$.[*].role", containsInAnyOrder(Role.ROLE_USER.toString(), Role.ROLE_USER.toString(), Role.ROLE_ADMIN.toString())));}

    private String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
