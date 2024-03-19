<template>
  <div>
    <!-- Section pour les films loués -->
    <section v-if="filteredRentMovies.length" class="role-section">
      <h1 class="role-title">Films Loués</h1>
      <div class="movie-card" v-for="movie in filteredRentMovies" :key="movie.id">
        <div class="movie-info">
          <h2>{{ movie.title }}</h2>
          <p>Prix de location : {{ priceOfRent(movie) }} €</p>
          <p>Date de location : du {{ movie.rental_date  }} au {{ movie.return_date  }}</p>
        </div>
      </div>
    </section>
    <p v-else>Aucun film actuellement loué.</p>

    <!-- Section pour les films achetés -->
    <section v-if="buyMovies.length" class="role-section">
      <h1 class="role-title">Films Achetés</h1>
      <div class="movie-card" v-for="movie in buyMovies" :key="movie.id">
        <div class="movie-info">
          <h3>{{ movie.title }}</h3>
          <p>ID du film : {{ movie.id }}</p>
          <p>Prix d'achat : {{ movie.purchase_price }} €</p>
          <p>Date d'achat : {{ movie.purchase_date | formatDate }}</p>
        </div>
      </div>
    </section>
    <p v-else>Aucun film acheté.</p>
  </div>
</template>

<script>
export default {
  name: 'CartList',
  props: {
    rentMovies: Array,
    buyMovies: Array,
  },
  data(){
    return {
    }
  },

  methods:{
    priceOfRent(movie){
      const dateDeLocation = new Date(movie.rental_date);
      const dateDeRetour = new Date(movie.return_date);
      const difference = dateDeRetour.getTime() - dateDeLocation.getTime();
      const jours = difference / (1000 * 3600 * 24);
      return jours * movie.daily_rental_price;
    }

  },
  computed: {
    filteredRentMovies() {
      const currentDate = new Date();
      return this.rentMovies.filter(movie => {
        const returnDate = new Date(movie.return_date);
        return returnDate >= currentDate;
      });
    }
  },

};
</script>

<style scoped>
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
  margin-bottom: 15px;
}

.movie-card {
  background: #f9f9f9;
  padding: 20px;
  border-radius: 5px;
  margin-bottom: 10px;
  transition: transform 0.2s, box-shadow 0.2s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.movie-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.movie-info h2, h3 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 20px;
}

.movie-info p {
  color: #666;
  margin: 5px 0;
  font-size: 16px;
}
</style>
