<template>
  <div class="bought-movies">
    <h3>Films acheté par {{ user.last_name }} {{ user.first_name }}</h3>
    <ul>
      <li v-for="movie in rentedMovies" :key="movie.id">
        {{ movie.title }} - Acheté le : {{ movie.purchase_date }}
      </li>
    </ul>
    <button @click="$emit('close')">Fermer</button>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  props: {
    user: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      rentedMovies: [],
    };
  },
  methods: {
    async fetchBoughtMovies() {
      try {
        const token = localStorage.getItem('token');
        const headers = {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        };
        console.log('Récupération des films achetés par l\'utilisateur', this.user.id);
        const response = await axios.get(`http://localhost:3000/movies/purchases/${this.user.id}`, { headers });
        this.rentedMovies = response.data;
      } catch (error) {
        console.error('Erreur lors de la récupération des films achetés:', error);
      }
    },
  },
  mounted() {
    this.fetchBoughtMovies();
  },
};
</script>

<style scoped>
.bought-movies {
  background: #f9f9f9;
  border-radius: 5px;
  padding: 15px;
  margin: 10px 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.bought-movies h3 {
  margin-bottom: 15px;
  color: #333;
}

.bought-movies ul {
  list-style-type: none;
  padding: 0;
}

.bought-movies li {
  padding: 8px 0;
  border-bottom: 1px solid #ddd;
}

.bought-movies li:last-child {
  border-bottom: none;
}

.bought-movies button {
  cursor: pointer;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  background-color: #007bff;
  color: white;
  transition: background-color 0.3s;
  margin-top: 15px;
}

.bought-movies button:hover {
  background-color: #0056b3;
}
</style>

