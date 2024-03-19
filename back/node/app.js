const express = require('express');
const {createProxyMiddleware} = require('http-proxy-middleware');
const jwt = require('jsonwebtoken');
const mysql = require('mysql');
const crypto = require('crypto');
const cors = require('cors');

const app = express();
const PORT = 3000;

const MOVIE_API_SERVICE_URL = "http://movie-api:8080";
const USER_API_SERVICE_URL = "http://user-api:8080";
const COMMENT_API_SERVICE_URL = "http://comment-api:8080";
const CART_API_SERVICE_URL = "http://cart-api:8080";

// const MOVIE_API_SERVICE_URL = "http://localhost:8081";
// const USER_API_SERVICE_URL = "http://localhost:8082";
// const COMMENT_API_SERVICE_URL = "http://localhost:8083";
// const CART_API_SERVICE_URL = "http://localhost:8084";
const SECRET_KEY = "votreCleSecrete";
const MAX_TENTATIVES = 24;
let tentativeDeConnexion = 0;

app.use(cors());
app.use(express.json());

// const db = mysql.createConnection({
//     host: 'mariadb',
//     port: '3306',
//     user: 'root',
//     password: 'root',
//     database: 'cine_rental_hub'
// });

const dbConfig = {
    host: 'mariadb',
    port: '3306',
    user: 'root',
    password: 'root',
    database: 'cine_rental_hub'
}

const connectWithRetry = () => {
    console.log('Tentative de connexion à la base de données MariaDB...');
    const db = mysql.createConnection(dbConfig);

    db.connect(err => {
        if (err) {
            console.error('Erreur de connexion à la base de données:', err);
            tentativeDeConnexion += 1;

            if (tentativeDeConnexion < MAX_TENTATIVES) {
                console.log(`Ré-essai de connexion dans 5 secondes... (Tentative ${tentativeDeConnexion}/${MAX_TENTATIVES})`);
                setTimeout(connectWithRetry, 5000); // Attendre 10 secondes avant de réessayer
            } else {
                console.error('Nombre maximal de tentatives de connexion atteint. Veuillez vérifier votre base de données.');
            }
        } else {
            console.log('Connecté à la base de données MySQL avec succès.');
            // Ici, vous pouvez initialiser d'autres parties de votre application qui dépendent de la connexion à la base de données.
        }
    });

    // Gestion de la déconnexion inopinée
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

db = connectWithRetry();

// const db = mysql.createConnection({
//     host: 'mariadb',
//     port: '3306',
//     user: 'root',
//     password: 'root',
//     database: 'cine_rental_hub'
// });
//
// db.connect((err) => {
//     if (err) {
//         throw err;
//     }
//     console.log('Connecté à la base de données MySQL');
// });
//
const verifyJWTAndRole = (req, res, next) => {
    const openRoutes = [
        {path: '/movies', methods: ['GET']},
        {path: '/movies/[^/]+/?', methods: ['GET']},
        {path: '/movies/images/[^/]+/?', methods: ['GET']},
        {path: '/movies/main_image/[^/]+/?', methods: ['GET']},
        {path: '/sign_in', methods: ['POST']},
    ];

    const protectedRoutes = [
        {path: '/movies', methods: ['POST'], roles: ['admin']},
        {path: '/movies/[^/]+/?', methods: ['PUT'], roles: ['admin']},
        {path: '/movies/deactivated/[^/]+/?', methods: ['PATCH'], roles: ['admin']},
        {path: '/movies/activated/[^/]+/?', methods: ['PATCH'], roles: ['admin']},
        {path: '/user/[^/]+/?', methods: ['PUT', 'DELETE'], roles: ['admin']},
        {path: '/comments/[^/]+/?', methods: ['POST', 'GET'], roles: ['user'], selfOnly: false},
        // Pb d'id, je ne sais pas comment récupérer l'id du commentaire à modifier
        {path: '/comments/manage/[^/]+/?', methods: ['PUT', 'DELETE'], roles: ['user'], selfOnly: false},
    ];

    const reqPath = req.originalUrl.split('?')[0];
    const reqMethod = req.method;

    const isRouteOpen = openRoutes.some(route => {
        const matchPath = reqPath.match(new RegExp("^" + route.path + "$"));
        return matchPath && route.methods.includes(reqMethod);
    });

    if (isRouteOpen) {
        return next();
    }

    const token = req.headers['authorization']?.split(' ')[1];
    if (!token) {
        return res.status(403).json({error: "Accès refusé, token non fourni"});
    }

    jwt.verify(token, SECRET_KEY, (err, decoded) => {
        if (err) {
            return res.status(401).json({error: "Token invalide"});
        }

        req.user = decoded;

        const isProtectedRoute = protectedRoutes.some(route => {
            const matchPath = reqPath.match(new RegExp("^" + route.path + "$"));
            const methodAllowed = route.methods.includes(reqMethod);
            const roleAllowed = route.roles.includes(decoded.role);
            const isSelfOnlyRoute = route.selfOnly;
            let isUserAllowed = true;

            if (isSelfOnlyRoute) {
                const urlParts = reqPath.match(/[^/]+/g); // Extraire les segments de l'URL
                const userIdFromURL = urlParts[urlParts.length - 1]; // Supposer que l'ID utilisateur est le dernier segment
                isUserAllowed = decoded.userId === userIdFromURL; // Vérifier si l'ID utilisateur du token correspond à l'ID dans l'URL
            }

            return matchPath && methodAllowed && roleAllowed && isUserAllowed;
        });

        if (!isProtectedRoute) {
            return res.status(403).json({error: "Accès refusé, rôle non autorisé, route invalide, ou action non autorisée sur la ressource"});
        }

        next();
    });
};

app.use(verifyJWTAndRole);

app.post('/sign_in', (req, res) => {
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
    },
    onError: (err, req, res) => {
        console.error('Une erreur s\'est produite lors du proxy de la requête:', err);
        res.status(500).json({error: 'Une erreur s\'est produite lors du proxy de la requête.'});
    }
});

app.use('/movies', createProxyMiddleware(proxyOptions(MOVIE_API_SERVICE_URL)));
app.use('/user', createProxyMiddleware(proxyOptions(USER_API_SERVICE_URL)));
app.use('/users', createProxyMiddleware(proxyOptions(USER_API_SERVICE_URL)));
app.use('/comments', createProxyMiddleware(proxyOptions(COMMENT_API_SERVICE_URL)));
app.use('/cart', createProxyMiddleware(proxyOptions(CART_API_SERVICE_URL)));


app.listen(PORT, () => console.log(`Serveur en écoute sur le port ${PORT}`));

const chatApp = express();
const server = http.createServer(chatApp); // Créez le serveur HTTP à partir de l'application Express
const io = socketIo(server, {
    cors: {
        origin: "*", // Autorisez les requêtes de toutes les origines
        methods: ["GET", "POST"]
    }
}); // Attachez Socket.IO au serveur HTTP

chatApp.use(express.json());
chatApp.use(cors());
// Serveur de fichiers statiques pour le front-end
// Remplacez 'path_to_your_front_end_dist_folder' par le chemin réel vers votre dossier 'dist' du front-end
chatApp.use(express.static(path.join(__dirname, '../front/dist')));

// Gestion des connexions Socket.IO
io.on('connection', (socket) => {
    console.log('Un utilisateur s\'est connecté');

    socket.on('chat message', (msg) => {
        // Traitez et enregistrez le message dans votre base de données
        // Puis émettez-le vers tous les clients connectés
        io.emit('chat message', msg);
    });
    socket.on('docEditor message', (msg) => {
        // Traitez et enregistrez le message dans votre base de données
        // Puis émettez-le vers tous les clients connectés sauf
        io.emit('docEditor message', msg);
    });
    socket.on('docEditor message canEdit', () => {


        io.emit('docEditor message canEdit');
    });
    socket.on('docEditor message BlockEdit', () => {
// Traitez et enregistrez le message dans votre base de données
        //Bloquer l'édition du document sauf pour la personne qui emet le message
        io.emit('docEditor message BlockEdit');
    });

    socket.on('disconnect', () => {
        console.log('Un utilisateur s\'est déconnecté');
    });
});

// Remplacez app.listen par server.listen pour intégrer Socket.IO
server.listen(port_http, () => {
    console.log(`Server is running on port ${port_http}`);
});
