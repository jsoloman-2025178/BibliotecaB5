package com.biblioteca.backend.controller;

import com.biblioteca.backend.dto.AuthLoginRequest;
import com.biblioteca.backend.dto.AuthLoginResponse;
import com.biblioteca.backend.dto.AuthRegisterRequest;
import com.biblioteca.backend.dto.AuthRegisterResponse;
import com.biblioteca.backend.model.Usuario;
import com.biblioteca.backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<AuthRegisterResponse> registrar(@RequestBody AuthRegisterRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        // La contraseña se establecerá en el service, pero necesitamos pasarla
        // Para simplificar, asignaremos temporalmente (en un caso real usaríamos un DTO específico)
        usuario.setPassword(request.getPassword()); // El service lo encriptará
        
        Usuario usuarioRegistrado = usuarioService.registrar(usuario);
        
        AuthRegisterResponse response = new AuthRegisterResponse();
        response.setId(usuarioRegistrado.getId());
        response.setNombre(usuarioRegistrado.getNombre());
        response.setEmail(usuarioRegistrado.getEmail());
        response.setRol(usuarioRegistrado.getRol());
        response.setEstado(usuarioRegistrado.getEstado());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(@RequestBody AuthLoginRequest request) {
        Usuario usuario = usuarioService.login(request.getEmail(), request.getPassword());
        
        // Generar JWT token (implementación simplificada)
        String token = usuarioService.generateToken(usuario);
        
        AuthLoginResponse response = new AuthLoginResponse();
        response.setToken(token);
        
        return ResponseEntity.ok(response);
    }
}