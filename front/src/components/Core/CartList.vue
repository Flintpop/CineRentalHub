<template>
  <div>
    <section v-if="rentMovies.length" class="role-section">
      <h1 class="role-title">Films à louer</h1>
      <div class="movie-card" v-for="movie in rentMovies" :key="movie.id">
        <div class="movie-info">
          <h3>ID du film : {{ movie.movie_id }}</h3>
          <p>Durée de location : {{ movie.rental_duration }} jours</p>
          <div class="movie-actions">
            <button @click.stop="removeFromCart(movie.id)">Supprimer</button>
          </div>
        </div>
      </div>
    </section>

    <p v-else>Aucun film à louer dans le panier.</p>

    <section v-if="buyMovies.length" class="role-section">
      <h1 class="role-title">Films à acheter</h1>
      <div class="movie-card" v-for="movie in buyMovies" :key="movie.id">
        <div class="movie-info">
          <h3>ID du film : {{ movie.movie_id }}</h3>
          <div class="movie-actions">
            <button @click.stop="removeFromCart(movie.id)">Supprimer</button>
          </div>
        </div>
      </div>
    </section>

    <p v-else>Aucun film à acheter dans le panier.</p>
  </div>
</template>

<script>
export default {
  name: 'CartList',
  props: {
    moviesCart: {
      type: Array,
      default: () => []
    },
  },
  data() {
    return {
      rentMovies: [],
      buyMovies: []
    };
  },
  methods: {
    removeFromCart(movieId) {
      this.$emit('remove-from-cart', movieId);
    }
  },
  watch: {
    moviesCart: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.rentMovies = newVal.filter(movie => movie.cart_type === 'rental');
          this.buyMovies = newVal.filter(movie => movie.cart_type === 'purchase');
        }
      }
    }
  },
  mounted() {
    this.rentMovies = this.moviesCart.filter(movie => movie.cart_type === 'rental');
    this.buyMovies = this.moviesCart.filter(movie => movie.cart_type === 'purchase');
    console.log('rentMovies:', this.rentMovies);
    console.log('buyMovies:', this.buyMovies);
  }
};
</script>

<style scoped>
.role-section {
  margin-bottom: 20px;
}

.role-title {
  margin-bottom: 10px;
}

.movie-card {
  background: #f9f9f9;
  padding: 10px;
  border-radius: 5px;
  margin-bottom: 10px;
}

.movie-info h3 {
  margin: 0;
  font-size: 16px;
}

.movie-actions {
  margin-top: 10px;
}

.movie-actions button {
  margin-right: 10px;
  padding: 5px 10px;
  cursor: pointer;
}
</style>