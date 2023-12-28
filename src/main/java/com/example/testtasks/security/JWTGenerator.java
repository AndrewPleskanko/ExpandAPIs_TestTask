package  com.example.testtasks.security;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JWTGenerator {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final Logger logger = LogManager.getLogger();
    private static final String JWT_EXPIRATION_ERROR_MESSAGE = "JWT was expired or incorrect";
    private static final String TOKEN_GENERATION_MESSAGE = "Generating token for user: {}";
    private static final String TOKEN_VALIDATION_MESSAGE = "Validating token: {}";
    private static final String USERNAME_EXTRACTION_MESSAGE = "Extracting username from JWT: {}";

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        logger.info(TOKEN_GENERATION_MESSAGE, username);
        return token;
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        String username = claims.getSubject();
        logger.info(USERNAME_EXTRACTION_MESSAGE, username);
        return username;
    }

    public boolean validateToken(String token) {
        try {
            logger.info(TOKEN_VALIDATION_MESSAGE, token);
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            logger.error(JWT_EXPIRATION_ERROR_MESSAGE, e);
            return false;
        }
    }
}
