<template>
  <div class="home">
    <!-- Navbar -->
    <Navbar/>

    <!-- Bannière principale -->
    <section class="banner">
      <h1>Bienvenue chez Tux & Co</h1>
      <p>Explorer | Apprendre | Connecter</p>
    </section>

    <!-- Section Événements -->
    <section class="events">
      <h2>Films</h2>
      <div class="event-list">
        <Movies></Movies>
      </div>
    </section>

    <!-- Section À propos -->
    <section class="about">
      <h2>À propos de Nous</h2>
      <p>Nous sommes les TUX, votre partenaire dans l'organisation d'événements . Notre mission est de rassembler les professionnels du secteur pour partager connaissances et innovations.</p>
    </section>

    <!-- Section Contact -->
    <section class="contact">
      <h2>Contactez-nous</h2>
      <p>Avez-vous des questions? Voulez-vous collaborer sur un événement? N'hésitez pas à nous contacter.</p>
      <p>Email: contact@tux.com</p>
    </section>

    <!-- Pied de page -->
    <Footer/>
  </div>
</template>

<script>
import Navbar from '../components/Navbar.vue';
import Footer from "../components/Footer.vue";
import Movies from "../components/Movies.vue";
import axios from 'axios';

export default {
  name: 'Home',
  components: {
    Footer,
    Navbar,
    Movies
  },

  mounted() {
    if (localStorage.getItem('membreId')) {
      this.$router.push('/homeConnected');
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
.banner, .about, .contact {
  padding: 40px 20px;
  text-align: center;
}

.events, .about, .contact {
  padding: 20px;
}

footer {
  background-color: #f8f9fa;
  text-align: center;
  padding: 20px;
  margin-top: 40px;
}
</style>
