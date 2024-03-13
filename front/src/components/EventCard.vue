<template>
  <div class="event-card">

    <div class="event-footer" v-if="!showDetails">
      <div class="event-header">
        <h3 class="event-title">{{ event.titre }}</h3>
        <p>Date de début : {{ formattedStartDate }}</p>
      </div>
      <p class="event-description">{{ event.description }}</p>
      <button @click="toggleDetails" class="event-detail-btn">Détails</button>

    </div>
    <div v-if="showDetails" class="event-detail-container">
      <EventDetail :event="event" />
      <button @click="toggleDetails" class="close-detail-btn">✕</button>
    </div>
  </div>
</template>

<script>
import EventDetail from './EventDetail.vue';

export default {
  name: 'EventCard',
  components: {
    EventDetail,
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
</style>
