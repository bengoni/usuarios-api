package com.bienesraices.api.web;


import com.bienesraices.api.dto.UsuarioRequest;
import com.bienesraices.api.dto.UsuarioResponse;
import com.bienesraices.api.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) { this.service = service; }

    @GetMapping("/health")
    public ResponseEntity<String> health() { return ResponseEntity.ok("ok"); }

    @GetMapping
    public List<UsuarioResponse> listar(@RequestParam(value = "soloActivos", required = false) Boolean soloActivos) {
        return service.listar(soloActivos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> porId(@PathVariable Long id) {
        return service.porId(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody UsuarioRequest req) {
        UsuarioResponse creado = service.crear(req);
        return ResponseEntity.created(URI.create("/api/usuarios/" + creado.getId())).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest req) {
        return service.actualizar(id, req).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> cambiarPassword(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String nuevo = body.get("password");
        if (nuevo == null || nuevo.isBlank()) return ResponseEntity.badRequest().build();
        return service.cambiarPassword(id, nuevo) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return service.eliminar(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

