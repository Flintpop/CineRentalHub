const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');
const jwt = require('jsonwebtoken');
const mysql = require('mysql');
const crypto = require('crypto');
const cors = require('cors');

const app = express();
const PORT = 3000;

const MOVIE_API_SERVICE_URL = "http://localhost:8081";
const USER_API_SERVICE_URL = "http://localhost:8082";
const COMMENT_API_SERVICE_URL = "http://localhost:8083";
const CART_API_SERVICE_URL = "http://localhost:8084";
const SECRET_KEY = "votreCleSecrete";

app.use(cors());
app.use(express.json());

const db = mysql.createConnection({
    host: 'localhost',
    port: '3306',
    user: 'root',
    password: 'root',
    database: 'cine_rental_hub'
});

db.connect((err) => {
    if (err) {
        throw err;
    }
    console.log('Connecté à la base de données MySQL');
});

const verifyJWTAndRole = (req, res, next) => {
    // Routes autorisées sans token. [^/]+ signifie "n'importe quel caractère sauf /"
    const openRoutes = [
        { path: '/movies', methods: ['GET'] },
        { path: '/movies/[^/]+/?', methods: ['GET'] },
        { path: '/movies/images/[^/]+/?', methods: ['GET'] },
        { path: '/movies/main_image/[^/]+/?', methods: ['GET'] },
        { path: '/signin', methods: ['POST'] },
    ];

    const reqPath = req.originalUrl.split('?')[0];
    const reqMethod = req.method;

    const isRouteOpen = openRoutes.some(route => {
        // correspondre exactement à la route depuis le début (^) jusqu'à la fin ($) de la chaîne.
        const matchPath = reqPath.match(new RegExp("^" + route.path + "$"));
        const methodAllowed = route.methods.includes(reqMethod);
        return matchPath && methodAllowed;
    });

    // Si la route est dans la liste des routes ouvertes ET que la méthode est GET, on passe au middleware suivant sans vérification du JWT
    if (isRouteOpen) {
        // Next c'est pour passer au middleware suivant, sinon la requête serait bloquée ici (si pas de token ou pas le bon)
        return next();
    }

    const token = req.headers['authorization']?.split(' ')[1];
    if (!token) {
        return res.status(403).json({ error: "Accès refusé, token non fourni" });
    }

    jwt.verify(token, SECRET_KEY, (err, decoded) => {
        if (err) {
            return res.status(401).json({ error: "Token invalide" });
        }
        req.user = decoded;
        next();
    });
};

app.use(verifyJWTAndRole);

app.post('/sign_in', (req, res) => {
    const { email, password } = req.body;

    if (!email || !password) {
        return res.status(400).send('Nom d’utilisateur et mot de passe sont requis');
    }

    db.query('SELECT id, role, email, password FROM users WHERE email = ?', [email], async (err, results) => {
        if (err) {
            return res.status(500).json({ error: "Erreur interne" });
        }

        if (results.length === 0) {
            return res.status(404).json({ error: "Utilisateur non trouvé" });
        }

        const user = results[0];

        const hash = crypto.createHash('sha512');
        hash.update(password);
        const hashedPassword = hash.digest('hex');

        if (hashedPassword === user.password) {
            const token = jwt.sign({ userId: user.id, role: user.role }, SECRET_KEY, { expiresIn: '2h' });
            res.json({ token });
        } else {
            res.status(401).json({ error: "Mot de passe incorrect" });
        }
    });
});

const proxyOptions = (target) => ({
    target: target,
    changeOrigin: true,
    /*
        pathRewrite: {
            [`^/movies`]: '',
        },
    */
    onProxyReq: (proxyReq, req, res) => {
        if (req.body && req.headers['content-type']) {
            let bodyData = JSON.stringify(req.body);
            proxyReq.setHeader('Content-Length', Buffer.byteLength(bodyData));
            proxyReq.write(bodyData);
        }
    }
});

app.use('/movies', createProxyMiddleware(proxyOptions(MOVIE_API_SERVICE_URL)));
app.use('/users', createProxyMiddleware(proxyOptions(USER_API_SERVICE_URL)));
app.use('/comments', createProxyMiddleware(proxyOptions(COMMENT_API_SERVICE_URL)));
app.use('/cart', createProxyMiddleware(proxyOptions(CART_API_SERVICE_URL)));

app.listen(PORT, () => console.log(`Serveur en écoute sur le port ${PORT}`));
