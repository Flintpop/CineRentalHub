const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');
const cors = require('cors');

const app = express();
const PORT = 3000;
const API_SERVICE_URL = "http://movie-api:8080";
const USER_API_SERVICE_URL = "http://user-api:8080"; // URL de la nouvelle API utilisateur

app.use(cors()); // Active CORS pour toutes les routes
app.use(express.json()); // Middleware pour parser le corps JSON des requêtes

// Configuration du middleware de proxy pour l'API de films
const movieProxyOptions = {
    target: API_SERVICE_URL,
    changeOrigin: true,
    pathRewrite: {
        [`^/movies`]: '/movies',
    },
    onProxyReq: (proxyReq, req, res) => {
        if (req.body && req.headers['content-type']) {
            let bodyData = JSON.stringify(req.body);
            proxyReq.setHeader('Content-Length', Buffer.byteLength(bodyData));
            proxyReq.write(bodyData);
        }
    }
};

// Configuration du middleware de proxy pour l'API utilisateur
const userProxyOptions = {
    target: USER_API_SERVICE_URL,
    changeOrigin: true,
    onProxyReq: (proxyReq, req, res) => {
        if (req.body && req.headers['content-type']) {
            let bodyData = JSON.stringify(req.body);
            proxyReq.setHeader('Content-Length', Buffer.byteLength(bodyData));
            proxyReq.write(bodyData);
        }
    }
};

// Utilisation du middleware de proxy pour l'API de films
app.use('/movies', createProxyMiddleware(movieProxyOptions));
app.use('/movies/image', createProxyMiddleware(movieProxyOptions));
app.use('/movies/desactivate', createProxyMiddleware(movieProxyOptions));
app.use('/movies/activate', createProxyMiddleware(movieProxyOptions));
app.use('/movies/main_image', createProxyMiddleware(movieProxyOptions));

// Utilisation du middleware de proxy pour l'API utilisateur
app.use('/user', createProxyMiddleware(userProxyOptions));
app.use('/users', createProxyMiddleware(userProxyOptions));

app.listen(PORT, () => console.log(`Serveur en écoute sur le port ${PORT}`));
