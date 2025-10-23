package com.bienesraices.api.web;



import com.bienesraices.api.model.Cobro;
import com.bienesraices.api.model.Usuario;
import com.bienesraices.api.repository.CobroRepository;
import com.bienesraices.api.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "cobros", description = "Gestión de cobros de los usuarios")
@RestController
@RequestMapping("/api/cobros")
@CrossOrigin
public class CobroController {

    private final CobroRepository cobroRepo;
    private final UsuarioRepository usuarioRepo;

    public CobroController(CobroRepository cobroRepo, UsuarioRepository usuarioRepo) {
        this.cobroRepo = cobroRepo;
        this.usuarioRepo = usuarioRepo;
    }

    // 🔹 Listar todos los cobros
    @GetMapping
    public List<Cobro> listAll() {
        return cobroRepo.findAll();
    }

    // 🔹 Listar cobros por usuario
    @GetMapping("/usuario/{usuarioId}")
    public List<Cobro> listByUsuario(@PathVariable Long usuarioId) {
        return cobroRepo.findByUsuario_Id(usuarioId);
    }

    // 🔹 Crear un cobro para un usuario
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<Cobro> create(@PathVariable Long usuarioId, @Valid @RequestBody Cobro cobro) {
        Usuario u = usuarioRepo.findById(usuarioId).orElse(null);
        if (u == null) return ResponseEntity.notFound().build();

        cobro.setId(null);
        cobro.setUsuario(u);
        Cobro saved = cobroRepo.save(cobro);
        return ResponseEntity.created(URI.create("/api/cobros/" + saved.getId())).body(saved);
    }

    // 🔹 Actualizar un cobro
    @PutMapping("/{id}")
    public ResponseEntity<Cobro> update(@PathVariable Long id, @Valid @RequestBody Cobro c) {
        return cobroRepo.findById(id).map(db -> {
            db.setMonto(c.getMonto());
            db.setFecha(c.getFecha());
            db.setConcepto(c.getConcepto());
            db.setEstado(c.getEstado());
            return ResponseEntity.ok(cobroRepo.save(db));
        }).orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Eliminar un cobro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!cobroRepo.existsById(id)) return ResponseEntity.notFound().build();
        cobroRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
