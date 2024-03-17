<template>
  <nav class="NavbarAdmin">
    <div class="container">
      <router-link to="/HomeAdmin" class="navbar-brand">CineRentalHub</router-link>
      <div class="navbar-links">
        <router-link to="/HomeAdmin">Accueil</router-link>
        <router-link to="/ManageMovies">Gérer les films</router-link>
        <router-link to="/ManageUsers">Gérer les utilisateurs</router-link>
        <router-link to="/">Déconnexion</router-link>
        <!-- Ajoutez d'autres liens de navigation ici -->
      </div>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'NavbarAdmin',
};
</script>

<style scoped>
.NavbarAdmin {
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
