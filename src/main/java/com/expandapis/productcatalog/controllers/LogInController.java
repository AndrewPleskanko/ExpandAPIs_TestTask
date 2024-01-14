package com.expandapis.productcatalog.controllers;

import com.expandapis.productcatalog.dto.AuthResponseDTO;
import com.expandapis.productcatalog.dto.UserDTO;
import com.expandapis.productcatalog.entity.User;
import com.expandapis.productcatalog.security.AuthenticationProviderImpl;
import com.expandapis.productcatalog.security.JWTGenerator;
import com.expandapis.productcatalog.services.UserServiceImp;
import io.swagger.annotations.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A controller for working with users
 */
@Slf4j
@RestController
@RequestMapping("/users")
@Api(value = "User Controller", tags = "User Controller", description = "REST API for managing users")
@RequiredArgsConstructor
public class LogInController {

    private final AuthenticationProviderImpl authenticationProvider;
    private final JWTGenerator tokenGenerator;
    private final UserServiceImp userService;

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param userSignUpRequest The user authentication request.
     * @return ResponseEntity containing the authentication response.
     */
    @PostMapping("authenticate")
    @ApiOperation(value = "Authenticate user and generate JWT token", response = AuthResponseDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully authenticated and generated token"),
            @ApiResponse(code = 401, message = "Unauthorized, authentication failed")
    })
    public ResponseEntity<AuthResponseDTO> authenticate(@Valid @RequestBody UserDTO userSignUpRequest) {
        log.info("Received authentication request for user: " + userSignUpRequest.getUsername());

        Authentication auth = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(
                userSignUpRequest.getUsername(),
                userSignUpRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = tokenGenerator.generateToken(auth);
        log.info("Generated token for user: " + userSignUpRequest.getUsername());
        return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
    }

    /**
     * Adds a new user.
     *
     * @param userSignUpRequest The user information for registration.
     * @return ResponseEntity indicating the result of the user addition.
     */
    @PostMapping("add")
    @ApiOperation(value = "Add a new user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added user"),
            @ApiResponse(code = 500, message = "Internal Server Error, error adding user")
    })
    public ResponseEntity<UserDTO> add(@Valid @RequestBody UserDTO userSignUpRequest) {
        userService.saveUser(userSignUpRequest);
        log.info("User added: " + userSignUpRequest.getUsername());
        return ResponseEntity.ok(userSignUpRequest);
    }

    /**
     * Get all users.
     *
     * @return ResponseEntity indicating the result of the user addition.
     */
    @GetMapping("all")
    @ApiOperation(value = "Get a user list")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get user list"),
            @ApiResponse(code = 500, message = "No static resource access-denied")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getList() {
        List<User> userList = userService.getList();
        return ResponseEntity.ok(userList);
    }
}