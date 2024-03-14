<template>
  <NavbarAdmin/>
  <div>
    <h1>Gestion des films</h1>

    <button @click="showAddMovieForm = true">Ajouter un film</button>

    <div v-if="showAddMovieForm">
      <h2>Ajouter un film</h2>
      <form @submit.prevent="addMovie">
        <label for="title">Titre:</label>
        <input type="text" id="title" v-model="newMovie.title" required>

        <label for="director">Réalisateur:</label>
        <input type="text" id="director" v-model="newMovie.director" required>

        <label for="year">Année:</label>
        <input type="number" id="year" v-model="newMovie.year" required>

        <button type="submit">Ajouter</button>
      </form>
    </div>

    <div v-for="movie in movies" :key="movie.id">
      <h2>{{ movie.title }}</h2>
      <p>Réalisateur: {{ movie.director }}</p>
      <p>Année: {{ movie.year }}</p>

      <button @click="editMovie(movie)">Modifier</button>
      <button @click="deleteMovie(movie.id)">Supprimer</button>
    </div>
  </div>
</template>

<script>
import NavbarAdmin from "../../components/Admin/NavbarAdmin.vue";

export default {
  components: {NavbarAdmin},
  data() {
    return {
      showAddMovieForm: false,
      newMovie: {
        title: '',
        director: '',
        year: null
      },
      movies: [
        // Liste des films
      ]
    };
  },
  methods: {
    addMovie() {
      // Logique pour ajouter un film
    },
    editMovie(movie) {
      // Logique pour modifier un film
    },
    deleteMovie(movieId) {
      // Logique pour supprimer un film
    }
  }
};
</script>

<style scoped>
h1 {
  text-align: center;
  color: #007bff;
}

button {
  display: block;
  margin: 10px auto;
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

form {
  margin: 20px auto;
  width: 80%;
  max-width: 500px;
}

form label {
  display: block;
  margin-top: 10px;
}

form input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-bottom: 10px;
}
</style>
