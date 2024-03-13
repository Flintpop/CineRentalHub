const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');
const cors = require('cors');

const app = express();
const PORT = 3000;
const API_SERVICE_URL = "http://movie-api:8080";

app.use(cors()); // Active CORS pour toutes les routes

// Middleware pour parser le corps JSON des requêtes
app.use(express.json());

// Configuration du middleware de proxy
const proxyOptions = {
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

// Utilisation du middleware de proxy pour différents chemins
app.use('/movies', createProxyMiddleware(proxyOptions));
app.use('/movies/image', createProxyMiddleware(proxyOptions));

app.listen(PORT, () => console.log(`Serveur en écoute sur le port ${PORT}`));
