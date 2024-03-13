<template>
  <form @submit.prevent="submitForm" class="movie-form">
    <div class="form-group">
      <label for="title">Titre</label>
      <input type="text" id="title" v-model="movie.title" required>
    </div>
    <div class="form-group">
      <label for="release_date">Date de sortie</label>
      <input type="date" id="release_date" v-model="movie.release_date" required>
    </div>
    <div class="form-group">
      <label for="daily_rental_price">Prix de location quotidien</label>
      <input type="number" step="0.01" id="daily_rental_price" v-model="movie.daily_rental_price" required>
    </div>
    <div class="form-group">
      <label for="purchase_price">Prix d'achat</label>
      <input type="number" step="0.01" id="purchase_price" v-model="movie.purchase_price" required>
    </div>
    <div class="form-group">
      <label for="description">Description</label>
      <textarea id="description" v-model="movie.description" required></textarea>
    </div>
    <div class="form-group">
      <label for="link">Lien de l'image</label>
      <input type="text" id="link" v-model="movie.link">
    </div>
    <button type="submit" class="submit-button">Ajouter le film</button>
  </form>
</template>

<script>
import axios from "axios";
import moment from 'moment';

export default {
  movieId: {
    type: String,
    required: false,
    default: null
  },
  data() {
    return {
      movie: {
        title: 'Titre du film',
        release_date: '2023-01-01',
        daily_rental_price: 10.00,
        purchase_price: 20.00,
        description: 'Description du film',
        link: 'https://example.com/image.jpg'
      }
    };
  },
  methods: {
    async submitForm() {
      try {
        this.movie.release_date = moment(this.movie.release_date).format('MMM DD, YYYY');
        console.log("Body de la requête : ", this.movie);
        const url = `http://localhost:3000/movies${this.movieId ? `/${this.movieId}` : ''}`;
        const method = this.movieId ? 'put' : 'post';

        await axios({
          method: method,
          url: url,
          data: this.movie
        });
        alert(`Film ${this.movieId ? 'modifié' : 'ajouté'} avec succès !`);
        this.resetForm();
        this.$emit('movie-added');
      } catch (error) {
        console.error(`Erreur lors de l'${this.movieId ? 'modification' : 'ajout'} du film :`);
        console.error(error.response.data);
        alert(`Erreur lors de l'${this.movieId ? 'modification' : 'ajout'} du film. Veuillez réessayer.`);
      }
    },
    resetForm() {
      this.movie = {
        title: '',
        release_date: '',
        daily_rental_price: null,
        purchase_price: null,
        description: '',
        link: ''
      };
    }
  }
};
</script>

<style scoped>
.movie-form {
  position: relative;
  max-width: 500px;
  margin: auto;
  padding: 20px;
  background: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group input,
.form-group textarea,
.form-group select {
  width: 100%;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
}

button {
  padding: 10px 15px;
  background-color: #5cb85c;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh; /* Adjust this value according to your needs */
}

.movie-form input {
  display: block;
  margin-left: auto;
  margin-right: auto;
}
</style>
