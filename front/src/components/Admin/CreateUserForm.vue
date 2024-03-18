<template>
  <div class="Create-member-form-container">
    <button class="close-form-button" @click="$emit('closeForm')">&#10005;</button>
    <form @submit.prevent="createUser">
      <h2>Inscription d'un membre</h2>

      <div class="form-field">
        <label for="memberEmail">Email</label>
        <input id="memberEmail" type="email" v-model="email" required>
      </div>

      <div class="form-field">
        <label for="memberName">Nom</label>
        <input id="memberName" type="text" v-model="last_name" required>
      </div>

      <div class="form-field">
        <label for="memberFirstName">Prénom</label>
        <input id="memberFirstName" type="text" v-model="first_name" required>
      </div>

      <div class="form-field">
        <label for="memberRole">Role</label>
        <select id="memberRole" v-model="role" required>
          <option value="user">User</option>
          <option value="admin">Admin</option>
        </select>
      </div>

      <div class="form-field">
        <label for="memberPassword">Mot de passe</label>
        <input id="memberPassword" type="password" v-model="password" required>
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
        first_name: '',
        last_name: '',
        email: '',
        password: '',
        role: '',
    };
  },

  methods: {
    async createUser() {
      const token = localStorage.getItem('token');
      const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      };
      try {
        const userData = {
          last_name: this.last_name,
          first_name: this.first_name,
          email: this.email,
          password: this.password,
          role: this.role,
        };
        console.log('Inscription en cours', userData);
        const response = await axios.post('http://localhost:3000/user', userData, {headers});
        console.log('Inscription réussie', response.data);
        this.$emit('userCreated');
      } catch (error) {
        console.error('Erreur lors de l\'inscription', error.response ? error.response.data.error : error);
        console.log('Inscription échouée');
        console.log('Inscription échouée', error);
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
