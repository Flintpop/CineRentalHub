<template>
  <NavbarUser/>
  <div class="mycart">
    <h1>Mon Panier</h1>
    <div class="cart-items">
      <CartList :rentMovies="rentMovies" :buyMovies="buyMovies"/>
    </div>
  </div>
</template>

<script>
import NavbarUser from "../components/User/NavbarUser.vue";
import axios from "axios";
import CartList from "../components/Core/CartList.vue";

export default {
  name: 'MyCart',
  components: {NavbarUser, CartList},
  data() {
    return {
      rentMovies: [],
      buyMovies: [],
    };
  },
  methods: {
    fetchRentMovies() {
      const token = localStorage.getItem('token');
      const headers = {
        'Content-Type': 'application/json',
        'authorization': 'Bearer ' + token
      };

      const userId = localStorage.getItem('userId');
      axios.get(`http://localhost:3000/movies/rentals/${userId}`, {headers})
          .then(response => {
            this.rentMovies = response.data;
          })
          .catch(error => {
            console.error('Error:', error);
          });
    },
    fetchBuyMovies() {
      const token = localStorage.getItem('token');
      const headers = {
        'Content-Type': 'application/json',
        'authorization': 'Bearer ' + token
      };

      const userId = localStorage.getItem('userId');
      axios.get(`http://localhost:3000/movies/purchases/${userId}`, {headers})
          .then(response => {
            this.buyMovies = response.data;
          })
          .catch(error => {
            console.error('Error:', error);
          });
    },
  },
  created() {
    this.fetchRentMovies();
    this.fetchBuyMovies();
  }
};
</script>

<style scoped>
.mycart {
  max-width: 80vw;
  margin: auto;
  padding: 20px;
}

.cart-items {
  display: flex;
  flex-direction: column;
}

.cart-item {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.item-image {
  width: 200px;
  height: 200px;
  margin-right: 20px;
}

.item-details h2 {
  margin: 0;
  font-size: 1.5rem;
}

.item-details p {
  margin: 5px 0;
}
</style>
