package com.bienesraices.api.dto;

public class LoginResponse {

    private String token;
    private String nombre;
    private String rol;

    public LoginResponse() {
        // Constructor vacío (necesario para serialización JSON)
    }

    // ✅ Constructor con todos los campos
    public LoginResponse(String token, String nombre, String rol) {
        this.token = token;
        this.nombre = nombre;
        this.rol = rol;
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
