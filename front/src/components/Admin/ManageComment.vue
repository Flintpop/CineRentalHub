<template>
  <div class="comments-section">
    <h2>Commentaires</h2>
    <div v-for="comment in comments" :key="comment.id" class="comment-bubble">
      <div class="comment-container">
        <div class="comment-content">
          <strong>{{ authors[comment.user_id] }} :</strong>
          <div v-if="editingCommentId !== comment.id">
            {{ comment.comment_text }}
          </div>
          <div v-else>
            <input type="text" v-model="editingCommentText">
          </div>
        </div>
        <div class="comment-date">
          {{ comment.comment_date }}
        </div>
        <div class="comment-buttons" v-show="editingCommentId !== comment.id">
          <button @click="deleteComment(comment.id)">Supprimer</button>
          <button @click="editComment(comment.id, comment.comment_text)">Modifier</button>
        </div>
        <div v-if="editingCommentId === comment.id" class="edit-comment-form">
          <button @click="updateComment(comment.id)">Mettre à jour</button>
        </div>
      </div>
    </div>
    <button @click="addComment">Ajouter un commentaire</button>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  props: {
    movieId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      editingCommentId: null,
      editingCommentText: null,
      authors: {},
      comments: [
        {
          id: 1,
          user_id: 1,
          comment_text: 'Super film !'
        },
        {
          id: 2,
          user_id: 2,
          comment_text: 'Je n\'ai pas aimé...'
        },
        {
          id: 3,
          user_id: 3,
          comment_text: 'Je suis d\'accord avec toi !'
        }

      ]
    };
  },
  mounted() {
    this.fetchComments();
  },
  methods: {
    fetchComments() {
      axios.get(`http://localhost:3000/comments/${this.movieId}`)
          .then(response => {
            this.comments = response.data;
            this.comments.forEach(comment => {
              if (!this.authors[comment.user_id]) {
                this.fetchAuthor(comment.user_id);
              }
            });
          })
          .catch(error => console.error('Erreur lors de la récupération des commentaires:', error));
    },
    async fetchAuthor(authorId) {
      try {
        const response = await axios.get(`http://localhost:3000/user/${authorId}`);
        this.authors[authorId] = response.data.last_name; // Mettez à jour l'objet authors ici
      } catch (error) {
        console.error('Erreur lors de la récupération de l\'auteur:', error);
      }
    },
    deleteComment(commentId) {
      axios.delete(`http://localhost:3000/comments/manage/${commentId}`)
          .then(() => {
            console.log('Commentaire supprimé avec succès');
            this.fetchComments(); // Recharger les commentaires pour afficher les modifications
          })
          .catch(error => console.error('Erreur lors de la suppression du commentaire:', error));
    },
    addComment() {
      const commentText = window.prompt('Veuillez entrer le texte du commentaire');
      if (commentText) {
        // const id_user = $_SESSION['user_id'];


        axios.post(`http://localhost:3000/comments/${this.movieId}`, {user_id: id_user, comment_text: commentText})
            .then(() => {
              console.log('Commentaire ajouté avec succès');
              this.fetchComments(); // Recharger les commentaires pour afficher les modifications
            })
            .catch(error => console.error('Erreur lors de l\'ajout du commentaire:', error));
      }
    },
    editComment(commentId, commentText) {
      this.editingCommentId = commentId;
      this.editingCommentText = commentText;
    },
    updateComment(commentId) {
      axios.put(`http://localhost:3000/comments/manage/${commentId}`, {comment_text: this.editingCommentText})
          .then(() => {
            console.log('Commentaire modifié avec succès');
            this.fetchComments(); // Recharger les commentaires pour afficher les modifications
            this.editingCommentId = null; // Réinitialiser l'ID du commentaire en cours de modification
            this.editingCommentText = null; // Réinitialiser le texte du commentaire en cours de modification
          })
          .catch(error => console.error('Erreur lors de la modification du commentaire:', error));
    },
  }
};
</script>

<style scoped>
.comment-form button {
  padding: 5px 15px;
}

.comment-form {
  display: flex;
  align-items: center;
  margin-top: 20px;
}

.comment-textarea {
  flex-grow: 1;
  margin-right: 10px;
  padding: 10px;
}

.comment-post-btn {
  padding: 10px 15px;
  background-color: #4CAF50; /* Green background */
  color: white; /* White text */
  border: none;
  cursor: pointer;
}

.comment-form {
  margin-top: 20px;
}

.comment-form textarea {
  width: 100%;
  margin-bottom: 10px;
  padding: 10px;
}

.comment-bubble {
  background-color: #f4f4f8;
  border-radius: 15px;
  padding: 10px;
  margin-bottom: 10px;
  max-width: 80%;
}

.comment-content {
  margin-bottom: 5px;
}

.comment-date {
  font-size: 0.8em;
  text-align: right;
  color: #888;
}

.comment-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.comment-buttons {
  display: flex;
  gap: 10px;
}
</style>>