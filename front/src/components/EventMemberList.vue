<template>
  <div class="event-member-list">
    <h3>Événements Inscrits</h3>
    <ul>
      <li v-for="event in events" :key="event.id">
        {{ event.titre }} - {{ formatDate(event.dateHeureDebut ) }} - {{ event.lieu.nom }} - {{ event.lieu.adresse }}
        <button @click="unsubscribeFromEvent(event.id)">Se désinscrire</button>
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  name: 'EventMemberList',
  props: {
    memberId: Number,
  },
  data() {
    return {
      events: [] // Initialisez events comme un tableau vide
    };
  },
  async created() {
    try {
      const response = await axios.get('http://localhost:8085/events');
      const allEvents = response.data;

      for (let event of allEvents) {
        const membersResponse = await axios.get(`http://localhost:8085/events/${event.id}/membres`);
        const members = membersResponse.data;

        if (members.some(member => member.id === this.memberId)) {
          console.log(event);
          if (event.lieuId !== undefined) {
            event.lieu = await this.fetchVenueName(event.lieuId);
          } else {
            console.error('L\'ID du lieu est undefined pour l\'événement', event.id);
          }
          this.events.push(event);
        }
      }
    } catch (error) {
      console.error('Erreur lors de la récupération des événements', error);
    }
  },
  methods: {
    async fetchEventDetails(eventId) {
      try {
        const response = await axios.get(`http://localhost:8085/events/${eventId}`);
        return response.data;
      } catch (error) {
        console.error(`Erreur lors de la récupération des détails de l'événement ${eventId}`, error);
      }
    },
    async fetchVenueName(venueId) {
      try {
        const response = await axios.get(`http://localhost:8085/lieux/${venueId}`);
        return response.data;
      } catch (error) {
        console.error(`Erreur lors de la récupération du nom du lieu ${venueId}`, error);
      }
    },
    async unsubscribeFromEvent(eventId) {
      try {
        await axios.delete(`http://localhost:8085/events/${eventId}/membres/${this.memberId}`);
        this.events = this.events.filter(event => event.id !== eventId);
        this.$emit('unsubscribe', { memberId: this.memberId, eventId: eventId });
      } catch (error) {
        console.error(`Erreur lors de la désinscription de l'événement ${eventId}`, error);
      }
    },
    formatDate(value) {
      const date = new Date(value);
      return date.toLocaleDateString('fr-FR', { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' });
    }
  }
};
</script>

<style scoped>
.event-member-list ul {
  list-style-type: none;
  padding: 0;
}

.event-member-list li {
  margin-bottom: 10px;
}

.event-member-list button {
  margin-left: 10px;
}
</style>

