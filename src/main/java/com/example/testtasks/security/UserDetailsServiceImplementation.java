package com.example.testtasks.security;

import com.example.testtasks.repositories.UserRepository;
import com.example.testtasks.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImplementation implements UserDetailsService {
    private final UserRepository userRepository;

    private static final String USER_NOT_FOUND_MSG = "User with username %s not found";
    private static final String USER_LOADED_SUCCESSFULLY_MSG = "User loaded successfully: {}";
    private static final String ATTEMPTING_TO_LOAD_USER_MSG = "Attempting to load user by username: {}";

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(ATTEMPTING_TO_LOAD_USER_MSG, username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username)));

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        log.info(USER_LOADED_SUCCESSFULLY_MSG, username);

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
