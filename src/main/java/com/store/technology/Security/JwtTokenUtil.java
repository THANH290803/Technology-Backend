package com.store.technology.Security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
//bTzV6WqX0r9!@#LkQwE8YfA2nD7$gH^pO3zRjB1cM4xUeI5sVtNqK9yLwZ+PqXr
@Component
public class JwtTokenUtil {
    private final JwtSecretManager secretManager;

    @Value("${jwt.expiration}")
    private long expirationMs;

    public JwtTokenUtil(JwtSecretManager secretManager) {
        this.secretManager = secretManager;
    }

    private Key getSigningKey() {
        // Nếu secretBase64 là Base64, decode -> Keys.hmacShaKeyFor(byte[])
        // Nếu bạn store plain text, prefer convert to bytes via UTF-8 and ensure length >= 32 bytes.
        byte[] keyBytes = Base64.getDecoder().decode(secretManager.getCurrentSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
