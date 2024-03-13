<template>
  <div class="gestionMembres">
    <h1>Gestion des Utilisateurs</h1>
    <div class="add-member-button">
      <button @click="CreateUserForm">Ajouter un utilisateur</button>
    </div>

    <!-- Formulaire de création de membre -->
    <CreateMemberForm v-if="showCreateMemberForm" @closeForm="closeForm" @saveMember="saveMember"
                      :member="selectedMember"/>
<!--    <EditMemberForm v-if="showEditMemberForm" @closeForm="closeForm" @saveMember="saveMember" @memberUpdated="fetchMembers; closeForm" :member="selectedMember"/>    &lt;!&ndash; Liste des membres &ndash;&gt;-->
<!--    <section class="members">-->
<!--      <h2>Liste des Membres</h2>-->
<!--      <div class="member-list">-->
<!--&lt;!&ndash;        <MembreCard v-for="member in members" :key="member.id" :member="member" @editMember="editMember"  @deleteMember="deleteMember"&ndash;&gt;-->
<!--        />-->
<!--      </div>-->
<!--      -->
<!--    </section>-->
<!--    -->
  </div>
  <Footer/>
</template>

<script>
import NavbarConnected from '../components/NavbarConnected.vue';
import CreateMemberForm from "../components/CreateUserForm.vue";
import EditMemberForm from "../components/EditMemberForm.vue";
import Footer from '../components/Footer.vue';
import axios from 'axios';


export default {
  name: 'GestionMembres',
  components: {
    EditMemberForm,
    NavbarConnected,
    CreateMemberForm,
    Footer,
  },
  data() {
    return {
      members: [],
      showCreateMemberForm: false,
      showEditMemberForm: false,
      selectedMember: null,
    };
  },
  methods: {
    async fetchMembers() {

    },
    CreateUserForm() {
      this.showEditMemberForm = false;
      this.showCreateMemberForm = true;
    },
    editMember(member) {
      this.showCreateMemberForm = false;
      this.selectedMember = member;
      this.showEditMemberForm = true;

    },
    async deleteMember(memberId) {
      try {
        await axios.delete(`http://localhost:8085/membres/${memberId}`);
        this.members = this.members.filter(member => member.id !== memberId);
      } catch (error) {
        console.error('Erreur lors de la suppression du membre', error);
      }
    },
    saveMember(member) {
      // Logique pour sauvegarder le membre dans la BDD
      console.log('Sauvegarder le membre', member);
      this.closeForm();
    },
    closeForm() {
      console.log('Fermer le formulaire');
      this.showCreateMemberForm = false;
      this.showEditMemberForm = false;
    },
  },
  created() {
    this.fetchMembers();
  },

};
</script>

<style>
.gestionMembres {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.add-member-button {
  text-align: center;
  margin-bottom: 20px;
}

.members {
  padding: 20px;
}
</style>
