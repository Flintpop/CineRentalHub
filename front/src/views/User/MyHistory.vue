<template>
  <NavbarUser></NavbarUser>
  <div class="viewing-history">
    <h2>Historique de visionnage</h2>
    <ul>
      <li v-for="entry in viewingHistory" :key="entry.id">
        Utilisateur ID: {{ entry.user_id }}, Film ID: {{ entry.movie_id }}, Date de visionnage: {{ entry.view_date }}
      </li>
    </ul>
  </div>
</template>

<script>
import NavbarUser from "../../components/User/NavbarUser.vue";

export default {
  name: 'MyHistory',
  components: {NavbarUser},
  data() {
    return {
      viewingHistory: []
    }
  },
  mounted() {
    this.fetchHistory();
  },
  methods: {
    fetchHistory() {
      try {
        // Remplacez `fetchViewingHistory` par la fonction globale réelle
        let userId = localStorage.getItem('userId');
        this.$getViewingStats(userId).then((res) => {
          this.viewingHistory = res
        });
        if (!this.viewingHistory) {
          this.viewingHistory = this.defaultData();
        }
        console.log("Historique de visionnage : ", this.viewingHistory)
        console.log("Différence avec default data :", this.defaultData())
      } catch (error) {
        console.error("Erreur lors de la récupération de l'historique de visionnage: ", error);
        this.viewingHistory = this.defaultData();
      }
    },
    defaultData() {
      return [
        {
          "id": 1,
          "user_id": 1,
          "movie_id": 1,
          "view_date": "Jan 1, 2020, 8:00:00PM"
        },
        {
          "id": 2,
          "user_id": 4,
          "movie_id": 1,
          "view_date": "Mar 21, 2024, 8:42:11AM"
        },
        {
          "id": 3,
          "user_id": 6,
          "movie_id": 1,
          "view_date": "Mar 21, 2024, 8:49:50AM"
        }
      ];
    }
  }
}
</script>

<style scoped>
.viewing-history {
  padding: 20px;
  background-color: #f0f0f0;
  border-radius: 8px;
  max-width: 600px;
  margin: auto;
}

.viewing-history h2 {
  text-align: center;
  color: #333;
}

.viewing-history ul {
  list-style-type: none;
  padding: 0;
}

.viewing-history li {
  background-color: #fff;
  margin: 10px 0;
  padding: 10px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}
</style>
