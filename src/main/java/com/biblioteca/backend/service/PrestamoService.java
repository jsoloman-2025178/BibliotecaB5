package com.biblioteca.backend.service;

import com.biblioteca.backend.model.Prestamo;
import com.biblioteca.backend.model.Libro;
import com.biblioteca.backend.model.Usuario;
import com.biblioteca.backend.model.EstadoPrestamo;
import com.biblioteca.backend.model.Estado;
import com.biblioteca.backend.repository.LibroRepository;
import com.biblioteca.backend.repository.PrestamoRepository;
import com.biblioteca.backend.repository.UsuarioRepository;
import com.biblioteca.backend.dto.PrestamoRequest;
import com.biblioteca.backend.dto.PrestamoResponse;
import com.biblioteca.backend.dto.MisPrestamosResponse;
import com.biblioteca.backend.dto.PrestamoResumen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public PrestamoResponse registrarPrestamo(PrestamoRequest request, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Libro libro = libroRepository.findById(request.getLibroId())
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        // Validar stock disponible
        if (libro.getStockDisponible() <= 0) {
            throw new RuntimeException("No hay ejemplares disponibles para este libro");
        }

        // Validar límite de 3 préstamos activos
        List<Prestamo> prestamosActivos = prestamoRepository.findByUsuarioAndEstado(usuario, EstadoPrestamo.ACTIVO);
        if (prestamosActivos.size() >= 3) {
            throw new RuntimeException("No puede tener más de 3 préstamos activos simultáneos");
        }

        // Validar sanción
        if (usuario.getEstado() == Estado.SANCIONADO) {
            throw new RuntimeException("Usuario sancionado, no puede realizar préstamos");
        }

        LocalDate fechaPrestamo = request.getFechaPrestamo() != null ? request.getFechaPrestamo() : LocalDate.now();
        LocalDate fechaDevolucionEsperada = fechaPrestamo.plusDays(14);

        Prestamo prestamo = new Prestamo(usuario, libro, fechaPrestamo, fechaDevolucionEsperada);
        prestamo = prestamoRepository.save(prestamo);

        // Descontar stock
        libro.setStockDisponible(libro.getStockDisponible() - 1);
        libroRepository.save(libro);

        return convertirAResponse(prestamo);
    }

    @Transactional
    public PrestamoResponse registrarDevolucion(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        if (prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new RuntimeException("Este préstamo ya fue devuelto");
        }

        // Actualizar fecha de devolución real
        LocalDate fechaActual = LocalDate.now();
        prestamo.setFechaDevolucionReal(fechaActual);

        // Si se devolvió después de la fecha esperada, marcar como ATRASADO
        if (fechaActual.isAfter(prestamo.getFechaDevolucionEsperada())) {
            prestamo.setEstado(EstadoPrestamo.ATRASADO);
        } else {
            prestamo.setEstado(EstadoPrestamo.DEVUELTO);
        }

        // Restaurar stock
        Libro libro = prestamo.getLibro();
        libro.setStockDisponible(libro.getStockDisponible() + 1);
        libroRepository.save(libro);

        prestamoRepository.save(prestamo);

        return convertirAResponse(prestamo);
    }

    public MisPrestamosResponse obtenerMisPrestamos(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Prestamo> prestamos = prestamoRepository.findByUsuario(usuario);
        
        int prestamosActivos = (int) prestamos.stream()
                .filter(p -> p.getEstado() == EstadoPrestamo.ACTIVO)
                .count();

        // Calcular multas por libros atrasados
        int multa = 0;
        for (Prestamo p : prestamos) {
            if (p.getEstado() == EstadoPrestamo.ATRASADO) {
                // Multiplica el número de días de atraso por una tarifa fija
                LocalDate fechaEsperada = p.getFechaDevolucionEsperada();
                LocalDate hoy = LocalDate.now();
                long diasAtraso = java.time.temporal.ChronoUnit.DAYS.between(fechaEsperada, hoy);
                if (diasAtraso > 0) {
                    multa += (int) diasAtraso * 2; // $2 por día de multa
                }
            }
        }

        MisPrestamosResponse response = new MisPrestamosResponse();
        response.setUsuarioId(usuario.getId());
        response.setNombreUsuario(usuario.getNombre());
        response.setPrestamosActivos(prestamosActivos);
        response.setMulta(multa);

        // Convertir a resumen
        List<PrestamoResumen> resumenes = prestamos.stream().map(p -> {
            PrestamoResumen r = new PrestamoResumen();
            r.setLibroId(p.getLibro().getId());
            r.setTitulo(p.getLibro().getTitulo());
            r.setFechaPrestamo(p.getFechaPrestamo());
            r.setFechaDevolucionEsperada(p.getFechaDevolucionEsperada());
            r.setEstado(p.getEstado());
            return r;
        }).collect(Collectors.toList());

        response.setPrestamos(resumenes);
        return response;
    }

    public java.util.List<Prestamo> obtenerPrestamosAtrasados() {
        LocalDate hoy = LocalDate.now();
        return prestamoRepository.findByFechaDevolucionEsperadaBeforeAndEstado(hoy, EstadoPrestamo.ACTIVO);
    }

    @Transactional
    public void renovarPrestamo(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        if (prestamo.getEstado() != EstadoPrestamo.ACTIVO) {
            throw new RuntimeException("No se puede renovar un préstamo devuelto o atrasado");
        }

        // Extender por 14 días más
        prestamo.setFechaDevolucionEsperada(prestamo.getFechaDevolucionEsperada().plusDays(14));
        prestamoRepository.save(prestamo);
    }

    private PrestamoResponse convertirAResponse(Prestamo prestamo) {
        PrestamoResponse response = new PrestamoResponse();
        response.setId(prestamo.getId());
        response.setTituloLibro(prestamo.getLibro().getTitulo());
        response.setNombreUsuario(prestamo.getUsuario().getNombre());
        response.setFechaPrestamo(prestamo.getFechaPrestamo());
        response.setFechaDevolucionEsperada(prestamo.getFechaDevolucionEsperada());
        response.setFechaDevolucionReal(prestamo.getFechaDevolucionReal());
        response.setEstado(prestamo.getEstado());
        return response;
    }
}