package com.bienesraices.api.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bienesraices.api.config.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final Key key;
    private final long expMillis;

    // Inyecta las props tipadas: app.jwt.secret y app.jwt.expMin
    public JwtUtil(JwtProperties props) {
        this.key = Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8));
        this.expMillis = props.getExpMin() * 60_000L; // minutos -> ms
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
                .setAllowedClockSkewSeconds(60) // tolerancia de reloj
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /** Valida sin lanzar excepción. */
    public boolean isValid(String token) {
        try { parse(token); return true; }
        catch (Exception e) { return false; }
    }
}
