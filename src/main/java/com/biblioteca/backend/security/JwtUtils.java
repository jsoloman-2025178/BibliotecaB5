package com.biblioteca.backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    private SecretKey llaveSecreta;

    @PostConstruct
    public void init() {
        // En producción, esta clave debería venir de application.properties
        this.llaveSecreta = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    }

    public String generarToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 24 horas
                .signWith(llaveSecreta)
                .compact();
    }

    public String obtenerEmailDelToken(String token) {
        return Jwts.parser()
                .verifyWith(llaveSecreta)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parser().verifyWith(llaveSecreta).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}