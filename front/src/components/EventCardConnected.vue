<template>
  <div class="event-card">
    <!-- En-tête de la carte -->
    <div class="event-footer" v-if="!showDetails">
      <div class="event-header">
        <h3 class="event-title">{{ event.titre }}</h3>
        <p>Date de début : {{ formattedStartDate }}</p>
      </div>
      <p class="event-description">{{ event.description }}</p>
      <button @click="toggleDetails" class="event-detail-btn">Détails</button>
      <button v-if="!isUserRegistered" @click="showConfirmationDialog = true" class="event-register-btn">S'inscrire</button>
      <button v-if="isUserRegistered" @click="unregisterFromEvent" class="event-register-btn">Désinscrire</button>

    </div>


    <!-- Détails de l'événement -->
    <div v-if="showDetails" class="event-detail-container">
      <EventDetailConnected :event="event" />
      <button @click="toggleDetails" class="close-detail-btn">✕</button>

      <button v-if="isUserRegistered" @click="showConfirmationDialog = true" class="event-register-btn">S'inscrire</button>
      <button v-if="!isUserRegistered" @click="unregisterFromEvent" class="event-register-btn">Désinscrire</button>

    </div>

    <!-- Dialogue de confirmation intégré -->
    <div v-if="showConfirmationDialog" class="dialog-overlay">
      <div class="dialog">
        <h3>Confirmation d'inscription</h3>
        <p>Voulez-vous vraiment vous inscrire à l'événement {{ event.titre }}?</p>
        <div class="dialog-actions">
          <button @click="registerForEvent" class="dialog-button confirm">Confirmer</button>
          <button @click="cancelRegistration" class="dialog-button cancel">Annuler</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import EventDetailConnected from './EventDetailConnected.vue';
import axios from "axios";

export default {
  name: 'EventCardConnected',
  components: {
    EventDetailConnected,
  },
  props: {
    event: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      isUserRegistered: false, // Ajoutez cette ligne pour gérer l'état d'inscription
      showDetails: false,
      showConfirmationDialog: false,
    };
  },
  mounted() {
    this.checkUserRegistration(); // Vérifiez l'inscription lors du chargement du composant
  },
  computed: {
    formattedStartDate() {
      const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' };
      return new Date(this.event.dateHeureDebut).toLocaleString('fr-FR', options);
    },
  },
  methods: {
    async checkUserRegistration() {
      const userId = localStorage.getItem('membreId');
      if (!userId) {
        console.log('Utilisateur non connecté');
        return;
      }
      const eventUrl = `http://localhost:8085/events/${this.event.id}/membres`;

      try {
        const response = await axios.get(eventUrl);
        // Si erreur 404 pas de membres inscrit
        if (response.status === 404) {
          console.log('Aucun membre inscrit à l\'événement');
          this.isUserRegistered = false;
          return;
        }
        const membres = response.data; // Assumons que cela renvoie un tableau d'objets membre
        console.log('Membres inscrits à l\'événement :\n', membres)
        this.isUserRegistered = membres.some(membre => membre.id.toString() === userId);
        console.log(`Vérification de l'inscription: ${this.isUserRegistered ? 'Inscrit' : 'Non inscrit'}`);
      } catch (error) {
        console.error("Erreur lors de la vérification de l'inscription :", error);
      }
    },
    async removeUserFromEvent(eventId) {
      const userId = localStorage.getItem('membreId');
      if (!userId) {
        alert('Veuillez vous connecter pour vous désinscrire de cet événement.');
        return;
      }

      const eventUrl = `http://localhost:8085/events/${eventId}/membres/${userId}`;

      try {
        await axios.delete(eventUrl);
        alert('Vous êtes désinscrit de cet événement.');
        this.isUserRegistered = false; // Mettre à jour l'état d'inscription
        console.log(`Désinscription de l'événement ${eventId} pour l'utilisateur ${userId}`);
      } catch (error) {
        console.error("Erreur API lors de la désinscription :", error);
      }
    },
    unregisterFromEvent() {
      this.removeUserFromEvent(this.event.id);
    },
    toggleDetails() {
      this.showDetails = !this.showDetails;
    },
    async addUserToEvent(eventId) {
      // Récupérer l'ID de l'utilisateur connecté
      const userId = localStorage.getItem('membreId');
      console.log("userId", userId)
      console.log("")
      console.log("eventId", eventId)


      // Vérifier si l'utilisateur est connecté
      if (!userId) {
        alert('Veuillez vous connecter pour vous inscrire à cet événement.');
        return;
      }
      // Préparer l'URL de l'API pour récupérer les détails du membre
      const userUrl = `http://localhost:8085/membres/${userId}`;

      // Récupérer les détails du membre
      let user;
      try {
        const response = await axios.get(userUrl);
        user = response.data;
        console.log('Détails du membre :', user);
      } catch (error) {
        console.error("Erreur API :", error);
        alert('Une erreur est survenue lors de la récupération des détails du membre.');
        return;
      }

      // Préparer l'URL de l'API
      const eventUrl = `http://localhost:8085/events/${eventId}/membres`;

      console.log("eventUrl", eventUrl)

      console.log("")
      console.log("user", user)

      // Préparer les données de la requête
      const requestData = {
        id: user.id,
      };

      // Appeler l'API pour ajouter l'utilisateur à l'événement
      try {
        await axios.put(eventUrl, requestData);
        alert('Vous êtes inscrit à cet événement.');
      } catch (error) {
        console.error("Erreur API :", error);
        alert('Une erreur est survenue lors de l\'inscription à l\'événement.');
      }
    },

    registerForEvent() {
      this.addUserToEvent(this.event.id);
      this.showConfirmationDialog = false;
    },
    cancelRegistration() {
      this.showConfirmationDialog = false;
    },
  },
};
</script>

<style scoped>
.event-card {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  background-color: #fff;
  border: 1px solid #eaeaea;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* Styles for the close button */
.close-detail-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  border: none;
  background: none;
  cursor: pointer;
  font-size: 1.5rem;
  color: #333;
}

.event-detail-container {
  position: relative;
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
