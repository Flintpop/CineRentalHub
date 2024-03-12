<template>
  <div class="lieu-form-card">
    <form @submit.prevent="onSubmit">
      <div class="form-group">
        <label for="nom">Nom du lieu:</label>
        <input id="nom" v-model="lieu.nom" type="text" required>
      </div>

      <div class="form-group">
        <label for="adresse">Adresse:</label>
        <input id="adresse" v-model="lieu.adresse" type="text" required>
      </div>

      <div class="form-group">
        <label for="capaciteAccueil">Capacité d'accueil:</label>
        <input id="capaciteAccueil" v-model="lieu.capaciteAccueil" type="number" min="0" required>
      </div>

      <button type="submit" class="submit-btn">Ajouter / modifier</button>
    </form>
  </div>
</template>

<script>
export default {
  props: {
    lieu: {
      type: Object,
      default: () => ({ nom: '', adresse: '', capaciteAccueil: 0 })
    }
  },
  methods: {
    onSubmit() {
      this.$emit('saveLieu', this.lieu);
    }
  }
};
</script>

<style scoped>
.lieu-form-card {
  background-color: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  max-width: 500px;
  margin: 0 auto;
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
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.submit-btn {
  background-color: #4CAF50;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  display: block;
  width: 100%;
}
</style>
