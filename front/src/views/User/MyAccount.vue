<template>
  <div class="MyAccount">
    <NavbarUser/>
    <h1>Mon Compte</h1>
    <!-- Ici, vous pouvez ajouter le code pour afficher les articles dans le panier -->
  </div>
</template>

<script>
import NavbarUser from "../../components/User/NavbarUser.vue";

export default {
  name: 'MyAccount',
  components: {NavbarUser},
  data() {
    return {
      // Ici, vous pouvez ajouter les données nécessaires pour votre panier
    };
  },
  methods: {
    // Ici, vous pouvez ajouter les méthodes nécessaires pour votre panier
  },
};
</script>

<style scoped>
.mycart {
  /* Ici, vous pouvez ajouter le style pour votre panier */
}
</style>