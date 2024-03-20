<template>
  <div>
    <div v-if="moviesCart.length">

      <div v-if="rentalMovies.length">
        <h2>Films à louer</h2>
        <div v-for="movie in rentalMovies" :key="movie.id" class="cart-item">
          <p>Film ID: {{ movie.movie_id }}</p>
          <p>Durée de location: {{ movie.rental_duration }} jours</p>
          <button @click="removeFromCart(movie.id)">Supprimer</button>
          <button v-if="editingMovieId !== movie.id" class="edit-btn" @click="editRentalDuration(movie)">Modifier la durée</button>
          <!-- Champ pour saisir la nouvelle durée si le film est en mode d'édition -->
          <div v-if="editingMovieId === movie.id">
            <input type="number" v-model="newRentalDuration" min="1">
            <button @click="updateRentalDuration(movie)">Valider</button>
            <button @click="cancelEdit">Annuler</button>
          </div>
        </div>
      </div>

      <div v-if="purchaseMovies.length">
        <h2>Films à acheter</h2>
        <div v-for="movie in purchaseMovies" :key="movie.id" class="cart-item">
          <p>Film ID: {{ movie.movie_id }}</p>
          <button @click="removeFromCart(movie.id)">Supprimer</button>
        </div>
      </div>

      <button v-if="moviesCart.length" @click="validateCart" class="validate-cart-btn">Valider le panier</button>
      <button v-if="moviesCart.length" @click="clearCart" class="clear-cart-btn">Vider le panier</button>
    </div>
    <div v-else class="empty-cart-message">
      <p>Votre panier est vide.</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CartList',
  props: {
    moviesCart: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      editingMovieId: null,
      newRentalDuration: '',
    };
  },
  computed: {
    rentalMovies() {
      return this.moviesCart.filter(movie => movie.cart_type === 'rental');
    },
    purchaseMovies() {
      return this.moviesCart.filter(movie => movie.cart_type === 'purchase');
    },
  },
  methods: {
    removeFromCart(itemId) {
      this.$emit('removeFromCart', itemId);
    },
    validateCart() {
      this.$emit('validateCart');
    },
    clearCart() {
      this.$emit('clearCart');
    },
    editRentalDuration(movie) {
      this.editingMovieId = movie.id;
      this.newRentalDuration = movie.rental_duration.toString(); // Convertir en chaîne pour le champ input
    },
    updateRentalDuration(movie) {
      if (this.newRentalDuration && this.newRentalDuration > 0) {
        this.$emit('updateRentalDuration', {
          movieId: movie.id,
          newRentalDuration: parseInt(this.newRentalDuration, 10) // Convertir en nombre
        });
        this.editingMovieId = null;
        this.newRentalDuration = '';
      }
    },
    cancelEdit() {
      this.editingMovieId = null;
      this.newRentalDuration = '';
    },
  },
};
</script>

<style scoped>
h2 {
  color: #333;
  margin-bottom: 20px;
  font-size: 24px;
  text-align: center;
}

.cart-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f9f9f9;
  border: 1px solid #e1e1e1;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.cart-item:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.cart-item p {
  margin: 0;
  color: #333;
  font-size: 16px;
}

button {
  cursor: pointer;
  padding: 8px 15px;
  border: none;
  border-radius: 4px;
  transition: background-color 0.3s;
  font-weight: bold;
  text-transform: uppercase;
}

.validate-cart-btn, .clear-cart-btn {
  width: 100%;
  color: white;
  margin-top: 20px;
}

.validate-cart-btn {
  background-color: #2ecc71;
}

.validate-cart-btn:hover {
  background-color: #27ae60;
}

.clear-cart-btn {
  background-color: #e74c3c;
}

.clear-cart-btn:hover {
  background-color: #c0392b;
}

.empty-cart-message {
  text-align: center;
  color: #777;
  font-size: 18px;
  margin-top: 20px;
}
</style>


