ALTER TABLE product ADD COLUMN category_name VARCHAR(255) NULL;

UPDATE product p
    JOIN category c ON p.category_id = c.id
SET p.category_name = c.name;

ALTER TABLE product MODIFY COLUMN category_name VARCHAR(255) NOT NULL;