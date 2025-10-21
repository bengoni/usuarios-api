package com.bienesraices.api.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final Key key;
    private final long expMillis;

    public JwtUtil(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expMin}") long expMin
    ) {
        // secret fijo desde application.properties (>=32 chars para HS256)
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expMillis = expMin * 60_000L; // minutos -> milisegundos
    }

    /** Genera un JWT con subject=userId y claims email/rol. */
    public String generateToken(Long userId, String email, String rol) {
        Instant now = Instant.now();
        Date iat = Date.from(now);
        Date exp = new Date(iat.getTime() + expMillis);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(iat)
                .setExpiration(exp)
                .addClaims(Map.of("email", email, "rol", rol))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** Parsea y valida el token; lanza excepción si es inválido/expirado. */
    public Claims parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .setAllowedClockSkewSeconds(60) // pequeña tolerancia de reloj
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /** Valida sin lanzar excepción: true si pasa parse(), false si no. */
    public boolean isValid(String token) {
        try {
            parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
