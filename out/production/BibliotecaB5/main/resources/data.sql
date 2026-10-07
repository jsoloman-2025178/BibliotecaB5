-- Creación de roles
INSERT INTO roles (id, nombre) VALUES (1, 'ADMIN');
INSERT INTO roles (id, nombre) VALUES (2, 'BIBLIOTECARIO');
INSERT INTO roles (id, nombre) VALUES (3, 'LECTOR');

-- Creación de usuario administrador por defecto
-- Contraseña: admin123 (encriptada con BCrypt)
INSERT INTO usuarios (id, nombre, email, password, rol, estado) VALUES (1, 'Administrador', 'admin@biblioteca.edu', '$2a$10$N.z3BL8iJz6yMVi9.u8OBuQ7Bc.T6.p2b3oqe.qR0x5W1I.W.E5.uK', 1, 'ACTIVO');

-- Insertar algunos libros de ejemplo
INSERT INTO libros (isbn, titulo, autor, categoria, stockTotal, stockDisponible) VALUES ('978-84-670-2168-6', 'Don Quijote de la Mancha', 'Miguel de Cervantes', 'Clásicos', 10, 10);
INSERT INTO libros (isbn, titulo, autor, categoria, stockTotal, stockDisponible) VALUES ('978-84-376-0386-7', 'Cien años de soledad', 'Gabriel García Márquez', 'Clásicos', 8, 8);
INSERT INTO libros (isbn, titulo, autor, categoria, stockTotal, stockDisponible) VALUES ('978-84-9778-007-5', 'La sombra del viento', 'Carlos Ruiz Zafón', 'Novelas', 15, 15);