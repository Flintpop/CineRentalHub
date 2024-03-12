<template>
  <div>
    <h1>Liste des commentaires</h1>
    <ul>
      <li v-for="commentaire in commentaires" :key="commentaire.id">
        <h2>{{ commentaire.texte }}</h2>
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Commentaire',
  data() {
    return {
      commentaires: [],
    };
  },
  created() {
    this.fetchCommentaire();
  },
  methods: {
    fetchCommentaire() {
      axios.get('http://localhost:8085/commentaire')
          .then(response => {
            this.commentaires = response.data;
          })
          .catch(error => {
            console.error("Il y a eu un problème avec la requête de l'API :", error);
          });
    }
  }
}
</script>
