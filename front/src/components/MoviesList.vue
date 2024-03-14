<template>
  <div class="movie-card-grid">
    <router-link v-for="movie in movies" :key="movie.id" :to="'MovieDetails'" class="movie-card">
      <img :src="movie.main_image_url" :alt="movie.title" class="movie-image"/>
    </router-link>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: 'MovieCardGrid',
  props: {
    movies: Array
  },
  watch: {
    // Surveiller les changements sur la prop 'movies'
    movies: {
      immediate: true, // Exécuter la fonction dès l'initialisation du composant
      handler: "fetchMainImages", // Nom de la méthode à exécuter
    }
  },
  methods: {
    async fetchMainImages() {
      if (!this.movies || this.movies.length === 0) {
        console.error("Movies is undefined or empty");
        return;
      }
      await Promise.all(this.movies.map((movie, i) => {
        return axios.get(`http://localhost:3000/movies/main_image/${movie.id}`)
            .then(response => {
              // this.$set(this.movies, i, {...movie, main_image_url: response.data.image_url});
              console.log("URL de l'image principale du film", response.data.image_url);
              this.movies[i].main_image_url = response.data.image_url;
            })
            .catch(error => {
              console.error(error.response.data);
              console.error(error.response);
            });
      }));
      console.log(this.movies); // Log après mise à jour des images
    },
  }
};
</script>


<style scoped>
.movie-card-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  padding: 20px;
}

.movie-card {

  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.4);
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.3s ease, filter 0.3s ease; /* Mise à jour pour inclure la transition du filtre */
  transform: scale(1); /* Assure que la propriété transform est animable */
  filter: brightness(65%); /* Assombrir légèrement par défaut */
}

.movie-card:hover {
  transform: scale(1.05); /* Effet de zoom au survol */
  filter: brightness(100%); /* Rend la carte totalement claire au survol */
}

.movie-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.movie-info {
  padding: 15px;
  text-align: center;
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

.movie-info > * {
  margin-bottom: 10px;
}

h3 {
  margin: 10px 0;
}

.info-button {
  padding: 10px 20px;
  margin-top: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.info-button:hover {
  background-color: #0056b3;
}
</style>
