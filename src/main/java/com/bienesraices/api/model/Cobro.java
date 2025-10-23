package com.bienesraices.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

// ✅ para evitar recursión y problemas al serializar
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "cobros")
public class Cobro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin("0.00")
    @Column(precision = 12, scale = 2)
    private BigDecimal monto;

    @NotNull
    private LocalDate fecha = LocalDate.now();

    @Size(max = 160)
    private String concepto; // Ej: “Mantenimiento”, “Agua”, etc.

    @NotBlank
    @Size(max = 20)
    private String estado = "PENDIENTE"; // PENDIENTE | PAGADO | ATRASADO

    // ✅ Ocultar para evitar recursión y LAZY serialization issues
    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getConcepto() { return concepto; }
    public void setConcepto(String concepto) { this.concepto = concepto; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
