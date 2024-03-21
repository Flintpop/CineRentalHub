<template>
  <div class="details-component" v-if="movie">
    <div class="details-wrapper">
      <button @click="closeDetails" class="details-close-btn">
        <span class="material-icons">close</span>
      </button>
      <div class="details-content">
        <div class="details-image-container">
          <Carousel v-if="movieImages.length > 0">
            <Slide v-for="(image, index) in movieImages" :key="index">
              <img :src="image.image_url" :alt="`Image ${index + 1} pour ${movie.title}`" class="details-image"/>
            </Slide>

            <template #addons>
              <Navigation/>
              <Pagination/>
            </template>
          </Carousel>
          <p v-else>Aucune image disponible</p>
        </div>

        <div class="details-info">
          <h2 class="details-title">{{ movie.title }}</h2>
          <div class="details-text">
            <p class="details-text-item">Date de sortie : {{ movie.release_date }}</p>
            <p class="details-text-item full-width">Résumé : {{ movie.description }}</p>
          </div>
          <div class="details-action-buttons">
            <div class="rental-options">
              <label for="rentalDuration">Durée de location (jours) :</label>
              <input type="number" v-model.number="rentalDuration" id="rentalDuration" min="1"
                     @input="updateRentalPrice"/>
            </div>
            <button @click="rentMovie(movie['id'])" class="details-action-btn rent-btn">
              Louer - {{ calculatedRentalPrice ? calculatedRentalPrice.toFixed(2) : '0.00' }}€
            </button>

            <button @click="purchaseMovie(movie['id'])" class="details-action-btn purchase-btn">
              Acheter - {{ movie.purchase_price.toFixed(2) }}€
            </button>
          </div>
          <div v-if="showConfirmation" class="confirmation-message">
            <p>{{ confirmationMessage }}</p>
          </div>


        </div>
      </div>
      <CommentList :movie-id="this.movie.id"></CommentList>
    </div>
  </div>
</template>


<script>
import axios from "axios";
import {defineComponent} from "vue";
import {Carousel, Slide, Pagination, Navigation} from 'vue3-carousel'
import 'vue3-carousel/dist/carousel.css'
import CommentList from "./CommentList.vue";

export default defineComponent({
  name: 'MovieDetailsComponent',
  components: {
    CommentList,
    Carousel,
    Slide,
    Pagination,
    Navigation,
  },
  props: {
    movie: Object
  },
  data() {
    return {
      rentalDuration: 1, // Définir une valeur initiale minimale
      calculatedRentalPrice: 0,
      movieImages: [],
      isPurchased: false,
      showConfirmation: false,
      confirmationMessage: '',
      isAddedToCart: false,
    };
  },
  methods: {
    closeDetails() {
      this.$emit('close');
    },
    addToCart(movieId, rentalDuration, type) {
      const userId = localStorage.getItem('userId'); //
      const token = localStorage.getItem('token');
      const headers = {
        'Content-Type': 'application/json',
        'authorization': 'Bearer ' + token
      };
      axios.post('http://localhost:3000/cart', {
        cart_type: type,
        movie_id: movieId,
        user_id: userId,
        rental_duration: rentalDuration,
      }, {headers})
          .then(response => {
            console.log('Success:', response.data);
          })
          .catch((error) => {
            console.error('Error:', error);
            console.error('Error:', error.response.data);
          });
    },
    rentMovie(movieId) {
      if (this.inYourCart(movieId)) {
        alert("Ce film est déjà dans votre panier.");
        return;
      } else {
        if (this.rentalDuration < 1) {
          alert("La durée de location doit être d'au moins un jour.");
          return;
        }
        this.calculatedRentalPrice = this.rentalDuration * this.movie.daily_rental_price;

        // Vérifiez si l'utilisateur est connecté
        const token = localStorage.getItem('token');
        if (token) {
          const reponse = this.addToCart(movieId, this.rentalDuration, 'rental');
          if(reponse){
            this.isAddedToCart = true;
            console.log(`Le film ${movieId} a été ajouté au panier pour ${this.rentalDuration} jour(s) à ${this.calculatedRentalPrice.toFixed(2)}€.`);
            this.isRented = true; // Supposons que la location est toujours réussie pour l'exemple
            this.showConfirmation = true;
            this.confirmationMessage = 'Le film a été ajouté dans votre panier.';
            setTimeout(() => this.showConfirmation = false, 3000); // Masque la confirmation après 3 secondes
          }else{
            this.isAddedToCart = false;
            console.log(`Le film ${movieId} n'a pas été ajouté au panier pour ${this.rentalDuration} jour(s) à ${this.calculatedRentalPrice.toFixed(2)}€.`);
            this.isRented = false;
            this.showConfirmation = true;
            this.confirmationMessage = 'Le film n\'a pas été ajouté dans votre panier.';
            setTimeout(() => this.showConfirmation = false, 3000); // Masque la confirmation après 3 secondes
          }
        } else {
          // Stockez le panier en localStorage si l'utilisateur n'est pas connecté
          this.saveToLocalStorage(movieId, this.rentalDuration, 'rental');
        }
      }
    },

    purchaseMovie(movieId) {
      if (this.inYourCart(movieId)) {
        alert("Ce film est déjà dans votre panier.");
        return;
      } else {
        const token = localStorage.getItem('token');
        if (token) {
          this.addToCart(movieId, 1, 'purchase');
          console.log(`Le film ${movieId} a été ajouté au panier pour achat.`);
          this.isPurchased = true; // Supposons que l'achat est toujours réussi pour l'exemple
          this.showConfirmation = true;
          this.confirmationMessage = 'Le film a été ajouté dans votre panier.';
          setTimeout(() => this.showConfirmation = false, 3000); // Masque la confirmation après 3 secondes
        } else {
          this.saveToLocalStorage(movieId, 1, 'purchase');
        }
      }
    },

    saveToLocalStorage(movieId, rentalDuration, type) {
      let cart = JSON.parse(localStorage.getItem('cart')) || [];
      let tempIdCounter = parseInt(localStorage.getItem('tempIdCounter'), 10) || 0;

      // Vérifier si l'élément existe déjà dans le panier
      const itemExists = cart.some(item => item.movie_id === movieId && item.cart_type === type);

      if (!itemExists) {
        tempIdCounter += 1; // Incrémenter le compteur pour un nouveau ID unique
        localStorage.setItem('tempIdCounter', tempIdCounter.toString());

        const newItem = {
          id: tempIdCounter, // Utiliser le compteur comme ID temporaire
          movie_id: movieId,
          rental_duration: type === 'rental' ? rentalDuration : 1,
          cart_type: type,
          user_id: null
        };

        cart.push(newItem);
        localStorage.setItem('cart', JSON.stringify(cart));
        console.log(`Le film ${movieId} (temp ID: ${tempIdCounter}) a été ajouté au panier.`);
      } else {
        console.log(`Le film ${movieId} est déjà présent dans le panier.`);
      }
    },
    inYourCart(movieId) {
      if (this.isUserConnected) {
        const userId = localStorage.getItem('userId');
        const cart = this.moviesCart;
        return cart.some(item => item.movie_id === movieId && item.user_id === userId);
      } else {
        const cart = JSON.parse(localStorage.getItem('cart')) || [];
        return cart.some(item => item.movie_id === movieId);
      }
    },


    updateRentalPrice() {
      if (this.movie && this.movie.daily_rental_price) {
        this.calculatedRentalPrice = this.rentalDuration * this.movie.daily_rental_price;
      }
    },
    fetchMovieImages() {
      if (!this.movie || !this.movie.id) {
        console.log("Movie is undefined or has no ID");
        this.useFictiveImages();
        return;
      }

      axios.get(`http://localhost:3000/movies/images/${this.movie.id}`)
          .then(response => {
            if (response.data.length > 0) {
              this.movieImages = response.data;
            } else {
              this.useFictiveImages();
            }
          })
          .catch(error => {
            console.error("Erreur lors de la récupération des images :", error);
            this.useFictiveImages();
          });
    },
    useFictiveImages() {
      this.movieImages = [
        {image_url: "https://www.picclickimg.com/QXQAAOSwm2JgR4Qq/Avatar-Affiche-Cinema-Originale-120x160-cm-roulee.webp"},
        {image_url: "https://business-cool.com/wp-content/uploads/2023/07/raw_62c3e2f1a9694_0.jpeg"},
        // Ajoutez plus d'images fictives si nécessaire
      ];
    },
  },
  watch: {
    movie: {
      handler(newVal) {
        if (newVal && newVal.id) {
          this.fetchMovieImages();
        }
      },
      immediate: true,
    },
  },
  mounted() {
    this.updateRentalPrice();
    if (this.movie && this.movie.id) {
      this.fetchMovieImages();
    }
  },
});
</script>


<style scoped>
.details-component {
  position: relative;
  max-width: 70vw;
  margin: auto;
  padding: 20px;
  background: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.details-content {
  display: flex;
  gap: 20px;
}

.details-image-container {
  flex-basis: 40%;
}

.details-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.details-close-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: #ff6347;
  border: none;
  color: white;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 5px;
}

.details-image {
  width: 100%;
  height: auto;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.details-text {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.details-text-item {
  margin-bottom: 10px;
}

.details-action-buttons {
  display: flex;
  justify-content: start;
  gap: 10px;
}

.details-action-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
  font-weight: 600;
}

.rent-btn {
  background-color: #ffcc00;
  color: #333;
}

.purchase-btn {
  background-color: #28a745;
  color: white;
}

.details-action-btn:hover {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

/* Style général des composants de saisie */
.rental-options {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
}

.rental-options label {
  font-weight: 600;
  font-size: 1rem;
}

.rental-options input[type="number"] {
  padding: 8px 12px;
  border: 2px solid #ccc; /* Border plus prononcé */
  border-radius: 5px;
  font-size: 1rem;
  transition: border-color 0.2s;
}

.rental-options input[type="number"]:focus {
  border-color: #3498db; /* Changement de couleur à la sélection */
  outline: none; /* Suppression de outline par défaut */
}

/* Styles des boutons */
.details-action-btn {
  padding: 12px 20px;
  font-size: 1rem;
  border-radius: 5px;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  width: 150px; /* Largeur fixe pour uniformiser la taille des boutons */
  text-align: center; /* Alignement du texte */
}

.rent-btn {
  background-color: #ffcc00;
  color: #333;
}

.purchase-btn {
  background-color: #28a745;
  color: white;
}

.details-action-btn:hover {
  transform: translateY(-2px); /* Effet de soulèvement au survol */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
}

.details-image {
  max-width: 80%; /* Assure que l'image ne dépasse pas le carrousel */
  max-height: 100%; /* Assure que l'image ne dépasse pas la hauteur du carrousel */
  object-fit: contain; /* Garde le ratio sans déformer l'image */
}

.confirmation-message {
  padding: 10px;
  margin: 20px 0;
  background-color: #d4edda;
  color: #155724;
  border-radius: 5px;
  text-align: center;
}
</style>
