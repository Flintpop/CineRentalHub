<template>
  <Navbar></Navbar>
  <div class="movie-detail-container">
    <h1>{{ this.movie.title }}</h1>
    <!-- Carrousel conditionnel -->
    <div v-if="images.length > 1" class="carousel-container">
      <Carousel :images="images"/>
    </div>
    <div v-else>
      <img :src="images[0].image_url" alt="Image principale du film"/>
    </div>
    <p>Description : {{ this.movie.description }}</p>
    <p>Date de sortie : {{ this.movie.release_date }}</p>
    <p>Prix d'achat : {{ this.movie.purchase_price }} </p>
    <p>Prix de location par jour : {{ this.movie.daily_rental_price }} </p>
    <button v-if="!isPurchased" @click="purchaseMovie">Acheter</button>
    <button v-else-if="isPurchased" disabled>Déjà acheté</button>

    <!-- Bouton Louer s'affiche si l'utilisateur est connecté, le film n'est pas déjà loué ou acheté -->
    <button v-if="!isPurchased && !isRented" @click="rentMovie">Louer</button>
  </div>
</template>

<script>
import axios from 'axios';
import Carousel from '../components/Core/Carousel.vue';
import Navbar from "../components/NoConnected/Navbar.vue"; // Assurez-vous d'importer le composant Carousel

export default {
  name: 'MovieDetailPage',
  components: {
    Navbar,
    Carousel,
  },
  data() {
    return {
      movie: {},
      images: [],
      isPurchased: false,
      isRented: false,

    };
  },
  async created() {
    await this.fetchMovieDetails();
    await this.fetchMovieImages();
    await this.checkPurchaseStatus();
    await this.checkRentalStatus();
  },
  methods: {
    // TODO: Changer les appels pour qu'ils correspondent à l'API pour savoir s'il a déjà acheté ou loué le film
    async fetchMovieDetails() {
      // Remplacez 'movieId' par l'ID réel du film obtenu dynamiquement
      const movieId = this.$route.params.movieId;
      console.log("L'id du movie " + movieId);
      try {
        const response = await axios.get(`http://localhost:3000/movies/${movieId}`);
        this.movie = response.data;
        console.log("Requête : \n " + +"\nMovie : \n" + JSON.stringify(response, null, 2));
      } catch (error) {
        console.error(error.response);
      }
    },
    async fetchMovieImages() {
      const movieId = this.$route.params.movieId;
      try {
        const response = await axios.get(`http://localhost:3000/movies/images/${movieId}`);
        this.images = response.data; // Supposons que l'API renvoie un objet avec une propriété 'images' qui est un tableau d'URLs
      } catch (error) {
        console.error(error);
      }
    },
    async checkPurchaseStatus() {
      if (!this.isUserLoggedIn()) {
        return; // Ne fait rien si l'utilisateur n'est pas connecté
      }
      // const movieId = this.$route.params.movieId;
      // try {
      //   const response = await axios.get(``);
      //   this.isPurchased = response.data.isPurchased;
      // } catch (error) {
      //   console.error(error);
      // }
    },

    async checkRentalStatus() {
      if (!this.isUserLoggedIn()) {
        return; // Ne fait rien si l'utilisateur n'est pas connecté
      }
      // const movieId = this.$route.params.movieId;
      // try {
      //   const response = await axios.get(`http://localhost:3000/movies/${movieId}/isRented`);
      //   this.isRented = response.data.isRented;
      // } catch (error) {
      //   console.error(error);
      // }
    },

    isUserLoggedIn() {
      // Exemple de vérification de l'état de connexion basé sur un token JWT stocké localement
      return !!localStorage.getItem('jwtToken');
    },
  },
};
</script>

<style scoped>
.movie-detail-container {
  max-width: 800px;
  margin: 40px auto;
  padding: 20px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.4);
  border-radius: 8px;
  overflow: hidden;
}

h1 {
  color: #007bff;
  text-align: center;
  margin-bottom: 20px;
}

.carousel-container {
  margin: 20px 0;
}

img {
  max-width: 100%;
  border-radius: 8px;
}

p {
  color: #333;
  line-height: 1.5;
  margin: 10px 0;
}

p span {
  font-weight: bold;
}

/* Adaptation du style des boutons et des liens si nécessaire */
button, a {
  display: inline-block;
  padding: 10px 20px;
  margin-top: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover, a:hover {
  background-color: #0056b3;
}

/* Style spécifique pour le carrousel, si nécessaire */
.carousel-slide {
  text-align: center;
}

.carousel-slide img {
  max-height: 400px; /* Ajustez selon vos besoins */
  margin-bottom: 20px;
}
</style>
