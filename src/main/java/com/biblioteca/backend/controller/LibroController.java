package com.biblioteca.backend.controller;

import com.biblioteca.backend.dto.LibroRequest;
import com.biblioteca.backend.dto.LibroResponse;
import com.biblioteca.backend.model.Libro;
import com.biblioteca.backend.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/libros")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'BIBLIOTECARIO', 'LECTOR')")
    public ResponseEntity<List<LibroResponse>> listar(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String categoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        List<Libro> libros;
        if (titulo != null && !titulo.isEmpty()) {
            libros = libroService.findByTituloContaining(titulo);
        } else if (categoria != null && !categoria.isEmpty()) {
            libros = libroService.findByCategoria(categoria);
        } else {
            libros = libroService.listarTodos();
        }
        
        // Convertir a DTOs (simplificado)
        List<LibroResponse> response = libros.stream().map(libro -> {
            LibroResponse resp = new LibroResponse();
            resp.setId(libro.getId());
            resp.setIsbn(libro.getIsbn());
            resp.setTitulo(libro.getTitulo());
            resp.setAutor(libro.getAutor());
            resp.setCategoria(libro.getCategoria());
            resp.setStockTotal(libro.getStockTotal());
            resp.setStockDisponible(libro.getStockDisponible());
            return resp;
        }).toList();
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'BIBLIOTECARIO', 'LECTOR')")
    public ResponseEntity<LibroResponse> obtenerPorId(@PathVariable Long id) {
        Libro libro = libroService.obtenerPorId(id);
        LibroResponse response = new LibroResponse();
        response.setId(libro.getId());
        response.setIsbn(libro.getIsbn());
        response.setTitulo(libro.getTitulo());
        response.setAutor(libro.getAutor());
        response.setCategoria(libro.getCategoria());
        response.setStockTotal(libro.getStockTotal());
        response.setStockDisponible(libro.getStockDisponible());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibroResponse> crear(@RequestBody LibroRequest request) {
        Libro libro = new Libro();
        libro.setIsbn(request.getIsbn());
        libro.setTitulo(request.getTitulo());
        libro.setAutor(request.getAutor());
        libro.setCategoria(request.getCategoria());
        libro.setStockTotal(request.getStockTotal());
        libro.setStockDisponible(request.getStockDisponible());
        
        Libro libroGuardado = libroService.crear(libro);
        
        LibroResponse response = new LibroResponse();
        response.setId(libroGuardado.getId());
        response.setIsbn(libroGuardado.getIsbn());
        response.setTitulo(libroGuardado.getTitulo());
        response.setAutor(libroGuardado.getAutor());
        response.setCategoria(libroGuardado.getCategoria());
        response.setStockTotal(libroGuardado.getStockTotal());
        response.setStockDisponible(libroGuardado.getStockDisponible());
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibroResponse> actualizar(@PathVariable Long id, @RequestBody LibroRequest request) {
        Libro libro = libroService.obtenerPorId(id);
        libro.setTitulo(request.getTitulo());
        libro.setAutor(request.getAutor());
        libro.setCategoria(request.getCategoria());
        libro.setStockTotal(request.getStockTotal());
        libro.setStockDisponible(request.getStockDisponible());
        
        Libro libroActualizado = libroService.actualizar(id, libro);
        
        LibroResponse response = new LibroResponse();
        response.setId(libroActualizado.getId());
        response.setIsbn(libroActualizado.getIsbn());
        response.setTitulo(libroActualizado.getTitulo());
        response.setAutor(libroActualizado.getAutor());
        response.setCategoria(libroActualizado.getCategoria());
        response.setStockTotal(libroActualizado.getStockTotal());
        response.setStockDisponible(libroActualizado.getStockDisponible());
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        libroService.eliminar(id);
        return ResponseEntity.ok().build();
    }
}