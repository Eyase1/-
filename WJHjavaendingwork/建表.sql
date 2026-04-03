--CREATE TABLE "USERS" (
--    id INT ,
--    username VARCHAR(50) NOT NULL,
--    password VARCHAR(100) NOT NULL
--);

--CREATE TABLE "PRODUCTS" (
--    id INT ,
--    name TEXT NOT NULL,
--    description TEXT,
--    price DECIMAL(10, 2) NOT NULL,
--    image_url VARCHAR(255)
--);

--CREATE TABLE "CARTITEMS" (
--    id INT PRIMARY KEY ,
--    user_id INT,
--    product_id INT,
--    quantity INT DEFAULT 1
--);

--CREATE TABLE "COMMENTS" (
--    id INT PRIMARY KEY,
--    product_id INT,
--    username VARCHAR(50) NOT NULL,
--    comment_text TEXT NOT NULL,
--    rating INT NOT NULL,
--    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
--);

--CREATE TABLE "ORDERS" (
--    id INT PRIMARY KEY AUTO_INCREMENT,
--    user_id INT,
--    product_id INT,
--    quantity INT,
--    total_amount DECIMAL(10, 2),
--    transaction_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
--);

CREATE TABLE "ADMIN" (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    last_login TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

