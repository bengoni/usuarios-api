package com.bienesraices.api.web;

import com.bienesraices.api.dto.LoginRequest;
import com.bienesraices.api.dto.LoginResponse;
import com.bienesraices.api.model.Usuario;
import com.bienesraices.api.repository.UsuarioRepository;
import com.bienesraices.api.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "auth-controller", description = "Autenticación con JWT")
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioRepository repo;
    private final JwtUtil jwt;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthController(UsuarioRepository repo, JwtUtil jwt) {
        this.repo = repo;
        this.jwt = jwt;
    }

    @Operation(summary = "Login", description = "Valida credenciales y devuelve un JWT")
    @PostMapping(
            value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        return repo.findByEmail(req.getEmail())
                .filter(Usuario::getActivo)
                .map(u -> {
                    // Comparación con BCrypt (raw vs hashed)
                    if (!encoder.matches(req.getPassword(), u.getPassword())) {
                        return unauthorized("Credenciales inválidas");
                    }
                    String token = jwt.generateToken(u.getId(), u.getEmail(), u.getRol().name());
                    return ResponseEntity.ok(new LoginResponse(token, u.getNombre(), u.getRol().name()));
                })
                .orElseGet(() -> unauthorized("Credenciales inválidas"));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Quién soy", description = "Devuelve info del usuario extraída del token")
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> me(@RequestHeader(name = "Authorization", required = false) String auth) {
        var token = getBearer(auth);
        if (token == null) return unauthorized("Falta header Authorization");
        try {
            var claims = jwt.parse(token);
            Map<String, Object> body = new HashMap<>();
            body.put("id", claims.getSubject());
            body.put("email", claims.get("email"));
            body.put("rol", claims.get("rol"));
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return unauthorized("Token inválido o expirado");
        }
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Validar token", description = "Retorna true/false si el token es válido")
    @GetMapping(value = "/validate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> validate(@RequestHeader(name = "Authorization", required = false) String auth) {
        var token = getBearer(auth);
        if (token == null) return unauthorized("Falta header Authorization");
        try {
            jwt.parse(token); // si no lanza excepción, es válido
            return ResponseEntity.ok(Map.of("valid", true));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("valid", false, "reason", "invalid_or_expired"));
        }
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Refrescar token", description = "Genera un nuevo JWT a partir de uno válido")
    @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> refresh(@RequestHeader(name = "Authorization", required = false) String auth) {
        var token = getBearer(auth);
        if (token == null) return unauthorized("Falta header Authorization");
        try {
            var claims = jwt.parse(token);
            Long userId = Long.valueOf(claims.getSubject());
            String email = (String) claims.get("email");
            String rol = (String) claims.get("rol");

            // (Opcional) Verificar que el usuario siga activo
            boolean ok = repo.findById(userId).filter(Usuario::getActivo).isPresent();
            if (!ok) return unauthorized("Usuario deshabilitado");

            String newToken = jwt.generateToken(userId, email, rol);
            return ResponseEntity.ok(new LoginResponse(newToken, null, rol));
        } catch (Exception e) {
            return unauthorized("Token inválido o expirado");
        }
    }

    // ---------------------- Helpers ----------------------

    private static String getBearer(String authHeader) {
        if (authHeader == null) return null;
        return authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
    }

    private static ResponseEntity<Map<String, String>> unauthorized(String msg) {
        return ResponseEntity.status(401).body(Map.of("error", msg));
    }
}

