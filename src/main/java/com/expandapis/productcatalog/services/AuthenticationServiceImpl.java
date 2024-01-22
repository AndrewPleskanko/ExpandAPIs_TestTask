package com.expandapis.productcatalog.services;

import com.expandapis.productcatalog.dto.AuthResponseDto;
import com.expandapis.productcatalog.dto.UserDto;
import com.expandapis.productcatalog.security.JWTGenerator;
import com.expandapis.productcatalog.services.interfaces.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationProvider authenticationProvider;
    private final JWTGenerator tokenGenerator;

    @Override
    public AuthResponseDto authenticateUser(UserDto userDTO) {
        log.info("Received authentication request for user: " + userDTO.getUsername());
        Authentication auth = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(
                userDTO.getUsername(),
                userDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = tokenGenerator.generateToken(auth);
        log.info("Generated token for user: " + userDTO.getUsername());
        return new AuthResponseDto(token);
    }
}
