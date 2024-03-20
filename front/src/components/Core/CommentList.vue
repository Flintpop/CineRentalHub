<template>
  <div>
    <h1>Liste des commentaires</h1>
    <ul>
      <li v-for="commentaire in commentaires" :key="commentaire.id">
        <h2>{{ commentaire.comment_text }}</h2>
      </li>
    </ul>
  </div>
</template>

<script>

export default {
  name: 'Commentaire',
  props: {
    movieId: {
      type: Number,
      required: true,
    },
  },
  data() {
    return {
      commentaires: [
        {
          id: 1,
          comment_text: 'Super film !',
        },
        {
          id: 2,
          comment_text: 'Je n\'ai pas aimé ce film',
        },
      ],
    };
  },
  created() {
    this.fetchCommentaire();
  },
  methods: {
    fetchCommentaire()
    {
      this.$getCommentsByMovieId(this.movieId).then((res) => {
        this.commentaires = res;
        console.log('Commentaires récupérés avec succès');
      });
    }

  }
}
</script>
