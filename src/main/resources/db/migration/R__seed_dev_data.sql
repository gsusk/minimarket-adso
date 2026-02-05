SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE cart_item;
TRUNCATE TABLE order_item;
TRUNCATE TABLE orders;
TRUNCATE TABLE cart;
TRUNCATE TABLE image;
TRUNCATE TABLE product;
TRUNCATE TABLE category;
TRUNCATE TABLE user;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO category (id, name, parent_id, attribute_definitions)
VALUES
(1, 'Electronics', NULL, '[{"name": "brand", "type": "string", "required": true}, {"name": "warranty_months", "type": "number", "min": 0}]'),
(2, 'Books', NULL, '[{"name": "author", "type": "string", "required": true}, {"name": "genre", "type": "string", "facetable": true}]'),
(3, 'Clothing', NULL, '[{"name": "size", "type": "string", "required": true, "options": ["XS", "S", "M", "L", "XL", "XXL"], "facetable": true}, {"name": "material", "type": "string", "facetable": true}]'),
(4, 'Sports', NULL, '[
  {
    "name": "sport_type",
    "type": "string",
    "required": true,
    "facetable": true
  },
  {
    "name": "weight_kg",
    "type": "number"
  }
]');

INSERT INTO category (id, name, parent_id, attribute_definitions)
VALUES
(5, 'TVs', 1, '[{"name": "screen_size_inch", "type": "number", "required": true}, {"name": "resolution", "type": "string", "options": ["4K", "8K", "Full HD"], "facetable":  true}]'),
(6, 'PCs', 1, '[{"name": "ram_gb", "type": "number", "required": true, "facetable":  true}, {"name": "cpu", "type": "string", "required": true}]'),
(7, 'Phones', 1, '[{"name": "storage_gb", "type": "number", "required": true, "options": [64, 128, 256, 512, 1024], "facetable": true}, {"name": "dual_sim", "type": "boolean"}]'),
(8, 'Earphones', 1, '[{"name": "type", "type": "string", "options": ["In-ear", "Over-ear", "On-ear"]}, {"name": "wireless", "type": "boolean", "required": true}]'),
(9, 'E-Books', 2, '[{"name": "file_format", "type": "string", "options": ["EPUB", "PDF", "MOBI"], "facetable": true}]'),
(10, 'Physical Books', 2, '[{"name": "weight_g", "type": "number", "required": true}, {"name": "hardcover", "type": "boolean"}]');

INSERT INTO product (name, description, price, stock, category_id, created_at, brand, attributes)
VALUES
('Samsung OLED 65', 'Crystal clear colors.', 1500.00, 10, 5, NOW(), 'Samsung', '{"brand": "Samsung", "warranty_months": 24, "screen_size_inch": 65, "resolution": "4K"}'),
('LG C3 OLED 55', 'Perfect blacks and infinite contrast.', 1200.00, 8, 5, NOW(), 'LG', '{"brand": "LG", "warranty_months": 24, "screen_size_inch": 55, "resolution": "4K"}'),
('Sony Bravia XR 75', 'Immersive cinematic experience.', 2500.00, 5, 5, NOW(), 'Sony', '{"brand": "Sony", "warranty_months": 12, "screen_size_inch": 75, "resolution": "4K"}'),
('MacBook Air M2', 'Powerful and light.', 1100.00, 15, 6, NOW(), 'Apple', '{"brand": "Apple", "warranty_months": 12, "ram_gb": 16, "cpu": "M2"}'),
('Gaming PC Ultra', 'Liquid cooled gaming beast.', 2800.00, 4, 6, NOW(), 'Custom', '{"brand": "Custom", "warranty_months": 24, "ram_gb": 32, "cpu": "Intel i9-14900K"}'),
('ThinkPad X1 Carbon', 'The ultimate business laptop.', 1600.00, 12, 6, NOW(), 'Lenovo', '{"brand": "Lenovo", "warranty_months": 36, "ram_gb": 16, "cpu": "Intel i7-1365U"}'),
('iPhone 15', 'Latest model.', 900.00, 25, 7, NOW(), 'Apple', '{"brand": "Apple", "warranty_months": 12, "storage_gb": 128, "dual_sim": false}'),
('Pixel 8 Pro', 'The best of Google AI.', 999.00, 20, 7, NOW(), 'Google', '{"brand": "Google", "warranty_months": 12, "storage_gb": 256, "dual_sim": true}'),
('Galaxy S24 Ultra', 'Unleash new levels of creativity.', 1299.00, 18, 7, NOW(), 'Samsung', '{"brand": "Samsung", "warranty_months": 24, "storage_gb": 512, "dual_sim": true}'),
('AirPods Pro', 'Noise cancelling.', 250.00, 40, 8, NOW(), 'Apple', '{"brand": "Apple", "warranty_months": 12, "type": "In-ear", "wireless": true}'),
('Sony WH-1000XM5', 'Industry leading noise cancellation.', 399.00, 15, 8, NOW(), 'Sony', '{"brand": "Sony", "warranty_months": 12, "type": "Over-ear", "wireless": true}'),
('Galaxy Buds 2 Pro', 'Studio quality sound.', 199.00, 30, 8, NOW(), 'Samsung', '{"brand": "Samsung", "warranty_months": 12, "type": "In-ear", "wireless": true}'),
('Digital Fortress', 'Dan Brown technothriller.', 9.99, 100, 9, NOW(), 'Corgi', '{"author": "Dan Brown", "genre": "Thriller", "file_format": "EPUB"}'),
('Foundation', 'The epic saga begins.', 12.99, 80, 9, NOW(), 'Gnome', '{"author": "Isaac Asimov", "genre": "Sci-Fi", "file_format": "MOBI"}'),
('The Hobbit', 'Classic fantasy.', 25.00, 30, 10, NOW(), 'Allen & Unwin', '{"author": "J.R.R. Tolkien", "genre": "Fantasy", "weight_g": 500, "hardcover": true}'),
('Clean Code', 'A handbook of agile software craftsmanship.', 45.00, 40, 10, NOW(), 'Prentice Hall', '{"author": "Robert C. Martin", "genre": "Technology", "weight_g": 800, "hardcover": false}'),
('1984', 'Big Brother is watching you.', 15.00, 60, 10, NOW(), 'Harcourt Brace', '{"author": "George Orwell", "genre": "Dystopian", "weight_g": 350, "hardcover": true}'),
('Cotton T-Shirt', 'Breathable cotton.', 20.00, 50, 3, NOW(), 'Generic', '{"size": "L", "material": "Cotton"}'),
('Denim Jacket', 'Classic blue denim.', 85.00, 20, 3, NOW(), 'Levis', '{"size": "M", "material": "Denim"}'),
('Slim Fit Jeans', 'Perfect fit for everyday wear.', 60.00, 35, 3, NOW(), 'Wrangler', '{"size": "S", "material": "Cotton/Elastane"}'),
('Tennis Racket Pro', 'Carbon fiber build.', 180.00, 15, 4, NOW(), 'Wilson', '{"sport_type": "Tennis", "weight_kg": 0.3}'),
('Basketball Official', 'Official size and weight.', 45.00, 40, 4, NOW(), 'Spalding', '{"sport_type": "Basketball", "weight_kg": 0.6}'),
('Yoga Mat High Grip', 'Extra thick for comfort.', 35.00, 100, 4, NOW(), 'Lululemon', '{"sport_type": "Fitness", "weight_kg": 1.2}');

INSERT INTO user (name, last_name, email, password, role, created_at)
VALUES 
('Mario', 'Contreras', 'marioc@gmail.com', 'password123', 'USER', NOW()),
('Laura', 'Gomez', 'laura@gmail.com', 'password123', 'USER', NOW()),
('Andres', 'Lopez', 'andres@gmail.com', 'password123', 'USER', NOW());

INSERT INTO cart (user_id, guest_id, status, created_at)
VALUES 
(1, NULL, 'ACTIVE', NOW()),
(2, NULL, 'ACTIVE', ADDDATE(NOW(), -10));

INSERT INTO cart_item (cart_id, product_id, unit_price, quantity)
VALUES 
(1, 1, 1500.00, 1), 
(2, 6, 25.00, 1);
