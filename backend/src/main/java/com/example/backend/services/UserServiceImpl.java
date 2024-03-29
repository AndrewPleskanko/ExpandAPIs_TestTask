package com.example.backend.services;

import java.util.List;
import java.util.NoSuchElementException;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.dto.UserDto;
import com.example.backend.entity.Role;
import com.example.backend.entity.User;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for working with users.
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Get a user by ID.
     *
     * @param id User ID.
     * @return User object.
     */
    @Override
    public User get(Long id) {
        log.info("Fetching user by ID: {}", id);
        return userRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("User not found with ID: " + id));
    }

    /**
     * Save a new user.
     *
     * @param request DTO for saving the user.
     */
    @Override
    @Transactional
    public void saveUser(UserDto request) {
        log.info("Saving user: {}", request.getUsername());
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        if (request.getRole() == null) {
            user.setRole(Role.ROLE_USER);
        } else {
            user.setRole(request.getRole());
        }
        userRepository.save(user);
        log.info("User saved successfully: {}", request.getUsername());
    }

    /**
     * Retrieves a list of users from the database.
     *
     * @return A List of User objects representing all users in the database.
     */
    @Override
    public List<User> getList() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user from the database based on the provided username.
     *
     * @param username The username of the user to fetch.
     * @return The user with the specified username.
     * @throws NoSuchElementException if no user is found with the given username.
     */
    @Override
    public User findByUsername(String username) {
        log.info("Fetching user by username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found with username: " + username));
    }
}
