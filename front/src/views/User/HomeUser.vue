<template>
  <div class="home">
    <!-- Navbar -->
    <NavbarUser/>

    <!-- Section Films -->
    <section class="films">
        <h2>Nos Films</h2>
        <div class="film-list">
          <MoviesList :movies="movies" @edit-movie="handleEditMovie"></MoviesList>
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
import NavbarUser from '../../components/User/NavbarUser.vue';
import Footer from "../../components/Core/Footer.vue";
import MoviesList from "../../components/Core/MoviesList.vue";
import MovieForm from '../../components/Admin/MovieForm.vue';
import axios from "axios";
import EditMemberForm from "../../components/Admin/EditMemberForm.vue";
import MovieEditForm from "../../components/Admin/MovieEditForm.vue";

export default {
  name: 'Home',
  components: {
    MovieEditForm,
    EditMemberForm,
    Footer,
    NavbarUser,
    MoviesList,
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
      clicked_modification_movie: false
    };
  },
};
</script>
<style scoped>
/* Styles existants adaptés pour la nouvelle thématique */
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
