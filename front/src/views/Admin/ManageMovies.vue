<template>
  <NavbarAdmin/>
  <div class="main-content">
    <h1>Gestion des films</h1>
    <button @click="showAddMovieForm = true">Ajouter un film</button>
    <!-- Modale d'ajout de film -->
    <MovieForm v-if="showAddMovieForm" @close="showAddMovieForm = false" @submit="addMovie"></MovieForm>

    <!-- Liste des films -->
    <div class="movie-card" v-for="movie in movies" :key="movie.id">
      <div class="movie-content">
        <div class="movie-image-wrapper">
          <img :src="movie.main_image_url" alt="Image du film" class="movie-image">
        </div>
        <div class="movie-info">
          <h2>{{ movie.title }}</h2>
          <p>{{ movie.description }}</p>
          <p>Date de sortie : {{ movie.release_date }}</p>
          <p>Prix de location quotidien : {{ movie.daily_rental_price }}€</p>
          <p>Prix d'achat : {{ movie.purchase_price }}€</p>
          <p>Disponible : {{ movie.available}}</p>
          <p>Lien de la video : {{ movie.link }}</p>
        </div>
        <div class="movie-actions">
          <button @click="selectMovie(movie)">Modifier</button>
          <!-- Bouton pour désactiver le film -->
          <button v-if="movie.available" @click="disableMovie(movie.id)" class="disable-button">Désactiver le film</button>
          <!-- Bouton pour activer le film -->
          <button v-else @click="enableMovie(movie.id)" class="enable-button">Activer le film</button>
          <button @click="showImage(movie.id)">Afficher les images</button>
          <button @click="showComments(movie.id)">Afficher les commentaires</button>
        </div>
      </div>
      <!-- Modale de modification de film -->
      <div class="movie-edit-form">
        <MovieEditForm v-if="showEditMovieForm && selectedMovie === movie" :movie="selectedMovie"
                       @close="clearSelectedMovie" @submit="editMovie"></MovieEditForm>
      </div>

      <MovieImages v-if="showImageManagement === movie.id" :movie-id="movie.id"></MovieImages>
<!--      <MovieImages v-if="showImageManagement && visibleImages === movie.id" :movie-id="movie.id"></MovieImages>-->
    </div>


  </div>
  <Footer/>
</template>


<script>
import NavbarAdmin from "../../components/Admin/NavbarAdmin.vue";
import Footer from "../../components/Core/Footer.vue";
import MovieForm from "../../components/Admin/MovieForm.vue";
import MovieEditForm from "../../components/Admin/MovieEditForm.vue";
import axios from "axios";
import MovieImages from "../../components/Admin/MovieImages.vue";
import moment from 'moment';

export default {
  components: {Footer, NavbarAdmin, MovieForm, MovieEditForm, MovieImages},
  mounted() {
    // Simuler la récupération de données
    this.fetchMovies();
  },
  data() {
    return {
      showAddMovieForm: false,
      showEditMovieForm: false,
      showImageManagement: null,
      movies: [],
      images: [],
    };
  },
  methods: {
    async fetchMovies() {
      // Simulation de la récupération de données depuis la base de données
      await axios.get("http://localhost:3000/movies")
          .then(async response => {
            this.movies = response.data;


            // Pour chaque film, récupérer l'image principale
            for (let i = 0; i < this.movies.length; i++) {
              console.log("Appel d'url : " + "http://localhost:3000/movies/main_image/" + this.movies[i].id)
              axios.get("http://localhost:3000/movies/main_image/" + this.movies[i].id)
                  .then(response => {
                    this.movies[i].main_image_url = response.data.image_url;
                  })
                  .catch(error => {
                    console.log(error.response.data);
                  });

            this.movies[i].release_date = moment(this.movies[i].release_date).format('YYYY-MM-DD');
              this.movies[i].images = await this.fetchMovieImages(this.movies[i].id);


            }

          })
          .catch(error => {
            console.log(error);
          });
    },
    async fetchMovieImages(movieId) {
      try {
        const response = await axios.get(`http://localhost:3000/movies/images/${movieId}`);
        return response.data; // Supposons que l'API renvoie un tableau d'URLs d'images
      } catch (error) {
        console.error(error);
      }
    },
    addMovie(movieData) {
      // Logique pour ajouter un film
    },
    selectMovie(movie) {
      this.selectedMovie = movie;
      this.showEditMovieForm = true;
    },
    clearSelectedMovie() {
      this.selectedMovie = null;
      this.showEditMovieForm = false;
    },
    async disableMovie(movieId) {
      const url = `http://localhost:3000/movies/deactivated/${movieId}`;
      try {
        await axios.patch(url);
        alert(`Film désactivé avec succès !`);
        this.fetchMovies(); // Rafraîchir la liste des films après la désactivation
      } catch (error) {
        console.log("Status d'erreur de la réponse :", error.response.status);
        console.log("Message d'erreur de la réponse :", error.response.data);
        console.error(`Erreur lors de la désactivation du film :`, error);
      }
    },
    async enableMovie(movieId) {
      const url = `http://localhost:3000/movies/activated/${movieId}`;

      try {
        await axios.patch(url);
        alert(`Film activé avec succès !`);
        this.fetchMovies(); // Rafraîchir la liste des films après l'activation
      } catch (error) {
        console.log("Status d'erreur de la réponse :", error.response.status);
        console.log("Message d'erreur de la réponse :", error.response.data);
        console.error(`Erreur lors de l'activation du film :`, error);
      }
    },
    async deleteMovie(movieId) {
      const url = `http://localhost:3000/movies/${movieId}`;

      try {
        await axios.delete(url);
        alert(`Film supprimé avec succès !`);
        this.fetchMovies(); // Rafraîchir la liste des films après la suppression
      } catch (error) {
        console.log("Status d'erreur de la réponse :", error.response.status);
        console.log("Message d'erreur de la réponse :", error.response.data);
        console.error(`Erreur lors de la suppression du film :`, error);
      }
    },
    showImage(movieId) {
      this.showImageManagement = movieId;
    },
    showComments(movieId) {
      // Logique pour afficher les commentaires
    },


  }
};
</script>

<style scoped>
h1 {
  text-align: center;
  color: #007bff;
}

.movie-card {
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 20px;
  margin-bottom: 20px;
  background-color: #f9f9f9;
}

.movie-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 250px;
}

.movie-image-wrapper {
  height: 100%;
}

.movie-info {
  flex: 1;
}

.movie-actions {
  display: flex;
  flex-direction: column;
}

button {
  margin: 10px 0;
  padding: 10px 20px;
  background-color: #3498db;
  text-align: center;
  border: none;
  border-radius: 4px;
  color: white;
  cursor: pointer;
}

button:hover {
  background-color: #2980b9;
}

.movie-image {
  height: 100%;
  object-fit: cover;
  margin-right: 20px;
}

.movie-edit-form {
  margin-top: 20px;
}

</style>
