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
        {path: '/user', methods: ['POST']},
        {path: '/comments/[^/]+/?', methods: ['GET']},
        {path: '/users', methods: ['GET']},
    ];

    const protectedRoutes = [
        {path: '/movies', methods: ['POST'], roles: ['admin']},
        {path: '/movies/[^/]+/?', methods: ['PUT'], roles: ['admin']},
        {path: '/movies/deactivated/[^/]+/?', methods: ['PATCH'], roles: ['admin']},
        {path: '/movies/activated/[^/]+/?', methods: ['PATCH'], roles: ['admin']},
        {path: '/movies/images/[^/]+/?', methods: ['POST', 'DELETE'], roles: ['admin']},
        {path: '/movies/main_image/[^/]+/?', methods: ['PUT'], roles: ['admin']},
        {path: '/movies/rentals/[^/]+?', methods: ['GET'], roles: ['user', 'admin'], selfOnly: false},
        {path: '/movies/purchases/[^/]+?', methods: ['GET'], roles: ['user'], selfOnly: false},

        {path: '/movies/rentals/[^/]+?', methods: ['GET'], roles: ['admin']},
        {path: '/movies/purchases/[^/]+?', methods: ['GET'], roles: ['admin']},

        {path: '/user/[^/]+/?', methods: ['GET', 'PUT', 'DELETE'], roles: ['admin', 'user']},
        {path: '/user/password/[^/]+/?', methods: ['PUT'], roles: ['user'], selfOnly: false},


        // Pb d'id, je ne sais pas comment récupérer l'id du commentaire à modifier
        {path: '/comments/[^/]+/?', methods: ['POST'], roles: ['user', 'admin'], selfOnly: false},
        {path: '/comments/manage/[^/]+/?', methods: ['PUT', 'DELETE', 'GET'], roles: ['user', 'admin'], selfOnly: false},
        {path: '/comments/image', methods: ['POST'], roles: ['user', 'admin'], selfOnly: false},
        {path: '/comments/image/[^/]+/?', methods: ['DELETE'], roles: ['user'], selfOnly: false},

        {path: '/cart', methods: ['POST'], roles: ['user'], selfOnly: false},
        // TODO: Implementer verification ci-dessous que l'id de l'url par rapport au token, mais que pour le get
        {path: '/cart/[^/]+/?', methods: ['GET', 'DELETE'], roles: ['user'], selfOnly: false},
        {path: '/cart/delete/[^/]+/?', methods: ['DELETE'], roles: ['user'], selfOnly: false}, // TODO: Implementer verification que l'id de l'url
        {path: '/cart/validate/[^/]+/?', methods: ['PATCH'], roles: ['user'], selfOnly: false}, // TODO: Implementer verification que l'id de l'url

        {path: '/stats', methods: ['GET', 'POST'], roles: ['user'], selfOnly: false},
        {path: '/stats/[^/]+?', methods: ['DELETE'], roles: ['user'], selfOnly: false},
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

    let reasonOfError = ""

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

            let isValid = matchPath && methodAllowed && roleAllowed && isUserAllowed;
            if (!isValid) {
                reasonOfError = {
                    message: "Vous n'avez pas les droits nécessaires pour accéder à cette ressource. Voici ce que vous avez envoyé :",
                    details: {
                        Path: reqPath,
                        Method: reqMethod,
                        Role: decoded.role,
                        UserID: decoded.userId,
                    }
                };
            }

            return isValid;
        });

        if (!isProtectedRoute) {
            return res.status(403).json({error: "Accès refusé", details: reasonOfError});
        }

        next();
    });
};

module.exports = verifyJWTAndRole;
