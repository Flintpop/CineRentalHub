<template>
  <div>
    <h1>Liste des Lieux</h1>
    <ul>
      <li v-for="lieu in lieux" :key="lieu.id">
        <h2>{{ lieu.nom }}</h2>
        <p>Adresse: {{ lieu.adresse }}</p>
        <p>Capacité d'accueil: {{ lieu.capaciteAccueil }}</p>
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'ListeLieux',
  data() {
    return {
      lieux: [],
    };
  },
  created() {
    this.fetchLieux();
    this.fetchComments(1);

  },
  methods: {
    fetchLieux() {
      axios.get('http://localhost:8085/lieux')
          .then(response => {
            this.lieux = response.data;
          })
          .catch(error => {
            console.error("Il y a eu un problème avec la requête de l'API :", error);
          });
    },
    fetchComments(eventId) {
      axios.get(`http://localhost:8085/commentaire/evenement/${eventId}`)
          .then(response => {
            this.comments = response.data;
          })
          .catch(error => {
            console.error(error);
          });
    },
  }
}
</script>
