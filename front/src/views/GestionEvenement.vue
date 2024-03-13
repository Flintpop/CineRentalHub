<template>
  <NavbarConnected/>
  <div class="gestionEvenement">
    <h1>Gestion des Événements</h1>
    <div class="add-event-button" v-if="!showFormCreate ">
      <button @click="showCreateEventForm" class="event-add-btn">Ajouter un événement</button>    </div>


    <!-- Formulaire de création d'événement -->
    <CreateEventForm v-if="showFormCreate" @closeForm="closeForm" @saveEvent="saveEvent" :lieux="lieux"/>
    <!-- Formulaire d'édition d'événement -->
    <EditEventForm v-if="showFormEdit" @closeForm="closeForm" @saveEvent="saveEvent" :event="selectedEvent" :lieux="lieux"/>

    <section class="events">
      <h2>Événements</h2>
      <div class="event-list">
        <EventCardGestion v-for="event in events" :key="event.id" :event="event" @eventDeleted="removeEvent"
                          @editEvent="editEvenement(event)"/>
      </div>
    </section>
  </div>
  <Footer/>
</template>

<script>
import NavbarConnected from '../components/NavbarConnected.vue';
import EventCardGestion from "../components/EventCardGestion.vue";
import CreateEventForm from "../components/CreateEventForm.vue";
import EditEventForm from "../components/EditEventForm.vue";
import Footer from '../components/Footer.vue';
import axios from 'axios';


export default {
  name: 'GestionEvenement',
  components: {
    NavbarConnected,
    EventCardGestion,
    CreateEventForm,
    EditEventForm,
    Footer,
  },
  mounted() {
    if (!localStorage.getItem('membreId')) {
      this.$router.push('/');
    }
  },
  data() {
    return {
      events: [],
      showFormEdit: false,
      selectedEvent: null,
      showFormCreate: false,
    };
  }
  ,
  methods: {
    fetchEvenements() {
      axios.get('http://localhost:8085/events')
          .then(response => {
            console.log(response.data);
            const events = response.data;
            return Promise.all(events.map(evenement => {
              return axios.get(`http://localhost:8085/lieux/${evenement.lieuId}`)
                  .then(response => {
                    evenement.lieu = response.data;
                    return evenement;
                  });
            }));
          })
          .then(evenementsAvecLieux => {
            this.events = evenementsAvecLieux;
          })
          .catch(error => {
            console.error(error);
          });
    },
    showCreateEventForm() {
      this.showFormEdit = false;
      this.showFormCreate = true;
    },
    editEvenement(event) {
      this.showFormEdit = false;
      this.selectedEvent = Object.assign({}, event);
      this.showFormEdit = true;
    },
    removeEvent(eventId) {
      this.events = this.events.filter(event => event.id !== eventId);
    },
    saveEvent(event) {
      // Logique pour sauvegarder l'événement dans la BDD
      console.log('Sauvegarder l\'événement', event);
      this.closeForm();
    },
    closeForm() {
      this.showFormEdit = false;
      this.showFormCreate = false;
    },
  },
  created() {
    this.fetchEvenements();
  },
};
</script>

<style>
.gestionEvenement {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.add-event-button {
  text-align: center;
  margin-bottom: 20px;
}

.events {
  padding: 20px;
}


</style>


