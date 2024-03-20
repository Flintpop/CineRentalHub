const { createProxyMiddleware } = require('http-proxy-middleware');

// const MOVIE_API_SERVICE_URL = "http://localhost:8081";
// const USER_API_SERVICE_URL = "http://localhost:8082";
// const COMMENT_API_SERVICE_URL = "http://localhost:8083";
// const CART_API_SERVICE_URL = "http://localhost:8084";

const MOVIE_API_SERVICE_URL = "http://movie-api:8080";
const USER_API_SERVICE_URL = "http://user-api:8080";
const COMMENT_API_SERVICE_URL = "http://comment-api:8080";
const CART_API_SERVICE_URL = "http://cart-api:8080";
const proxyOptions = target => ({
    target: target,
    changeOrigin: true,
    onProxyReq: (proxyReq, req, res) => {
        if (req.body && req.headers['content-type']) {
            let bodyData = JSON.stringify(req.body);
            proxyReq.setHeader('Content-Length', Buffer.byteLength(bodyData));
            proxyReq.write(bodyData);
        }
    },
    onError: (err, req, res) => {
        console.error('Une erreur s\'est produite lors du proxy de la requête:', err);
        res.status(500).json({error: 'Une erreur s\'est produite lors du proxy de la requête.'});
    }
});

module.exports = function(app) {
    app.use('/movies', createProxyMiddleware(proxyOptions(MOVIE_API_SERVICE_URL)));
    app.use('/user', createProxyMiddleware(proxyOptions(USER_API_SERVICE_URL)));
    app.use('/users', createProxyMiddleware(proxyOptions(USER_API_SERVICE_URL)));
    app.use('/comments', createProxyMiddleware(proxyOptions(COMMENT_API_SERVICE_URL)));
    app.use('/cart', createProxyMiddleware(proxyOptions(CART_API_SERVICE_URL)));
};
