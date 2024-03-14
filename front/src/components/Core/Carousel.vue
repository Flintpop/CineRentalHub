<template>
  <div class="carousel-container">
    <div class="carousel" :style="{transform: `translateX(-${currentIndex * 100}%)`}">
      <div v-for="(image, index) in images" :key="index" class="carousel-slide">
        <img :src="image.image_url" :alt="`Slide ${index}`" />
      </div>
    </div>
    <button v-if="images.length > 1" @click="prevSlide" class="prev">&lt;</button>
    <button v-if="images.length > 1" @click="nextSlide" class="next">&gt;</button>
  </div>
</template>

<script>
export default {
  name: 'Carousel',
  props: {
    images: Array,
  },
  data() {
    return {
      currentIndex: 0,
    };
  },
  methods: {
    nextSlide() {
      this.currentIndex = (this.currentIndex + 1) % this.images.length;
    },
    prevSlide() {
      this.currentIndex = (this.currentIndex - 1 + this.images.length) % this.images.length;
    },
  },
};
</script>

<style scoped>
.carousel-container {
  position: relative;
  overflow: hidden;
}

.carousel {
  display: flex;
  transition: transform 0.5s ease;
}

.carousel-slide {
  min-width: 100%; /* Assurez que chaque slide prend toute la largeur du conteneur */
  text-align: center;
}

.carousel-slide img {
  width: auto; /* Ajustez selon vos besoins */
  max-width: 100%; /* S'assurer que l'image ne dépasse pas du slide */
  height: auto; /* Ajustez selon vos besoins */
}

.prev, .next {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: #fff;
  border: none;
  padding: 10px;
  cursor: pointer;
  z-index: 100;
}

.prev {
  left: 10px;
}

.next {
  right: 10px;
}

/* Style optionnel pour améliorer la visibilité des boutons */
.prev:hover, .next:hover {
  background-color: #eee;
}
</style>
