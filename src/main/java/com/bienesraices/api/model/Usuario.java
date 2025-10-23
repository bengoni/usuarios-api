package com.bienesraices.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

// ✅ para ocultar campos al serializar a JSON
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 60)
    private String nombre;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    // ✅ no exponer el password en las respuestas
    @JsonIgnore
    @NotBlank
    @Size(min = 6, max = 120)
    private String password;

    @Enumerated(EnumType.STRING)
    private Rol rol;   // ADMIN, AGENTE, CLIENTE

    private Boolean activo = true;

    // número de casa
    @Size(max = 20)
    @Column(name = "numero_casa", length = 20)
    private String numeroCasa;

    // ✅ evitar recursión al serializar (Usuario -> Cobros -> Usuario ...)
    @JsonIgnore
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cobro> cobros = new ArrayList<>();

    private LocalDateTime creadoEn;

    @PrePersist
    public void prePersist() {
        if (creadoEn == null) creadoEn = LocalDateTime.now();
        if (activo == null) activo = true;
    }

    // ===== Getters y Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public String getNumeroCasa() { return numeroCasa; }
    public void setNumeroCasa(String numeroCasa) { this.numeroCasa = numeroCasa; }

    public List<Cobro> getCobros() { return cobros; }
    public void setCobros(List<Cobro> cobros) { this.cobros = cobros; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
}

