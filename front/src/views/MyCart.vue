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
        if(decoded && 'role' in decoded && decoded.role === 'user') {
          return true;
        }

      } catch (error) {
        // console.error('Erreur lors du décodage du token:', error);
        return false;
      }
    },
  },
  created() {
    // this.fetchUserPurchases();
  },
  methods: {
    // fetchUserPurchases() {
    //   if (!this.isUserConnected) {
    //     return;
    //   }
    //   const userId = localStorage.getItem('userId');
    //   const token = localStorage.getItem('token');
    //   const headers = {
    //     'Content-Type': 'application/json',
    //     'authorization': 'Bearer ' + token
    //   };
    //
    //   axios.get(`http://localhost:3000/movies/purchases${userId}`, {headers})
    //       .then(response => {
    //         this.filterLocalCart(response.data);
    //       })
    //       .catch(error => {
    //         console.error('Erreur lors de la récupération des achats de l\'utilisateur:', error);
    //       });
    // },
    //
    // filterLocalCart(userPurchases) {
    //   let localCart = JSON.parse(localStorage.getItem('cart')) || [];
    //
    //   // Filtrer le panier local pour enlever les films déjà achetés par l'utilisateur
    //   localCart = localCart.filter(localItem => {
    //     return !userPurchases.some(purchase => purchase.movie_id === localItem.movie_id);
    //   });
    //
    //   // Mettre à jour le panier local dans le localStorage
    //   localStorage.setItem('cart', JSON.stringify(localCart));
    //
    //   console.log('Panier local filtré:', localCart);
    // },

    fetchCartItems() {
      const userId = localStorage.getItem('userId');
      const token = localStorage.getItem('token');

      if (this.isUserConnected) {
        //FILTRAGE du panier local en enlevant les déja existant dans les film acheté de l'utilisateur

        console.log('Récupération des éléments du panier pour l\'utilisateur', userId);
        axios.get(`http://localhost:3000/cart/${userId}`, {
          headers: {Authorization: `Bearer ${token}`}
        }).then(response => {
          if (response.data && response.data.length > 0) {
            if (localStorage.getItem('cart')) {
              console.log('Panier local trouvé:', JSON.parse(localStorage.getItem('cart')));
            }
            console.log('Éléments du panier récupérés avec succès:', response.data);
            this.mergeCarts(response.data, JSON.parse(localStorage.getItem('cart') || '[]'));
          } else {
            this.loadLocalCart();
          }
        }).catch(error => {
          // console.error('Erreur lors de la récupération des éléments du panier:', error);
          this.loadLocalCart();
        });
      } else {
        this.loadLocalCart();
      }
    },

    removeFromCart(itemId) {
      console.log('Suppression de l\'élément du panier avec ID', itemId);
      if (this.isUserConnected) {
        console.log('Suppression de l\'élément du panier avec ID', itemId);
        this.removeItemFromServer(itemId);
      } else {
        this.removeItemLocal(itemId);
      }
    },
    mergeCarts(serverCart, localCart) {
      const mergedCart = [...serverCart];

      localCart.forEach(localItem => {
        const serverItem = mergedCart.find(item => item.movie_id === localItem.movie_id);
        if (!serverItem) {
          // Si l'élément n'existe pas dans le panier du serveur, l'ajouter
          this.addToCart(localItem.movie_id, localItem.rental_duration, localItem.cart_type);
        } else {
          // Si l'élément existe dans le panier du serveur, comparer les détails
          if (localItem.cart_type !== serverItem.cart_type) {
            // Si les types sont différents, supprimer l'élément du serveur et l'ajouter localement
            this.removeItemFromServer(serverItem.id).then(() => {
              this.addToCart(localItem.movie_id, localItem.rental_duration, localItem.cart_type);
            });
          } else if (localItem.cart_type === 'rental' && localItem.rental_duration > serverItem.rental_duration) {
            // Si c'est une location et la durée est plus longue, supprimer l'élément du serveur et l'ajouter localement
            this.removeItemFromServer(serverItem.id).then(() => {
              this.addToCart(localItem.movie_id, localItem.rental_duration, localItem.cart_type);
            });
          }
          // Si les types sont les mêmes et c'est un achat, ne rien faire
          // Si les types sont les mêmes et c'est une location et la durée est la même, ne rien faire
        }
      });

      this.moviesCart = mergedCart;
      this.clearLocalCart(); // Supprimer le panier local après la synchronisation
    },

    clearLocalCart() {
      localStorage.removeItem('cart');
      console.log('Panier local supprimé.');
    },

    addToCart(movieId, rentalDuration, type) {
      const userId = localStorage.getItem('userId');
      const token = localStorage.getItem('token');
      const headers = {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`};

      // Créez un objet contenant les données à envoyer à l'API
      const requestData = {
        cart_type: type,
        user_id: userId,
        movie_id: parseInt(movieId), // Assurez-vous que movieId est un nombre
        rental_duration: parseInt(rentalDuration), // Assurez-vous que rentalDuration est un nombre
      };
      //affichage de la
      axios.post('http://localhost:3000/cart', requestData, {headers})
          .then(response => {
            console.log('Success:', response.data);
            this.fetchCartItems(); // Rafraîchir le panier pour refléter les changements
          })
          .catch(error => {
            console.error('Error:', error);
            console.log('response', error.response.data);
          });
    },

    removeItemFromServer(itemId) {
      const token = localStorage.getItem('token');
      const headers = {'Authorization': `Bearer ${token}`};
      console.log('Suppression de l\'élément du panier sur le serveur avec ID', itemId);
      return axios.delete(`http://localhost:3000/cart/${itemId}`, { headers });
    },

    // updateCartItem(movieId, rentalDuration) {
    //   const userId = localStorage.getItem('userId');
    //   const token = localStorage.getItem('token');
    //   const headers = {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`};
    //
    //   axios.put(`http://localhost:3000/cart/${userId}/${movieId}`, {
    //     rental_duration: rentalDuration,
    //   }, {headers}).then(response => {
    //     console.log('Success:', response.data);
    //     this.fetchCartItems(); // Rafraîchir le panier pour refléter les changements
    //   }).catch(error => {
    //     console.error('Error:', error);
    //   });
    // },


    loadLocalCart() {
      // Charger le panier à partir de localStorage
      console.log(localStorage.getItem('cart'))
      const localCart = localStorage.getItem('cart');
      if (localCart) {
        this.moviesCart = JSON.parse(localCart);
        console.log('Panier local chargé:', this.moviesCart);
      } else {
        // S'il n'y a pas de panier dans localStorage, utilisez des données fictives ou laissez le panier vide
        this.useMockData();
        console.log('Panier fictif utilisé:', this.moviesCart);
      }
    },
    validateCart() {
      if (this.isUserConnected) {
        const userId = localStorage.getItem('userId');
        const token = localStorage.getItem('token');

        axios.patch(`http://localhost:3000/cart/validate/${userId}`,{
          headers: {Authorization: `Bearer ${token}`}
        }).then(response => {
          // Handle successful validation
          alert('Votre panier a été validé avec succès');
          this.moviesCart = [];
          this.clearLocalCart();
          console.log('Cart validated successfully:', response.data);
        }).catch(error => {
          // Handle error during validation
          console.error('Error during cart validation:', error);
        });
      } else {
        alert('Veuillez vous connecter pour valider votre panier');
        this.$nextTick(() => {
          this.$router.push('/Login');
        });
      }



    },
    removeItem(id_item) {
      if (this.isUserConnected) {
        this.removeItemFromServer(id_item).then(() => {
          console.log('Suppression de l\'élément du panier avec ID', id_item);
          this.fetchCartItems();
        }).catch(error => {
          console.error('Erreur lors de la suppression de l\'élément du panier:', error);
        });
      } else {
        console.log('Suppression de l\'élément du panier avec ID', id_item);
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
      this.moviesCart = [
        // {id: 1, movie_id: 1, cart_type: 'rental', rental_duration: 3},
        // {id: 2, movie_id: 2, cart_type: 'purchase'},
        // {id: 3, movie_id: 3, cart_type: 'rental', rental_duration: 7},
      ];
    }
  }
  ,
  mounted() {
    this.fetchCartItems();
    console.log(this.$router);

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
