<template>
  <nav class="navbar">
    <div class="container">
      <router-link to="/" class="navbar-brand">CineRentalHub </router-link>
      <div class="navbar-links">
        <router-link to="/">Accueil</router-link>
        <router-link to="/about">À propos</router-link>
        <router-link to="/login">Connexion</router-link>
        <router-link to="/register">Inscription</router-link>
        <router-link to="/MyCart" class="navbar-cart">
          <i class="fas fa-shopping-cart"></i> Panier
        </router-link>
        <!-- Ajoutez d'autres liens de navigation ici -->
      </div>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'Navbar',
};
</script>

<style scoped>
.navbar {
  background-color: #007bff;
  padding: 1rem 0;
  color: #ffffff;
}

.container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1200px;
  margin: auto;
  padding: 0 20px;
}

.navbar-brand {
  font-weight: bold;
  color: #ffffff;
}

.navbar-links a {
  color: #ffffff;
  margin-left: 20px;
}

.navbar-links a:hover {
  text-decoration: underline;
}
</style>
