<template>
  <div class="main-body">
    <NavbarAdmin/>
    <div class="main-content">
      <h1>Gestion des utilisateurs</h1>
      <button class="create-user-button" @click="createUser">Créer un utilisateur</button>
      <div class="content-wrapper">
        <div class="user-list">

          <!-- Boucle sur les rôles prédéfinis -->
          <div v-for="role in roles" :key="role" class="role-section">
            <h1 class="role-title">{{ role }}</h1>
            <!-- Boucle sur les utilisateurs du rôle actuel -->
            <div class="user-card" @click="selectUser(user)"
                 v-for="user in usersFilteredByRole(role)"
                 :key="user.id">
              <div class="user-info" @click="selectUser(user, 'details')">
                <h3>{{ user.last_name }} {{ user.first_name }}</h3>
                <p><strong> Email : </strong> {{ user.email }}</p>
                <p><strong> Activé : </strong> {{ user.activated ? 'Oui' : 'Non' }}</p>
                <p><strong> Rôle : </strong> {{ user.role }}</p>
                <div class="user-actions">

                  <!-- Boutons d'actions, notez l'utilisation de stop pour empêcher la propagation de l'événement au conteneur parent -->
                  <button @click.stop="selectUser(user, 'edit')"> Modifier </button>
                  <button @click.stop="deleteUser(user)"> Supprimer </button>
                  <button @click.stop="selectUser(user, 'rentedMovies')"> Films loués </button>
                  <button @click.stop="selectUser(user, 'purchasedMovies')"> Films achetés </button>
                </div>
              </div>
            </div>
          </div>

        </div>

        <div class="user-details">
          <div class="user-card">

            <!-- Les composants de détail / modification sont affichés ici -->
            <!-- Affichage conditionnel basé sur l'utilisateur sélectionné -->
            <div v-if="selectedUser">
              <component v-if="currentAction" :is="components[currentAction]" :user="selectedUser"
                         @close="currentAction = null"></component>


            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import NavbarAdmin from '../../components/Admin/NavbarAdmin.vue';
import Footer from '../../components/Core/Footer.vue';
import axios from 'axios';
import UserEditForm from "../../components/Admin/UserEditForm.vue";

export default {
  components: {
    NavbarAdmin,
    Footer,
    UserEditForm,

  },


  data() {
    return {
      users: [],
      selectedUser: null,
      roles: ['admin', 'user'], // Rôles prédéfinis
      currentAction: null,
    };
  },
  computed: {
    components() {
      return {
        // 'details': UserDetailsComponent,
        'edit': UserEditForm,
        // 'delete': UserDeleteComponent,
        // 'rentedMovies': UserRentedMoviesComponent,
        // 'purchasedMovies': UserPurchasedMoviesComponent,
      };
    },
  },
  methods: {

    async fetchUsers() {
      try {
        const response = await axios.get('http://localhost:3000/users');
        this.users = response.data;
      } catch (error) {
        console.error("Erreur lors de la récupération des utilisateurs:", error);
      }
    },
    usersFilteredByRole(role) {
      return this.users.filter(user => user.role === role);
    },
    selectUser(user, action) {
      this.selectedUser = user;
      this.currentAction = action;
    },
    async deleteUser(user) {
      try {
        await axios.delete(`http://localhost:3000/users/${user.id}`);
        this.users = this.users.filter(u => u.id !== user.id);
      } catch (error) {
        console.error("Erreur lors de la suppression de l'utilisateur:", error);
      }
    },
    showRentedMovies(user) {
      // Logique pour afficher les films loués par l'utilisateur
    },
    showPurchasedMovies(user) {
      // Logique pour afficher les films achetés par l'utilisateur
    },
  },
  mounted() {
    this.fetchUsers(); // Charge les utilisateurs dès que le composant est monté
  },
};

</script>

<style scoped>
/* Styles pour votre page */

.main-body {
  max-height: 100vh;
  overflow-y: hidden;
}
.main-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  max-height: 90vh;
  overflow-y: hidden;
}

.content-wrapper {
  display: flex;
  width: 100%;
}

.user-list {
  flex: 1;
  overflow-y: auto; /* Barre de défilement pour la liste des utilisateurs */
  height: 75vh; /* Hauteur fixe pour activer le défilement */
  padding: 20px;
}

.user-details {
  flex: 1;
  padding: 20px;
}

.role-section {
  margin-bottom: 30px;
}

.user-card {
  background: #f9f9f9;
  margin: 10px 0;
  padding: 15px;
  border-radius: 5px;
}

.user-actions button {
  /* Styles pour vos boutons */
  margin-top: 10px; /* Ajouter un peu d'espace au-dessus des boutons */
  width: 100%; /* Assurez-vous que les boutons occupent toute la largeur */
}

body {
  overflow: hidden;
}

</style>