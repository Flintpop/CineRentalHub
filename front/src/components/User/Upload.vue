<template>
  <div>
    <input type="file" @change="previewImage" accept="image/*" />
    <button @click="uploadImage">Upload Image</button>
    <button @click="deleteImage">Delete Image</button>
    <div v-if="imagePreview">
      <img :src="imagePreview" alt="Preview" style="max-width: 200px; max-height: 200px;" />
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      imagePreview: null,
      imageBase64: '',
      commentId: 1, // L'id du commentaire, à définir selon le contexte
    };
  },
  methods: {
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
    uploadImage() {
      this.$uploadImage(this.commentId, this.imageBase64)
    },
    deleteImage() {
      this.$deleteImage(this.commentId)
          .then(() => {
            console.log('Image deleted successfully');
            // Mettre à jour l'UI en conséquence, par exemple supprimer l'aperçu de l'image
          })
    },
  },
};
</script>

<style scoped>
/* Ajoutez vos styles ici */
</style>
