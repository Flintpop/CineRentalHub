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
        <input type="password" id="password" v-model="user.motDePasse" required>
      </div>
      <!--        bouton avec redirection vers la page HommeConnected en type bouton et non routerlink-->
      <button type="submit">Se connecter</button>

    </form>
  </div>
  <Footer/>
</template>

<script>
import Navbar from "../components/Navbar.vue";
import Footer from "../components/Footer.vue";
import axios from "axios";

export default {
  name: 'Login',
  components: {
    Navbar,
    Footer
  },
  mounted() {
    if (localStorage.getItem('membreId')) {
      this.$router.push('/homeConnected');
    }
  },
  data() {
    return {
      user: {
        email: '',
        motDePasse: '',
      },
      membres: [],
    };
  },
  methods: {
    async login() {
      await axios.get("http://localhost:8085/membres")
          .then(response => {
            this.membres = response.data;
            let estConnecte = false;

            for (let i = 0; i < this.membres.length; i++) {
              if (this.membres[i].mail === this.user.email && this.membres[i].motDePasse === this.user.motDePasse) {
                localStorage.setItem('membreId', this.membres[i].id);
                estConnecte = true;
                this.$router.push('/homeConnected'); // Redirection vers la page souhaitée
                return; // Pour arrêter la boucle et la fonction après la réussite de la connexion
              }
            }

            if (!estConnecte) {
              alert('E-mail ou mot de passe incorrect');
            }
          })
          .catch(error => {
            console.error("Il y a eu un problème avec la requête de l'API :", error);
          });
    },
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
  text-align: center;
  border: none;
  border-radius: 4px;
  color: white;
  cursor: pointer;
}

.router-link {
  padding: 10px 20px;
  background-color: #3498db;
  text-align: center;
  border: none;
  border-radius: 4px;
  color: white;
  cursor: pointer;
}

button:hover {
  background-color: #2980b9;
}
</style>
