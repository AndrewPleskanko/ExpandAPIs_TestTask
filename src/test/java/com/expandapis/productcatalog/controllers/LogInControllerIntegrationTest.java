package com.expandapis.productcatalog.controllers;

import static com.expandapis.productcatalog.utils.TokenGenerationUtils.asJsonString;
import static com.expandapis.productcatalog.utils.TokenGenerationUtils.extractTokenFromResponse;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.expandapis.productcatalog.dto.UserDto;
import com.expandapis.productcatalog.entity.Role;
import com.expandapis.productcatalog.entity.User;
import com.expandapis.productcatalog.security.JwtAuthEntryPoint;
import com.expandapis.productcatalog.security.JwtGenerator;
import com.expandapis.productcatalog.security.UserDetailsServiceImpl;
import com.expandapis.productcatalog.security.WebSecurityConfig;
import com.expandapis.productcatalog.services.AuthenticationServiceImpl;
import com.expandapis.productcatalog.services.UserServiceImpl;
import com.expandapis.productcatalog.utils.UserTestUtils;

@WebMvcTest(LogInController.class)
@Import({WebSecurityConfig.class, JwtGenerator.class, JwtAuthEntryPoint.class, AuthenticationServiceImpl.class})
public class LogInControllerIntegrationTest {

    private UserDto userDtoUser;
    private UserDto userDtoAdmin;
    private List<User> userList;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @Autowired
    AuthenticationServiceImpl authenticationService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @MockBean
    UserServiceImpl userService;

    @BeforeEach
    public void setup() {
        // Given
        userDtoUser = UserTestUtils.createUserDto("john", "123", Role.ROLE_USER);
        userDtoAdmin = UserTestUtils.createUserDto("admin", "admin", Role.ROLE_ADMIN);
        userList = UserTestUtils.createUsers(2, Role.ROLE_USER);
        userList.add(UserTestUtils.createUser("user3", "password3", Role.ROLE_ADMIN));
    }

    @Test
    @DisplayName("Should return a valid access token for a valid user")
    public void testAuthenticate_returnsValidToken() throws Exception {
        // When
        mockLoadUserByUsername();

        // Then
        MvcResult authenticateResult = mockMvc.perform(post("/users/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDtoUser)).with(csrf()))
                .andExpect(status().isOk())
                .andReturn();
        assertNotNull(authenticateResult);
    }

    @Test
    @DisplayName("Should add a new user and return the user details")
    public void testAddNewUser_expectSuccess() throws Exception {
        // When
        doNothing().when(userService).saveUser(any(UserDto.class));
        mockLoadUserByUsername();

        String token = generateAuthToken(userDtoUser);

        //Then
        mockMvc.perform(post("/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(asJsonString(userDtoUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(userDtoUser.getUsername()))
                .andExpect(jsonPath("$.password").value(userDtoUser.getPassword()))
                .andExpect(jsonPath("$.role").value(userDtoUser.getRole().toString()));

        verify(userService, times(1)).saveUser(any(UserDto.class));
    }

    @Test
    @DisplayName("Should return a list of all users")
    public void testGetList_returnsAllUsers() throws Exception {
        //When
        when(userService.getList()).thenReturn(userList);
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new User("admin",
                        "$2a$10$H7Tw60M/fe.vwwBgxCTvYuDrHGOhJ6s.yxArIrjsgfQOwL6lR2RdO", Role.ROLE_ADMIN));
        when(authenticationProvider.authenticate(any(Authentication.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(
                        userDtoAdmin.getUsername(),
                        userDtoAdmin.getPassword()));
        String token = generateAuthToken(userDtoAdmin);

        //Then
        mockMvc.perform(get("/users/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[*].username", hasItems("user1", "user2", "user3")))
                .andExpect(jsonPath("$.[*].password", hasItems("password1", "password2", "password3")))
                .andExpect(jsonPath("$.[*].role", containsInAnyOrder(Role.ROLE_USER.toString(),
                        Role.ROLE_USER.toString(), Role.ROLE_ADMIN.toString())));
    }

    @Test
    @DisplayName("Should return 403 Forbidden for an unauthorized user")
    public void testGetList_returnsForbidden() throws Exception {
        //Given
        when(userService.getList()).thenReturn(userList);
        mockLoadUserByUsername();

        String token = generateAuthToken(userDtoUser);

        //Then
        mockMvc.perform(get("/users/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return 500 Internal Server Error for an existing user")
    public void testAddNewUser_returnsServerError() throws Exception {
        //Given
        doThrow(new DataIntegrityViolationException("User already exists"))
                .when(userService).saveUser(any(UserDto.class));
        mockLoadUserByUsername();
        String token = generateAuthToken(userDtoUser);

        //Then
        mockMvc.perform(post("/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(asJsonString(userDtoUser)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("User already exists"));
    }

    @Test
    @DisplayName("Should return 401 Unauthorized for an invalid user")
    public void testAuthenticate_returnsUnauthorized() throws Exception {
        //Given
        UserDto invalidUserDto = new UserDto();
        invalidUserDto.setUsername("invalid");
        invalidUserDto.setRole(Role.ROLE_USER);

        //When
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenThrow(new UsernameNotFoundException("User not found"));
        when(authenticationProvider.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        //Then
        mockMvc.perform(post("/users/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidUserDto)).with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    private String generateAuthToken(UserDto userDto) throws Exception {
        MvcResult authenticateResult = mockMvc.perform(post("/users/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto)).with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        String response = authenticateResult.getResponse().getContentAsString();
        return extractTokenFromResponse(response);
    }

    private void mockLoadUserByUsername() {
        //When
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new User("john",
                        "$2a$10$5vvbROzmmXGkfPVaZTyNjODxOEBkobazyMcGWaoSKugSaMLSER0Pq", Role.ROLE_USER));
        when(authenticationProvider.authenticate(any(Authentication.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(
                        userDtoUser.getUsername(),
                        userDtoUser.getPassword()));
    }
}
