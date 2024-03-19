const express = require('express');
const path = require('path');
const port_http = 3001
const cors = require('cors');
const http = require('http'); // Ajoutez http
const socketIo = require('socket.io'); // Ajoutez socket.io

const app = express();
const server = http.createServer(app); // Créez le serveur HTTP à partir de l'application Express
const io = socketIo(server, {
  cors: {
    origin: "*", // Autorisez les requêtes de toutes les origines
    methods: ["GET", "POST"]
  }
}); // Attachez Socket.IO au serveur HTTP

app.use(express.json());
app.use(cors());
// Serveur de fichiers statiques pour le front-end
// Remplacez 'path_to_your_front_end_dist_folder' par le chemin réel vers votre dossier 'dist' du front-end
app.use(express.static(path.join(__dirname, '../front/dist')));

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

// Pour toutes les autres requêtes, renvoie à l'application front-end
// Assurez-vous que cette ligne est après toutes vos autres routes d'API
app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, '../front/dist', 'index.html'));
});
