const jwt = require('jsonwebtoken');
require('dotenv').config();

const SECRET_KEY = process.env.SECRET_KEY || "votreCleSecrete";

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

module.exports = verifyJWTAndRole;
