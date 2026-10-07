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