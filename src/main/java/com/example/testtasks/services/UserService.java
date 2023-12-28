package com.example.testtasks.services;


import com.example.testtasks.dto.UserDTO;
import com.example.testtasks.repositories.UserRepository;
import com.example.testtasks.entity.User;
import com.example.testtasks.services.interfaces.UserServiceImp;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class UserService implements UserServiceImp {
    private static final Logger logger = LogManager.getLogger();
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String FETCHING_USER_BY_ID_MESSAGE = "Fetching user by ID: {}";
    private static final String SAVING_USER_MESSAGE = "Saving user: {}";
    private static final String USER_SAVED_SUCCESSFULLY_MESSAGE = "User saved successfully: {}";

    @Override
    @Transactional
    public User get(Long id) {
        logger.info(FETCHING_USER_BY_ID_MESSAGE, id);
        return userRepository.findById(id).get();
    }

    @Transactional
    public void saveUser(UserDTO request) {
        logger.info(SAVING_USER_MESSAGE, request.getUsername());
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        logger.info(USER_SAVED_SUCCESSFULLY_MESSAGE, request.getUsername());
    }
}
