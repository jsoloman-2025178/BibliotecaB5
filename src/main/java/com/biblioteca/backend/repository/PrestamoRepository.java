package com.biblioteca.backend.repository;

import com.biblioteca.backend.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    List<Prestamo> findByUsuario(Usuario usuario);
    List<Prestamo> findByEstado(EstadoPrestamo estado);
    List<Prestamo> findByUsuarioAndEstado(Usuario usuario, EstadoPrestamo estado);
    List<Prestamo> findByFechaDevolucionEsperadaBefore(LocalDate fecha);
}