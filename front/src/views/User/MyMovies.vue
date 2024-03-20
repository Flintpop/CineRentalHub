<template>
  <div class="MyMovies">
    <NavbarUser/>
    <h1>Mes films</h1>
    <div class="cart-items">
      <MyMovieList :rentMovies="rentMovies" :buyMovies="buyMovies"/>
    </div>
  </div>
</template>

<script>
import NavbarUser from "../../components/User/NavbarUser.vue";
import MyMovieList from "../../components/User/MyMovieList.vue";
import axios from "axios";

export default {
  components: {NavbarUser, MyMovieList},
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
  /* Ici, vous pouvez ajouter le style pour votre panier */
}
</style>