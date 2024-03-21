<template>
  <Navbar/>
  <div class="register-container">
    <h1>Inscription</h1>
    <form @submit.prevent="register">
      <div class="form-group">
        <label for="lastName">Nom:</label>
        <input type="text" id="lastName" v-model="user.lastName" placeholder="Entrez votre nom" required>
      </div>
      <div class="form-group">
        <label for="firstName">Prénom:</label>
        <input type="text" id="firstName" v-model="user.firstName" placeholder="Entrez votre prénom" required>
      </div>
      <div class="form-group">
        <label for="email">E-mail:</label>
        <input type="email" id="email" v-model="user.email" placeholder="Entrez votre e-mail" required pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$">
      </div>
      <div class="form-group">
        <label for="password">Mot de passe:</label>
        <input type="password" id="password" v-model="user.password" placeholder="Créez un mot de passe" required>
      </div>
      <div class="form-group">
        <label for="confirmPassword">Confirmation de Mot de passe:</label>
        <input type="password" id="confirmPassword" v-model="user.confirmPassword" placeholder="Confirmez votre mot de passe" required>
      </div>
      <button type="submit">S'inscrire</button>
    </form>
  </div>
  <Footer/>
</template>
<script>
import Navbar from "../../components/NoConnected/Navbar.vue";
import Footer from "../../components/Core/Footer.vue";
import axios from 'axios';


export default {
  name: 'Login',
  components: {
    Navbar,
    Footer
  },
  data() {
    return {
      user: {
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        confirmPassword: '',
        role: 'user'
      },
      emailRegex: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
      passwordRegex: /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$/
    };
  },
  methods: {
      register() {
        if (!this.emailRegex.test(this.user.email)) {
          alert('Veuillez entrer une adresse e-mail valide.');
          return;
        }
        if (!this.passwordRegex.test(this.user.password)) {
          alert('Le mot de passe doit contenir au moins 8 caractères, une lettre majuscule, une lettre minuscule et un chiffre.');
          return;
        }
        if (this.user.password !== this.user.confirmPassword) {
          alert('Les mots de passe ne correspondent pas.');
          return;
        }
        axios.post('http://localhost:3000/user', {
          last_name: this.user.lastName,
          first_name: this.user.firstName,
          email: this.user.email,
          state: 'active',
          password: this.user.password,
          role: 'user'
        })
            .then(response => {
              console.log('Inscription réussie:', response.data);
              this.$router.push('/login');

            })
            .catch(error => {
              console.error('Erreur lors de l\'inscription:', error);
              if (error.response.data.error) {
                alert(error.response.data.error)
              }
            });

      }
    }

};
</script>

<style>
.register-container {
  max-width: 300px;
  margin: 50px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  padding: 10px 20px;
  background-color: #3498db;
  border: none;
  border-radius: 4px;
  color: white;
  cursor: pointer;
}

button:hover {
  background-color: #2980b9;
}
</style>
