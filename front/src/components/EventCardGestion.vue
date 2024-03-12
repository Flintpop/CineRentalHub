<template>
  <div class="event-card">
    <!-- En-tête de la carte -->
    <div class="event-header" v-if="!showDetails">
      <h3 class="event-title">{{ event.titre }}</h3>
      <p>Date de début : {{ formattedStartDate }}</p>
      <p class="event-description">{{ event.description }}</p>
      <button @click="toggleDetails">Détails</button>
      <button @click="deleteEvent(event.id)">Supprimer l'événement</button>

      <button @click="$emit('editEvent', event)">Modifier</button>
    </div>
    <!-- Détails de l'événement -->
    <div v-if="showDetails" class="event-detail-container">
      <EventDetailGestion :event="event" />
      <button @click="toggleDetails" class="close-detail-btn">✕</button>
      <button @click="deleteEvent(event.id)">Supprimer</button>
      <button @click="$emit('editEvent', event)">Modifier</button>
    </div>
  </div>
</template>

<script>
import EventDetailGestion from './EventDetailGestion.vue';
import axios from 'axios';

export default {
  name: 'EventCardGestion',
  components: {
    EventDetailGestion,
  },
  props: {
    event: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      showDetails: false,
    };
  },
  computed: {
    formattedStartDate() {
      const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' };
      return new Date(this.event.dateHeureDebut).toLocaleString('fr-FR', options);
    },
  },
  methods: {
    toggleDetails() {
      this.showDetails = !this.showDetails;
    },
    async deleteEvent(eventId) {
      // Préparez l'URL de l'API
      const url = `http://localhost:8085/events/${eventId}`;
      console.log('url', url);

      // Appeler l'API pour supprimer l'événement
      try {
        await axios.delete(url);
        alert('L\'événement a été supprimé.');

        // Vous pouvez ici rafraîchir la liste des événements ou rediriger l'utilisateur
        this.$emit('eventDeleted', eventId);
      } catch (error) {
        console.error("Erreur API :", error);
        alert('Une erreur est survenue lors de la suppression de l\'événement.');
      }
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

