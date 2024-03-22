<template>
  <div class="home">

    <!-- Navbar -->
    <Navbar/>
    <div class="main-content">
      <!-- Section banner -->
      <section class="banner">
        <h1>CineRentalHub</h1>
        <p>Découvrez | Louez | Partagez</p>
      </section>


      <!-- Section À propos -->
      <section class="about">
        <h2>À propos de CineRentalHub</h2>
        <p>Nous sommes votre destination ultime pour la location de films en ligne. Parcourez une vaste sélection de
          films, louez vos favoris et partagez vos avis !</p>
      </section>


      <!-- Section Films -->
      <!--      <section class="films">-->
      <!--        <h2>Nos Films</h2>-->
      <!--        <div class="film-list">-->
      <!--          <MoviesList :movies="movies" @edit-movie="handleEditMovie"></MoviesList>-->
      <!--        </div>-->

      <section class="films" v-if="showMovieList">
        <h2>Nos Films</h2>
        <ListMovies :movies="movies" @movie-detail="handleMovieDetail" @edit-movie="handleEditMovie"></ListMovies>
      </section>
      <MovieDetail
          v-else-if="selectedMovie"
          :movie="selectedMovie"
          @close="handleCloseDetails"
      />


      <!-- Section Contact -->
      <section class="contact">
        <h2>Contactez-nous</h2>
        <p>Des questions ou des suggestions ? Nous sommes à votre écoute ! Contactez-nous.</p>
        <p>Email: contact@CineRentalHub.com</p>
      </section>
    </div>
    <!-- Pied de page -->
    <Footer/>
  </div>
</template>

<script>
import Navbar from '../../components/NoConnected/Navbar.vue';
import Footer from "../../components/Core/Footer.vue";
import MoviesList from "../../components/Core/MoviesList.vue";
import MovieForm from '../../components/Admin/MovieForm.vue';
import axios from "axios";
import EditMemberForm from "../../components/Admin/EditMemberForm.vue";
import MovieEditForm from "../../components/Admin/MovieEditForm.vue";
import {jwtDecode} from "jwt-decode";
import ListMovies from "../../components/Core/ListMovies.vue";
import MovieDetail from "../../components/Core/MovieDetail.vue";


export default {
  name: 'Home',
  components: {
    MovieDetail,
    ListMovies,
    MovieEditForm,
    EditMemberForm,
    Footer,
    Navbar,
    MoviesList,
    MovieForm,
  },

  mounted() {
// test si token est présent dans le local storage et test le role de l'utilisateur pour rediriger vers la page d'accueil
    const token = localStorage.getItem('token');
    if (token) {
      const decoded = jwtDecode(token);
      if (decoded.role === 'admin') {
        this.$router.push('/HomeAdmin');
      } else {
        this.$router.push('/HomeUser');
      }
    }
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
      await axios.get("http://localhost:3000/movies")
          .then(response => {
            if (response.data && response.data.length > 0) {
              this.movies = response.data.filter(movie => movie.available === 1); // Filtrage côté client, au cas où
            } else {
              // Utiliser des données fictives si aucune donnée n'est récupérée
              this.usefictiveData();
            }
            this.clicked_added_movie = false;
            this.clicked_modification_movie = false;
          })
          .catch(error => {
            console.error("Erreur lors de la récupération des films:", error);
            // Utiliser des données fictives en cas d'erreur
            this.usefictiveData();
          });
    },
    usefictiveData() {
      this.movies = [
        {
          id: 1,
          available: 1,
          title: "Film fictif 1",
          release_date: "2023-01-01",
          daily_rental_price: 5.99,
          purchase_price: 19.99,
          description: "Description du film fictif 1",
          link: "https://www.example.com/film1"
        },
        {
          id: 2,
          available: 1,
          title: "Film fictif 2",
          release_date: "2023-01-02",
          daily_rental_price: 6.99,
          purchase_price: 21.99,
          description: "Description du film fictif 2",
          link: "https://www.example.com/film2"
        },

      ];
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
/* Styles existants adaptés pour la nouvelle thématique */
.banner, .about, .contact {
  padding: 40px 20px;
  text-align: center;
}

.films, .contact {
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
}
</style>
