package com.example.testtasks.services;


import com.example.testtasks.dto.UserDTO;
import com.example.testtasks.repositories.UserRepository;
import com.example.testtasks.entity.User;
import com.example.testtasks.services.interfaces.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for working with users.
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {

    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String FETCHING_USER_BY_ID_MESSAGE = "Fetching user by ID: {}";
    private static final String SAVING_USER_MESSAGE = "Saving user: {}";
    private static final String USER_SAVED_SUCCESSFULLY_MESSAGE = "User saved successfully: {}";

    /**
     * Get a user by ID.
     *
     * @param id User ID.
     * @return User object.
     */
    @ApiOperation(value = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User fetched successfully"),
            @ApiResponse(code = 500, message = "Error fetching user")
    })
    @Override
    @Transactional
    public User get(Long id) {
        log.info(FETCHING_USER_BY_ID_MESSAGE, id);
        return userRepository.findById(id).get();
    }

    /**
     * Save a new user.
     *
     * @param request DTO for saving the user.
     */
    @ApiOperation(value = "Save user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User saved successfully"),
            @ApiResponse(code = 500, message = "Error saving user")
    })
    @Transactional
    public void saveUser(UserDTO request) {
        log.info(SAVING_USER_MESSAGE, request.getUsername());
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        log.info(USER_SAVED_SUCCESSFULLY_MESSAGE, request.getUsername());
    }
}
