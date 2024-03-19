<template>
  <div class="movies-list">
    <div
        class="movie-card"
        v-for="movie in movies"
        :key="movie.id"
        @click="goToMovieDetails(movie.id)"
    >
      <div class="movie-image-container">
        <img :src="movie.main_image_url" :alt="movie.title" class="movie-image"/>
        <div class="movie-details-overlay">
          <div class="movie-details-content">
            <h3 class="movie-title">{{ movie.title }}</h3>
            <div class="movie-meta">
              <span class="movie-year">{{ movie.release_date }}</span>
            </div>
            <p class="movie-description">{{ movie.description }}</p>
            <button class="movie-detail-button" @click.stop="$emit('movie-detail', movie.id)">+ d'information</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import axios from "axios";

export default {
  name: 'ListMovies',
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
        console.log("Movies is undefined or empty");
        return;
      }
      await Promise.all(this.movies.map((movie, i) => {
        return axios.get(`http://localhost:3000/movies/main_image/${movie.id}`)
            .then(response => {
              // console.log("URL de l'image principale du film", response.data.image_url);
              this.movies[i].main_image_url = response.data.image_url;
            })
            .catch(error => {
              console.error(error.response.data);
              console.error(error.response);
            });
      }));
      console.log(this.movies); // Log après mise à jour des images
    },
    goToMovieDetails(movieId) {
      // Utilisez Vue Router pour naviguer programmatically
      $emit('movie-detail', movieId)
    }
  }
}
</script>

<style scoped>
.movies-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 20px;
}

.movie-card {
  flex: 0 0 calc(25% - 20px);
  border-radius: 10px;
  overflow: hidden;
  position: relative;
  cursor: pointer;
  transition: all 0.3s ease;
  background-color: #000; /* Fond noir pour les images avec des formats différents */
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.movie-image-container {
  position: relative;
  width: 100%;
  padding-top: 150%; /* Ratio 2:3 pour les images */
  background: #000; /* Fond noir pour les images avec des formats différents */
}

.movie-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover; /* Garantit que l'image couvre le conteneur sans être déformée */
  transition: transform 0.3s ease;
}

.movie-details-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.8);
  color: white;
  display: flex;
  flex-direction: column;
  justify-content: flex-end; /* Aligner le contenu au bas de la carte */
  opacity: 0;
  visibility: hidden;
  transition: opacity 0.3s ease;

}

.movie-card:hover .movie-details-overlay {
  opacity: 1;
  visibility: visible;
}
.movie-card:hover .movie-details-content{
transform: translateY(0);
}
.movie-card:hover  {
  transform: scale(1.05);
  box-shadow: 0 6px 12px rgba(0,0,0,0.5);
}

.movie-details-header {
  display: flex;
  justify-content: flex-end;
  padding: 10px;
}

.movie-icon {
  /* Icônes pour l'achat et la location */
  background: rgba(255, 255, 255, 0.8);
  padding: 5px;
  border-radius: 50%;
  font-size: 24px; /* Ajustez la taille au besoin */
  margin-left: 5px;
}

.movie-details-content {
  padding: 10px;
  text-align: center;
  transform: translateY(20%);
  transition: transform 0.3s ease;
}

.movie-title {
  font-size: 1.5rem;
  margin-bottom: 0.5rem;
}

.movie-meta {
  font-size: 0.875rem;
  margin-bottom: 1rem;
}

.movie-description {
  display: block; /* Toujours afficher la description */
  opacity: 0; /* Commencer transparent */
  transition: opacity 0.3s ease;
}

.movie-card:hover .movie-description {
  display: block; /* Affiche la description au survol */
  opacity: 1;
}

.movie-detail-button {
  background-color: #007bff;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  margin-top: 10px;
  cursor: pointer;
  box-shadow: 0 2px 4px rgba(0,0,0,0.2);
  opacity: 0;
  transition: opacity 0.3s ease;
}
.movie-card:hover .movie-detail-button {
  opacity: 1;
}

.movie-card:hover .movie-edit-button {
  display: block;
}


</style>
