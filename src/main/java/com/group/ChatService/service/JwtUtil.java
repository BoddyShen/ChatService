package com.group.ChatService.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Utility class for handling JWT
 */
@Service
public class JwtUtil implements InitializingBean {

    @Value("${jwt.secret}")
    private String jwtSecret;
    private Algorithm algorithm;

    @Override
    public void afterPropertiesSet() {
        this.algorithm = Algorithm.HMAC256(jwtSecret);
    }

    /**
     * Extracts the user ID from the JWT token.
     *
     * @param token the JWT token
     * @return the user ID extracted from the token
     * @throws JWTVerificationException if the token verification fails
     */
    public String extractUserId(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getSubject();
    }

    /**
     * Creates a new JWT token for the given email.
     *
     * @param email
     * @return the generated JWT token
     */
    public String createToken(String email) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + 24 * 60 * 60 * 1000); // expire after one day

        return JWT.create()
                .withSubject(email)
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .sign(algorithm);
    }
}