package com.example.testtasks.controllers;

import com.example.testtasks.dto.AuthResponseDTO;
import com.example.testtasks.dto.UserDTO;
import com.example.testtasks.security.AuthenticationProviderImplementation;
import com.example.testtasks.security.JWTGenerator;
import com.example.testtasks.services.UserService;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * A controller for working with users
 */
@RestController
@RequestMapping("/user")
@Api(value = "User Controller", tags = "User Controller", description = "REST API for managing users")
public class LogInController {
    private static final Logger logger = LogManager.getLogger();
    private final AuthenticationProviderImplementation authenticationProvider;
    private final JWTGenerator tokenGenerator;
    private final UserService userService;

    private static final String AUTHENTICATION_REQUEST_RECEIVED = "Received authentication request for user: ";
    private static final String TOKEN_GENERATED_FOR_USER = "Generated token for user: ";
    private static final String USER_ADDED = "User added: ";
    private static final String ERROR_ADDING_USER = "Error adding user: ";
    private static final String AUTHENTICATION_FAILED = "Authentication failed: ";

    @Autowired
    public LogInController(AuthenticationProviderImplementation authenticationProvider, JWTGenerator tokenGenerator, UserService userService) {
        this.authenticationProvider = authenticationProvider;
        this.tokenGenerator = tokenGenerator;
        this.userService = userService;
    }

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
            logger.info(AUTHENTICATION_REQUEST_RECEIVED + userSignUpRequest.getUsername());

            Authentication auth = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(
                    userSignUpRequest.getUsername(),
                    userSignUpRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(auth);
            String token = tokenGenerator.generateToken(auth);
            logger.info(TOKEN_GENERATED_FOR_USER + userSignUpRequest.getUsername());
            return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(AUTHENTICATION_FAILED + e.getMessage(), e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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
            logger.info(USER_ADDED + userSignUpRequest.getUsername());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error(ERROR_ADDING_USER + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}