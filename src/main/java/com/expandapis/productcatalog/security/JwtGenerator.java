package com.expandapis.productcatalog.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtGenerator {
    @Value("${jwt.expiration}")
    private long jwtExpiration;
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

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

        log.info("Generating token for user: {}", username);
        log.debug("New token: {}", token);
        return token;
    }

    public String getUsernameFromJwt(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        String username = claims.getSubject();
        log.info("Extracting username from JWT: {}", username);
        return username;
    }

    public boolean validateToken(String token) {
        log.info("Validating token: {}", token);
        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        return true;
    }
}
