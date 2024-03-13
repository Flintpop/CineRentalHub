<template>
  <div class="member-card">
    <h3>{{ member.prenom }} {{ member.nom }}</h3>
    <p>Date de naissance: {{ member.dateNaissance }}</p>
    <p>Adresse: {{ member.adresse }}</p>
    <p>Email: {{ member.mail }}</p>
    <button @click="$emit('editMember', member)">Modifier</button>
    <button @click="$emit('deleteMember', member.id)">Supprimer</button>
    <EventMemberList :memberId="member.id" />
  </div>
</template>

<script>
import EventMemberList from './EventMemberList.vue';
console.log(EventMemberList);
export default {
  name: 'MembreCard',
  components: {
    EventMemberList,
  },
  props: {
    member: {
      type: Object,
      required: true,
    },
  },
};
</script>

<style scoped>
.member-card {
  background-color: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.member-card h3 {
  margin-top: 0;
}

.member-card p {
  margin: 5px 0;
  color: #333;
  font-size: 14px;
}

.member-card button {
  margin-top: 10px;
  margin-right: 10px;
}
</style>