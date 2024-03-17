<template>
  <!-- Carrousel principal pour afficher l'image sélectionnée -->
  <Carousel id="gallery" :itemsToShow="1" :wrapAround="false" v-model="currentSlide">
    <Slide v-for="(image, index) in images" :key="index">
      <div class="carousel__item">
        <img :src="image.image_url" alt="Image du film" />
        <div class="carousel__item__actions">
          <button @click="setMainImage(image.id)">Définir comme image principale</button>
          <button @click="deleteImage(image.id)">Supprimer</button>
        </div>
      </div>

    </Slide>
  </Carousel>

  <!-- Carrousel des vignettes pour naviguer dans le carrousel principal -->
  <Carousel
      id="thumbnails"
      :itemsToShow="4"
      :wrapAround="true"
      v-model="currentSlide"
      @update:modelValue="slideTo"
  >
    <Slide v-for="(image, index) in images" :key="index">
      <div class="carousel__item thumbnail" @click="slideTo(index)">
        <img :src="image.image_url" alt="Thumbnail du film" />
      </div>
    </Slide>
  </Carousel>
  <button @click="addImage">Ajouter une image</button>
</template>


<script>
import axios from 'axios';
import { defineComponent } from 'vue';
import { Carousel, Slide } from 'vue3-carousel';
import 'vue3-carousel/dist/carousel.css';

export default defineComponent({
  name: 'MovieGallery',
  components: {
    Carousel,
    Slide
  },
  props: {
    movieId: Number
  },
  data() {
    return {
      images: [],
      currentSlide: 0
    };
  },
  methods: {
    fetchImages() {
      axios.get(`http://localhost:3000/movies/images/${this.movieId}`)
          .then(response => {
            this.images = response.data;
          })
          .catch(error => console.error("Erreur lors de la récupération des images :", error));
    },
    setMainImage(imageId) {
      const image_id = imageId;
      axios.put(`http://localhost:3000/movies/main_image/${this.movieId}`, { image_id })
          .then(() => {
            console.log('Image principale mise à jour avec succès');
            window.location.reload();
            // this.fetchImages(); // Recharger les images pour afficher les modifications
          })
          .catch(error => console.error('Erreur lors de la mise à jour de l\'image principale:', error));
    },
    deleteImage(imageId) {
      axios.delete(`http://localhost:3000/movies/images/${imageId}`)
          .then(() => {
            console.log('Image supprimée avec succès');
            this.fetchImages(); // Recharger les images pour afficher les modifications
          })
          .catch(error => console.error('Erreur lors de la suppression de l\'image:', error));
    },
    addImage() {
      const imageUrl = window.prompt('Veuillez entrer l\'URL de l\'image');
      if (imageUrl) {
        const image_url = imageUrl;
        axios.post(`http://localhost:3000/movies/images/${this.movieId}`, { image_url })
            .then(() => {
              console.log('Image ajoutée avec succès');
              this.fetchImages(); // Recharger les images pour afficher les modifications
            })
            .catch(error => console.error('Erreur lors de l\'ajout de l\'image:', error));
      }
    },
    slideTo(index) {
      this.currentSlide = index;
    },
  },
  mounted() {
    this.fetchImages();
  }
});
</script>


<style scoped>
.carousel__item {
  position: relative;
  padding: 2px;
}

.carousel__item img {
  width: 50%;
  object_fit: cover;
}

.thumbnail {
  cursor: pointer;
  opacity: 0.6;
  transition: opacity 0.3s;
}

.thumbnail:hover, .thumbnail:focus {
  opacity: 1;
}

/* Active thumbnail style */
.thumbnail.active {
  border: 2px solid #3498db;
}

.slide{
  margin-right: 0px;
  margin-left: 0px;
}
</style>

