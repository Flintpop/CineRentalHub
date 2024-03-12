<template>
  <div class="create-event-form-container">
    <button class="close-form-button" @click="$emit('closeForm')">&#10005;</button>
    <form @submit.prevent="submitForm">
      <h2>Créer un nouvel événement</h2>

      <div class="form-field">
        <label for="eventTitle">Titre de l'événement</label>
        <input id="eventTitle" type="text" v-model="eventData.titre" required>
      </div>

      <div class="form-field">
        <label for="startDate">Date de début</label>
        <input id="startDate" type="datetime-local" v-model="eventData.dateHeureDebut" required>
      </div>

      <div class="form-field">
        <label for="endDate">Date de fin</label>
        <input id="endDate" type="datetime-local" v-model="eventData.dateHeureFin" required>
      </div>

      <div class="form-field">
        <label for="description">Description</label>
        <textarea id="description" v-model="eventData.description" required></textarea>
      </div>

      <div class="form-field">
        <label for="lieux">Lieu</label>
        <select id="lieux" v-model="eventData.lieu" required>
          <option v-for="lieu in lieux" :key="lieu.id" :value="lieu">
            {{ lieu.nom + ' - ' + lieu.adresse + ' - ' + lieu.capaciteAccueil + ' places'}}
          </option>
        </select>
      </div>

      <button type="submit">Créer l'événement</button>
    </form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'CreateEventForm',
  data() {
    return {
      eventData: {
        titre: '',
        dateHeureDebut: '',
        dateHeureFin: '',
        description: '',
        lieu: null
      },
      lieux: [] // Vous devez remplir ce tableau avec les données de vos lieux depuis la BDD
    };
  },
  mounted() {
    this.loadLocations();
  },
  methods: {
    async submitForm() {
      // Préparez l'URL de l'API
      const url = `http://localhost:8085/events`;

      // Appeler l'API pour ajouter l'événement
      try {
        const requestBody = {
          titre: this.eventData.titre,
          dateHeureDebut: this.eventData.dateHeureDebut,
          dateHeureFin: this.eventData.dateHeureFin,
          description: this.eventData.description,
          lieuId: this.eventData.lieu.id
        }
        console.log('Envoi des données:', requestBody)
        await axios.post(url, requestBody);
        alert('Votre événement a été ajouté.');

        // Émettez un événement pour informer le composant parent que l'événement a été ajouté
        this.$emit('eventAdded');

        // Réinitialisez les données du formulaire
        this.eventData = {
          titre: '',
          dateHeureDebut: '',
          dateHeureFin: '',
          description: '',
          lieu: ''
        };
      } catch (error) {
        console.error("Erreur API :", error);
        alert('Une erreur est survenue lors de l\'ajout de l\'événement.');
      }
    },
    async loadLocations() {
      // Préparez l'URL de l'API
      const url = `http://localhost:8085/lieux`;

      // Appeler l'API pour obtenir la liste des lieux
      try {
        const response = await axios.get(url);
        this.lieux = response.data;
        console.log('Lieux:', this.lieux);
      } catch (error) {
        console.error("Erreur API :", error);
        alert('Une erreur est survenue lors du chargement des lieux.');
      }
    }
  }
};
</script>

<style scoped>
.create-event-form-container {
  max-width: 500px;
  margin: auto;
  padding: 20px;
  background: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.form-field {
  margin-bottom: 20px;
}

.form-field label {
  display: block;
  margin-bottom: 5px;
}

.form-field input,
.form-field textarea,
.form-field select {
  width: 100%;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
}

button {
  padding: 10px 15px;
  background-color: #5cb85c;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.close-form-button {
  position: absolute;
  top: 10px;
  right: 10px;
  background: transparent;
  border: none;
  font-size: 24px;
  cursor: pointer;
}
</style>
