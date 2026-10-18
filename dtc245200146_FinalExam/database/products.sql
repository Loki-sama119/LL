CREATE DATABASE IF NOT EXISTS product_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE product_management;
CREATE TABLE IF NOT EXISTS products (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  price DECIMAL(15,2) NOT NULL,
  quantity INT NOT NULL,
  CONSTRAINT chk_price_nonnegative CHECK (price >= 0),
  CONSTRAINT chk_quantity_nonnegative CHECK (quantity >= 0)
) ENGINE=InnoDB;
INSERT INTO products (name,price,quantity)
SELECT 'Laptop Dell Inspiron', 15990000, 12 WHERE NOT EXISTS (SELECT 1 FROM products WHERE name='Laptop Dell Inspiron');
INSERT INTO products (name,price,quantity)
SELECT 'Chuot Logitech', 350000, 45 WHERE NOT EXISTS (SELECT 1 FROM products WHERE name='Chuot Logitech');
INSERT INTO products (name,price,quantity)
SELECT 'Ban phim co', 890000, 24 WHERE NOT EXISTS (SELECT 1 FROM products WHERE name='Ban phim co');
