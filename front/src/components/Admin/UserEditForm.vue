<template>
  <div class="edit-user-form">
    <h2>Édition de l'utilisateur : {{ user.last_name }} {{ user.first_name }}</h2>
    <!-- Formulaire d'édition d'utilisateur -->
    <form @submit.prevent="submitEdit">
      <!-- Ajoutez tous les champs nécessaires -->
      <label for="email">Email:</label>
      <input type="email" id="email" v-model="editFormData.email">

      <label for="role">Rôle:</label>
      <select id="role" v-model="editFormData.role">
        <option value="user">Utilisateur</option>
        <option value="admin">Admin</option>
        <!-- Ajoutez d'autres rôles si nécessaire -->
      </select>

      <!-- Champs existants -->
      <label for="first-name">Prénom:</label>
      <input type="text" id="first-name" v-model="editFormData.first_name">

      <label for="last-name">Nom de famille:</label>
      <input type="text" id="last-name" v-model="editFormData.last_name">


      <button type="submit">Sauvegarder les modifications</button>


      <!-- Bouton pour activer le formulaire de changement de mot de passe -->
      <button type="button" v-if="!showPasswordChangeForm" @click="showPasswordChangeForm = true">Modifier le mot de passe</button>

      <!-- Bouton pour fermer le formulaire de changement de mot de passe -->
      <button type="button" v-if="showPasswordChangeForm" @click="showPasswordChangeForm = false">Annuler le  changement de mot de passe</button>
    </form>

    <!-- Formulaire de changement de mot de passe -->
    <div v-if="showPasswordChangeForm">
      <form @submit.prevent="submitPasswordChange">
        <label for="new-password">Nouveau mot de passe:</label>
        <input type="password" id="newPassword" v-model="newPassword">

        <label for="confirm-password">Confirmer le mot de passe:</label>
        <input type="password" id="confirmPassword" v-model="confirmPassword">

        <button type="submit">Changer le mot de passe</button>
      </form>
    </div>

    <button @click="$emit('close')">Annuler</button>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  props: {
    user: {
      type: Object,
      required: true
    },
    updateUser: {
      type: Function,
      required: true
    }
  },
  data() {
    return {
      editFormData: {...this.user},
      showPasswordChangeForm: false,
      newPassword: '',
      confirmPassword: '',
    };
  },computed: {
    isPasswordInvalid() {
      // Vous pouvez ajouter d'autres conditions de validation ici
      return this.newPassword !== this.confirmPassword || !this.newPassword || !this.confirmPassword;

    }
  },

  methods: {
    async submitEdit() {
      // Logique de mise à jour de l'utilisateur
      console.log('Submitted', this.editFormData);
      // Mise à jour via API...
      this.updateUser(this.editFormData);
      this.$emit('close');
    },
    async submitPasswordChange() {
      if (this.isPasswordInvalid) {
        alert("Le mot de passe n'est pas valide.");
        return;
      }

      try {
        const token = localStorage.getItem('token');
        const headers = {
          'Content-Type': 'application/json',
          'authorization': 'Bearer ' + token
        };
        const response = await axios.put(`http://localhost:3000/user/password/${this.user.id}`, {
          password: this.newPassword,
          // Vous pourriez avoir besoin d'envoyer l'ancien mot de passe également, selon votre API
        }, {headers});
        console.log('Changement de mot de passe réussi', response.data);
        this.showPasswordChangeForm = false; // Cacher le formulaire après la réussite
      } catch (error) {
        console.error('Erreur lors du changement de mot de passe', error);
        this.errorMessage = 'Une erreur est survenue lors du changement de mot de passe.';
      }
    }
  }
};
</script>

<style scoped>
.edit-user-form {
  background: #f4f4f4;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  max-width: 500px;
  margin: auto;
}

.edit-user-form h2 {
  margin-bottom: 20px;
  color: #333;
}

.edit-user-form form {
  display: grid;
  grid-gap: 10px;
  margin-bottom: 20px;
}

label {
  font-weight: bold;
  margin-bottom: 5px;
}

input[type="text"],
input[type="email"],
input[type="password"],
select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  cursor: pointer;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  background-color: #007bff;
  color: white;
  transition: background-color 0.3s;
}

button[type="button"] {
  background-color: #f44336;
}

button[type="submit"]:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>
