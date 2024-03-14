<template>
  <Navbar />
  <div class="about-page-container">
    <div class="about-page-content">
      <h1>À Propos de CineRentalHub</h1>
      <div class="text-section">
        <p>
          "CineRentalHub" est une plateforme de location de films en ligne conçue pour offrir une expérience utilisateur immersive et intuitive. Développée par des passionnés de cinéma et de technologie à l'UBO, notre application permet aux utilisateurs de découvrir, louer et commenter un large éventail de films.
        </p>
      </div>

      <div class="section">
        <h2>Notre Mission</h2>
        <p>
          Notre mission est de révolutionner la manière dont les films sont loués et appréciés, en fournissant un accès facile à une variété de titres, de genres et de formats. Nous nous engageons à offrir une plateforme conviviale qui encourage la découverte de films et le partage d'opinions au sein de notre communauté.
        </p>
      </div>

      <div class="section">
        <h2>Fonctionnalités Clés</h2>
        <ul>
          <li>Large catalogue de films avec options de recherche et de filtrage avancées.</li>
          <li>Système de location intuitive avec gestion de panier.</li>
          <li>Création de compte utilisateur et système d'authentification.</li>
          <li>Section de commentaires pour partager et lire des avis sur les films.</li>
          <li>Recommandations personnalisées basées sur les préférences et l'historique de location.</li>
        </ul>
      </div>

      <div class="section">
        <h2>Technologies Utilisées</h2>
        <p>
          CineRentalHub utilise une architecture n-tiers avec des technologies modernes pour offrir une expérience utilisateur fluide et sécurisée :
        </p>
        <ul>
          <li>Front-end :  Vue.js pour une interface utilisateur dynamique.</li>
          <li>Back-end : Node.js avec Express pour la gestion des authentifications et des autorisations, servlet pour l'accès aux données.</li>
          <li>Bases de données : MongoDB pour stocker les statistiques et SQL pour gérer le catalogue de films, les utilisateurs et les commentaires.</li>
          <li>Communication en temps réel avec les Websockets pour le chat .</li>
        </ul>
      </div>

      <div class="section">
        <h2>L'Équipe de Développement</h2>
        <p>
          CineRentalHub est le résultat du travail acharné et de la passion d'une équipe dédiée d'étudiants à l'UBO, qui ont collaboré pour créer une plateforme innovante qui répond aux besoins des cinéphiles modernes.
        </p>
      </div>
    </div>
    <Footer />
  </div>
</template>

<script>
import Navbar from '../../components/NoConnected/Navbar.vue';
import Footer from '../../components/Core/Footer.vue';

export default {
  name: 'AboutPage',
  components: {
    Navbar,
    Footer,
  },
};
</script>

<style scoped>
.about-page-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.about-page-content {
  flex: 1;
  padding: 20px;
  max-width: 800px;
  margin: auto;
}

.section {
  margin-bottom: 20px;
}

.text-section, .section h2, .section p, .section ul {
  padding: 0 20px;
}

.section ul {
  list-style-type: disc;
  margin-left: 40px;
}

h1, h2 {
  color: #007bff;
}

p, li {
  color: #333;
  line-height: 1.6;
}
</style>
