package com.bienesraices.api.dto;



import com.bienesraices.api.model.Rol;

public class UsuarioResponse {
    private Long id;
    private String nombre;
    private String email;
    private Rol rol;
    private Boolean activo;

    // Constructor vacío (recomendado para frameworks)
    public UsuarioResponse() {}

    // ✅ Constructor con todos los campos (el que te pide el servicio)
    public UsuarioResponse(Long id, String nombre, String email, Rol rol, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.activo = activo;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
