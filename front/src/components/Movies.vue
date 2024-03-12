<template>
  <div>
    <ul>
      <li v-for="movie in movies" :key="movie.id">
        <h2>{{ movie.title }}</h2>
        <p>Date de sortie: {{ movie.releaseDate }}</p>
        <p>Prix d'achat: {{ movie.dailyRentalPrice }}</p>
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'MovieList',
  data() {
    return {
      movies: [],
    };
  },
  created() {
    this.fetchMovies();
  },
  methods: {
    fetchMovies() {
      axios.get('http://localhost:3000/movies')
          .then(response => {
            this.movies = response.data;
          })
          .catch(error => {
            console.error("Il y a eu un problème avec la requête de l'API :", error);
          });
    },
  }
}
</script>
