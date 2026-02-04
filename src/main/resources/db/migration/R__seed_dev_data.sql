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

-- 1. Categories
INSERT INTO category (id, name, attribute_definitions)
VALUES
(1, 'Consumer Electronics', '[{"name": "storage_gb", "type": "number", "required": true, "options": [128, 256, 512, 1024]}, {"name": "warranty_months", "type": "number", "min": 12, "max": 60}]'),
(2, 'Fashion (Apparel)', '[{"name": "size", "type": "string", "required": true, "options": ["XS", "S", "M", "L", "XL", "XXL"]}, {"name": "gender", "type": "string", "required": true, "options": ["Men", "Women", "Unisex"]}, {"name": "material", "type": "string", "required": true}]'),
(3, 'Footwear', '[{"name": "size_us", "type": "number", "required": true, "min": 4, "max": 15}, {"name": "gender", "type": "string", "required": true, "options": ["Men", "Women", "Kids"]}, {"name": "type", "type": "string", "options": ["Sneakers", "Boots", "Sandals", "Formal"]}]'),
(4, 'Furniture', '[{"name": "material", "type": "string", "required": true, "options": ["Wood", "Metal", "Glass", "Fabric"]}, {"name": "assembly_required", "type": "boolean", "required": true}, {"name": "width_cm", "type": "number", "min": 10}, {"name": "height_cm", "type": "number", "min": 10}]'),
(5, 'Books', '[{"name": "format", "type": "string", "required": true, "options": ["Hardcover", "Paperback", "E-Book"]}, {"name": "page_count", "type": "number", "min": 1}, {"name": "genre", "type": "string", "required": true}]'),
(6, 'Sports Equipment', '[{"name": "sport", "type": "string", "required": true, "options": ["Soccer", "Basketball", "Tennis", "Gym", "Cycling"]}, {"name": "weight_kg", "type": "number", "min": 0.1, "max": 100}, {"name": "is_professional", "type": "boolean", "required": false}]'),
(7, 'Beauty & Personal Care', '[{"name": "volume_ml", "type": "number", "required": true, "min": 10, "max": 1000}, {"name": "skin_type", "type": "string", "options": ["Oily", "Dry", "Combination", "Normal", "Sensitive"]}, {"name": "cruelty_free", "type": "boolean", "required": true}]'),
(8, 'Home Appliances', '[{"name": "power_watts", "type": "number", "required": true, "min": 100, "max": 5000}, {"name": "energy_rating", "type": "string", "options": ["A+++", "A++", "A+", "A", "B", "C"]}, {"name": "warranty_years", "type": "number", "required": true, "min": 1, "max": 10}]');

-- 2. Products
INSERT INTO product (name, description, price, stock, category_id, created_at, brand, attributes)
VALUES
-- Consumer Electronics (Cat ID 1)
('iPhone 15 Pro', 'Titanium design, A17 Pro chip.', 999.00, 50, 1, NOW(), 'Apple', '{"storage_gb": 256, "warranty_months": 12}'),
('Samsung Galaxy S24', 'AI features, excellent camera.', 899.00, 45, 1, NOW(), 'Samsung', '{"storage_gb": 128, "warranty_months": 24}'),
('Dell XPS 13', 'Ultra-thin laptop, high performance.', 1200.00, 30, 1, NOW(), 'Dell', '{"storage_gb": 512, "warranty_months": 24}'),
('Sony Headphones', 'Noise cancelling, top tier sound.', 350.00, 60, 1, NOW(), 'Sony', '{"storage_gb": 128, "warranty_months": 12}'),
('HP Spectre x360', 'Convertible laptop with pen.', 1400.00, 20, 1, NOW(), 'HP', '{"storage_gb": 1024, "warranty_months": 36}'),

-- Fashion (Cat ID 2) (Brand generic)
('Vintage T-Shirt', '100% Cotton, classic fit.', 25.00, 100, 2, NOW(), 'Generic', '{"size": "L", "gender": "Unisex", "material": "Cotton"}'),
('Slim Fit Jeans', 'Dark blue denim.', 45.00, 80, 2, NOW(), 'Levis', '{"size": "M", "gender": "Men", "material": "Denim"}'),
('Summer Dress', 'Floral pattern, breathable.', 55.00, 60, 2, NOW(), 'Zara', '{"size": "S", "gender": "Women", "material": "Polyester"}'),
('Leather Jacket', 'Genuine leather biker jacket.', 250.00, 15, 2, NOW(), 'Mango', '{"size": "XL", "gender": "Men", "material": "Leather"}'),

-- Footwear (Cat ID 3)
('Running Sneakers', 'High arch support.', 120.00, 40, 3, NOW(), 'Nike', '{"size_us": 10, "gender": "Men", "type": "Sneakers"}'),
('Formal Leather Shoes', 'Elegant black leather.', 150.00, 25, 3, NOW(), 'Clarks', '{"size_us": 9, "gender": "Men", "type": "Formal"}'),
('Winter Boots', 'Insulated and waterproof.', 180.00, 30, 3, NOW(), 'Timberland', '{"size_us": 8, "gender": "Women", "type": "Boots"}'),
('Kids Sandals', 'Velcro strap, colorful.', 35.00, 50, 3, NOW(), 'Crocs', '{"size_us": 5, "gender": "Kids", "type": "Sandals"}'),

-- Furniture (Cat ID 4)
('Office Desk', 'Minimalist wooden desk.', 220.00, 10, 4, NOW(), 'IKEA', '{"material": "Wood", "assembly_required": true, "width_cm": 120, "height_cm": 75}'),
('Ergonomic Chair', 'Mesh back, adjustable.', 300.00, 15, 4, NOW(), 'Herman Miller', '{"material": "Metal", "assembly_required": true, "width_cm": 60, "height_cm": 110}'),
('Glass Coffee Table', 'Modern design.', 150.00, 8, 4, NOW(), 'West Elm', '{"material": "Glass", "assembly_required": false, "width_cm": 90, "height_cm": 45}'),

-- Books (Cat ID 5)
('The Great Gatsby', 'Classic novel.', 15.00, 200, 5, NOW(), 'Scribner', '{"format": "Hardcover", "page_count": 180, "genre": "Fiction"}'),
('Clean Code', 'Guide to better programming.', 45.00, 100, 5, NOW(), 'Pearson', '{"format": "Paperback", "page_count": 464, "genre": "Technology"}'),
('Sci-Fi Anthology', 'Collection of short stories.', 20.00, 50, 5, NOW(), 'Tor', '{"format": "E-Book", "page_count": 350, "genre": "Science Fiction"}'),

-- Sports Equipment (Cat ID 6)
('Pro Soccer Ball', 'FIFA approved size 5.', 80.00, 40, 6, NOW(), 'Adidas', '{"sport": "Soccer", "weight_kg": 0.45, "is_professional": true}'),
('Adjustable Dumbbells', 'Set of 2, up to 20kg each.', 150.00, 20, 6, NOW(), 'Bowflex', '{"sport": "Gym", "weight_kg": 40, "is_professional": false}'),
('Tennis Racket', 'Carbon fiber frame.', 180.00, 25, 6, NOW(), 'Wilson', '{"sport": "Tennis", "weight_kg": 0.3, "is_professional": true}'),

-- Beauty & Personal Care (Cat ID 7)
('Hydrating Moisturizer', 'Daily face cream.', 30.00, 60, 7, NOW(), 'CeraVe', '{"volume_ml": 50, "skin_type": "Dry", "cruelty_free": true}'),
('Matte Sunscreen', 'SPF 50 protection.', 25.00, 70, 7, NOW(), 'La Roche-Posay', '{"volume_ml": 60, "skin_type": "Oily", "cruelty_free": true}'),
('Gentle Cleanser', 'Foaming face wash.', 18.00, 80, 7, NOW(), 'Cetaphil', '{"volume_ml": 200, "skin_type": "Sensitive", "cruelty_free": false}'),

-- Home Appliances (Cat ID 8)
('Robot Vacuum', 'Smart navigation.', 450.00, 12, 8, NOW(), 'iRobot', '{"power_watts": 60, "energy_rating": "A+", "warranty_years": 2}'),
('Blender 3000', 'High speed smoothing.', 120.00, 25, 8, NOW(), 'Ninja', '{"power_watts": 1200, "energy_rating": "B", "warranty_years": 1}'),
('Washing Machine', 'Front load, silent.', 850.00, 5, 8, NOW(), 'LG', '{"power_watts": 2200, "energy_rating": "A+++", "warranty_years": 5}');

-- 3. Users
INSERT INTO user (name, last_name, email, password, role, created_at)
VALUES 
('Mario', 'Contreras', 'marioc@gmail.com', 'password123', 'USER', NOW()),
('Laura', 'Gomez', 'laura@gmail.com', 'password123', 'USER', NOW()),
('Andres', 'Lopez', 'andres@gmail.com', 'password123', 'USER', NOW());

-- 4. Carts
INSERT INTO cart (user_id, guest_id, status, created_at)
VALUES 
(1, NULL, 'ACTIVE', NOW()),
(2, NULL, 'ACTIVE', ADDDATE(NOW(), -10));

INSERT INTO cart_item (cart_id, product_id, unit_price, quantity)
VALUES 
(1, 1, 999.00, 1), 
(1, 6, 25.00, 2), 
(2, 4, 350.00, 1);
