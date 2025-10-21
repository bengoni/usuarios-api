package com.bienesraices.api.dto;


import com.bienesraices.api.model.Rol;
import jakarta.validation.constraints.*;

public class UsuarioRequest {

    @NotBlank @Size(max = 80)
    private String nombre;

    @NotBlank @Email @Size(max = 120)
    private String email;

    @NotBlank @Size(min = 6, max = 120)
    private String password;

    @NotNull
    private Rol rol = Rol.CLIENTE;

    private Boolean activo = true;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
