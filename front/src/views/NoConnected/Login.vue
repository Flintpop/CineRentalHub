<template>
  <Navbar/>
  <div class="login-container">

    <h1>Connexion</h1>
    <form @submit.prevent="login">
      <div class="form-group">
        <label for="email">E-mail:</label>
        <input type="email" id="email" v-model="user.email" required>
      </div>
      <div class="form-group">
        <label for="password">Mot de passe:</label>
        <input type="password" id="password" v-model="user.password" required>
      </div>
      <button type="submit">Se connecter</button>


    </form>

  </div>
<!--  <router-link to="/HomeUser" class="router-link">HomeUser</router-link>-->
<!--  <router-link to="/HomeAdmin" class="router-link">HomeAdmin</router-link>-->
  <Footer/>
</template>

<script>
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import Navbar from "../../components/NoConnected/Navbar.vue";
import Footer from "../../components/Core/Footer.vue";

export default {
  name: 'Login',
  components: {
    Navbar,
    Footer
  },
  mounted() {
   // vérifier si un token est présent dans le local storage
    const token = localStorage.getItem('token');
    if (token) {
      // Si un token est présent, décodez-le pour obtenir les informations de l'utilisateur
      const decoded = jwtDecode(token);
      // Redirigez l'utilisateur vers la page d'accueil en fonction de son rôle
      if (decoded.role === 'admin') {
        this.$router.push('/HomeAdmin');
      } else {
        this.$router.push('/HomeUser');
      }
    }
  },
  data() {
    return {
      user: {
        email: "",
        password: "",
      },
      users: [],
    };
  },
  methods: {
    async login() {
      try {
        console.log('Connexion de l\'utilisateur', this.user.email, this.user.password);
        const response = await axios.post('http://localhost:3000/sign_in', {
          email: this.user.email,
          password: this.user.password,
        }, {
          headers: {
            'Content-Type': 'application/json'
          }
        });

        const { token } = response.data;
        const decoded = jwtDecode(token);

        // Stockez le token, l'ID de l'utilisateur et son rôle dans localStorage
        localStorage.setItem('token', token);
        localStorage.setItem('userId', decoded.userId); // Assurez-vous que c'est bien userId et non id
        localStorage.setItem('role', decoded.role);

        // Redirection basée sur le rôle de l'utilisateur
        if (decoded.role === 'admin') {
          this.$router.push('/HomeAdmin');
        } else {
          this.$router.push('/HomeUser');
        }
      } catch (error) {
        this.$handleError(error);
      }
    }
  },
};
</script>

<style>
.login-container {
  max-width: 300px;
  margin: 50px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  text-align: left; /* Centrer le texte */
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group input {
  box-sizing: border-box; /* Ajouter cette ligne */
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-bottom: 10px;
  margin-right: 10px;
}

button {
  display: block; /* Ajouter cette ligne */
  margin: auto; /* Ajouter cette ligne */
  padding: 10px 20px;
  background-color: #3498db;
  text-align: center;
  border: none;
  border-radius: 4px;
  color: white;
  cursor: pointer;
  margin-top: 10px;
}

.router-link {
  padding: 10px 20px;
  background-color: #3498db;
  text-align: center;
  border: none;
  border-radius: 4px;
  color: white;
  cursor: pointer;
  margin-top: 10px; /* Ajouter de l'espacement au-dessus du lien */
}

button:hover {
  background-color: #2980b9;
}
h1 {
  color: #007bff;
  text-align: center;
}
</style>