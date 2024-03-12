<template>
  <div class="Create-member-form-container">
    <button class="close-form-button" @click="$emit('closeForm')">&#10005;</button>
    <form @submit.prevent="createMember">
      <h2>Inscription d'un membre</h2>

      <div class="form-field">
        <label for="memberName">Nom</label>
        <input id="memberName" type="text" v-model="memberData.nom" required>
      </div>

      <div class="form-field">
        <label for="memberFirstName">Prénom</label>
        <input id="memberFirstName" type="text" v-model="memberData.prenom" required>
      </div>

      <div class="form-field">
        <label for="memberBirthdate">Date de naissance</label>
        <input id="memberBirthdate" type="date" v-model="memberData.dateNaissance" required>
      </div>

      <div class="form-field">
        <label for="memberAddress">Adresse</label>
        <input id="memberAddress" type="text" v-model="memberData.adresse" required>
      </div>

      <div class="form-field">
        <label for="memberPassword">Mot de passe</label>
        <input id="memberPassword" type="text" v-model="memberData.motDePasse" required>
      </div>

      <div class="form-field">
        <label for="memberEmail">Email</label>
        <input id="memberEmail" type="email" v-model="memberData.mail" required>
      </div>



      <button type="submit">Inscription</button>
    </form>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  name: 'CreateMemberForm',
  data() {
    return {
      memberData: {
        nom: '',
        prenom: '',
        dateNaissance: '',
        adresse: '',
        mail: '',
        motDePasse: '',
      },
    };
  },


  methods: {
    async createMember() {
      try {
        console.log('Création du membre', this.memberData);
        const response = await axios.post('http://localhost:8085/membres', this.memberData);
        console.log('Inscription réussie', response.data);
        this.$emit('memberCreated');
        // Vous pouvez également rediriger l'utilisateur vers une autre page ici
      } catch (error) {
        console.error('Erreur lors de l\'inscription', error);
      }
    }
  }
};
</script>

<style scoped>
.Create-member-form-container {
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

.form-field input {
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
