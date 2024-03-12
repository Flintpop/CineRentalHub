-- Création de la base de données
CREATE DATABASE IF NOT EXISTS cine_rental_hub;
USE cine_rental_hub;

-- Création de la table Films
CREATE TABLE IF NOT EXISTS movies
(
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    available          BOOLEAN default TRUE NOT NULL,
    title              VARCHAR(255)      NOT NULL,
    release_date       DATE,                       -- Si null, mettre "prochainement"
    daily_rental_price DECIMAL(10, 2)    NOT NULL, -- Si -1 et si on doit l'implementer, non louable
    purchase_price     DECIMAL(10, 2)    NOT NULL, -- Pareil que location, -1 == pas vendable. Attention si pas vendable ni louable pas afficher ou bug
    description        TEXT              NOT NULL,
    link               VARCHAR(2000)     NOT NULL -- chatGPT a voir taille max lien attention mettre verif dans le front
);

CREATE TABLE IF NOT EXISTS users
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    last_name  VARCHAR(255)           NOT NULL,
    first_name VARCHAR(255)           NOT NULL,
    email      VARCHAR(255)           NOT NULL UNIQUE,
    password   CHAR(255)              NOT NULL, -- sha512, est-ce que c'est la bonne taille ?
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
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (movie_id) REFERENCES movies(id)
);

-- Création de la table Utilisateurs
CREATE TABLE IF NOT EXISTS users
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    last_name  VARCHAR(255)           NOT NULL,
    first_name VARCHAR(255)           NOT NULL,
    email      VARCHAR(255)           NOT NULL UNIQUE,
    state      ENUM ('active', 'inactive') NOT NULL,
    password   CHAR(255)              NOT NULL, -- sha512, est-ce que c'est la bonne taille ?
    role       ENUM ('user', 'admin') NOT NULL  -- connexion avec email + mdp
);

-- Création de la table Locations
CREATE TABLE IF NOT EXISTS rentals
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id     INT      NOT NULL,
    movie_id    INT      NOT NULL,
    rental_date DATETIME NOT NULL,
    return_date DATETIME NOT NULL, -- avec les dates on peut calculer le nombre de jours de location pour connaitre le prix total et on suppose que le coût d'un film location ne change pas
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (movie_id) REFERENCES movies(id)
);

-- Table des achats
CREATE TABLE purchases
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    user_id       INT  NOT NULL,
    movie_id      INT  NOT NULL,
    purchase_date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (movie_id) REFERENCES movies(id)
);


-- Création de la table Commentaires
CREATE TABLE IF NOT EXISTS comments
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    movie_id     INT      NOT NULL,
    user_id      INT      NOT NULL,
    comment_text TEXT     NOT NULL,
    comment_date DATETIME NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Création de la table Consultations -- à mettre en mongodb
CREATE TABLE IF NOT EXISTS views
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    user_id   INT      NOT NULL,
    movie_id  INT      NOT NULL,
    view_date DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (movie_id) REFERENCES movies(id)
);

-- Création de la table Images
CREATE TABLE IF NOT EXISTS images
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    movie_id   INT                NOT NULL,
    image_url  VARCHAR(2000)      NOT NULL, -- 2000 car url
    main_image BOOLEAN DEFAULT FALSE NOT NULL, -- Image d'affiche principale, sert pour afficher dans la liste des movies. Faire un trigger car une seule image ne peut etre autorisee en main_image pour un film
    FOREIGN KEY (movie_id) REFERENCES movies(id)
);