const express = require('express');
const jwt = require('jsonwebtoken');
const crypto = require('crypto');
const db = require('../database'); // Ajustez le chemin en fonction de votre structure de projet

const router = express.Router();

const SECRET_KEY = process.env.SECRET_KEY || "votreCleSecrete"; // Utilisez des variables d'environnement

router.post('/sign_in', (req, res) => {
    const {email, password} = req.body;

    if (!email || !password) {
        return res.status(400).send('Nom d’utilisateur et mot de passe sont requis');
    }

    db.query('SELECT id, role, email, password FROM users WHERE email = ?', [email], async (err, results) => {
        if (err) {
            return res.status(500).json({error: "Erreur interne"});
        }

        if (results.length === 0) {
            return res.status(404).json({error: "Utilisateur non trouvé"});
        }

        const user = results[0];

        const hash = crypto.createHash('sha512');
        hash.update(password);
        const hashedPassword = hash.digest('hex');

        if (hashedPassword === user.password) {
            const token = jwt.sign({userId: user.id, role: user.role}, SECRET_KEY, {expiresIn: '2h'});
            res.json({token});
        } else {
            res.status(401).json({error: "Mot de passe incorrect"});
        }
    });
});

module.exports = router;
