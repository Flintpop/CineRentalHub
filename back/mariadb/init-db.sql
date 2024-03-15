-- Création de la base de données
CREATE DATABASE IF NOT EXISTS cine_rental_hub;
USE cine_rental_hub;

-- Drop Procedures
DROP PROCEDURE IF EXISTS add_movie;
DROP PROCEDURE IF EXISTS update_movie;
DROP PROCEDURE IF EXISTS disable_movie;
DROP PROCEDURE IF EXISTS set_main_image;
DROP PROCEDURE IF EXISTS add_image_to_movie;
DROP PROCEDURE IF EXISTS delete_image_by_id;
DROP PROCEDURE IF EXISTS create_user;
DROP PROCEDURE IF EXISTS update_user;
DROP PROCEDURE IF EXISTS disable_user;
DROP PROCEDURE IF EXISTS add_to_cart;
DROP PROCEDURE IF EXISTS remove_from_cart;
DROP PROCEDURE IF EXISTS add_rental;
DROP PROCEDURE IF EXISTS add_purchase;
DROP PROCEDURE IF EXISTS add_comment;
DROP PROCEDURE IF EXISTS delete_comment;
DROP PROCEDURE IF EXISTS get_main_image_by_movie_id;
DROP PROCEDURE IF EXISTS get_images_by_movie_id;


-- Drop Triggers
DROP TRIGGER IF EXISTS before_insert_shopping_cart;
DROP TRIGGER IF EXISTS before_insert_update_movies;
DROP TRIGGER IF EXISTS before_insert_update_images;
DROP TRIGGER IF EXISTS before_insert_shopping_cart_rental_duration;
DROP TRIGGER IF EXISTS before_disable_movies;
DROP TRIGGER IF EXISTS before_update_users;
DROP TRIGGER IF EXISTS before_insert_movies;
DROP TRIGGER IF EXISTS before_update_movies;
DROP TRIGGER IF EXISTS before_insert_rentals;
DROP TRIGGER IF EXISTS before_insert_purchases;
DROP TRIGGER IF EXISTS check_rental_overlap;
DROP TRIGGER IF EXISTS prevent_double_purchase;
DROP TRIGGER IF EXISTS check_return_date;

-- Drop Tables
DROP TABLE IF EXISTS shopping_cart;
DROP TABLE IF EXISTS rentals;
DROP TABLE IF EXISTS purchases;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS images;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS movies;


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
    last_name  VARCHAR(255)           NOT NULL,
    first_name VARCHAR(255)           NOT NULL,
    email      VARCHAR(255)           NOT NULL UNIQUE,
    activated  BOOLEAN default TRUE   NOT NULL,
    password   CHAR(128)              NOT NULL, -- sha512, est-ce que c'est la bonne taille ?
    role       ENUM ('user', 'admin') NOT NULL  -- connexion avec email + mdp
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
VALUES (TRUE, 'Schindler\'s List', '1994-02-04', 2.99, 9.99,
        'In German-occupied Poland during World War II, industrialist Oskar Schindler gradually becomes concerned for his Jewish workforce after witnessing their persecution',
        'https://www.youtube.com/watch?v=gG22XNhtnoY');

-- jeux de données pour la table users
INSERT INTO users (last_name, first_name, email, activated, password, role)
VALUES ('Doe', 'John', 'john.doe@example.com', TRUE, SHA2('password123', 512), 'user');
INSERT INTO users (last_name, first_name, email, activated, password, role)
VALUES ('Smith', 'Jane', 'jane.smith@example.com', TRUE, SHA2('password456', 512), 'admin');
INSERT INTO users (last_name, first_name, email, activated, password, role)
VALUES ('Brown', 'Bob', 'bob.brown@example.com', TRUE, SHA2('password789', 512), 'user');
INSERT INTO users (last_name, first_name, email, activated, password, role)
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
VALUES (1, 'https://m.media-amazon.com/images/M/MV5BNDE3ODcxYzMtY2YzZC00NmNlLWJiNDMtZDViZWM2MzIxZDYwXkEyXkFqcGdeQXVyNjAwNDUxODI@._V1_.jpg', FALSE);
INSERT INTO images (movie_id, image_url, main_image)
VALUES (1, 'https://m.media-amazon.com/images/I/815qtzaP9iL._AC_UF1000,1000_QL80_.jpg', FALSE);
INSERT INTO images (movie_id, image_url, main_image)
VALUES (2, 'https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_FMjpg_UX1000_.jpg', TRUE);
INSERT INTO images (movie_id, image_url, main_image)
VALUES (3, 'https://fr.web.img2.acsta.net/medias/nmedia/18/63/97/89/18949761.jpg', TRUE);
INSERT INTO images (movie_id, image_url, main_image)
VALUES (4, 'https://m.media-amazon.com/images/M/MV5BN2EyZjM3NzUtNWUzMi00MTgxLWI0NTctMzY4M2VlOTdjZWRiXkEyXkFqcGdeQXVyNDUzOTQ5MjY@._V1_.jpg', TRUE);
INSERT INTO images (movie_id, image_url, main_image)
VALUES (5, 'https://www.ecranlarge.com/media/cache/1600x1200/uploads/image/001/121/7p8x4u3o3p1jzmbqny3zaloby3m-861.jpg', TRUE);
INSERT INTO images (movie_id, image_url, main_image)
VALUES (6, 'https://m.media-amazon.com/images/I/817sLmprCSL._AC_UF1000,1000_QL80_.jpg', TRUE);

-- TRIGGER

-- Vérifie que le film est disponible à la location / vente
DELIMITER //
CREATE TRIGGER before_insert_shopping_cart
    BEFORE INSERT
    ON shopping_cart
    FOR EACH ROW
BEGIN
    DECLARE msg VARCHAR(255);
    IF (NEW.cart_type = 'rental' AND (SELECT available FROM movies WHERE id = NEW.movie_id) = FALSE) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le film n\'est plus disponible.';
    END IF;
    IF (NEW.cart_type = 'purchase' AND (SELECT purchase_price FROM movies WHERE id = NEW.movie_id) = -1) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le film n\'est pas disponible pour l\'achat.';
    END IF;
END;
//
DELIMITER ;

-- Vérifie si le prix est négatif il doit être égale à -1
DELIMITER //
CREATE TRIGGER before_insert_update_movies
    BEFORE INSERT
    ON movies
    FOR EACH ROW
BEGIN
    DECLARE msg VARCHAR(255);
    IF NEW.daily_rental_price < 0 AND NEW.daily_rental_price <> -1 THEN
        SET msg = 'Vous avez sasi ' + NEW.daily_rental_price + ' comme prix de location. Le prix de location doit être supérieur à 0.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg;
    END IF;
    IF NEW.purchase_price < 0 AND NEW.purchase_price <> -1 THEN
        SET msg = 'Vous avez sasi ' + NEW.purchase_price + ' comme prix d\'achat. Le prix d\'achat doit être supérieur à 0.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg;
    END IF;
END;
//
DELIMITER ;


-- si l’image ajouté est la principale, met toutes les autres images en “non principale”
DELIMITER //
CREATE TRIGGER before_insert_update_images
    BEFORE INSERT
    ON images
    FOR EACH ROW
BEGIN
    IF NEW.main_image = TRUE THEN
        UPDATE images SET main_image = FALSE WHERE movie_id = NEW.movie_id AND main_image = TRUE;
    END IF;
END;
//
DELIMITER ;

-- vérifie lors de la location d’un film que la durée de location est supérieur à 0
DELIMITER //
CREATE TRIGGER before_insert_shopping_cart_rental_duration
    BEFORE INSERT
    ON shopping_cart
    FOR EACH ROW
BEGIN
    DECLARE msg VARCHAR(255);
    IF NEW.cart_type = 'rental' AND NEW.rental_duration <= 0 THEN
        SET msg = 'La durée de location doit être supérieure à 0.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg;
    END IF;
END;
//
DELIMITER ;


-- supprime le film des paniers si il est désactivé
DELIMITER //
CREATE TRIGGER before_disable_movies
    BEFORE UPDATE
    ON movies
    FOR EACH ROW
BEGIN
    IF OLD.available = TRUE AND NEW.available = FALSE THEN
        DELETE FROM shopping_cart WHERE movie_id = OLD.id;
    END IF;
END;
//
DELIMITER ;

-- S'assure que l'email mis à jour n'est pas déjà utilisé par un autre utilisateur.

DELIMITER //
CREATE TRIGGER before_update_users
    BEFORE UPDATE
    ON users
    FOR EACH ROW
BEGIN
    DECLARE msg VARCHAR(255);
    IF NEW.email <> OLD.email AND EXISTS (SELECT id FROM users WHERE email = NEW.email) THEN
        SET msg = CONCAT('L\'email ', NEW.email, ' est déjà utilisé.');
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg;
    END IF;
END;
//
DELIMITER ;


-- Vérification d’un titre de film unique à l’ajout :

DELIMITER //
CREATE TRIGGER before_insert_movies
    BEFORE INSERT
    ON movies
    FOR EACH ROW
BEGIN
    DECLARE msg VARCHAR(255);
    IF EXISTS (SELECT title FROM movies WHERE title = NEW.title) THEN
        SET msg = CONCAT('Un film avec le titre ', NEW.title, ' existe déjà.');
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg;
    END IF;
END;
//
DELIMITER ;
-- Vérification d’un titre de film unique à la modification:
DELIMITER //
CREATE TRIGGER before_update_movies
    BEFORE UPDATE
    ON movies
    FOR EACH ROW
BEGIN
    DECLARE msg VARCHAR(255);
    IF EXISTS (SELECT title FROM movies WHERE title = NEW.title) THEN
        SET msg = CONCAT('Un film avec le titre ', NEW.title, ' existe déjà.');
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg;
    END IF;
END;
//
DELIMITER ;



-- Vérifie que le film est disponible à la location et que l'utilisateur est activé
DELIMITER //
CREATE TRIGGER before_insert_rentals
    BEFORE INSERT
    ON rentals
    FOR EACH ROW
BEGIN
    IF (SELECT activated FROM users WHERE id = NEW.user_id) = FALSE THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Un utilisateur désactivé ne peut pas louer de films.';
    END IF;

    IF (SELECT available FROM movies WHERE id = NEW.movie_id) = FALSE THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le film n\'est plus disponible.';
    END IF;

    IF (SELECT daily_rental_price FROM movies WHERE id = NEW.movie_id) = -1 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le film n\'est pas disponible pour la location.';
    END IF;

END;
//
DELIMITER ;

-- Vérifie que le film est disponible à la location et que l'utilisateur est activé
DELIMITER //
CREATE TRIGGER before_insert_purchases
    BEFORE INSERT
    ON purchases
    FOR EACH ROW
BEGIN

    IF (SELECT activated FROM users WHERE id = NEW.user_id) = FALSE THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Un utilisateur désactivé ne peut pas acheter de films.';
    END IF;

    IF (SELECT available FROM movies WHERE id = NEW.movie_id) = FALSE THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le film n\'est pas disponible.';
    END IF;
    -- test si le prix d'achat n'est pas égal à -1
    IF (SELECT purchase_price FROM movies WHERE id = NEW.movie_id) = -1 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le film n\'est pas disponible pour l\'achat.';
    END IF;
END;
//
DELIMITER ;


-- Trigger pour vérifier le non-chevauchement des locations :
DELIMITER //
CREATE TRIGGER check_rental_overlap
    BEFORE INSERT
    ON rentals
    FOR EACH ROW
BEGIN
    DECLARE msg VARCHAR(255);
    IF EXISTS (SELECT *
               FROM rentals
               WHERE user_id = NEW.user_id
                 AND movie_id = NEW.movie_id
                 AND (NEW.rental_date BETWEEN rental_date AND return_date
                   OR NEW.return_date BETWEEN rental_date AND return_date)) THEN
        SET msg = 'Vous avez déjà essayé de louer ce film pour une période qui se chevauche avec une location existante.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg;
    END IF;
END;
//
DELIMITER ;

-- Trigger pour éviter les doubles achats :
DELIMITER //
CREATE TRIGGER prevent_double_purchase
    BEFORE INSERT
    ON purchases
    FOR EACH ROW
BEGIN
    IF EXISTS (SELECT *
               FROM purchases
               WHERE user_id = NEW.user_id
                 AND movie_id = NEW.movie_id) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Un utilisateur ne peut pas acheter le même film plusieurs fois.';
    END IF;
END;
//
DELIMITER ;


-- Trigger pour vérifier que la date de retour est bien après la date de début :
DELIMITER //
CREATE TRIGGER check_return_date
    BEFORE INSERT
    ON rentals
    FOR EACH ROW
BEGIN
    DECLARE msg VARCHAR(255);
    IF NEW.return_date <= NEW.rental_date THEN
        SET msg = 'La date de retour ( ' + NEW.return_date + ' ) doit être après la date de location ( ' + NEW.rental_date + ' ).';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg;
    END IF;
END;
//
DELIMITER ;


-- PROCEDURE

-- Gestion des films

-- Ajouter un nouveau film
DELIMITER //
CREATE PROCEDURE add_movie(IN title VARCHAR(255), IN release_date DATE, IN description TEXT,
                           IN daily_rental_price DECIMAL(10, 2), IN purchase_price DECIMAL(10, 2),
                           IN link VARCHAR(2000))
BEGIN
    INSERT INTO movies (title, release_date, description, daily_rental_price, purchase_price, link)
    VALUES (title, release_date, description, daily_rental_price, purchase_price, link);
END //
DELIMITER ;

-- CALL add_movie('Inception', '2010-07-16', 'A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a CEO.', 3.99, 14.99, 'https://www.youtube.com/watch?v=YoHD9XEInc0');


-- Mettre à jour un film par son ID
DELIMITER //
CREATE PROCEDURE update_movie(IN movie_id INT, IN title VARCHAR(255), IN release_date DATE, IN description TEXT,
                              IN daily_rental_price DECIMAL(10, 2), IN purchase_price DECIMAL(10, 2),
                              IN link VARCHAR(2000))
BEGIN
    DECLARE msg VARCHAR(255);
    IF daily_rental_price < 0 AND daily_rental_price <> -1 THEN
        SET msg = 'Vous avez saisi ' + daily_rental_price + ' comme prix de location. Le prix de location doit être supérieur à 0.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg;
    END IF;
    IF purchase_price < 0 AND purchase_price <> -1 THEN
        SET msg = 'Vous avez saisi ' + purchase_price + ' comme prix d\'achat. Le prix d\'achat doit être supérieur à 0.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg;
    END IF;
    IF NOT EXISTS (SELECT id FROM movies WHERE id = movie_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le film n\'existe pas.';
    ELSE
        UPDATE movies
        SET title              = title,
            release_date       = release_date,
            description        = description,
            daily_rental_price = daily_rental_price,
            purchase_price     = purchase_price,
            link               = link
        WHERE id = movie_id;
    END IF;
END //
DELIMITER ;

-- CALL update_movie(1, 'Inception', '2010-07-16', 'Updated description here.', 3.99, 14.99, 'https://www.youtube.com/watch?v=YoHD9XEInc0');


-- désactiver un film par son ID
DELIMITER //
CREATE PROCEDURE disable_movie(IN _movie_id INT)
BEGIN
    IF NOT EXISTS (SELECT id FROM movies WHERE id = _movie_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le film n\'existe pas.';
    ELSE
        UPDATE movies SET available = FALSE WHERE id = _movie_id;
        DELETE FROM shopping_cart WHERE movie_id = _movie_id;
    END IF;
END //
DELIMITER ;

-- CALL disable_movie(1);

-- désactiver un film par son ID
DELIMITER //
DROP PROCEDURE IF EXISTS enable_movie;
CREATE PROCEDURE enable_movie(IN _movie_id INT)
BEGIN
    IF NOT EXISTS (SELECT id FROM movies WHERE id = _movie_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le film n\'existe pas.';
    ELSE
        UPDATE movies SET available = TRUE WHERE id = _movie_id;
        DELETE FROM shopping_cart WHERE movie_id = _movie_id;
    END IF;
END //
DELIMITER ;

-- Définir l'image principale d'un film et désactiver les autres images
DELIMITER //
CREATE PROCEDURE set_main_image(IN id_movie INT, IN id_image INT)
BEGIN
    IF NOT EXISTS (SELECT id FROM movies WHERE id = id_movie) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le film spécifié n\'existe pas.';
    ELSEIF NOT EXISTS (SELECT id FROM images WHERE id = id_image AND movie_id = id_movie) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L\'image spécifiée n\'existe pas pour ce film.';
    ELSE
        UPDATE images SET main_image = FALSE WHERE movie_id = id_movie;
        UPDATE images SET main_image = TRUE WHERE id = id_image;
    END IF;
END //
DELIMITER ;

-- CALL set_main_image(1, 2);

-- Récupérer l'image principale d'un film
DELIMITER //
DROP PROCEDURE IF EXISTS get_main_image_by_movie_id;
CREATE PROCEDURE get_main_image_by_movie_id(IN id_movie INT)
BEGIN
    -- Vérifie si le film existe
    IF NOT EXISTS (SELECT id FROM movies WHERE id = id_movie) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le film n\'existe pas.';
    ELSEIF NOT EXISTS (SELECT id FROM images WHERE movie_id = id_movie) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Aucune image n\'est disponible pour ce film.';
    ELSE
        -- Essayez de sélectionner l'image principale; sinon, sélectionnez l'image avec le plus petit ID
        SELECT * FROM images
        WHERE movie_id = id_movie AND main_image = TRUE
        UNION ALL
        SELECT * FROM images
        WHERE movie_id = id_movie
        ORDER BY main_image DESC, id
        LIMIT 1;
    END IF;
END //
DELIMITER ;

-- call getMainImageByMovieId(1);


DELIMITER //
DROP PROCEDURE IF EXISTS get_images_by_movie_id;
CREATE PROCEDURE get_images_by_movie_id(IN id_movie INT)
BEGIN
    -- Vérifie si le film existe
    IF NOT EXISTS (SELECT id FROM movies WHERE id = id_movie) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le film n\'existe pas.';
    ELSEIF NOT EXISTS (SELECT id FROM images WHERE movie_id = id_movie) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Aucune image n\'est disponible pour ce film.';
    ELSE
        SELECT * FROM images WHERE movie_id = id_movie;
    END IF;
END //
DELIMITER ;

-- call getImagesByMovieId(1);


-- AJouter une image à un film
DELIMITER //
DROP PROCEDURE IF EXISTS add_image_to_movie;
CREATE PROCEDURE add_image_to_movie(IN _movie_id INT, IN _image_url VARCHAR(2000))
BEGIN
    IF NOT EXISTS (SELECT id FROM movies WHERE id = _movie_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le film spécifié n\'existe pas.';
    ELSE
        INSERT INTO images (movie_id, image_url, main_image) VALUES (_movie_id, _image_url, FALSE);
    END IF;
END //
DELIMITER ;

-- CALL add_image_to_movie(1, 'https://example.com/new-image.jpg');


-- Supprimer une image d'un film
DELIMITER //
CREATE PROCEDURE delete_image_by_id(IN _image_id INT)
BEGIN
    IF NOT EXISTS (SELECT id FROM images WHERE id = _image_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L\'image spécifiée n\'existe pas.';
    ELSE
        DELETE FROM images WHERE id = _image_id;
    END IF;
END //
DELIMITER ;

-- CALL delete_image_by_id(8);


-- Créer un nouvel utilisateur
DELIMITER //
CREATE PROCEDURE create_user(IN last_name VARCHAR(255), IN first_name VARCHAR(255), IN new_email VARCHAR(255),
                             IN password CHAR(255), IN role ENUM ('user', 'admin'))
BEGIN
    DECLARE msg VARCHAR(255);
    IF EXISTS (SELECT email FROM users WHERE email = new_email) THEN
        SET msg = CONCAT('L\'email ', new_email, ' est déjà utilisé.');
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg;
    ELSE
        INSERT INTO users (last_name, first_name, email, password, role, activated)
        VALUES (last_name, first_name, new_email, password, role, TRUE);
    END IF;
END //
DELIMITER ;

-- CALL create_user('Doe', 'Jane', 'jane.doe@example.com', 'newpassword123', 'user');


-- Mettre à jour un utilisateur
DELIMITER //
CREATE PROCEDURE update_user(IN user_id INT, IN new_last_name VARCHAR(255), IN new_first_name VARCHAR(255),
                             IN new_email VARCHAR(255), IN new_activated BOOLEAN, IN new_role ENUM ('user', 'admin'))
BEGIN
    IF NOT EXISTS (SELECT id FROM users WHERE id = user_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L\'utilisateur n\'existe pas.';
    ELSE
        UPDATE users
        SET last_name  = new_last_name,
            first_name = new_first_name,
            email      = new_email,
            activated=new_activated,
            role       = new_role
        WHERE id = user_id;
    END IF;
END //
DELIMITER ;

-- CALL update_user(1, 'Doe', 'Jane', 'jane.doe@example.com', TRUE, 'admin');


-- Désactiver un utilisateur
DELIMITER //
CREATE PROCEDURE disable_user(IN user_id INT)
BEGIN
    IF NOT EXISTS (SELECT id FROM users WHERE id = user_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L\'utilisateur n\'existe pas.';
    ELSE
        UPDATE users SET activated = FALSE WHERE id = user_id;
    END IF;
END //
DELIMITER ;

-- CALL disable_user(1);


-- Ajouter un article au panier
DELIMITER //
CREATE PROCEDURE add_to_cart(IN user_id INT, IN movie_id INT, IN cart_type ENUM ('purchase', 'rental'),
                             IN rental_duration INT)
BEGIN
    IF NOT EXISTS (SELECT id FROM users WHERE id = user_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L\'utilisateur n\'existe pas.';
    ELSEIF NOT EXISTS (SELECT id FROM movies WHERE id = movie_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le film n\'existe pas.';
    ELSE
        INSERT INTO shopping_cart (user_id, movie_id, cart_type, rental_duration)
        VALUES (user_id, movie_id, cart_type, rental_duration);
    END IF;
END //
DELIMITER ;

-- CALL add_to_cart(1, 1, 'rental', 5);


-- Supprimer un article du panier
DELIMITER //
CREATE PROCEDURE remove_from_cart(IN cart_id INT)
BEGIN
    IF NOT EXISTS (SELECT id FROM shopping_cart WHERE id = cart_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L\'article n\'existe pas dans le panier.';
    ELSE
        DELETE FROM shopping_cart WHERE id = cart_id;
    END IF;
END //
DELIMITER ;

-- CALL remove_from_cart(1);


-- Gestion des Locations (Rentals)


-- Enregistrer une nouvelle location
DELIMITER //
CREATE PROCEDURE add_rental(IN user_id INT, IN movie_id INT, IN rental_date DATETIME, IN return_date DATETIME)
BEGIN
    IF NOT EXISTS (SELECT id FROM users WHERE id = user_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L\'utilisateur n\'existe pas.';
    ELSEIF NOT EXISTS (SELECT id FROM movies WHERE id = movie_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le film n\'existe pas.';
    ELSE
        INSERT INTO rentals (user_id, movie_id, rental_date, return_date)
        VALUES (user_id, movie_id, rental_date, return_date);
    END IF;
END //
DELIMITER ;

-- CALL add_rental(1, 1, '2024-03-01 00:00:00', '2024-03-06 00:00:00');


-- Gestion des Achats (Purchases)


-- Enregistrer un nouvel achat
DELIMITER //
CREATE PROCEDURE add_purchase(IN user_id INT, IN movie_id INT, IN purchase_date DATE)
BEGIN
    IF NOT EXISTS (SELECT id FROM users WHERE id = user_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L\'utilisateur n\'existe pas.';
    ELSEIF NOT EXISTS (SELECT id FROM movies WHERE id = movie_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le film n\'existe pas.';
    ELSE
        INSERT INTO purchases (user_id, movie_id, purchase_date) VALUES (user_id, movie_id, purchase_date);
    END IF;
END //
DELIMITER ;

-- CALL add_purchase(1, 1, '2024-03-01');


-- Gestion des Commentaires (Comments)


-- Ajouter un commentaire
DELIMITER //
CREATE PROCEDURE add_comment(IN movie_id INT, IN user_id INT, IN comment_text TEXT, IN comment_date DATETIME)
BEGIN
    IF NOT EXISTS (SELECT id FROM users WHERE id = user_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L\'utilisateur n\'existe pas.';
    ELSEIF NOT EXISTS (SELECT id FROM movies WHERE id = movie_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le film n\'existe pas.';
    ELSE
        INSERT INTO comments (movie_id, user_id, comment_text, comment_date)
        VALUES (movie_id, user_id, comment_text, comment_date);
    END IF;
END //
DELIMITER ;

-- CALL add_comment(1, 1, 'Amazing movie!', '2024-03-01 00:00:00');


-- Supprimer un commentaire
DELIMITER //
CREATE PROCEDURE delete_comment(IN comment_id INT)
BEGIN
    IF NOT EXISTS (SELECT id FROM comments WHERE id = comment_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le commentaire n\'existe pas.';
    ELSE
        DELETE FROM comments WHERE id = comment_id;
    END IF;
END //
DELIMITER ;

-- CALL delete_comment(1);


