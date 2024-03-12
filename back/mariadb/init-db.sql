-- Création de la base de données
CREATE DATABASE IF NOT EXISTS cine_rental_hub;
USE cine_rental_hub;

-- Création de la table Films
CREATE TABLE IF NOT EXISTS movies
(
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    available          BOOLEAN default TRUE NOT NULL,
    title              VARCHAR(255)         NOT NULL,
    release_date       DATE,                          -- Si null, mettre "prochainement"
    daily_rental_price DECIMAL(10, 2)       NOT NULL, -- Si -1 et si on doit l'implementer, non louable
    purchase_price     DECIMAL(10, 2)       NOT NULL, -- Pareil que location, -1 == pas vendable. Attention si pas vendable ni louable pas afficher ou bug
    description        TEXT                 NOT NULL,
    link               VARCHAR(2000)        NOT NULL  -- chatGPT a voir taille max lien attention mettre verif dans le front
);

-- Création de la table Utilisateurs
CREATE TABLE IF NOT EXISTS users
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    last_name  VARCHAR(255)                NOT NULL,
    first_name VARCHAR(255)                NOT NULL,
    email      VARCHAR(255)                NOT NULL UNIQUE,
    activated  BOOLEAN        default TRUE NOT NULL,
    password   CHAR(255)                   NOT NULL, -- sha512, est-ce que c'est la bonne taille ?
    role       ENUM ('user', 'admin')      NOT NULL  -- connexion avec email + mdp
);

-- Shopping Cart table creation
-- Synchronisation automatique lors de la connexion( fusion  du local et de la bdd)
CREATE TABLE IF NOT EXISTS shopping_cart
(
    id              INT AUTO_INCREMENT PRIMARY KEY,
    cart_type       ENUM ('purchase', 'rental') NOT NULL,
    user_id         INT                         NOT NULL,
    movie_id        INT                         NOT NULL,
    rental_duration INT                         NOT NULL DEFAULT -1, -- trigger impossible -1 et location
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (movie_id) REFERENCES movies (id)
);


-- Création de la table Locations
CREATE TABLE IF NOT EXISTS rentals
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id     INT      NOT NULL,
    movie_id    INT      NOT NULL,
    rental_date DATETIME NOT NULL,
    return_date DATETIME NOT NULL, -- avec les dates on peut calculer le nombre de jours de location pour connaitre le prix total et on suppose que le coût d'un film location ne change pas
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (movie_id) REFERENCES movies (id)
);

-- Table des achats
CREATE TABLE purchases
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    user_id       INT  NOT NULL,
    movie_id      INT  NOT NULL,
    purchase_date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (movie_id) REFERENCES movies (id)
);


-- Création de la table Commentaires
CREATE TABLE IF NOT EXISTS comments
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    movie_id     INT      NOT NULL,
    user_id      INT      NOT NULL,
    comment_text TEXT     NOT NULL,
    comment_date DATETIME NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);


-- Création de la table Images
CREATE TABLE IF NOT EXISTS images
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    movie_id   INT                   NOT NULL,
    image_url  VARCHAR(2000)         NOT NULL, -- 2000 car url
    main_image BOOLEAN DEFAULT FALSE NOT NULL, -- Image d'affiche principale, sert pour afficher dans la liste des movies. Faire un trigger car une seule image ne peut etre autorisee en main_image pour un film
    FOREIGN KEY (movie_id) REFERENCES movies (id)
);


-- jeux de données

-- jeux de données pour la table movies
INSERT INTO movies (available, title, release_date, daily_rental_price, purchase_price, description, link)
VALUES (TRUE, 'The Shawshank Redemption', '1994-09-23', 2.99, 9.99, 'Two imprisoned',
        'https://www.youtube.com/watch?v=6hB3S9bIaco');
INSERT INTO movies (available, title, release_date, daily_rental_price, purchase_price, description, link)
VALUES (TRUE, 'The Godfather', '1972-03-24', 2.99, 9.99,
        'The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.',
        'https://www.youtube.com/watch?v=sY1S34973zA');
INSERT INTO movies (available, title, release_date, daily_rental_price, purchase_price, description, link)
VALUES (TRUE, 'The Dark Knight', '2008-07-18', 2.99, 9.99, 'When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept
    one of the greatest psychological and physical tests of his ability to fight injustice.',
        'https://www.youtube.com/watch?v=EXeTwQWrcwY');
INSERT INTO movies (available, title, release_date, daily_rental_price, purchase_price, description, link)
VALUES (FALSE, 'The Lord of the Rings: The Return of the King', '2003-12-17', 2.99, 9.99, 'Gandalf and Aragorn',
        'https://www.youtube.com/watch?v=r5X-hFf6Bwo');
INSERT INTO movies (available, title, release_date, daily_rental_price, purchase_price, description, link)
VALUES (TRUE, 'Pulp Fiction', '1994-10-14', 2.99, 9.99,
        'The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.',
        'https://www.youtube.com/watch?v=s7EdQ4FqbhY');
INSERT INTO movies (available, title, release_date, daily_rental_price, purchase_price, description, link)
VALUES (TRUE, 'Schindler''s List', '1994-02-04', 2.99, 9.99,
        'In German-occupied Poland during World War II, industrialist Oskar Schindler gradually becomes concerned for his Jewish workforce after witnessing their persecution',
        'https://www.youtube.com/watch?v=gG22XNhtnoY');

-- jeux de données pour la table users
INSERT INTO users (last_name, first_name, email, 	activated, password, role)
VALUES ('Doe', 'John', 'john.doe@example.com', TRUE, SHA2('password123', 512), 'user');
INSERT INTO users (last_name, first_name, email, 	activated, password, role)
VALUES ('Smith', 'Jane', 'jane.smith@example.com', TRUE, SHA2('password456', 512), 'admin');
INSERT INTO users (last_name, first_name, email, 	activated, password, role)
VALUES ('Brown', 'Bob', 'bob.brown@example.com', TRUE, SHA2('password789', 512), 'user');
INSERT INTO users (last_name, first_name, email, 	activated, password, role)
VALUES ('Johnson', 'Alice', 'alice.johnson@example.com', TRUE, SHA2('password321', 512), 'admin');

-- jeux de données pour la table shopping_cart
INSERT INTO shopping_cart (cart_type, user_id, movie_id, rental_duration)
VALUES ('rental', 1, 1, 3);
INSERT INTO shopping_cart (cart_type, user_id, movie_id, rental_duration)
VALUES ('rental', 1, 2, 3);
INSERT INTO shopping_cart (cart_type, user_id, movie_id, rental_duration)
VALUES ('rental', 2, 3, 3);
INSERT INTO shopping_cart (cart_type, user_id, movie_id, rental_duration)
VALUES ('rental', 2, 4, 3);
INSERT INTO shopping_cart (cart_type, user_id, movie_id, rental_duration)
VALUES ('rental', 3, 5, 3);
INSERT INTO shopping_cart (cart_type, user_id, movie_id, rental_duration)
VALUES ('rental', 3, 6, 3);

-- jeux de données pour la table rentals

INSERT INTO rentals (user_id, movie_id, rental_date, return_date)
VALUES (1, 1, '2022-01-01 00:00:00', '2022-01-04 00:00:00');
INSERT INTO rentals (user_id, movie_id, rental_date, return_date)
VALUES (1, 2, '2022-01-01 00:00:00', '2022-01-04 00:00:00');
INSERT INTO rentals (user_id, movie_id, rental_date, return_date)
VALUES (2, 3, '2022-01-01 00:00:00', '2022-01-04 00:00:00');
INSERT INTO rentals (user_id, movie_id, rental_date, return_date)
VALUES (2, 4, '2022-01-01 00:00:00', '2022-01-04 00:00:00');
INSERT INTO rentals (user_id, movie_id, rental_date, return_date)
VALUES (3, 5, '2022-01-01 00:00:00', '2022-01-04 00:00:00');
INSERT INTO rentals (user_id, movie_id, rental_date, return_date)
VALUES (3, 6, '2022-01-01 00:00:00', '2022-01-04 00:00:00');

-- jeux de données pour la table purchases avec des dates en mars 2024

INSERT INTO purchases (user_id, movie_id, purchase_date)
VALUES (1, 1, '2024-03-01');
INSERT INTO purchases (user_id, movie_id, purchase_date)
VALUES (1, 2, '2024-03-01');
INSERT INTO purchases (user_id, movie_id, purchase_date)
VALUES (2, 3, '2024-03-01');
INSERT INTO purchases (user_id, movie_id, purchase_date)
VALUES (2, 4, '2024-03-01');
INSERT INTO purchases (user_id, movie_id, purchase_date)
VALUES (3, 5, '2024-03-01');
INSERT INTO purchases (user_id, movie_id, purchase_date)
VALUES (3, 6, '2024-03-01');

-- jeux de données pour la table comments
INSERT INTO comments (movie_id, user_id, comment_text, comment_date)
VALUES (1, 1, 'This is a great movie!', '2022-01-01 00:00:00');
INSERT INTO comments (movie_id, user_id, comment_text, comment_date)
VALUES (2, 1, 'This is a great movie!', '2022-01-01 00:00:00');
INSERT INTO comments (movie_id, user_id, comment_text, comment_date)
VALUES (3, 2, 'This is a great movie!', '2022-01-01 00:00:00');
INSERT INTO comments (movie_id, user_id, comment_text, comment_date)
VALUES (4, 2, 'This is a great movie!', '2022-01-01 00:00:00');
INSERT INTO comments (movie_id, user_id, comment_text, comment_date)
VALUES (5, 3, 'This is a great movie!', '2022-01-01 00:00:00');
INSERT INTO comments (movie_id, user_id, comment_text, comment_date)
VALUES (6, 3, 'This is a great movie!', '2022-01-01 00:00:00');

-- jeux de données pour la table images
INSERT INTO images (movie_id, image_url, main_image)
VALUES (1, 'https://www.imdb.com/title/tt0111161/mediaviewer/rm10105600/', TRUE);
INSERT INTO images (movie_id, image_url, main_image)
VALUES (2, 'https://www.imdb.com/title/tt0068646/mediaviewer/rm10105600/', TRUE);
INSERT INTO images (movie_id, image_url, main_image)
VALUES (3, 'https://www.imdb.com/title/tt0468569/mediaviewer/rm10105600/', TRUE);
INSERT INTO images (movie_id, image_url, main_image)
VALUES (4, 'https://www.imdb.com/title/tt0167260/mediaviewer/rm10105600/', TRUE);
INSERT INTO images (movie_id, image_url, main_image)
VALUES (5, 'https://www.imdb.com/title/tt0110912/mediaviewer/rm10105600/', TRUE);
INSERT INTO images (movie_id, image_url, main_image)
VALUES (6, 'https://www.imdb.com/title/tt0108052/mediaviewer/rm10105600/', TRUE);



