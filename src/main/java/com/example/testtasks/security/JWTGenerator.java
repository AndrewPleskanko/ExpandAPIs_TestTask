package com.example.testtasks.security;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JWTGenerator {
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final String JWT_EXPIRATION_ERROR_MESSAGE = "JWT was expired or incorrect";
    private static final String TOKEN_GENERATION_MESSAGE = "Generating token for user: {}";
    private static final String TOKEN_VALIDATION_MESSAGE = "Validating token: {}";
    private static final String USERNAME_EXTRACTION_MESSAGE = "Extracting username from JWT: {}";

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpiration);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        log.info(TOKEN_GENERATION_MESSAGE, username);
        log.debug("New token: {}", token);
        return token;
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        String username = claims.getSubject();
        log.info(USERNAME_EXTRACTION_MESSAGE, username);
        return username;
    }

    public boolean validateToken(String token) {
        try {
            log.info(TOKEN_VALIDATION_MESSAGE, token);
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error(JWT_EXPIRATION_ERROR_MESSAGE, e);
            return false;
        }
    }
}
