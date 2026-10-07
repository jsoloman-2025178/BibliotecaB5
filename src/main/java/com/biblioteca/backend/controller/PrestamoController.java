package com.biblioteca.backend.controller;

import com.biblioteca.backend.dto.PrestamoRequest;
import com.biblioteca.backend.dto.PrestamoResponse;
import com.biblioteca.backend.dto.MisPrestamosResponse;
import com.biblioteca.backend.dto.PrestamoResumen;
import com.biblioteca.backend.model.EstadoPrestamo;
import com.biblioteca.backend.model.Estado;
import com.biblioteca.backend.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @PostMapping
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO', 'ADMIN')")
    public ResponseEntity<PrestamoResponse> registrarPrestamo(@RequestBody PrestamoRequest request, Authentication authentication) {
        String email = authentication.getName();
        PrestamoResponse response = prestamoService.registrarPrestamo(request, email);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/devolucion")
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO', 'ADMIN')")
    public ResponseEntity<PrestamoResponse> registrarDevolucion(@PathVariable Long prestamoId) {
        PrestamoResponse response = prestamoService.registrarDevolucion(prestamoId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/mis-prestamos")
    @PreAuthorize("hasRole('LECTOR')")
    public ResponseEntity<MisPrestamosResponse> misPrestamos(Authentication authentication) {
        String email = authentication.getName();
        MisPrestamosResponse response = prestamoService.obtenerMisPrestamos(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/atrasados")
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO', 'ADMIN')")
    public ResponseEntity<List<PrestamoResponse>> prestamosAtrasados() {
        List<Prestamo> prestamos = prestamoService.obtenerPrestamosAtrasados();
        List<PrestamoResponse> response = prestamos.stream().map(prestamo -> {
            PrestamoResponse resp = new PrestamoResponse();
            resp.setId(prestamo.getId());
            resp.setTituloLibro(prestamo.getLibro().getTitulo());
            resp.setNombreUsuario(prestamo.getUsuario().getNombre());
            resp.setFechaPrestamo(prestamo.getFechaPrestamo());
            resp.setFechaDevolucionEsperada(prestamo.getFechaDevolucionEsperada());
            resp.setFechaDevolucionReal(prestamo.getFechaDevolucionReal());
            resp.setEstado(prestamo.getEstado());
            return resp;
        }).toList();
        return ResponseEntity.ok(response);
    }
}