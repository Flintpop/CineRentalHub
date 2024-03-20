<template>
  <div class="home ">
    <!-- Navbar -->
    <NavbarUser/>
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
import NavbarUser from '../../components/User/NavbarUser.vue';
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
    MovieEditForm,
    EditMemberForm,
    Footer,
    NavbarUser,
    ListMovies,
    MovieForm
  },

  mounted() {
    this.$nextTick(() => {

      const token = localStorage.getItem('token');
      if (!token) {
        console.log("Vous devez vous connecter pour accéder à cette page");
        this.$router.push('/Login');
        return;
      }

      try {
        const decoded = jwtDecode(token);
        if (decoded.role === 'admin') {
          console.log("Bienvenue dans l'espace administrateur");
          this.$router.push('/HomeAdmin');
        } else {
          console.log("Bienvenue dans l'espace utilisateur");
          this.$router.push('/HomeUser');
        }
      } catch (error) {
        console.error("Erreur lors de la décodage du token: ", error);
        localStorage.removeItem('token');
        this.$router.push('/Login');
      }
    });
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
              this.movies = response.data;
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
      // Définir des données fictives pour les films:
      // private int id;
      // private byte available;
      // private String title;
      // private Date release_date;
      // private BigDecimal daily_rental_price;
      // private BigDecimal purchase_price;
      // private String description;
      // private String link;

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
      showMovieList: true, // Ajoutez ceci
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
