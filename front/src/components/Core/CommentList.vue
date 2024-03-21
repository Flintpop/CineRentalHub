<template>
  <div class="commentaires-container">
    <h1>Liste des commentaires</h1>
    <div v-if="isUserLoggedIn">
      <textarea v-model="newCommentText" placeholder="Ajoutez un commentaire..."></textarea>
      <button @click="addComment">Ajouter un commentaire</button>
      <!--      <input type="file" @change="previewImageAddComment" accept="image/*" />-->
      <!--      <div v-if="newCommentImage">-->
      <!--        <img :src="newCommentImage" alt="Preview" style="max-width: 200px; max-height: 200px;" />-->
      <!--      </div>-->
    </div>
    <div v-else>
      <p>Pour ajouter un commentaire,
        <router-link to="/login">connectez-vous</router-link>
        .
      </p>
    </div>

    <div v-if="commentaires.length">
      <div class="commentaire" v-for="commentaire in commentaires" :key="commentaire.id">
        <div class="comment-header">
          <h3>{{ commentaire.user.first_name }}</h3>
        </div>
        <div v-if="commentaire.image_text">
          <img :src="'data:image/png;base64,' + commentaire.image_text" alt="Image du commentaire"
               style="max-width: 200px; max-height: 200px;">
        </div>
        <div v-if="editingComment && editingComment.id === commentaire.id">
          <textarea v-model="editedText" placeholder="Modifiez votre commentaire..."></textarea>
          <!-- Afficher le bouton d'upload uniquement si aucune image n'est associée au commentaire -->
          <div v-if="!commentaire.image_text">
            <input type="file" @change="previewImage" accept="image/*"/>
          </div>
          <div v-else>
            <input type="checkbox" v-model="isImageBeingDeleted" id="deleteImage"/>
            <label for="deleteImage">Supprimer l'image</label>
          </div>
          Taille limite : 10kb
          <div v-if="imagePreview">
            <img :src="imagePreview" alt="Preview" style="max-width: 200px; max-height: 200px;"/>
          </div>
          <div class="button-container">
            <button @click="confirmEdit">Confirmer</button>
            <button @click="cancelEdit">Annuler</button>
          </div>
        </div>
        <div v-else>
          <p class="comment-text">{{ commentaire.comment_text }}</p>
          <div v-if="commentaire.user.id === userId" class="button-container">
            <button @click="startEdit(commentaire)">Modifier</button>
            <button @click="deleteComment(commentaire.id)">Supprimer</button>
          </div>
        </div>
      </div>
    </div>
    <div v-else>
      <p>Aucun commentaire n'a été trouvé pour ce film.</p>
    </div>
  </div>
</template>


<script>
import Upload from "../User/Upload.vue";

export default {
  components: {
    Upload,
  },
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
      newCommentText: '',
      newCommentImage: null,
      newCommentImageBase64: '',
      isUserLoggedIn: false,
      userId: null,
      editingComment: null, // L'objet commentaire en cours de modification
      editedText: '', // Le texte modifié du commentairei
      imagePreview: null, // L'aperçu de l'image
      imageBase64: '', // L'image en base64 pour l'upload
      isImageBeingDeleted: false,
    };
  },
  created() {
    this.fetchCommentaires();
    this.checkUserLoggedIn();
    this.userId = parseInt(localStorage.getItem('userId'), 10); // Assurez-vous de stocker l'ID lors de la connexion
  },
  methods: {
    fetchCommentaires() {
      this.$getCommentsByMovieId(this.movieId).then((res) => {
        this.commentaires = res;
        console.log('Commentaires récupérés avec succès');
      });
    },
    checkUserLoggedIn() {
      // Vérifiez l'existence d'un token dans localStorage pour déterminer si l'utilisateur est connecté
      this.isUserLoggedIn = !!localStorage.getItem('token');
    },
    addComment() {
      if (!this.newCommentText.trim()) return;

      // Ici, vous ajouteriez le nouveau commentaire à votre base de données ou serveur backend
      console.log('Ajout du commentaire:', this.newCommentText, 'Image:', this.newCommentImage);

      const userId = localStorage.getItem('userId');
      this.$addComment(this.movieId, userId, this.newCommentText)
          .then(() => {
            console.log('Commentaire ajouté avec succès');
            this.fetchCommentaires();
          });
      const first_name = this.commentaires.find(c => c.user.id === userId).user.first_name;
      // Après l'ajout, vous pourriez vouloir réinitialiser le champ de texte et rafraîchir la liste des commentaires
      this.commentaires.push({
        id: this.commentaires.length + 1,
        comment_text: this.newCommentText,
        user: {
          id: userId,
          first_name,
        },
      });
      this.newCommentText = '';
      this.newCommentImage = null;


    },
    previewImage(event) {
      const file = event.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.imagePreview = e.target.result;
          this.imageBase64 = e.target.result.split(',')[1]; // Supprime le préfixe de la data URL
        };
        reader.readAsDataURL(file);
      }
    },
    previewImageAddComment(event) {
      const file = event.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.newCommentImage = e.target.result;
          this.newCommentImageBase64 = e.target.result.split(',')[1]; // Supprime le préfixe de la data URL
        };
        reader.readAsDataURL(file);
      }
    },
    startEdit(commentaire) {
      this.editingComment = commentaire;
      this.editedText = commentaire.comment_text; // Initialiser avec le texte actuel du commentaire
    },

    confirmEdit() {
      console.log("Modification confirmée pour:", this.editingComment.id, "avec le nouveau texte:", this.editedText);
      // Ici, implémentez la logique pour envoyer la modification au serveur...

      this.$updateComment(this.editingComment.id, this.editedText)
      if (this.imageBase64) {
        this.$uploadImage(this.editingComment.id, this.imageBase64)
      }

      if (this.isImageBeingDeleted) {
        this.$deleteImage(this.editingComment.id)
            .then(() => {
              console.log('Image deleted successfully');
              this.imagePreview = null; // Supprime l'aperçu de l'image
              this.isImageBeingDeleted = false; // Réinitialise la case à cocher
              this.commentaires.find(c => c.id === this.editingComment.id).image_text = null; // Supprime l'image du commentaire
            })
      }

      const index = this.commentaires.findIndex(c => c.id === this.editingComment.id);
      if (index !== -1) {
        this.commentaires[index].comment_text = this.editedText;
        // Optionnellement, réinitialisez l'état de modification ici
        this.editingComment = null;
        this.editedText = '';
        this.isImageBeingDeleted = false;
      }
      this.fetchCommentaires()
    },

    cancelEdit() {
      this.editingComment = null;
      this.editedText = '';
      this.isImageBeingDeleted = false;
    },

    deleteImage() {
      this.isImageBeingDeleted = true;
    },
    deleteComment(commentId) {
      // Logique pour supprimer un commentaire
      console.log("Supprimer le commentaire", commentId);
      // Exemple de requête pour supprimer un commentaire (à adapter selon votre backend)
      this.$deleteComment(commentId).then(() => {
        // Retirer le commentaire de la liste des commentaires
        this.commentaires = this.commentaires.filter(c => c.id !== commentId);
      });
    },
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
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
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

textarea {
  width: 100%;
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 4px;
  border: 1px solid #ccc;
}

button {
  background-color: #007bff;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}

p {
  margin-top: 20px;
}

.button-container {
  display: flex;
}
</style>
