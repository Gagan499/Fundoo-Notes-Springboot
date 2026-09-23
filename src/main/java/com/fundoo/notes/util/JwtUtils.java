package com.fundoo.notes.util;

import io.jsonwebtoken.Claims;
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
    private final Long resetExpiration;

    public JwtUtils(@Value("${jwt.secret}") String secret,
                    @Value("${jwt.expiration}") Long expiration,@Value("${jwt.reset.expiration}") Long resetExpiration){
        this.secret = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration=expiration;
        this.resetExpiration=resetExpiration;
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

    public Long extractUserIdFromToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("userId",Long.class);
    }
    public String generateresettoken(String email, Long userId){
        Date issueAt = new Date();
        Date expiry = new Date(issueAt.getTime()+resetExpiration);
        return Jwts.builder()
                .claim("userId",userId)
                .issuedAt(issueAt)
                .expiration(expiry)
                .signWith(secret)
                .compact();
    }
}
