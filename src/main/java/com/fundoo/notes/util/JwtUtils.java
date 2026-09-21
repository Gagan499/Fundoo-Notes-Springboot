package com.fundoo.notes.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {

    private final SecretKey secret;
    private final Long expiration;

    public JwtUtils(@Value("${jwt.secret}") String secret,
                    @Value("${jwt.expiration}") Long expiration){
        this.secret = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration=expiration;
        System.out.println("length of the secret in byte : "+secret.length());
    }

    public String generateToken(String email){
        Date issuedAt = new Date();
        Date expiry = new Date((issuedAt.getTime()+expiration));
        return Jwts.builder()
                .subject(email)
                .issuedAt(issuedAt)
                .expiration(expiry)
                .signWith(secret)
                .compact();
    }

    public String extractEmail(String token){
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secret)
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
