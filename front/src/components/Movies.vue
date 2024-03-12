<template>
  <div>
    <ul>
      <li v-for="movie in movies" :key="movie.id">
        <h2>{{ movie.title }}</h2>
        <p>Date de sortie: {{ movie.release_date }}</p>
        <p>Prix d'achat: {{ movie.purchase_price }}</p>
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'ListeLieux',
  data() {
    return {
      movies: [],
    };
  },
  created() {
    this.fetchLieux();
  },
  methods: {
    fetchLieux() {
      axios.get('http://localhost:8082/movies')
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
