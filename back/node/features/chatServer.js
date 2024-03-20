const { createServer } = require("http");
const socketIo = require("socket.io");
const express = require('express');
const path = require('path');

function startChatServer(port) {
    const chatApp = express();
    const server = createServer(chatApp);
    const io = socketIo(server, {
        cors: {
            origin: "*", // Autorisez les requêtes de toutes les origines
            methods: ["GET", "POST"]
        }
    });

    chatApp.use(express.json());
    chatApp.use(express.static(path.join(__dirname, '../front/dist'))); // Ajustez selon votre structure de dossier

    io.on('connection', (socket) => {
        console.log('Un utilisateur s\'est connecté');

        socket.on('chat message', (msg) => {
            io.emit('chat message', msg);
        });
        socket.on('docEditor message', (msg) => {
            io.emit('docEditor message', msg);
        });
        socket.on('docEditor message canEdit', () => {
            io.emit('docEditor message canEdit');
        });
        socket.on('docEditor message BlockEdit', () => {
            io.emit('docEditor message BlockEdit');
        });

        socket.on('disconnect', () => {
            console.log('Un utilisateur s\'est déconnecté');
        });
    });

    server.listen(port, () => {
        console.log(`Server is running on port ${port}`);
    });
}

module.exports = { startChatServer };
