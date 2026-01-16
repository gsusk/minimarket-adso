INSERT INTO category ( name)
VALUES ( 'Ropa'),
       ( 'Electrodomesticos'),
       ( 'Muebles'),
       ( 'Hogar'),
       ( 'Accesorios');

INSERT INTO product ( name, description, price, stock, category_id, created_at)
VALUES
-- Ropa
( 'Camiseta blanca', 'Grande y cómoda', 20.15, 50, 1, NOW()),
( 'Jeans azul', 'Corte clásico', 14.99, 40, 1, NOW()),
( 'Chaqueta negra', 'Impermeable y térmica', 7200.00, 25, 1, NOW()),
( 'Sudadera gris', 'Algodón suave', 4200.00, 35, 1, NOW()),
( 'Gorra deportiva', 'Ajustable', 1500.00, 60, 5, NOW()),

-- Electrodomesticos
( 'Pc gamer', 'Con luz led y ventana', 1025, 5, 2, NOW()),
( 'Smartphone', 'Cámara 64MP', 985.00, 12, 2, NOW()),
( 'Televisor 55"', '4K UHD Smart TV', 215.00, 6, 2, NOW()),
( 'Auriculares inalámbricos', 'Cancelación de ruido', 18.05, 20, 2, NOW()),
( 'Microondas', 'Digital 30L', 450.00, 8, 2, NOW()),

-- Muebles
( 'Mesa grande', '2 metros de madera', 10000.00, 3, 3, NOW()),
( 'Silla de oficina', 'Ergonómica reclinable', 320.99, 10, 3, NOW()),
( 'Sofá 3 puestos', 'Tela antimanchas', 1850.00, 4, 3, NOW()),
( 'Estantería', '5 niveles de madera', 270.00, 7, 3, NOW()),

-- Hogar
( 'Lámpara de mesa', 'Luz cálida LED', 85.50, 18, 4, NOW()),
( 'Juego de sábanas', 'Algodón 400 hilos', 22.00, 15, 4, NOW()),
( 'Cafetera eléctrica', 'Filtro reutilizable', 390.00, 9, 4, NOW()),
( 'Aspiradora', 'Alta potencia', 67.00, 6, 4, NOW()),

-- Accesorios
( 'Mochila urbana', 'Resistente al agua', 26.00, 14, 5, NOW()),
( 'Reloj digital', 'Resistente al agua', 310.00, 11, 5, NOW());

INSERT INTO user ( name, last_name, email, password, role, created_at)
VALUES ( 'mario', 'contreras', 'marioc@gmail.com', 'password123', 'USER', NOW()),
       ( 'laura', 'gomez', 'laura@gmail.com', 'password123', 'USER', NOW()),
       ( 'andres', 'lopez', 'andres@gmail.com', 'password123', 'USER', NOW());

INSERT INTO cart ( user_id, guest_id, status, created_at)
VALUES (1, NULL, 'ACTIVE', NOW()),
       (2, NULL, 'ACTIVE', ADDDATE(NOW(), -10)),
       (3, NULL, 'ACTIVE', ADDDATE(NOW(), -15));

INSERT INTO cart_item (cart_id, product_id, unit_price, quantity)
VALUES (1, 1, 100, 2),
       (2, 1, 95, 1),
       (3, 2, 700, 1),
       (1, 2, 52, 2),
       (2, 3, 7200, 1),
       (3, 3, 7200, 2);

INSERT INTO cart ( user_id, guest_id, status)
VALUES (NULL, uuid_to_bin(UUID()), 'ACTIVE'),
       (NULL, uuid_to_bin(UUID()), 'ACTIVE');

INSERT INTO cart_item (cart_id, product_id, unit_price, quantity)
VALUES (1, 4, 4200, 5),
       (5, 4, 4200, 2),
       (4, 4, 4200, 1),
       (4, 5, 1500, 1),
       (5, 5, 1500, 1),
       (3, 5, 1500, 1);
