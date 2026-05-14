CREATE DATABASE stock_management;

USE stock_management;

CREATE TABLE products (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    quantity INT,
    price DOUBLE
);
