<template>
  <div class="main-content">
    <div class="filter-buttons">
      <button @click="setFilter('all')">Tous les films</button>
      <button @click="setFilter('rented')">Films Loués</button>
      <button @click="setFilter('purchased')">Films Achetés</button>
      <button v-if="currentFilter === 'all'" @click="toggleExpiredRentals">
        {{ showExpiredRentals ? 'Masquer' : 'Afficher' }} les locations expirées
      </button>
    </div>

    <!-- Section pour tous les films -->
    <section v-if="currentFilter === 'all'">
      <h1 class="role-title">Tous les Films</h1>
      <div v-if="showAllMovies.length">
        <div class="movie-card" v-for="movie in showAllMovies" :key="movie.id">
          <div class="movie-info">
            <h2>{{ movie.title }}</h2>
            <p v-if="movie.rental_date">
              <strong>Date de location:</strong> {{ movie.rental_date }}
            </p>
            <p v-if="movie.return_date">
              <strong>Date de retour:</strong> {{ movie.return_date }}
            </p>
            <p v-if="movie.daily_rental_price">
              <strong>Prix de location par jour:</strong> {{ movie.daily_rental_price }} €
            </p>
            <p v-if="movie.purchase_price">
              <strong>Prix d'achat:</strong> {{ movie.purchase_price }} €
            </p>
            <p v-if="movie.purchase_date">
              <strong>Date d'achat:</strong> {{ movie.purchase_date }}
            </p>

            <button v-if="isMovieAvailable(movie)" @click="viewMovie(movie)">Voir le film</button>
            <button v-else disabled>Indisponible</button>


          </div>
        </div>
      </div>
      <p v-else>Aucun film disponible.</p>
    </section>

    <!-- Section pour les films loués -->
    <section v-else-if="currentFilter === 'rented'">
      <h1 class="role-title">Films Loués</h1>
      <div v-if="filteredRentMovies.length">
        <div class="movie-card" v-for="movie in filteredRentMovies" :key="movie.id">
          <div class="movie-info">
            <h2>{{ movie.title }}</h2>
            <p>
              <strong>Date de location:</strong> {{ movie.rental_date }}
            </p>
            <p>
              <strong>Date de retour:</strong> {{ movie.return_date }}
            </p>
            <p>
              <strong>Prix de location:</strong> {{ priceOfRent(movie) }} €
            </p>

            <button v-if="isMovieAvailable(movie)" @click="viewMovie(movie)">Voir le film</button>
            <button v-else disabled>Indisponible</button>
          </div>
        </div>
      </div>
      <p v-else>Aucun film loué.</p>

    </section>

    <!-- Section pour les films achetés -->
    <section v-else-if="currentFilter === 'purchased'">
      <h1 class="role-title">Films Achetés</h1>
      <div v-if="localBuyMovies.length">
        <div class="movie-card" v-for="movie in localBuyMovies" :key="movie.id">
          <div class="movie-info">
            <h2>{{ movie.title }}</h2>
            <p>
              <strong>Prix d'achat:</strong> {{ movie.purchase_price }} €
            </p>
            <p>
              <strong>Date d'achat:</strong> {{ movie.purchase_date }}
            </p>

            <button  @click="viewMovie(movie)">Voir le film</button>
          </div>
        </div>
      </div>
      <p v-else>Aucun film acheté.</p>
    </section>
  </div>
</template>

<script>
export default {
  props: {
    rentMovies: Array,
    buyMovies: Array,
  },
  data(){
    return {
      localRentMovies: this.rentMovies,
      localBuyMovies: this.buyMovies,
      currentFilter: 'all',
      showExpiredRentals: true,
    }
  },

  methods:{
    priceOfRent(movie){
      const dateDeLocation = new Date(movie.rental_date);
      const dateDeRetour = new Date(movie.return_date);
      const difference = dateDeRetour.getTime() - dateDeLocation.getTime();
      const jours = difference / (1000 * 3600 * 24);
      return jours * movie.daily_rental_price;
    },
    setFilter(filter) {
      this.currentFilter = filter;
    },
    toggleExpiredRentals() {
      this.showExpiredRentals = !this.showExpiredRentals;
    },
    viewMovie(movie) {
      // Implement the logic to view the movie
      const userId = localStorage.getItem('userId');
      console.log(`Viewing movie: ${movie.title} with movie ID: ${movie.id}`);
      console.log(`User ID: ${userId}`);
      this.$newStats(movie.id, userId)
      console.log(`Viewing movie: ${movie.title}`);
      this.$router.push({ name: 'MovieViewer', params: { movieId: movie.id } });
    },
    isMovieAvailable(movie) {
      if (movie.rental_date) {
        return this.isRentalAvailable(movie);
      } else {
        return true;
      }
    },
    isRentalAvailable(movie) {
      const currentDate = new Date();
      const returnDate = new Date(movie.return_date);
      return returnDate >= currentDate;
    }

  },
  computed: {
    filteredRentMovies() {
      const currentDate = new Date();
      return this.localRentMovies.filter(movie => {
        const returnDate = new Date(movie.return_date);
        return returnDate >= currentDate;
      });
    },
    showAllMovies() {
      let allMovies = [...this.localRentMovies, ...this.localBuyMovies];
      if (!this.showExpiredRentals) {
        allMovies = allMovies.filter(movie => {
          return this.filteredRentMovies.some(filteredMovie => filteredMovie.id === movie.id);
        });
      }
      if (allMovies.length > 0) {
        return allMovies;
      }
      // Film fictif
      return [
        {
          id: 1,
          title: 'Vous n\'avez pas encore de films',
          rental_date: '0000',
          return_date: '0000',
          daily_rental_price: 0,
          purchase_price: 0,
          purchase_date: '0000',
        }
      ]
    }
  },
  created() {


    this.localRentMovies = this.rentMovies;
    this.localBuyMovies = this.buyMovies;
    console.log("films loués local:",this.localRentMovies);
    console.log("films achetés local:",this.localBuyMovies);
  },
  watch: {
    rentMovies(newVal) {
      this.localRentMovies = newVal;
      console.log("Mise à jour des films loués local:", this.localRentMovies);
    },
    buyMovies(newVal) {
      this.localBuyMovies = newVal;
      console.log("Mise à jour des films achetés local:", this.localBuyMovies);
    },
  },

};
</script>

<style scoped>
.main-content {
  max-width: 800px;
  margin: auto;
}

.filter-buttons {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.filter-buttons button {
  margin: 5px;
  padding: 8px 16px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.filter-buttons button:hover {
  background-color: #0056b3;
}

.filter-buttons button:active {
  transform: translateY(2px);
}

.role-section {
  margin-bottom: 30px;
  padding: 15px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.role-title {
  color: #333;
  font-size: 24px;
  margin-bottom: 20px;
  text-align: center;
}

.movie-card {
  background: #f9f9f9;
  padding: 20px;
  border-radius: 5px;
  margin-bottom: 10px;
  transition: transform 0.2s, box-shadow 0.2s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
  align-items: center;
}

.movie-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.movie-info h2 {
  margin: 0;
  color: #333;
  font-size: 20px;
  font-weight: bold;
  text-align: center;
}

.movie-info h3 {
  color: #333;
  font-size: 18px;
  margin: 10px 0;
  text-align: center;
}

.movie-info p {
  color: #666;
  margin: 5px 0;
  font-size: 16px;
  text-align: center;
}

.movie-card button {
  margin-top: 15px;
  padding: 8px 16px;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.movie-card button:hover {
  background-color: #218838;
}

.movie-card button:disabled {
  background-color: #6c757d;
  cursor: not-allowed;
}
</style>

