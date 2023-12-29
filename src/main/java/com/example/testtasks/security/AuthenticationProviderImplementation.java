package com.example.testtasks.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationProviderImplementation implements AuthenticationProvider {
    private final UserDetailsServiceImplementation userDetailsService;
    private final PasswordEncoder passwordEncoder;

    private static final String USER_NOT_FOUND_ERROR = "No such user was found";
    private static final String USER_SERVICE_NULL_ERROR = "User service is null";
    private static final String AUTHENTICATION_FAILURE_ERROR = "Unable to authenticate user due to some problems";
    private static final String AUTHENTICATION_SUCCESSFUL = "Authentication successful for user: {}";
    private static final String ERROR_DURING_AUTHENTICATION = "Error during authentication: {}";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        try {
            if (userDetailsService == null) {
                throw new InternalAuthenticationServiceException(USER_SERVICE_NULL_ERROR);
            }
            UserDetails user = userDetailsService.loadUserByUsername(username);
            if (user == null) {
                throw new AuthenticationCredentialsNotFoundException(USER_NOT_FOUND_ERROR);
            }
            if (passwordEncoder.matches(password, user.getPassword())) {
                log.info(AUTHENTICATION_SUCCESSFUL, username);
                return new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(), user.getAuthorities());
            } else {
                throw new AuthenticationServiceException(AUTHENTICATION_FAILURE_ERROR);
            }
        } catch (Exception e) {
            log.error(ERROR_DURING_AUTHENTICATION + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}