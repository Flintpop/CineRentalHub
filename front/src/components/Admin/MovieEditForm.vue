<template>
  <div class="movie-form-wrapper">
    <form @submit.prevent="submitForm" class="movie-form">
      <button class="close-button" @click="closeForm">X</button>

      <div class="left-section">
        <img :src="movie.main_image_url" alt="Image principale du film" class="main-image-preview">

      </div>

      <div class="right-section">
        <div class="text-fields">
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
          <div class="form-group full-width">
            <label for="description">Description</label>
            <textarea id="description" v-model="movie.description" required></textarea>
          </div>
          <div class="form-group">
            <label for="link">Lien du film</label>
            <input type="text" id="link" v-model="movie.link">
          </div>

        </div>
        <button type="submit" class="submit-button">Modifier le film</button>
      </div>
    </form>
  </div>
</template>


<script>
import axios from "axios";
import moment from 'moment';

export default {
  props: {
    movieId: {
      type: String,
      required: false,
      default: null
    },
    movie: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      selectedImage: '',
      images: [] // Vous devez remplir ce tableau avec les images de la base de données
    };
  },
  methods: {
    async submitForm() {
      // Créez une nouvelle instance de l'objet movie sans main_image_url
      const movieData = {
        ...this.movie,
      };
      movieData.release_date = moment(movieData.release_date).format('MMM DD, YYYY');

      delete movieData.main_image_url; // Supprimez la propriété main_image_url


      console.log("Body de la requête : ", movieData);
      const url = `http://localhost:3000/movies/${this.movie.id}`;
      const token = localStorage.getItem('token');
      const headers = {
        'Content-Type': 'application/json',
        'authorization': 'Bearer ' + token
      };

      try {

        await axios.put(url, movieData, {headers});
        alert(`Film modifié avec succès !`);
        this.resetForm();
        this.$emit('movie-added');
      } catch (error) {
        console.log("Status d'erreur de la réponse :", error.response.status);
        console.log("Message d'erreur de la réponse :", error.response.data);
        console.error(`Erreur lors de la modification du film :`, error);
      }
    },


    updateImage() {
      this.movie.main_image_url = this.selectedImage;
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
    },
    closeForm() {
      this.$emit('close');
    },
  }
};
</script>

<style scoped>
.movie-form-wrapper {
  position: relative;
  max-width: 800px;
  margin: auto;
  padding: 20px;
  background: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.movie-form {
  display: flex;
  gap: 20px;
}

.left-section, .right-section {
  flex: 1;
}

.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: #ff6347;
  border: none;
  color: white;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 5px;
}

.main-image-preview {
  width: 100%;
  height: auto;
  margin-bottom: 10px;
}

.text-fields {
  display: grid;
  grid-template-columns: repeat(2, 1fr); /* Continuez à utiliser deux colonnes */
  gap: 20px; /* Réglez l'écart comme vous le souhaitez */
}

.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 10px;
}
.form-group textarea#description {
  grid-column: 1 / -1; /* Faites s'étendre la textarea sur toutes les colonnes */
  min-height: 150px; /* Réglez la hauteur comme vous le souhaitez */
}


.form-group.full-width {
  grid-column: 1 / span 2; /* Faites s'étendre le form-group sur deux colonnes */
}
.submit-button {
  margin-top: auto; /* Push the button to the bottom */
  padding: 10px 20px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.radio-buttons {
  display: flex;
  align-items: center;
  gap: 10px; /* Espace entre les boutons radio et leurs labels */
}

.radio-buttons label {
  margin-right: 20px; /* Espace après le label avant le prochain bouton radio */
}
</style>

