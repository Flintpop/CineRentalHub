<template>
  <Navbar />
  <div class="about-page-container">
    <div class="about-page-content">
      <h1>À Propos de Tux & Co</h1>
      <div class="text-section">
        <p>
          "Tux & Co" est une application web innovante conçue pour simplifier la gestion d'événements au sein des associations. Développée par des étudiants passionnés du Master 1ère année TIIL-A & ILIADE à l'UBO, cette plateforme permet aux membres d'une association de se connecter, de s'inscrire à des événements, et de participer activement à la vie associative.
        </p>
      </div>

      <div class="section">
        <h2>Notre Mission</h2>
        <p>
          Notre mission est de fournir une solution intuitive et complète pour la gestion d'événements, en facilitant l'organisation, la participation, et le partage d'expériences au sein des associations. Nous visons à rassembler les professionnels du secteur pour partager connaissances et innovations, en offrant une plateforme centralisée pour tous leurs besoins événementiels.
        </p>
      </div>

      <div class="section">
        <h2>Fonctionnalités Clés</h2>
        <ul>
          <li>Gestion complète des membres, avec système d'authentification.</li>
          <li>Création, modification, et suppression d'événements par les membres.</li>
          <li>Système d'inscription aux événements, avec gestion des contraintes de capacité et de chevauchement d'horaire.</li>
          <li>Commentaires sur les événements pour favoriser l'interaction et le retour d'expérience.</li>
          <li>Intégration d'OpenStreetMap pour localiser les lieux d'événements.</li>
        </ul>
      </div>

      <div class="section">
        <h2>Technologies Utilisées</h2>
        <p>
          L'application "Tux & Co" s'appuie sur une architecture moderne et robuste, utilisant des technologies de pointe pour le développement web :
        </p>
        <ul>
          <li>Front-end : Vue.js</li>
          <li>Back-end : Spring pour la logique métier, Servlets pour les services web simples.</li>
          <li>Bases de données : SQL pour les données relationnelles, MongoDB pour les commentaires.</li>
          <li>API REST pour faciliter la communication entre le front-end et le back-end.</li>
        </ul>
      </div>

      <div class="section">
        <h2>L'Équipe de Développement</h2>
        <p>
          Ce projet est le fruit du travail collaboratif de trois étudiants ambitieux : THOMAS Antoine, LE GOFF Tanguy, et AMIL Abdelaziz, guidés par la passion de créer des solutions technologiques innovantes pour répondre aux besoins réels des utilisateurs.
        </p>
      </div>
    </div>
    <Footer />
  </div>
</template>

<script>
import Navbar from '../components/Navbar.vue';
import Footer from '../components/Footer.vue';

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
