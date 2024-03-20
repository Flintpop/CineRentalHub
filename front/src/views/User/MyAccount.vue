<template>
  <div class="MyAccount">
    <NavbarUser/>
    <h1>Mon Compte</h1>
    <div v-if="!isEditing" class="user-details">
      <h2>Informations personnelles</h2>
      <p><strong>Prénom :</strong> {{ user.first_name }}</p>
      <p><strong>Nom :</strong> {{ user.last_name }}</p>
      <p><strong>Email :</strong> {{ user.email }}</p>
      <button @click="toggleEdit">Modifier</button>
    </div>

    <UserEditForm v-else :user="user" @updateUser="updateUser" @close="toggleEdit"/>  </div>
</template>

<script>
import NavbarUser from "../../components/User/NavbarUser.vue";
import UserEditForm from "../../components/Core/UserEditForm.vue";
import axios from "axios";

export default {
  name: 'MyAccount',
  components: { NavbarUser, UserEditForm },
  data() {
    return {
      isEditing: false,
      user: {
        id: 1,
        first_name: 'Jean',
        last_name: 'Dupont',
        email: 'jean.dupont@example.com',
        role: 'user',
      }
    };
  },
  methods: {
    toggleEdit() {
      this.isEditing = !this.isEditing;
    },
    updateUser: async (user) => {
      try {
        const token = localStorage.getItem('token');
        const headers = {
          'Content-Type': 'application/json',
          'authorization': 'Bearer ' + token
        };
        const response = await axios.put(`http://localhost:3000/user/${user.id}`, user, {headers});
        console.log('Mise à jour réussie', response.data);
        this.fetchUsers(); // Rafraîchir la liste des utilisateurs après la mise à jour
      } catch (error) {
        console.error('Erreur lors de la mise à jour de l\'utilisateur', error);
      }
    },
  }
};

</script>

<style scoped>
.user-details {
  background: #f4f4f4;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  max-width: 500px;
  margin: auto;
}

.user-details h2 {
  margin-bottom: 20px;
  color: #333;
}

.user-details p {
  margin: 10px 0;
  color: #666;
}

.user-details button {
  cursor: pointer;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  background-color: #007bff;
  color: white;
  transition: background-color 0.3s;
}

.user-details button:hover {
  background-color: #0056b3;
}
</style>
