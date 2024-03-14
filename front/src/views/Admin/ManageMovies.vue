<template>
  <NavbarAdmin/>
  <div class="main-content">
    <h1>Gestion des films</h1>

    <div class="movie-card" v-for="movie in movies" :key="movie.id">
      <div class="movie-image-wrapper">
        <img :src="movie.main_image_url" alt="Image du film" class="movie-image">
      </div>
      <div class="movie-info">
        <h2>{{ movie.title }}</h2>
        <p>{{ movie.description }}</p>
        <p>Date de sortie : {{ movie.release_date }}</p>
        <p>Prix de location quotidien : {{ movie.dayly_rental_price }}</p>
        <p>Prix d'achat : {{ movie.purchase_price }}</p>
        <p>Disponible : {{ movie.available }}</p>
        <p><a :href="movie.link">Lien de la vidéo</a></p>
      </div>
      <div class="movie-actions">
        <button @click="editMovie(movie)">Modifier</button>
        <button @click="deleteMovie(movie.id)">Supprimer</button>
      </div>
    </div>
  </div>
  <Footer/>
</template>

<script>
import NavbarAdmin from "../../components/Admin/NavbarAdmin.vue";
import Footer from "../../components/Core/Footer.vue";

export default {
  components: {Footer, NavbarAdmin},
  data() {
    return {
      showAddMovieForm: false,
      movies: [
        {
          id: 1,
          available: true,
          title: "Le Parrain",
          release_date: "1972-03-24",
          dayly_rental_price: 2.99,
          purchase_price: 9.99,
          description: "Le patriarche d'une famille mafieuse de New York, âgé et malade, est soutenu par son fils cadet. Celui-ci doit prendre la relève du père et régner sur le crime organisé.",
          link: "https://www.youtube.com/watch?v=bmtuIhesQWA",
          main_image_url: "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcQyO8trmFbGTJIY6MaIFqzfPB6hW8UFCAYxTqtdKWGz7EJ6Jt-d"
        },
        {
          id: 2,
          available: true,
          title: "Le Parrain 2",
          release_date: "1974-12-18",
          dayly_rental_price: 2.99,
          purchase_price: 9.99,
          description: "Michael Corleone tente de se réhabiliter socialement et de légaliser les activités de sa famille. Mais ses anciens ennemis lui déclarent à nouveau la guerre.",
          link: "https://www.youtube.com/watch?v=9O1Iy9od7-A",
          main_image_url: "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcSm7mhU1VNBbj92y8jqRnTmGEPslNFw-T5nMBswgfis03J2OMRa"
        }

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

.movie-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 20px;
  margin-bottom: 20px;
  height: 250px;

}
.movie-image-wrapper {
  height: 100%;
}

.movie-info {
  flex: 1;
}

.movie-actions {
  display: flex;
  flex-direction: column;
}

button {
  margin: 10px 0;
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
.movie-image {
  height: 100%;
  object-fit: cover;
  margin-right: 20px;
}
.main-content {
  width: 80%;
  margin: 0 auto;
}
</style>
