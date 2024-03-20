<template>
  <NavbarUser/>
  <div class="main-content">
    <h1>Mon Panier</h1>
    <CartList :moviesCart="moviesCart" @updateRentalDuration="updateRentalDuration" />
  </div>
</template>

<script>
import NavbarUser from "../components/User/NavbarUser.vue";
import CartList from "../components/Core/CartList.vue";
import axios from "axios";

export default {
  name: 'MyCart',
  components: {NavbarUser, CartList},
  data() {
    return {
      moviesCart: [], // Fusionner les éléments de location et d'achat dans une seule liste
    };
  },
  methods: {
    fetchCartItems() {
      const userId = localStorage.getItem('userId');
      const token = localStorage.getItem('token');

      axios.get(`http://localhost:3000/cart/${userId}`, {
        headers: {Authorization: `Bearer ${token}`}
      }).then(response => {
        // Vérifier si la réponse contient des éléments du panier
        if (response.data && response.data.length > 0) {
          this.moviesCart = response.data;
        } else {
          // Utiliser des données fictives si la réponse est vide
          this.useMockData();
        }
      }).catch(error => {
        console.error('Erreur lors de la récupération des éléments du panier:', error);
        // Utiliser des données fictives en cas d'erreur
        this.useMockData();
      });
    },
    validateCart() {
      const userId = localStorage.getItem('userId');
      const token = localStorage.getItem('token');

      axios.post(`http://localhost:3000/cart/validate/${userId}`, this.moviesCart, {
        headers: {Authorization: `Bearer ${token}`}
      }).then(response => {
        // Handle successful validation
        console.log('Cart validated successfully:', response.data);
      }).catch(error => {
        // Handle error during validation
        console.error('Error during cart validation:', error);
      });
    },
    removeItem(itemId) {
      console.log('Supprimer l\'élément:', itemId);
      // Implémenter la logique de suppression ici
    },
    clearCart() {
      const userId = localStorage.getItem('userId');
      const token = localStorage.getItem('token');

      axios.delete(`http://localhost:3000/cart/delete/${userId}`, {
        headers: {Authorization: `Bearer ${token}`}
      }).then(() => {
        this.moviesCart = [];
        console.log('Panier vidé avec succès');
      }).catch(error => {
        console.error('Erreur lors du vidage du panier:', error);
      });
    },
    updateRentalDuration({ movieId, newRentalDuration }) {
      const movieIndex = this.moviesCart.findIndex(movie => movie.id === movieId);
      if (movieIndex !== -1) {
        this.moviesCart[movieIndex].rental_duration = newRentalDuration;
        // Mettre à jour via API ici...
      }
    },
    useMockData() {
      // Implémenter la logique pour utiliser des données fictives
      this.moviesCart = [
        {id: 1, movie_id: 1, cart_type: 'rental', rental_duration: 3},
        {id: 2, movie_id: 2, cart_type: 'purchase'},
        {id: 3, movie_id: 3, cart_type: 'rental', rental_duration: 7},
      ];
    }
  },
  mounted() {
    this.fetchCartItems();
  }
};
</script>

<style scoped>
.main-content {
  max-width: 800px;
  margin: auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

h1 {
  text-align: center;
  color: #333;
  margin-bottom: 20px;
}

.cart-list {
  margin-top: 20px;
}

.movie-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 15px;
  margin-bottom: 10px;
  background: #f7f7f7;
  border-radius: 5px;
}

.movie-info {
  flex: 1;
}

.movie-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.movie-details {
  margin-top: 5px;
  font-size: 16px;
  color: #666;
}

.action-buttons {
  display: flex;
  align-items: center;
}

button {
  padding: 10px 15px;
  margin-left: 10px;
  border: none;
  border-radius: 5px;
  background-color: #3498db;
  color: white;
  cursor: pointer;
  transition: background-color 0.2s;
}

button:hover {
  background-color: #2980b9;
}

button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>
