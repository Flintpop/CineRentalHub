<template>
  <div class="movie-form-wrapper">
    <form @submit.prevent="submitForm" class="movie-form">
      <button class="close-button" @click="closeForm">X</button>

<!--      <div class="left-section">-->
<!--        &lt;!&ndash; On pourrait avoir une zone de drop ou un bouton pour charger une image &ndash;&gt;-->
<!--        <label for="image-upload" class="image-upload-label">-->
<!--          <img v-if="movie.main_image_url" :src="movie.main_image_url" alt="Image principale du film" class="main-image-preview">-->
<!--          <div v-else class="image-upload-placeholder">Charger une image</div>-->
<!--        </label>-->
<!--        <input type="file" id="image-upload" @change="handleImageUpload" style="display: none;">-->
<!--      </div>-->

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
          <div class="form-group">
            <label>Disponibilité</label>
            <div>
              <input type="radio" id="available-true" value="true" v-model="movie.available">
              <label for="available-true">Actif</label>
            </div>
            <div>
              <input type="radio" id="available-false" value="false" v-model="movie.available">
              <label for="available-false">Non actif</label>
            </div>
          </div>

        </div>
        <button type="submit" class="submit-button">Ajouter le film</button>
      </div>
    </form>
  </div>
</template>
<script>
import axios from "axios";
import moment from "moment/moment.js";

export default {
  data() {
    return {
      movie: {
        available: '',
        title: '',
        release_date: '',
        daily_rental_price: null,
        purchase_price: null,
        description: '',
        link: '',
      }
    };
  },
  methods: {
    handleImageUpload(event) {
      const file = event.target.files[0];
      this.movie.main_image_url = URL.createObjectURL(file);
    },
    submitForm() {
      this.movie.release_date = moment(this.movie.release_date).format('MMM DD, YYYY');
      this.$emit('submit', this.movie);
    },
    resetForm() {
      this.movie = {
        title: '',
        release_date: '',
        daily_rental_price: null,
        purchase_price: null,
        description: '',
        link: '',
        main_image_url: ''
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
