<template>
  <div class="home">
    <!-- Navbar -->
    <Navbar/>

    <!-- Bannière principale -->
    <section class="banner">
      <h1>CineRentalHub</h1>
      <p>Découvrez | Louez | Partagez</p>
    </section>

    <!-- Section Films -->
    <section class="films">
      <section class="add-movie">
        <MovieForm v-if="!this.selectedMovieId" @movie-added="fetchMovies"/>
        <MovieEditForm v-if="this.selectedMovieId" :movie="selectedMovie" @movie-added="fetchMovies"/>
      </section>
      <h2>Nos Films</h2>
      <div class="film-list">
        <movies_list :movies="movies" @edit-movie="handleEditMovie"></movies_list>
      </div>
    </section>

    <!-- Section À propos -->
    <section class="about">
      <h2>À propos de CineRentalHub</h2>
      <p>Nous sommes votre destination ultime pour la location de films en ligne. Parcourez une vaste sélection de
        films, louez vos favoris et partagez vos avis !</p>
    </section>

    <!-- Section Contact -->
    <section class="contact">
      <h2>Contactez-nous</h2>
      <p>Des questions ou des suggestions ? Nous sommes à votre écoute ! Contactez-nous.</p>
      <p>Email: contact@CineRentalHub.com</p>
    </section>

    <!-- Pied de page -->
    <Footer/>
  </div>
</template>

<script>
import Navbar from '../components/Navbar.vue';
import Footer from "../components/Footer.vue";
import movies_list from "../components/movies_list.vue";
import MovieForm from '../components/MovieForm.vue';
import axios from "axios";
import EditMemberForm from "../components/EditMemberForm.vue";
import MovieEditForm from "../components/MovieEditForm.vue";

export default {
  name: 'Home',
  components: {
    MovieEditForm,
    EditMemberForm,
    Footer,
    Navbar,
    movies_list,
    MovieForm
  },

  mounted() {
    if (localStorage.getItem('userId')) {
      this.$router.push('/homeConnected');
    }
    // Simuler la récupération de données
    this.fetchMovies();
  },

  methods: {
    async fetchMovies() {
      // Simulation de la récupération de données depuis la base de données
      await axios.get("http://localhost:3000/movies")
          .then(async response => {
            this.movies = response.data;
          })
          .catch(error => {
            console.log(error);
          });

      // this.movies = [
      // {
      //   id: 1,
      //   available: true,
      //   title: "AVATAR",
      //   release_date: "2009-12-16",
      //   daily_rental_price: 2.99,
      //   purchase_price: 9.99,
      //   description: "Malgré sa paralysie, Jake Sully, un ancien marine immobilisé dans un fauteuil roulant, est resté un combattant au plus profond de son être. Il est recruté pour se rendre à des années-lumière de la Terre, sur Pandora, où de puissants groupes industriels exploitent un minerai rarissime destiné à résoudre la crise énergétique sur Terre. Parce que l'atmosphère de Pandora est toxique pour les humains, ceux-ci ont créé le Programme Avatar, qui permet à des \" pilotes \" humains de lier leur esprit à un avatar, un corps biologique commandé à distance, capable de survivre dans cette atmosphère létale. Ces avatars sont des hybrides créés génétiquement en croisant l'ADN humain avec celui des Na'vi, les autochtones de Pandora.",
      //   link: "https://m.media-amazon.com/images/I/51EUk47IF8L._AC_SL1080_.jpg"
      // },
      // ];
    },
    handleEditMovie(movieId) {
      this.selectedMovieId = movieId;
      // Trouver le film par son ID
      const movieToEdit = this.movies.find(movie => movie.id === movieId);
      if (movieToEdit) {
        this.selectedMovie = movieToEdit;
      } else {
        console.error("Film non trouvé");
        this.selectedMovie = null;
      }
    },
  },

  data() {
    return {
      movies: [],
      selectedMovieId: null, // Ajoutez ceci
      selectedMovie: null
    };
  },
};
</script>

<style scoped>
/* Styles adaptés pour la nouvelle thématique */
.banner, .about, .contact {
  padding: 40px 20px;
  text-align: center;
}

.films, .about, .contact {
  padding: 20px;
}

footer {
  background-color: #f8f9fa;
  text-align: center;
  padding: 20px;
  margin-top: 40px;
}
</style>