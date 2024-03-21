<template>
  <NavbarUser/>
  <div class="movie-viewer" v-if="movie">
    <div class="movie-details">
      <h1>{{ movie.title }}</h1>
      <p>{{ movie.description }}</p>
      <p> lien de la vidéo : {{ movie.link }} </p>
      <video width="750" controls>
        <source :src="movie.link" type="video/mp4">
        Votre navigateur ne supporte pas la balise vidéo.
      </video>
    </div>
  </div>
  <div v-else>
    <p>Chargement du film...</p>
  </div>
</template>

<script>
import axios from 'axios';
import NavbarUser from '../../components/User/NavbarUser.vue';


export default {
  components: {
    NavbarUser,
  },
  props: {
    movieId: {
      type: [String, Number],
      required: true
    }
  },
  data() {
    return {
      movie: null
    };
  },
  methods: {
    fetchMovie() {
      const token = localStorage.getItem('token');
      axios.get(`http://localhost:3000/movies/${this.movieId}`, {
        headers: {'Authorization': `Bearer ${token}`}
      })
          .then(response => {
            this.movie = response.data;
            console.log('movie récupéré', this.movie);

          })
          .catch(error => {
            console.error("Erreur lors de la récupération du film:", error);
          });
    }
  },
  mounted() {
    this.fetchMovie();
  }
};
</script>

<style scoped>
.movie-viewer {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 20px;
}

.movie-details {
  text-align: center;
}

.movie-details h1 {
  margin-bottom: 10px;
}

video {
  margin-top: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
}
</style>
