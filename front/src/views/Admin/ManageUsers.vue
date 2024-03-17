<template>
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
            <div class="user-info">
              <h3>{{ user.last_name }} {{ user.first_name }}</h3>
              <p><strong>Email :</strong> {{ user.email }}</p>
              <p><strong>Activé :</strong> {{ user.activated ? 'Oui' : 'Non' }}</p>
              <p><strong>Rôle :</strong> {{ user.role }}</p>
              <div class="user-actions">
                <button @click="selectUser(user)">Détails</button>
              </div>
            </div>
          </div>
        </div>

      </div>
      <div class="user-details user-card">
        <!-- Les composants de détail / modification sont affichés ici -->
        <!-- Affichage conditionnel basé sur l'utilisateur sélectionné -->
        <div v-if="selectedUser">
          <h3>Détails de {{ selectedUser.last_name }} {{ selectedUser.first_name }}</h3>
          <p><strong>Email :</strong> {{ selectedUser.email }}</p>
          <p><strong>Activé :</strong> {{ selectedUser.activated ? 'Oui' : 'Non' }}</p>
          <p><strong>Rôle :</strong> {{ selectedUser.role }}</p>
          <!-- Boutons d'actions -->
          <button @click="editUser(selectedUser)">Modifier</button>
          <button @click="deleteUser(selectedUser)">Supprimer</button>
          <button @click="showRentedMovies(selectedUser)">Afficher les films loués</button>
          <button @click="showPurchasedMovies(selectedUser)">Afficher les films achetés</button>

        </div>
      </div>
    </div>
  </div>
</template>


<script>
import NavbarAdmin from '../../components/Admin/NavbarAdmin.vue';
import Footer from '../../components/Core/Footer.vue';
import axios from 'axios';

export default {
  components: {
    NavbarAdmin,
    Footer
  },

  data() {
    return {
      users: [],
      selectedUser: null,
      roles: ['admin', 'user'], // Rôles prédéfinis
    };
  },
  computed: {
    uniqueRoles() {
      if (!this.users.length) return []; // S'assure que users n'est pas vide
      return [...new Set(this.users.map(user => user.role))];
    }
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
    selectUser(user) {
      this.selectedUser = user;
    },
    editUser(user) {
      // Logique pour modifier l'utilisateur
    },
    deleteUser(user) {
      // Logique pour supprimer l'utilisateur
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
.main-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.content-wrapper {
  display: flex;
  width: 100%;
}

.user-list {
  flex: 1;
  overflow-y: auto; /* Barre de défilement pour la liste des utilisateurs */
  height: 80vh; /* Hauteur fixe pour activer le défilement */
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
  cursor: pointer;
}


</style>