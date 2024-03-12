<template>
  <div>
    <h1>Liste des Evenements</h1>
    <ul>
      <li v-for="evenement in evenements" :key="evenement.id">
        <h3>{{ evenement.titre }}</h3>
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Evenement',
  data() {
    return {
      evenements: [],
    };
  },
  created() {
    this.fetchCommentaire();
  },
  methods: {
    fetchCommentaire() {
      axios.get('http://localhost:8085/events')
          .then(response => {
            this.evenements = response.data;
          })
          .catch(error => {
            console.error("Il y a eu un problème avec la requête de l'API :", error);
          });
    }
  }
}
</script>
