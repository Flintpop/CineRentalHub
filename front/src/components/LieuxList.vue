<template>
  <div class="lieux-list">
    <div v-for="lieu in lieux" :key="lieu.id" class="lieu-card">
      <h3>{{ lieu.nom }}</h3>
      <p>{{ lieu.adresse }}</p>
      <p>Capacité d'accueil : {{ lieu.capaciteAccueil }}</p>
      <button @click="$emit('editLieu', lieu)">Modifier</button>
      <button @click="$emit('deleteLieu', lieu.id)">Supprimer</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LieuxList',
  props: {
    lieux: Array,
  },
};
</script>

<style scoped>
.lieux-list {
  display: flex;
  flex-wrap: wrap;
  margin: -10px; /* ajustement pour le gap */
}

.lieu-card {
  background-color: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  width: calc(33.333% - 20px); /* 33.333% pour trois colonnes, -20px pour le gap */
  margin: 10px; /* ajustement pour le gap */
  box-sizing: border-box; /* s'assure que padding et border sont inclus dans la largeur */
}

.lieu-card h3 {
  margin-top: 0;
}

.lieu-card button {
  margin-top: 10px;
  margin-right: 10px;
}
</style>
