<template>
  <div class="homeAdmin">
    <NavbarAdmin/>
    <div class="main-content">
      <!-- Condition pour afficher la liste des films OU les détails du film -->
      <section class="films" v-if="showMovieList">
        <h2>Nos Films</h2>
        <ListMovies :movies="movies" @movie-detail="handleMovieDetail" @edit-movie="handleEditMovie"></ListMovies>
      </section>

      <!-- Condition pour afficher les détails d'un film spécifique -->
      <MovieDetail
          v-else-if="selectedMovie"
          :movie="selectedMovie"
          @close="handleCloseDetails"
      />

    </div>
    <!-- Pied de page -->
    <Footer/>
  </div>
</template>

<script>
import NavbarAdmin from '../../components/Admin/NavbarAdmin.vue';
import Footer from "../../components/Core/Footer.vue";
import MoviesList from "../../components/Core/MoviesList.vue";
import MovieForm from '../../components/Admin/MovieForm.vue';
import axios from "axios";
import EditMemberForm from "../../components/Admin/EditMemberForm.vue";
import MovieEditForm from "../../components/Admin/MovieEditForm.vue";
import ListMovies from "../../components/Core/ListMovies.vue";
import MovieDetail from "../../components/Core/MovieDetail.vue";

export default {
  name: 'homeAdmin',
  components: {
    MovieEditForm,
    EditMemberForm,
    Footer,
    NavbarAdmin,
    MoviesList,
    ListMovies,
    MovieForm,
    MovieDetail
  },

  mounted() {
    // Simuler la récupération de données
    this.fetchMovies();
  },

  methods: {
    handleCloseDetails() {
      this.selectedMovie = null; // Réinitialise le film sélectionné
      this.showMovieList = true; // Montre la liste des films
    },
    handleMovieDetail(movieId) {
      this.selectedMovieId = movieId;
      const movieToShow = this.movies.find(movie => movie.id === movieId);
      if (movieToShow) {
        this.selectedMovie = movieToShow; // Assigne le film sélectionné
        this.showMovieList = false; // Cache la liste des films
      } else {
        console.error("Film non trouvé");
      }
    },
    async fetchMovies() {
      // Simulation de la récupération de données depuis la base de données
      await axios.get("http://localhost:3000/movies")
          .then(async response => {
            this.movies = response.data;

            this.clicked_added_movie = false;
            this.clicked_modification_movie = false;
          })
          .catch(error => {
            console.log(error);
          });
    },
    handleEditMovie(movieId) {
      this.selectedMovieId = movieId;
      // Trouver le film par son ID
      const movieToEdit = this.movies.find(movie => movie.id === movieId);
      if (movieToEdit) {
        this.selectedMovie = movieToEdit;
        this.clicked_modification_movie = true;
        this.clicked_added_movie = false;
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
      selectedMovie: null,
      clicked_added_movie: false,
      clicked_modification_movie: false,
      showMovieList: true,
      showMovieDetails: false,
    };
  },
};
</script>
<style scoped>


.films, .about, .contact {
  padding: 20px;
}

footer {
  background-color: #f8f9fa;
  text-align: center;
  padding: 20px;
  margin-top: 40px;
}

/* Modifications pour le texte de CineRentalHub */
.banner h1 {
  font-size: 4rem; /* Ajustez la taille selon vos préférences */
  background: -webkit-linear-gradient(#007bff, #6c757d); /* Gradient bleu à gris pour les navigateurs WebKit */
  background: linear-gradient(#007bff, #6c757d); /* Gradient bleu à gris pour les autres navigateurs */
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent; /* Technique pour l'effet de gradient sur le texte */
  margin: 0;
  padding: 0;
  padding-bottom: 100vh;
}

</style>
