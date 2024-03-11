drop table if exists rentals;
drop table if exists purchases;
drop table if exists users;
drop table if exists films;
drop table if exists admin;
drop database if exists film_rental;

CREATE DATABASE IF NOT EXISTS film_rental;
USE film_rental;

-- Table des films avec prix de location et d'achat
CREATE TABLE films (
                       film_id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       release_date DATE NOT NULL,
                       description TEXT,
                       photo_urls TEXT,
                       rental_price DECIMAL(10,2),
                       purchase_price DECIMAL(10,2)
);

-- Table des utilisateurs
CREATE TABLE users (
                       user_id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password_hash CHAR(60) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       role ENUM('user', 'admin') NOT NULL DEFAULT 'user'
);

-- Table des locations
CREATE TABLE rentals (
                         rental_id INT AUTO_INCREMENT PRIMARY KEY,
                         user_id INT,
                         film_id INT,
                         rental_date DATE NOT NULL,
                         return_date DATE NOT NULL,
                         FOREIGN KEY (user_id) REFERENCES users(user_id),
                         FOREIGN KEY (film_id) REFERENCES films(film_id)
);

-- Table des achats
CREATE TABLE purchases (
                           purchase_id INT AUTO_INCREMENT PRIMARY KEY,
                           user_id INT,
                           film_id INT,
                           purchase_date DATE NOT NULL,
                           price DECIMAL(10,2), -- Prix d'achat, dupliqué ?
                           FOREIGN KEY (user_id) REFERENCES users(user_id),
                           FOREIGN KEY (film_id) REFERENCES films(film_id)
);