require('dotenv').config();
const mysql = require('mysql');

const dbConfig = {
    host: process.env.DB_HOST,
    port: process.env.DB_PORT,
    user: process.env.DB_USER,
    password: process.env.DB_PASSWORD,
    database: process.env.DB_DATABASE
};

let tentativeDeConnexion = 0;
const MAX_TENTATIVES = parseInt(process.env.MAX_TENTATIVES, 10);

const connectWithRetry = () => {
    console.log('Tentative de connexion à la base de données...');
    const db = mysql.createConnection(dbConfig);

    db.connect(err => {
        if (err) {
            console.error('Erreur de connexion à la base de données:', err);
            tentativeDeConnexion += 1;

            if (tentativeDeConnexion < MAX_TENTATIVES) {
                console.log(`Ré-essai de connexion dans 5 secondes... (Tentative ${tentativeDeConnexion}/${MAX_TENTATIVES})`);
                setTimeout(connectWithRetry, 5000);
            } else {
                console.error('Nombre maximal de tentatives de connexion atteint. Veuillez vérifier votre base de données.');
            }
        } else {
            console.log('Connecté à la base de données MariaDB avec succès.');
        }
    });

    db.on('error', (err) => {
        console.error('Erreur de base de données', err);
        if (err.code === 'PROTOCOL_CONNECTION_LOST') {
            console.log('Connexion à la base de données perdue. Tentative de reconnexion...');
            connectWithRetry();
        } else {
            throw err;
        }
    });

    return db;
};

module.exports = connectWithRetry();
