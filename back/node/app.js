const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');

const app = express();
const PORT = 3000;
const API_SERVICE_URL = "http://movie-api"; // Remplacez par l'URL réelle de l'API

// Middleware pour parser le corps JSON des requêtes
app.use(express.json());

// Fonction de middleware pour rediriger les requêtes vers l'API
const proxy = createProxyMiddleware({
    target: API_SERVICE_URL,
    changeOrigin: true,
    pathRewrite: {
        [`^/movies`]: '/movies', // Conserve le préfixe /movies dans l'URL de destination
    },
    onProxyReq: (proxyReq, req, res) => {
        if (req.body && req.headers['content-type']) {
            let bodyData = JSON.stringify(req.body);
            proxyReq.setHeader('Content-Length', Buffer.byteLength(bodyData));
            proxyReq.write(bodyData);
        }
    }
});

// Utilisation du middleware de proxy pour différents chemins
app.use('/movies', proxy); // Gère à la fois /movies et /movies/{movieId}
app.use('/movies/image', proxy); // Gère /movies/image et /movies/image/{imageId}

app.listen(PORT, () => console.log(`Serveur en écoute sur le port ${PORT}`));
