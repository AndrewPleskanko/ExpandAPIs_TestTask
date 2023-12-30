package com.expandapis.productcatalog.controllers;

import com.expandapis.productcatalog.dto.AuthResponseDTO;
import com.expandapis.productcatalog.dto.UserDTO;
import com.expandapis.productcatalog.security.AuthenticationProviderImplementation;
import com.expandapis.productcatalog.security.JWTGenerator;
import com.expandapis.productcatalog.services.UserServiceImp;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * A controller for working with users
 */
@Slf4j
@RestController
@RequestMapping("/users")
@Api(value = "User Controller", tags = "User Controller", description = "REST API for managing users")
@RequiredArgsConstructor
public class LogInController {
    private final AuthenticationProviderImplementation authenticationProvider;
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
    public ResponseEntity<AuthResponseDTO> authenticate(@RequestBody UserDTO userSignUpRequest) {
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
    public ResponseEntity<String> add(@RequestBody UserDTO userSignUpRequest) {
        userService.saveUser(userSignUpRequest);
        log.info("User added: " + userSignUpRequest.getUsername());
        return ResponseEntity.ok().build();
    }
}