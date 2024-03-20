<template>
  <NavbarUser v-if="isUserConnected"/>
  <Navbar v-else/>
  <div class="main-content">
    <h1>Mon Panier</h1>
    <CartList :moviesCart="moviesCart" @updateRentalDuration="updateRentalDuration" @removeFromCart="removeItem"
              @validateCart="validateCart" @clearCart="clearCart"/>
  </div>
</template>

<script>
import NavbarUser from "../components/User/NavbarUser.vue";
import CartList from "../components/Core/CartList.vue";
import Navbar from "../components/NoConnected/Navbar.vue";
import axios from "axios";
import {jwtDecode} from "jwt-decode";

export default {
  name: 'MyCart',
  components: {NavbarUser, CartList, Navbar},
  data() {
    return {
      moviesCart: [], // Fusionner les éléments de location et d'achat dans une seule liste
    };
  },
  computed: {
    isUserConnected() {
      const token = localStorage.getItem('token');
      try {
        const decoded = jwtDecode(token);
        return !!decoded && decoded.role === 'user';
      } catch {
        return false;
      }
    },
  },
  methods: {

    fetchCartItems() {
      const userId = localStorage.getItem('userId');
      const token = localStorage.getItem('token');

      // Si l'utilisateur est connecté, récupérer le panier du serveur
      if (this.isUserConnected) {
        axios.get(`http://localhost:3000/cart/${userId}`, {
          headers: {Authorization: `Bearer ${token}`}
        }).then(response => {
          // Si des éléments existent dans le panier serveur, les utiliser
          if (response.data && response.data.length > 0) {
            this.moviesCart = response.data;
          } else {
            // Sinon, utiliser le panier local s'il existe
            this.loadLocalCart();
          }
        }).catch(error => {
          console.error('Erreur lors de la récupération des éléments du panier:', error);
          this.loadLocalCart(); // Charger le panier local en cas d'erreur
        });
      } else {
        // Si l'utilisateur n'est pas connecté, utiliser le panier local
        console.log('Utilisateur non connecté. Charger le panier local.');
        this.loadLocalCart();
      }
    },
    loadLocalCart() {
      // Charger le panier à partir de localStorage
      console.log(localStorage.getItem('cart'))
      const localCart = localStorage.getItem('cart');
      if (localCart) {
        this.moviesCart = JSON.parse(localCart);
      } else {
        // S'il n'y a pas de panier dans localStorage, utilisez des données fictives ou laissez le panier vide
        this.useMockData();
        console.log('Panier fictif utilisé:', this.moviesCart);
      }
    },
    validateCart() {
      if (this.isUserConnected) {
        // Ici, ajoutez la logique pour supprimer l'élément du panier sur le serveur
      } else {
        alert('Veuiilez vous connecter pour valider votre panier');
        setTimeout(() => {
          this.$router.push('/Login');
        }, 1000);
      }


      // const userId = localStorage.getItem('userId');
      // const token = localStorage.getItem('token');
      //
      // axios.post(`http://localhost:3000/cart/validate/${userId}`, this.moviesCart, {
      //   headers: {Authorization: `Bearer ${token}`}
      // }).then(response => {
      //   // Handle successful validation
      //   console.log('Cart validated successfully:', response.data);
      // }).catch(error => {
      //   // Handle error during validation
      //   console.error('Error during cart validation:', error);
      // });
    },
    removeItem(id_item) {
      if (this.isUserConnected) {
        // Ici, ajoutez la logique pour supprimer l'élément du panier sur le serveur
      } else {
        this.removeItemLocal(id_item);
      }
    },
    removeItemLocal(id_item) {
      let cart = JSON.parse(localStorage.getItem('cart')) || [];

      // Trouver l'index de l'élément avec l'id correspondant
      const itemIndex = cart.findIndex(item => item.id === id_item);

      // Vérifier si l'élément existe dans le panier
      if (itemIndex !== -1) {
        cart.splice(itemIndex, 1); // Supprimer l'élément du tableau
        localStorage.setItem('cart', JSON.stringify(cart)); // Sauvegarder le nouveau panier
        console.log(`Film avec l'ID ${id_item} supprimé du panier.`);
        this.fetchCartItems();
      } else {
        console.log(`Aucun film avec l'ID ${id_item} trouvé dans le panier.`);
      }
    },

  clearCart() {
    if (this.isUserConnected) {
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
    } else {
      localStorage.removeItem('cart');
      this.moviesCart = [];
      console.log('Panier vidé.');
    }
  },
  updateRentalDuration({id_item, newRentalDuration}) {
    if (this.isUserConnected) {
      // Ici, ajoutez la logique pour supprimer l'élément du panier sur le serveur
    } else {
      this.updateRentalDurationLocal(id_item, newRentalDuration);
    }
  },
    updateRentalDurationLocal(id, newRentalDuration) {
      let cart = JSON.parse(localStorage.getItem('cart')) || [];
      const itemIndex = cart.findIndex(item => item.id === id && item.cart_type === 'rental');
      if (itemIndex !== -1) {
        cart[itemIndex].rental_duration = newRentalDuration;
        localStorage.setItem('cart', JSON.stringify(cart));
        console.log(`Durée de location mise à jour pour l'article avec ID ${id} à ${newRentalDuration} jours.`);
        this.fetchCartItems();
      } else {
        console.log(`Aucun article avec l'ID ${id} trouvé dans le panier.`);
      }
    },

    useMockData() {
    // Implémenter la logique pour utiliser des données fictives
    this.moviesCart = [
      // {id: 1, movie_id: 1, cart_type: 'rental', rental_duration: 3},
      // {id: 2, movie_id: 2, cart_type: 'purchase'},
      // {id: 3, movie_id: 3, cart_type: 'rental', rental_duration: 7},
    ];
  }
}
,
mounted()
{
  this.fetchCartItems();
}
}
;
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
