<template>
  <div class="homeConnected">
    <!-- Navbar -->
    <NavbarConnected />

    <!-- Section Événements -->
    <section class="events">
      <h2>Événements à venir</h2>
      <div class="event-list">
        <EventCardConnected v-for="event in events" :key="event.id" :event="event" />
      </div>
    </section>

    <!-- Pied de page -->
    <Footer />
  </div>
</template>

<script>
import NavbarConnected from '../components/NavbarConnected.vue';
import EventCardConnected from '../components/EventCardConnected.vue';
import Footer from "../components/Footer.vue";
import Commentaires from "../components/Commentaire.vue";
import Membres from "../components/Membres.vue";
import ListeLieux from "../components/Lieux.vue";
import axios from 'axios';

export default {
  name: 'Home',
  components: {
    Footer,
    ListeLieux,
    NavbarConnected,
    EventCardConnected,
    Commentaires,
    Membres,
  },

  mounted() {
    if (!localStorage.getItem('membreId')) {
      this.$router.push('/');
    }
  },
  data() {
    return {
      events: [],

    };
  },
  methods: {
    // ...
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
  },
  created() {
    this.fetchEvenements();
  },

};
</script>

<style scoped>
/* Ajoutez ou modifiez le CSS ici pour styliser vos sections supplémentaires */


.events {
  padding: 20px;
}

footer {
  background-color: #f8f9fa;
  text-align: center;
  padding: 20px;
  margin-top: 40px;
}
</style>
