<template>
  <div class="event-detail-Gestion">
    <!-- Détails de l'événement -->
    <h2>{{ event.titre }}</h2>
    <p><strong>Date de début :</strong> {{ formattedStartDate }}</p>
    <p><strong>Date de fin :</strong> {{ formattedEndDate }}</p>
    <p><strong>Description :</strong> {{ event.description }}</p>
    <p><strong>Nombre maximum de participants :</strong> {{ location.capaciteAccueil }}</p>
    <p><strong>Nombre de participants :</strong> {{ participants.length }}</p>
    <p><strong>Lieu :</strong> {{ location.nom }} - {{ location.adresse }}</p>

    <!-- Liste des participants -->
    <h3>Liste des participants :</h3>
    <ul>
      <li v-for="participant in participants" :key="participant.id">
        {{ participant.nom }} {{ participant.prenom }}
      </li>
    </ul>

    <!--     Section des commentaires-->
    <h3>Commentaires :</h3>
    <div class="comments-section">
      <div v-for="commentaire in commentaires" :key="index" class="comment-bubble">
        <div class="comment-content">
          <strong>{{ commentaire.auteur.nom }}{{ commentaire.auteur.prenom }}:</strong> {{ commentaire.texte }}
        </div>
        <div class="comment-date">
          {{ formatCommentDate(commentaire.dateMessage) }}
        </div>
        <button @click="prepareDeleteComment(commentaire)" class="comment-delete-btn">Supprimer</button>
        <button @click="prepareModifyComment(commentaire)" class="comment-modify-btn">Modifier</button>
      </div>
    </div>

    <div v-if="showConfirmationDialogSuppr" class="dialog-overlay" @click.self="cancelDelete">
      <div class="dialog">
        <h3>Confirmation de suppression</h3>
        <p>Voulez-vous vraiment supprimer ce commentaire?</p>
        <p><strong>Commentaire :</strong> {{ commentToDelete.texte }}</p>
        <div class="dialog-actions">
          <button @click="deleteComment(commentToDelete.id)" class="dialog-button confirm">Confirmer</button>
          <button @click="cancelDelete" class="dialog-button cancel">Annuler</button>
        </div>
      </div>
    </div>

    <div v-if="showModificationDialog" class="dialog-overlay" @click.self="cancelModify">
      <div class="dialog">
        <h3>Modification du commentaire</h3>
        <form @submit.prevent="modifyComment(commentToModify.id)">
          <textarea v-model="commentToModify.texte"></textarea>
          <div class="dialog-actions">
            <button type="submit" class="dialog-button confirm">Confirmer</button>
            <button @click="cancelModify" class="dialog-button cancel">Annuler</button>
          </div>
        </form>
      </div>
    </div>
    <!-- Carte pour afficher l'emplacement de l'événement -->
    <div id="event-map" style="height: 300px;"></div>
  </div>
</template>

<script>
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import axios from "axios";
import markerIconUrl from '../assets/marker.svg';
import markerShadowUrl from '../assets/marker-shadow.svg';

export default {
  name: 'EventDetail',
  props: {
    event: {
      type: Object,
      default: () => ({}),
    },
  },
  data() {
    return {
      newCommentText: '',
      map: null,
      latitude: null,
      longitude: null,
      commentaires: [],
      participants: [],
      location: [],
      showConfirmationDialogSuppr: false,
      commentToDelete: null,
      showModificationDialog: false,
      commentToModify: null,
    };
  },
  mounted() {
    this.fetchLocation(this.event.id);
  },
  methods: {
    formatCommentDate(date) {
      const options = {year: 'numeric', month: 'long', day: 'numeric'};
      return new Date(date).toLocaleString('fr-FR', options);
    },
    fetchComments(eventId) {
      axios.get(`http://localhost:8085/commentaire/evenement/${eventId}`)
          .then(response => {
            const commentaires = response.data;
            return Promise.all(commentaires.map(commentaire => {
              return axios.get(`http://localhost:8085/membres/${commentaire.auteurId}`)
                  .then(response => {
                    console.log(response.data);
                    commentaire.auteur = response.data;
                    return commentaire;
                  });
            }));
          })
          .then(commentairesAvecAuteurs => {
            this.commentaires = commentairesAvecAuteurs;
            console.log(commentairesAvecAuteurs);
          })
          .catch(error => {
            console.error(error);
          });
    },
    fetchParticipants(eventId) {
      axios.get(`http://localhost:8085/events/${eventId}/membres`)
          .then(response => {
            this.participants = response.data;
            console.log(response.data);
          })
          .catch(error => {
            console.error(error);
          });
    },
    async fetchLocation(eventId) {
      axios.get(`http://localhost:8085/lieux/${eventId}`)
          .then(response => {
            this.location = response.data;
            return this.geocodeAddress(this.location.adresse);
          })
          .then(coords => {
            if (coords) {
              this.latitude = parseFloat(coords.lat);
              this.longitude = parseFloat(coords.lon);
              this.initMap();
            }
          })
          .catch(error => {
            console.error(error);
          });
    },
    async geocodeAddress(address) {
      const url = `https://nominatim.openstreetmap.org/search?format=json&limit=1&q=${encodeURI(address)}`;
      const response = await fetch(url);
      const data = await response.json();
      if (data.length > 0) {
        const {lat, lon} = data[0];
        return {lat, lon};
      } else {
        console.error('Adresse non trouvée:', address);
      }
      return null;
    },
    initMap() {
      this.map = L.map('event-map').setView([this.latitude, this.longitude], 13);
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
      }).addTo(this.map);

      let icon = L.icon({
        // récuperation de l'image dans le dossier src/assets/marker.svg
        iconUrl: markerIconUrl ,
        shadowUrl: markerShadowUrl ,
        iconSize: [25, 41], // taille de l'icône
        shadowSize: [41, 41], // taille de l'ombre
        iconAnchor: [12, 41], // point de l'icône qui correspondra à la position du marqueur
        shadowAnchor: [12, 41],  // point de l'ombre qui correspondra à la position de l'ombre
        popupAnchor: [1, -34], // point à partir duquel le popup doit s'ouvrir par rapport à l'iconAnchor
      });

      L.marker([this.latitude, this.longitude], {icon: icon}).addTo(this.map);
    },
    prepareDeleteComment(commentaire) {
      this.commentToDelete = commentaire;
      this.showConfirmationDialogSuppr = true;
    },
    async deleteComment(commentId) {
      // Préparez l'URL de l'API
      const url = `http://localhost:8085/commentaire/${commentId}`;

      // Appeler l'API pour supprimer le commentaire
      try {
        await axios.delete(url);
        alert('Votre commentaire a été supprimé.');

        // Mettre à jour la liste des commentaires
        this.fetchComments(this.event.id);
      } catch (error) {
        console.error("Erreur API :", error);
        alert('Une erreur est survenue lors de la suppression du commentaire.');
      }

      // Fermer le pop-up
      this.showConfirmationDialogSuppr = false;
      this.commentToDelete = null;
    },
    cancelDelete() {
      this.showConfirmationDialogSuppr = false;
      this.commentToDelete = null;
    },
    prepareModifyComment(commentaire) {
      // Créez une copie du commentaire pour éviter de modifier directement le commentaire original
      this.commentToModify = commentaire;
      console.log(this.commentToModify);
      this.showModificationDialog = true;
    },
    async modifyComment(commentId) {
      // Préparez l'URL de l'API
      const url = `http://localhost:8085/commentaire/${commentId}`;

      // Appeler l'API pour modifier le commentaire
      try {
        await axios.put(url, this.commentToModify);
        alert('Votre commentaire a été modifié.');

        // Mettre à jour la liste des commentaires
        this.fetchComments(this.event.id);
      } catch (error) {
        console.error("Erreur API :", error);
        alert('Une erreur est survenue lors de la modification du commentaire.');
      }

      // Fermer le pop-up
      this.showModificationDialog = false;
      this.commentToModify = null;
    },
    cancelModify() {
      this.showModificationDialog = false;
      this.commentToModify = null;
    },

  },
  created() {
    this.fetchComments(this.event.id);
    this.fetchParticipants(this.event.id);
    this.fetchLocation(this.event.id);
  },
  computed: {
    formattedStartDate() {
      const options = {year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit'};
      return new Date(this.event.dateHeureDebut).toLocaleString('fr-FR', options);
    },
    formattedEndDate() {
      const options = {year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit'};
      return new Date(this.event.dateHeureFin).toLocaleString('fr-FR', options);
    },
  },
};
</script>

<style scoped>

.comment-form textarea {
  width: 100%;
  margin-bottom: 10px;
  padding: 10px;
}

.comment-form button {
  padding: 5px 15px;
}

.comment-bubble {
  background-color: #f4f4f8;
  border-radius: 15px;
  padding: 10px;
  margin-bottom: 10px;
  max-width: 80%;
}

.comment-content {
  margin-bottom: 5px;
}

.comment-date {
  font-size: 0.8em;
  text-align: right;
  color: #888;
}

.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.6);
  z-index: 10000;
}
.dialog {
  background: white;
  padding: 20px;
  border-radius: 5px;
  text-align: center;
}
.dialog-actions {
  margin-top: 20px;
}
.dialog-button {
  margin: 0 10px;
  padding: 5px 15px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.confirm {
  background-color: #4CAF50;
  color: white;
}
.cancel {
  background-color: #f44336;
  color: white;
}
</style>
