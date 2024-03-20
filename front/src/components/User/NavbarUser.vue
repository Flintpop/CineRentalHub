<template>
  <nav class="navbar">
    <div class="container">
      <router-link to="/HomeUser" class="navbar-brand">CineRentalHub </router-link>
      <div class="navbar-links">
        <router-link to="/HomeUser">Accueil</router-link>
        <router-link to="/MyMovies">Mes films</router-link>
        <router-link to="/MyHistory">Mon historique</router-link>
        <router-link to="/MyAccount">Mon profil</router-link>
        <router-link to="/MyMessages">Mes messages</router-link>
        <router-link to="/MyCart">Mon panier</router-link>
        <router-link to="/" @click.native="logout">Déconnexion</router-link> <!-- Lien de déconnexion -->

        <!-- Ajoutez d'autres liens de navigation ici -->
      </div>
    </div>
  </nav>
</template>

<script>
export default {
  methods: {
    logout() {
      // Supprimez les informations de l'utilisateur de la session ou du local storage
      localStorage.removeItem('user');
      localStorage.removeItem('token');

      // Redirigez l'utilisateur vers la page de connexion
      this.$router.push('/');
    }
  }
}
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
