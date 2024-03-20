<template>
  <div class="commentaires-container">
    <h1>Liste des commentaires</h1>
    <div v-if="commentaires.length">
      <div class="commentaire" v-for="commentaire in commentaires" :key="commentaire.id">
        <div class="comment-header">
          <h3>{{ commentaire.user.first_name }}</h3>
        </div>
        <p class="comment-text">{{ commentaire.comment_text }}</p>
      </div>
    </div>
    <div v-else>
      <p>Aucun commentaire n'a été trouvé pour ce film.</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Commentaires',
  props: {
    movieId: {
      type: Number,
      required: true,
    },
  },
  data() {
    return {
      commentaires: [],
    };
  },
  created() {
    this.fetchCommentaires();
  },
  methods: {
    fetchCommentaires() {
      this.$getCommentsByMovieId(this.movieId).then((res) => {
        this.commentaires = res;
        console.log('Commentaires récupérés avec succès');
      });
    }
  }
}
</script>

<style scoped>
.commentaires-container {
  margin: 20px;
}

.commentaire {
  background-color: #f9f9f9;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 10px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.comment-header h3 {
  margin: 0;
  color: #333;
  font-size: 20px;
}

.comment-text {
  font-size: 16px;
  line-height: 1.5;
  color: #555;
  margin-top: 10px;
}
</style>
