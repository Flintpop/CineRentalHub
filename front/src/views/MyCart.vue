<template>
  <NavbarUser/>
  <div class="mycart">
    <h1>Mon Panier</h1>
    <div class="cart-items">
      <div v-for="(item, index) in cartItems" :key="index" class="cart-item">
        <img :src="item.image" alt="" class="item-image">
        <div class="item-details">
          <h2>{{ item.name }}</h2>
          <p>Quantité: {{ item.quantity }}</p>
          <p>Prix: {{ item.price }} €</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NavbarUser from "../components/User/NavbarUser.vue";

export default {
  name: 'MyCart',
  components: {NavbarUser},
  data() {
    return {
      cartItems: [
        { name: 'Film A', quantity: 2, price: 10, image: 'path/to/imageA.jpg' },
        { name: 'Film B', quantity: 1, price: 15, image: 'path/to/imageB.jpg' }
      ]
    };
  },
  methods: {
    // Méthodes pour gérer le panier ici
  },
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
