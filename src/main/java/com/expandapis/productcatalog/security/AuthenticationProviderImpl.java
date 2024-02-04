package com.expandapis.productcatalog.security;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationProviderImpl implements AuthenticationProvider {
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (userDetailsService == null) {
            throw new InternalAuthenticationServiceException("User service is null");
        }
        UserDetails user = userDetailsService.loadUserByUsername(username);
        if (user == null) {
            throw new AuthenticationCredentialsNotFoundException("No such user was found");
        }
        if (passwordEncoder.matches(password, user.getPassword())) {
            log.info("Authentication successful for user: {}", username);
            return new UsernamePasswordAuthenticationToken(user,
                    authentication.getCredentials(), user.getAuthorities());
        } else {
            throw new AuthenticationServiceException("Unable to authenticate user due to some problems");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}