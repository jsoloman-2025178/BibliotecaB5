package com.biblioteca.backend.repository;

import com.biblioteca.backend.model.Libro;
import com.biblioteca.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    Libro findByIsbn(String isbn);
    Libro findByTituloContaining(String titulo);
    Libro findByCategoria(String categoria);
}

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);
}

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    java.util.List<Prestamo> findByUsuario(Usuario usuario);
    java.util.List<Prestamo> findByEstado(EstadoPrestamo estado);
    java.util.List<Prestamo> findByUsuarioAndEstado(Usuario usuario, EstadoPrestamo estado);
    java.util.List<Prestamo> findByFechaDevolucionEsperadaBefore(LocalDate fecha);
}