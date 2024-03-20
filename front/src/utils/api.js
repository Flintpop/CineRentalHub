import axios from 'axios';

export default {
    install(app) {
        // Variable globale pour l'URL de base de l'API
        app.config.globalProperties.$baseApiUrl = 'http://localhost:3000';

        // Configurer Axios pour ajouter le token dans chaque requête
        app.config.globalProperties.$configureAxios = function () {
            axios.interceptors.request.use(function (config) {
                // Ici, récupérez votre token depuis là où il est stocké
                // Cela pourrait être localStorage, sessionStorage, ou un état global (vuex par exemple)
                const token = localStorage.getItem('token');

                if (token) {
                    config.headers.Authorization = `Bearer ${token}`;
                }

                return config;
            }, function (error) {
                // Faire quelque chose avec une erreur de requête
                return Promise.reject(error);
            });
        };

        // Fonction globale pour gérer les erreurs
        app.config.globalProperties.$handleError = function (error, requestBody = null) {
            if (error.response && error.response.status !== 500) {

                // Si la réponse existe et que le code d'état est différent de 500, affiche une alerte avec le message d'erreur
                if (error.response.data) {
                    alert(error.response.data.error)
                } else {
                    if (requestBody) {
                        // Si requestBody est fourni, affiche le body de la requête
                        console.log("Body de la requête envoyée:", requestBody);
                    }
                    alert(`Erreur: ${error.response.status} ${error.response.data}`);
                }
            } else {
                if (requestBody) {
                    // Si requestBody est fourni, affiche le body de la requête
                    console.log("Body de la requête envoyée:", requestBody);
                }
                if (error.request) {
                    // Si la requête a été faite mais qu'aucune réponse n'a été reçue ou si le statut est 500
                    console.log("Requête envoyée:", error.request);
                    if (error.response) {
                        // Si une réponse existe, affiche seulement le body de la réponse
                        console.log("Body de la réponse:", error.response.data);
                    }
                } else {
                    // Pour les autres types d'erreurs, affiche l'erreur elle-même
                    console.log("Erreur:", error.message);
                }
            }

            // Afficher toujours l'erreur complète en console pour le débogage
            console.log("Détails de l'erreur:", error);
        };

        // Fonction globale pour effectuer une requête GET
        app.config.globalProperties.$getMovies = async function () {
            try {
                const response = await axios.get(`${this.$baseApiUrl}/movies`);
                // Axios rejette automatiquement la promesse si le statut de la réponse est en dehors de la plage 2xx
                return response.data; // Avec Axios, les données sont directement accessibles via `response.data`
            } catch (error) {
                // Axios encapsule l'erreur d'origine dans `error.response`
                // Vous pouvez donc accéder au code d'état HTTP via `error.response.status` et au message via `error.response.data`
                this.$handleError(error);
            }
        };

        app.config.globalProperties.$uploadImage = async function (commentId, imageBase64) {
            const payload = {
                comment_id: 1,
                image_base64: "askjsfk",
            };

            try {
                const response = await axios.post(`${this.$baseApiUrl}/comments/image`, payload, {
                    headers: {
                        "Content-Type": "application/json",
                    },
                });
                alert("Image envoyée avec succès !");
                return response.data;
            } catch (error) {
                this.$handleError(error, payload);
            }
        };

        app.config.globalProperties.$deleteImage = async function (imageId) {
            try {
                const response = await axios.delete(`${this.$baseApiUrl}/comments/image/${imageId}`);
                alert("Image supprimée avec succès !");
                return response.data;
            } catch (error) {
                this.$handleError(error);
            }
        }

        app.config.globalProperties.$getCommentsByMovieId = async function (movieId) {
            try {
                const response = await axios.get(`${this.$baseApiUrl}/comments/${movieId}`);
                const userResponse = await axios.get(`${this.$baseApiUrl}/users`);
                const comments = response.data;
                const users = userResponse.data;
                const commentsWithUsers = comments.map((comment) => {
                    const user = users.find((user) => user.id === comment.user_id);
                    return {
                        ...comment,
                        user,
                    };
                });
                console.log(commentsWithUsers)
                return commentsWithUsers;
            } catch (error) {
                this.$handleError(error);
            }
        }
    }
};
