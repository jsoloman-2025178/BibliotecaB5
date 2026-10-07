package com.biblioteca.backend.dto;

import java.time.LocalDate;
import java.util.List;

public class MisPrestamosResponse {
    private Long usuarioId;
    private String nombreUsuario;
    private int prestamosActivos;
    private int multa;
    private List<PrestamoResumen> prestamos;

    // Getters and setters
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public int getPrestamosActivos() { return prestamosActivos; }
    public void setPrestamosActivos(int prestamosActivos) { this.prestamosActivos = prestamosActivos; }
    public int getMulta() { return multa; }
    public void setMulta(int multa) { this.multa = multa; }
    public List<PrestamoResumen> getPrestamos() { return prestamos; }
    public void setPrestamos(List<PrestamoResumen> prestamos) { this.prestamos = prestamos; }
}

public class PrestamoResumen {
    private Long libroId;
    private String titulo;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionEsperada;
    private EstadoPrestamo estado;

    // Getters and setters
    public Long getLibroId() { return libroId; }
    public void setLibroId(Long libroId) { this.libroId = libroId; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(LocalDate fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }
    public LocalDate getFechaDevolucionEsperada() { return fechaDevolucionEsperada; }
    public void setFechaDevolucionEsperada(LocalDate fechaDevolucionEsperada) { this.fechaDevolucionEsperada = fechaDevolucionEsperada; }
    public EstadoPrestamo getEstado() { return estado; }
    public void setEstado(EstadoPrestamo estado) { this.estado = estado; }
}