<template>
  <nav class="navbar">
    <div class="container">
      <router-link to="/homeConnected" class="navbar-brand">TUX</router-link>
      <div class="navbar-links">
        <router-link to="/homeConnected">Accueil</router-link>
<!--        <router-link to="/about">À propos</router-link>-->
        <router-link to="/GestionLieu">Gestion des Lieux</router-link>
        <router-link to="/GestionEvenement">Gestion des Événements</router-link>
        <router-link to="/GestionMembre">Gestion des Membres</router-link>
        <button @click="logout">Déconnexion</button>
<!--        <router-link to="/">Déconnexion</router-link>-->
        <!-- Ajoutez d'autres liens de navigation ici -->


      </div>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'Navbar',
  methods: {
    logout() {
      localStorage.removeItem('membreId');
      this.$router.push('/');
    }
  }
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
