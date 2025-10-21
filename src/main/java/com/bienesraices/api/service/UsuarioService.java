package com.bienesraices.api.service;


import com.bienesraices.api.dto.UsuarioRequest;
import com.bienesraices.api.dto.UsuarioResponse;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    // Lista usuarios; si soloActivos=true, filtra solo activos
    List<UsuarioResponse> listar(Boolean soloActivos);

    // Obtiene un usuario por id (envuelve en Optional para 404 limpio)
    Optional<UsuarioResponse> porId(Long id);

    // Crea un usuario (el password se hashea en la implementación)
    UsuarioResponse crear(UsuarioRequest req);

    // Actualiza campos; si viene password no vacío, se re-hashea
    Optional<UsuarioResponse> actualizar(Long id, UsuarioRequest req);

    // Elimina por id (true si se eliminó, false si no existe)
    boolean eliminar(Long id);

    // Cambia únicamente el password (hash con BCrypt)
    boolean cambiarPassword(Long id, String nuevoPassword);
}
