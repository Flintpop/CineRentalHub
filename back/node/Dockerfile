# Utiliser l'image officielle Node.js comme image de base
FROM node:21.7.1

# Définir le répertoire de travail dans le conteneur
WORKDIR /usr/src/app

# Copier les fichiers de dépendances
COPY package*.json ./

# Installer les dépendances de l'application
RUN npm install

# Copier tous les fichiers sources dans le répertoire de travail
COPY . .

# Exposer le port sur lequel l'application va s'exécuter
EXPOSE 3000

# Définir la commande pour démarrer l'application
CMD ["node", "app.js"]
