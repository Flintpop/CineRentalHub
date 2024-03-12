<template>
  <footer class="footer">
    <div class="container">
      <p>© 2024 TUX. Tous droits réservés.</p>
      <p> THOMAS Antoine | LE GOFF Tanguy | AMIL Abdelaziz</p>
      <div class="footer-links">
        <router-link to="/about">À propos</router-link>
        <router-link to="/contact">Contact</router-link>
        <!-- Ajoutez d'autres liens utiles ici -->
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'Footer',
};
</script>

<style scoped>
.footer {
  background-color: #007bff;
  color: #ffffff;
  padding: 1rem;
  text-align: center;
}

.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  max-width: 1200px;
  margin: auto;
  padding: 0 20px;
}

.footer-links a {
  color: #ffffff;
  margin: 0 10px;
}

.footer-links a:hover {
  text-decoration: underline;
}
</style>
