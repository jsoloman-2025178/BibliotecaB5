package com.biblioteca.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BibliotecaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BibliotecaApplication.class, args);
        System.out.println("=== Sistema de Gestión de Biblioteca Universitaria iniciado ===");
        System.out.println("API disponible en: http://localhost:8080/api/v1");
        System.out.println("Documentación: http://localhost:8080/swagger-ui.html");
    }
}