ALTER TABLE category
ADD COLUMN parent_id BIGINT;

ALTER TABLE category
ADD CONSTRAINT fk_category_parent
FOREIGN KEY (parent_id) REFERENCES category(id);
