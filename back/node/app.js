const express = require('express');
const cors = require('cors');
const { startChatServer } = require('./features/chatServer'); // Ajustez le chemin selon votre structure de projet
const verifyJWTAndRole = require('./middleware/authMiddleware');
const authRoutes = require('./routes/sign_in');
const setupProxies = require('./config/proxyConfig'); // Assurez-vous que le chemin d'accès est correct

const app = express();
const PORT = 3000;
const CHAT_PORT = 3001; // Port pour le serveur de chat Socket.IO;

app.use(cors());
app.use(express.json());

app.use(verifyJWTAndRole);
app.use(authRoutes);

setupProxies(app); // Configurez les proxys

app.listen(PORT, () => {
    console.log(`Serveur principal en écoute sur le port ${PORT}`);
    startChatServer(CHAT_PORT); // Démarrer le serveur de chat après que le serveur principal est lancé
});
