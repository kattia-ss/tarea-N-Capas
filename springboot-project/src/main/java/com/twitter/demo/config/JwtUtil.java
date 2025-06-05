package com.twitter.demo.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    private static final String SECRET = "mySecretKeyForJWTTokenGenerationAndValidation123456789"; // mínimo 256 bits
    private static final long EXPIRATION_TIME_MS = 5 * 60 * 60 * 1000;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(UUID userId) {
        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserIdFromToken(String token) {
        return Jwts.parser()  // Cambiado de parserBuilder() a parser()
                .verifyWith((SecretKey) getSigningKey())  // Cambiado de setSigningKey() a verifyWith()
                .build()
                .parseSignedClaims(token)  // Cambiado de parseClaimsJws() a parseSignedClaims()
                .getPayload()  // Cambiado de getBody() a getPayload()
                .getSubject();
    }

    public boolean validateToken(String token, String userId) {
        try {
            String extractedId = getUserIdFromToken(token);
            return extractedId.equals(userId) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()  // Cambiado de parserBuilder() a parser()
                .verifyWith((SecretKey) getSigningKey())  // Cambiado de setSigningKey() a verifyWith()
                .build()
                .parseSignedClaims(token)  // Cambiado de parseClaimsJws() a parseSignedClaims()
                .getPayload()  // Cambiado de getBody() a getPayload()
                .getExpiration();
        return expiration.before(new Date());
    }
}