<template>
  <div class="movie-card-grid">
    <div class="movie-card" v-for="film in films" :key="film.id">
      <img :src="film.link" :alt="film.title" class="movie-image"/>
      <div class="movie-info">
        <h3>{{ film.title }}</h3>
        <p>{{ film.release_date | formatDate }}</p>
        <p>{{ film.description }}</p>
        <p>Rental Price: {{ film.daily_rental_price }} €</p>
        <p>Purchase Price: {{ film.purchase_price }} €</p>
        <button @click="showMoreInfo(film)" class="info-button">Plus d'informations</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MovieCardGrid',
  props: {
    films: Array
  },
  methods: {
    showMoreInfo(film) {
      alert(`Plus d'infos sur : ${film.title}`);
      // Ici, vous pouvez rediriger l'utilisateur ou ouvrir un modal avec plus d'informations
    },
  },
  filters: {
    formatDate(value) {
      if (value) {
        return new Date(value).toLocaleDateString();
      }
    },
  },
};
</script>




<style scoped>
.movie-card-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr); /* Affiche 4 cartes par ligne */
  gap: 20px;
  padding: 20px;
}

.movie-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.3s; /* Ajoute une transition pour un effet au survol */
}

.movie-card:hover {
  transform: scale(1.05); /* Effet de zoom au survol */
}

.movie-image {
  width: 100%;
  height: 200px; /* Hauteur fixe pour toutes les images */
  object-fit: cover; /* Assure que l'image couvre bien l'espace sans être déformée */
}

.movie-info {
  padding: 15px;
  text-align: center;
  display: flex;
  flex-direction: column;
  flex-grow: 1; /* Permet aux informations de prendre tout l'espace disponible */
}

.movie-info > * {
  margin-bottom: 10px; /* Espacement entre les éléments dans .movie-info */
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
