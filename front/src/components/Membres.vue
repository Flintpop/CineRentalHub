<template>
  <div>
    <h1>Liste des membres</h1>
    <ul>
      <li v-for="membre in membres" :key="membre.id">
        <h2>{{ membre.nom }} {{ membre.prenom }}</h2>
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Membres',
  data() {
    return {
      membres: [],
    };
  },
  created() {
    this.fetchMembres();
  },
  methods: {
    fetchMembres() {
      axios.get('http://localhost:8085/membres')
          .then(response => {
            this.membres = response.data;
          })
          .catch(error => {
            console.error("Il y a eu un problème avec la requête de l'API :", error);
          });
    }
  }
}
</script>
