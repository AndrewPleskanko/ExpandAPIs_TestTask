package com.example.testtasks.controllers;

import com.example.testtasks.dto.AuthResponseDTO;
import com.example.testtasks.dto.UserDTO;
import com.example.testtasks.handler.GlobalExceptionHandler;
import com.example.testtasks.security.AuthenticationProviderImplementation;
import com.example.testtasks.security.JWTGenerator;
import com.example.testtasks.services.UserServiceImp;
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
    private final GlobalExceptionHandler globalExceptionHandler;

    private static final String AUTHENTICATION_REQUEST_RECEIVED = "Received authentication request for user: ";
    private static final String TOKEN_GENERATED_FOR_USER = "Generated token for user: ";
    private static final String USER_ADDED = "User added: ";

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
        try {
            log.info(AUTHENTICATION_REQUEST_RECEIVED + userSignUpRequest.getUsername());

            Authentication auth = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(
                    userSignUpRequest.getUsername(),
                    userSignUpRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(auth);
            String token = tokenGenerator.generateToken(auth);
            log.info(TOKEN_GENERATED_FOR_USER + userSignUpRequest.getUsername());
            return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
        } catch (Exception e) {
            AuthResponseDTO authResponseDTO = new AuthResponseDTO(globalExceptionHandler.handleStringException(e).getBody());
            return new ResponseEntity<>(authResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
        try {
            userService.saveUser(userSignUpRequest);
            log.info(USER_ADDED + userSignUpRequest.getUsername());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return globalExceptionHandler.handleStringException(e);
        }
    }
}