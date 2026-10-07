package com.biblioteca.backend.service;

import com.biblioteca.backend.model.Libro;
import com.biblioteca.backend.model.Usuario;
import com.biblioteca.backend.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    @Transactional(readOnly = true)
    public List<Libro> listarTodos() {
        return libroRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Libro obtenerPorId(Long id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
    }

    @Transactional
    public Libro crear(Libro libro) {
        validarStock(libro);
        return libroRepository.save(libro);
    }

    @Transactional
    public Libro actualizar(Long id, Libro libroActualizado) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        
        libro.setTitulo(libroActualizado.getTitulo());
        libro.setAutor(libroActualizado.getAutor());
        libro.setCategoria(libroActualizado.getCategoria());
        libro.setStockTotal(libroActualizado.getStockTotal());
        libro.setStockDisponible(libroActualizado.getStockDisponible());
        
        return libroRepository.save(libro);
    }

    @Transactional
    public void eliminar(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        libroRepository.delete(libro);
    }

    @Transactional
    public void incrementarStock(Long id, int cantidad) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        libro.setStockDisponible(libro.getStockDisponible() + cantidad);
        libroRepository.save(libro);
    }

    @Transactional
    public void decrementarStock(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        if (libro.getStockDisponible() > 0) {
            libro.setStockDisponible(libro.getStockDisponible() - 1);
        }
        libroRepository.save(libro);
    }

    @Transactional(readOnly = true)
    public Libro findByIsbn(String isbn) {
        return libroRepository.findByIsbn(isbn);
    }

    @Transactional(readOnly = true)
    public List<Libro> findByCategoria(String categoria) {
        return libroRepository.findByCategoria(categoria);
    }

    private void validarStock(Libro libro) {
        if (libro.getStockDisponible() <= 0) {
            libro.setStockDisponible(libro.getStockTotal());
        }
    }
}