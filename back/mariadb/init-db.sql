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
DROP TABLE IF EXISTS comments;
CREATE TABLE IF NOT EXISTS comments
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    movie_id     INT      NOT NULL,
    user_id      INT      NOT NULL,
    comment_text TEXT     NOT NULL,
    comment_date DATETIME NOT NULL,
    image_text   TEXT,
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
        'https://www.youtube.com/embed/6hB3S9bIaco'),
       (TRUE, 'The Godfather', '1972-03-24', 2.99, 9.99,
        'The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.',
        'https://www.youtube.com/embed/sY1S34973zA'),
       (TRUE, 'The Dark Knight', '2008-07-18', 2.99, 9.99,
        'When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.',
        'https://www.youtube.com/embed/EXeTwQWrcwY'),
       (FALSE, 'The Lord of the Rings: The Return of the King', '2003-12-17', 2.99, 9.99, 'Gandalf and Aragorn',
        'https://www.youtube.com/embed/r5X-hFf6Bwo'),
       (TRUE, 'Pulp Fiction', '1994-10-14', 2.99, 9.99,
        'The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.',
        'https://www.youtube.com/embed/s7EdQ4FqbhY'),
       (TRUE, 'Schindler\'s List', '1994-02-04', 2.99, 9.99,
        'In German-occupied Poland during World War II, industrialist Oskar Schindler gradually becomes concerned for his Jewish workforce after witnessing their persecution',
        'https://www.youtube.com/embed/gG22XNhtnoY'),
       (TRUE, 'Avatar', '2009-12-18', 3.99, 14.99,
        'A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.',
        'https://www.youtube.com/embed/5PSNL1qE6VY'),
       (TRUE, 'Iron man', '2008-05-02', 3.99, 14.99,
        'After being held captive in an Afghan cave, billionaire engineer Tony Stark creates a unique weaponized suit of armor to fight evil.',
        'https://www.youtube.com/embed/8hYlB38asDY');

-- jeux de données pour la table users
INSERT INTO users (last_name, first_name, email, password, role)
VALUES ('User', 'Anonyme', 'anonyme@exemple.com', SHA2('anonyme', 512), 'user'),
       ('Doe', 'John', 'john.doe@exemple.com', SHA2('password123', 512), 'user'),
       ('Smith', 'Jane', 'jane.smith@example.com', SHA2('password456', 512), 'admin'),
       ('Brown', 'Bob', 'bob.brown@example.com', SHA2('password789', 512), 'user'),
       ('Johnson', 'Alice', 'alice.johnson@example.com', SHA2('password321', 512), 'admin'),
       ('Garcia', 'Maria', 'Maria.Garcia@gmail.com', SHA2('password123', 512), 'user'),
       ('Smith', 'John', 'John.Smith@gmail.com', SHA2('password124', 512), 'user'),
       ('Brown', 'Sarah', 'Sarah.Brown@gmail.com', SHA2('password125', 512), 'user'),
       ('Johnson', 'Mike', 'Mike.Johnson@gmail.com', SHA2('password126', 512), 'user'),
       ('Lee', 'Karen', 'Karen.Lee@gmail.com', SHA2('password127', 512), 'user'),
       ('Patel', 'Raj', 'Raj.Patel@gmail.com', SHA2('password128', 512), 'user'),
       ('Davis', 'Jessica', 'Jessica.Davis@gmail.com', SHA2('password129', 512), 'user'),
       ('Martinez', 'Carlos', 'Carlos.Martinez@gmail.com', SHA2('password130', 512), 'user'),
       ('Nguyen', 'Linda', 'Linda.Nguyen@gmail.com', SHA2('password131', 512), 'user'),
       ('Clark', 'James', 'James.Clark@gmail.com', SHA2('password132', 512), 'user'),
       ('Wilson', 'Emma', 'Emma.Wilson@gmail.com', SHA2('password233', 512), 'user'),
       ('Moore', 'Jack', 'Jack.Moore@gmail.com', SHA2('password234', 512), 'user'),
       ('Taylor', 'Olivia', 'Olivia.Taylor@gmail.com', SHA2('password235', 512), 'user'),
       ('Anderson', 'George', 'George.Anderson@gmail.com', SHA2('password236', 512), 'user'),
       ('Thomas', 'Mia', 'Mia.Thomas@gmail.com', SHA2('password237', 512), 'user'),
       ('Jackson', 'Ethan', 'Ethan.Jackson@gmail.com', SHA2('password238', 512), 'user'),
       ('White', 'Sophia', 'Sophia.White@gmail.com', SHA2('password239', 512), 'user'),
       ('Harris', 'Noah', 'Noah.Harris@gmail.com', SHA2('password240', 512), 'user'),
       ('Martin', 'Amelia', 'Amelia.Martin@gmail.com', SHA2('password241', 512), 'user'),
       ('Garcia', 'Lucas', 'Lucas.Garcia@gmail.com', SHA2('password242', 512), 'user');


-- jeux de données pour la table shopping_cart
INSERT INTO shopping_cart (cart_type, user_id, movie_id, rental_duration)
VALUES ('rental', 4, 1, 3);
INSERT INTO shopping_cart (cart_type, user_id, movie_id, rental_duration)
VALUES ('rental', 4, 2, 3);
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
VALUES (4, 1, '2022-01-01 00:00:00', '2022-01-04 00:00:00');
INSERT INTO rentals (user_id, movie_id, rental_date, return_date)
VALUES (4, 2, '2022-01-01 00:00:00', '2022-01-04 00:00:00');
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
VALUES (4, 1, '2024-03-01');
INSERT INTO purchases (user_id, movie_id, purchase_date)
VALUES (4, 2, '2024-03-01');
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
VALUES (1, 2, 'Un classique absolu. Les performances et la direction sont intouchables.', '2024-03-15 00:00:00'),
       (1, 5, 'Ce film change la vie. Je le recommande à tous ceux qui cherchent un récit profond et significatif.',
        '2024-03-16 00:00:00'),
       (2, 6, 'Marlon Brando est phénoménal. Le meilleur film sur le crime organisé jamais réalisé.',
        '2024-03-17 00:00:00'),
       (2, 7, 'Le scénario, la mise en scène et la musique créent une atmosphère incroyable. À voir absolument.',
        '2024-03-18 00:00:00'),
       (3, 8, 'Heath Ledger en Joker est légendaire. Un film sombre qui redéfinit les films de super-héros.',
        '2024-03-19 00:00:00'),
       (3, 9, 'Christopher Nolan a créé un chef-d\'œuvre. Un film incontournable pour les amateurs de Batman.',
        '2024-03-20 00:00:00'),
       (5, 10, 'Tarantino à son meilleur. Dialogues incroyables et scènes mémorables.', '2024-03-21 00:00:00'),
       (5, 11, 'Une exploration brillante de la violence et de la rédemption. Pulp Fiction reste intemporel.',
        '2024-03-22 00:00:00'),
       (6, 12,
        'Une histoire poignante qui montre le meilleur et le pire de l\'humanité. Schindler\'s List est un incontournable.',
        '2024-03-23 00:00:00'),
       (6, 13, 'Ce film m\'a profondément ému. L\'importance de se souvenir et d\'apprendre de l\'histoire.',
        '2024-03-24 00:00:00'),
       (7, 14, 'Avatar est visuellement époustouflant et novateur. Cameron repousse les limites du cinéma.',
        '2024-03-25 00:00:00'),
       (7, 15, 'Une aventure immersive sur Pandora. Le message sur l\'environnement et la connexion est puissant.',
        '2024-03-26 00:00:00'),
       (8, 16, 'Iron Man a lancé l\'univers Marvel avec style. Robert Downey Jr. est né pour ce rôle.',
        '2024-03-27 00:00:00'),
       (8, 17, 'Action, humour et cœur. Iron Man a tout pour plaire et pose les bases d\'une saga épique.',
        '2024-03-28 00:00:00');

-- jeux de données pour la table images
INSERT INTO images (movie_id, image_url, main_image)
VALUES (1,
        'https://m.media-amazon.com/images/M/MV5BNDE3ODcxYzMtY2YzZC00NmNlLWJiNDMtZDViZWM2MzIxZDYwXkEyXkFqcGdeQXVyNjAwNDUxODI@._V1_.jpg',
        FALSE);
INSERT INTO images (movie_id, image_url, main_image)
VALUES (1, 'https://m.media-amazon.com/images/I/815qtzaP9iL._AC_UF1000,1000_QL80_.jpg', FALSE);
INSERT INTO images (movie_id, image_url, main_image)
VALUES (2,
        'https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_FMjpg_UX1000_.jpg',
        TRUE);
INSERT INTO images (movie_id, image_url, main_image)
VALUES (3, 'https://fr.web.img2.acsta.net/medias/nmedia/18/63/97/89/18949761.jpg', TRUE);
INSERT INTO images (movie_id, image_url, main_image)
VALUES (4,
        'https://m.media-amazon.com/images/M/MV5BN2EyZjM3NzUtNWUzMi00MTgxLWI0NTctMzY4M2VlOTdjZWRiXkEyXkFqcGdeQXVyNDUzOTQ5MjY@._V1_.jpg',
        TRUE);
INSERT INTO images (movie_id, image_url, main_image)
VALUES (5, 'https://www.ecranlarge.com/media/cache/1600x1200/uploads/image/001/121/7p8x4u3o3p1jzmbqny3zaloby3m-861.jpg',
        TRUE);
INSERT INTO images (movie_id, image_url, main_image)
VALUES (6, 'https://m.media-amazon.com/images/I/817sLmprCSL._AC_UF1000,1000_QL80_.jpg', TRUE);
INSERT INTO images (movie_id, image_url, main_image)
VALUES (1, 'https://cdn.theasc.com/Shawshank-Featured.jpg', FALSE);
INSERT INTO images (movie_id, image_url, main_image)
VALUES (1, 'https://resizing.flixster.com/-XZAfHZM39UwaGJIFWKAE8fS0ak=/v3/t/assets/p15987_k_h8_ad.jpg', FALSE);
INSERT INTO images (movie_id, image_url, main_image)
VALUES (2, 'https://www.artmajeur.com/medias/hd/p/a/paul-stowe/artwork/15733837_godfather-31cm-x-44cm.jpg', FALSE);
INSERT INTO images (movie_id, image_url, main_image)
VALUES (2, 'https://wordpress.wbur.org/wp-content/uploads/2020/12/1207_godfather-part-3-1-1000x669.jpg', FALSE);
INSERT INTO images (movie_id, image_url, main_image)
VALUES (3,
        'https://thumb.canalplus.pro/http/unsafe/1200x630/filters:quality(80)/img-hapi.canalplus.pro:80/ServiceImage/ImageID/52831084',
        FALSE);
INSERT INTO images (movie_id, image_url, main_image)
VALUES (3, 'https://www.critikat.com/wp-content/uploads/fly-images/37370/arton2307-1450x800-c.jpg', FALSE);
INSERT INTO images (movie_id, image_url, main_image)
VALUES (4, 'https://variety.com/wp-content/uploads/2017/01/lord-of-the-rings-return-of-the-king.jpg', FALSE);
INSERT INTO images (movie_id, image_url, main_image)
VALUES (4, 'https://assets.mubicdn.net/images/film/2112/image-w1280.jpg?1546470042', FALSE);
INSERT INTO images (movie_id, image_url, main_image)
VALUES (4, 'https://www.pluggedin.com/wp-content/uploads/2019/12/lotr-the-return-of-the-king.jpg', FALSE);


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
        SET msg = 'Vous avez sasi ' + NEW.daily_rental_price +
                  ' comme prix de location. Le prix de location doit être supérieur à 0.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg;
    END IF;
    IF NEW.purchase_price < 0 AND NEW.purchase_price <> -1 THEN
        SET msg = 'Vous avez sasi ' + NEW.purchase_price +
                  ' comme prix d\'achat. Le prix d\'achat doit être supérieur à 0.';
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

    IF OLD.id = 1 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Impossible de modifier le premier utilisateur, il est fictif.';
    ELSE
        IF NEW.email <> OLD.email AND EXISTS (SELECT id FROM users WHERE email = NEW.email) THEN
            SET msg = CONCAT('L\'email ', NEW.email, ' est déjà utilisé.');
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg;
        END IF;
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
    IF OLD.title <> NEW.title AND EXISTS (SELECT title FROM movies WHERE title = NEW.title) THEN
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
        SET msg =
                'Vous avez déjà essayé de louer ce film pour une période qui se chevauche avec une location existante.';
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
        SET msg = 'La date de retour ( ' + NEW.return_date + ' ) doit être après la date de location ( ' +
                  NEW.rental_date + ' ).';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg;
    END IF;
END;
//
DELIMITER ;


-- Trigger pour supprimer toutes les informations d'un user avant de le supprimer. Les commentaires ne seront pas supprimés, mais réassignés au premier faux utilisateur.
DELIMITER //
DROP TRIGGER IF EXISTS before_delete_user;
CREATE TRIGGER before_delete_user
    BEFORE DELETE
    ON users
    FOR EACH ROW
BEGIN
    DECLARE fake_user_id INT DEFAULT 1; -- ID de l'utilisateur fictif
    IF OLD.id = 1 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Impossible de supprimer le premier utilisateur, il est fictif.';
    ELSE
        -- Réassignation des commentaires à l'utilisateur fictif
        UPDATE comments SET user_id = fake_user_id WHERE user_id = OLD.id;

        -- Suppression des enregistrements de l'utilisateur dans d'autres tables
        DELETE FROM shopping_cart WHERE user_id = OLD.id;
        DELETE FROM rentals WHERE user_id = OLD.id;
        DELETE FROM purchases WHERE user_id = OLD.id;

        -- Pas besoin de supprimer explicitement des images ou des films, car
        -- ils ne sont pas directement liés à l'ID de l'utilisateur supprimé.
        -- Ajouter ici des commandes de suppression pour d'autres tables liées si nécessaire
    END IF;
end //
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
        SET msg = 'Vous avez saisi ' + daily_rental_price +
                  ' comme prix de location. Le prix de location doit être supérieur à 0.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg;
    END IF;
    IF purchase_price < 0 AND purchase_price <> -1 THEN
        SET msg = 'Vous avez saisi ' + purchase_price +
                  ' comme prix d\'achat. Le prix d\'achat doit être supérieur à 0.';
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
        SELECT *
        FROM images
        WHERE movie_id = id_movie
          AND main_image = TRUE
        UNION ALL
        SELECT *
        FROM images
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

DELIMITER //
DROP PROCEDURE IF EXISTS get_movies_rented_by_user;
CREATE PROCEDURE get_movies_rented_by_user(IN user_id INT)
BEGIN
    -- Vérifier si l'utilisateur a des locations
    IF (SELECT COUNT(*) FROM rentals WHERE rentals.user_id = user_id) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Aucune location trouvée pour cet utilisateur.';
    ELSE

        -- Prend tout le movie + rental
        SELECT r.id,
               m.id as movie_id,
               m.available,
               m.title,
               m.release_date,
               m.description,
               m.daily_rental_price,
               m.purchase_price,
               m.link,
               r.rental_date,
               r.return_date
        FROM rentals r
                 JOIN movies m ON r.movie_id = m.id
        WHERE r.user_id = user_id;
    END IF;
end //
DELIMITER ;

CALL get_movies_rented_by_user(2);


DELIMITER //
DROP PROCEDURE IF EXISTS get_movies_purchased_by_user;
CREATE PROCEDURE get_movies_purchased_by_user(IN user_id INT)
BEGIN
    -- Vérifier si l'utilisateur a des achats
    IF (SELECT COUNT(*) FROM purchases WHERE purchases.user_id = user_id) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Aucun achat trouvé pour cet utilisateur.';
    ELSE
        -- Prend tout le movie + purchase
        SELECT p.id,
               m.id as movie_id,
               m.available,
               m.title,
               m.release_date,
               m.description,
               m.daily_rental_price,
               m.purchase_price,
               m.link,
               p.purchase_date
        FROM purchases p
                 JOIN movies m ON p.movie_id = m.id
        WHERE p.user_id = user_id;
    END IF;
end //
DELIMITER ;

CALL get_movies_purchased_by_user(2);


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
        INSERT INTO users (last_name, first_name, email, password, role)
        VALUES (last_name, first_name, new_email, password, role);
    END IF;
END //
DELIMITER ;

-- CALL create_user('Doe', 'Jane', 'jane.doe@example.com', 'newpassword123', 'user');


-- Mettre à jour un utilisateur
DELIMITER //
CREATE PROCEDURE update_user(IN user_id INT, IN new_last_name VARCHAR(255), IN new_first_name VARCHAR(255),
                             IN new_email VARCHAR(255), new_role ENUM ('user', 'admin'))
BEGIN
    IF NOT EXISTS (SELECT id FROM users WHERE id = user_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L\'utilisateur n\'existe pas.';
    ELSE
        UPDATE users
        SET last_name  = new_last_name,
            first_name = new_first_name,
            email      = new_email,
            role       = new_role
        WHERE id = user_id;
    END IF;
END //
DELIMITER ;

-- CALL update_user(1, 'Doe', 'Jane', 'jane.doe@example.com', TRUE, 'admin');

DELIMITER //
DROP PROCEDURE IF EXISTS delete_user;
CREATE PROCEDURE delete_user(IN user_id INT)
BEGIN
    IF NOT EXISTS (SELECT id FROM users WHERE id = user_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L\'utilisateur n\'existe pas.';
    ELSE
        DELETE FROM users WHERE id = user_id;
    END IF;
END //

-- CALL delete_user(1);

-- Mettre à jour le mot de passe d'un utilisateur

DELIMITER //
DROP PROCEDURE IF EXISTS update_user_password;
CREATE PROCEDURE update_user_password(IN user_id INT, IN new_password CHAR(255))
BEGIN
    IF NOT EXISTS (SELECT id FROM users WHERE id = user_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L\'utilisateur n\'existe pas.';
    ELSE
        UPDATE users SET password = new_password WHERE id = user_id;
    END IF;
END //
DELIMITER ;


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

DELIMITER //
DROP PROCEDURE IF EXISTS delete_cart_by_id;
CREATE PROCEDURE delete_cart_by_id(IN cart_id INT)
BEGIN
    IF NOT EXISTS (SELECT id FROM shopping_cart WHERE id = cart_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Aucun article de cet id n\'existe pas dans le panier.';
    ELSE
        DELETE FROM shopping_cart WHERE id = cart_id;
    END IF;
END //


DELIMITER //
DROP PROCEDURE IF EXISTS delete_user_cart;
CREATE PROCEDURE `delete_user_cart`(IN `id_user` INT)
BEGIN
    -- Vérifier si l'utilisateur a des entrées dans le panier
    IF (SELECT COUNT(*) FROM shopping_cart WHERE user_id = id_user) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Aucun panier trouvé pour cet utilisateur.';
    ELSE
        -- Supprimer les entrées du panier pour cet utilisateur
        DELETE FROM shopping_cart WHERE user_id = id_user;
    END IF;
END //
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS get_user_cart;
CREATE PROCEDURE get_user_cart(IN id_user INT)
BEGIN
    -- Vérifier si l'utilisateur a des entrées dans le panier
    IF (SELECT COUNT(*) FROM shopping_cart WHERE user_id = id_user) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Aucun panier trouvé pour cet utilisateur.';
    ELSE
        -- Sélectionner et retourner les entrées du panier pour cet utilisateur
        SELECT * FROM shopping_cart WHERE user_id = id_user;
    END IF;
END //
DELIMITER ;


DELIMITER //
DROP PROCEDURE IF EXISTS validate_user_cart;
CREATE PROCEDURE `validate_user_cart`(IN `id_user` INT)
BEGIN
    DECLARE cartItemCount INT DEFAULT 0;
    DECLARE done INT DEFAULT FALSE;
    DECLARE cartId INT;
    DECLARE cartType ENUM ('purchase', 'rental');
    DECLARE movieId INT;
    DECLARE rentalDuration INT;
    DECLARE cur CURSOR FOR SELECT id, cart_type, movie_id, rental_duration FROM shopping_cart WHERE user_id = id_user;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    -- Compter les éléments dans le panier de l'utilisateur
    SELECT COUNT(*) INTO cartItemCount FROM shopping_cart WHERE user_id = id_user;
    IF cartItemCount = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Aucun article n\'est dans le panier.';
    END IF;

    -- Parcourir les éléments du panier
    OPEN cur;
    read_loop:
    LOOP
        FETCH cur INTO cartId, cartType, movieId, rentalDuration;
        IF done THEN
            LEAVE read_loop;
        END IF;

        -- Traitement en fonction du type d'élément dans le panier
        IF cartType = 'purchase' THEN
            INSERT INTO purchases (user_id, movie_id, purchase_date) VALUES (id_user, movieId, CURDATE());
        ELSEIF cartType = 'rental' THEN
            INSERT INTO rentals (user_id, movie_id, rental_date, return_date)
            VALUES (id_user, movieId, NOW(), DATE_ADD(NOW(), INTERVAL rentalDuration DAY));
        END IF;
    END LOOP;
    CLOSE cur;

    -- Vider le panier de l'utilisateur en appelant la procédure précédemment créée
    CALL delete_user_cart(id_user);
END //
DELIMITER ;

-- Gestion des Locations (Rentals)


-- Enregistrer une nouvelle location
DELIMITER //
DROP PROCEDURE IF EXISTS add_rental;
CREATE PROCEDURE add_rental(IN id_user INT, IN id_movie INT, IN date_rental DATETIME, IN date_return DATETIME)
BEGIN
    IF NOT EXISTS (SELECT id FROM users WHERE id = id_user) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L\'utilisateur n\'existe pas.';
    ELSEIF NOT EXISTS (SELECT id FROM movies WHERE id = id_movie) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le film n\'existe pas.';
    ELSE
        INSERT INTO rentals (user_id, movie_id, rental_date, return_date)
        VALUES (id_user, id_movie, date_rental, date_return);
    END IF;
END //
DELIMITER ;

-- CALL add_rental(1, 1, '2024-03-01 00:00:00', '2024-03-06 00:00:00');


-- Gestion des Achats (Purchases)


-- Enregistrer un nouvel achat
DELIMITER //
DROP PROCEDURE IF EXISTS add_purchase;
CREATE PROCEDURE add_purchase(IN id_user INT, IN id_movie INT, IN date_purchase DATE)
BEGIN
    IF NOT EXISTS (SELECT id FROM users WHERE id = id_user) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L\'utilisateur n\'existe pas.';
    ELSEIF NOT EXISTS (SELECT id FROM movies WHERE id = id_movie) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le film n\'existe pas.';
    ELSE
        INSERT INTO purchases (user_id, movie_id, purchase_date) VALUES (id_user, id_movie, date_purchase);
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


-- Récupère les commentaires d'un film
DELIMITER //
DROP PROCEDURE IF EXISTS get_comments_by_movie_id;
CREATE PROCEDURE get_comments_by_movie_id(IN id_movie INT)
BEGIN
    IF NOT EXISTS (SELECT id FROM movies WHERE id = id_movie) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le film n\'existe pas.';
    ELSE
        SELECT * FROM comments WHERE movie_id = id_movie;
    END IF;
END //
DELIMITER ;

-- CALL get_comments_by_movie_id(1);

DELIMITER //
DROP PROCEDURE IF EXISTS get_comment_by_id;
CREATE PROCEDURE get_comment_by_id(IN id_comment INT)
BEGIN
    IF NOT EXISTS (SELECT id FROM comments WHERE id = id_comment) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Aucun commentaire trouvé';
    ELSE
        SELECT * FROM comments WHERE id = id_comment;
    END IF;
END //

# CALL get_comment_by_comment_id(1);

-- modifier un commentaire
DELIMITER //
DROP PROCEDURE IF EXISTS update_comment_by_id;
CREATE PROCEDURE update_comment_by_id(IN comment_id INT, IN text_comment TEXT)
BEGIN
    IF NOT EXISTS (SELECT id FROM comments WHERE id = comment_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le commentaire n\'existe pas.';
    ELSE
        UPDATE comments SET comment_text = text_comment WHERE id = comment_id;
    END IF;
END //

-- Ajouter une image à un commentaire
DELIMITER //
DROP PROCEDURE IF EXISTS add_image_to_comment;
CREATE PROCEDURE add_image_to_comment(IN comment_id INT, IN image_base64 TEXT)
BEGIN
    IF NOT EXISTS (SELECT id FROM comments WHERE id = comment_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le commentaire n\'existe pas.';
    ELSE
        UPDATE comments SET comments.image_text = image_base64 WHERE id = comment_id;
    END IF;
END //
DELIMITER ;

# CALL add_image_to_comment(1, 'base64imagestring');

DELIMITER //
DROP PROCEDURE IF EXISTS delete_image_from_comment;
CREATE PROCEDURE delete_image_from_comment(IN id_comment INT)
BEGIN
    IF NOT EXISTS (SELECT id FROM comments WHERE id = id_comment) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le commentaire n\'existe pas.';
    ELSEIF NOT EXISTS (SELECT image_text FROM comments WHERE id = id_comment AND image_text IS NOT NULL) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le commentaire n\'a pas d\'image.';
    ELSE
        UPDATE comments SET image_text = NULL WHERE id = id_comment;
    END IF;
END //
DELIMITER ;

# CALL delete_image_from_comment(1);