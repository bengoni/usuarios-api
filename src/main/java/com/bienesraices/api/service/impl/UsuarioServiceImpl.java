package com.bienesraices.api.service.impl;

import com.bienesraices.api.dto.UsuarioRequest;
import com.bienesraices.api.dto.UsuarioResponse;
import com.bienesraices.api.model.Usuario;
import com.bienesraices.api.repository.UsuarioRepository;
import com.bienesraices.api.service.UsuarioService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UsuarioServiceImpl(UsuarioRepository repo) {
        this.repo = repo;
    }

    // Conversión Entidad -> DTO de salida (para no exponer password)
    private UsuarioResponse toResp(Usuario u) {
        return new UsuarioResponse(
                u.getId(),
                u.getNombre(),
                u.getEmail(),
                u.getRol(),
                u.getActivo()
        );
    }

    @Override
    public List<UsuarioResponse> listar(Boolean soloActivos) {
        return repo.findAll().stream()
                .filter(u -> soloActivos == null || !Boolean.TRUE.equals(soloActivos) || Boolean.TRUE.equals(u.getActivo()))
                .map(this::toResp)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UsuarioResponse> porId(Long id) {
        return repo.findById(id).map(this::toResp);
    }

    @Override
    public UsuarioResponse crear(UsuarioRequest req) {
        Usuario u = new Usuario();
        u.setNombre(req.getNombre());
        u.setEmail(req.getEmail());
        // 🔐 Hash de contraseña con BCrypt
        u.setPassword(encoder.encode(req.getPassword()));
        u.setRol(req.getRol());
        u.setActivo(req.getActivo());
        try {
            return toResp(repo.save(u));
        } catch (DataIntegrityViolationException ex) {
            // Si tienes email único, esto captura duplicados (409 en el handler)
            throw ex;
        }
    }

    @Override
    public Optional<UsuarioResponse> actualizar(Long id, UsuarioRequest req) {
        return repo.findById(id).map(db -> {
            db.setNombre(req.getNombre());
            db.setEmail(req.getEmail());
            // Solo re-hashear si vino un password NO vacío
            if (req.getPassword() != null && !req.getPassword().isBlank()) {
                db.setPassword(encoder.encode(req.getPassword()));
            }
            db.setRol(req.getRol());
            db.setActivo(req.getActivo());
            return toResp(repo.save(db));
        });
    }

    @Override
    public boolean eliminar(Long id) {
        return repo.findById(id).map(u -> { repo.delete(u); return true; }).orElse(false);
    }

    @Override
    public boolean cambiarPassword(Long id, String nuevoPassword) {
        return repo.findById(id).map(u -> {
            u.setPassword(encoder.encode(nuevoPassword)); // 🔐 re-hash
            repo.save(u);
            return true;
        }).orElse(false);
    }
}
