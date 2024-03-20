<template>
  <Navbar></Navbar>
  <div class="chat-container">
    <div v-for="message in messages" :key="message.id" class="message">
      <div class="message-text">{{ message.text }}</div>
      <div class="message-time">{{ formatTime(message.created_at) }}</div>
    </div>
    <input v-model="newMessage" @keyup.enter="sendMessage">
  </div>
</template>

<script>
import io from 'socket.io-client';
import Navbar from "../../components/User/NavbarUser.vue";

const baseUrl = process.env.NODE_ENV === 'production' ?
    'https://app-779d80a6-6ecb-4f58-8311-7029241cbbd8.cleverapps.io' : 'http://localhost:3001';
const socket = io(baseUrl);
export default {
  name: 'ChatMessages',
  components: {Navbar},
  data() {
    return {
      // Exemple de messages
      messages: [
        {id: 1, text: 'Bonjour, comment vas-tu ?', created_at: '2023-11-01 10:00:00'},
        {id: 2, text: 'Je vais bien, merci ! Et toi ?', created_at: '2023-11-02 10:15:00'},
        {id: 3, text: 'Moi aussi, je vais bien. Quoi de neuf ?', created_at: '2023-11-02 10:30:00'},
        {
          id: 4,
          text: 'Pas grand-chose, juste en train de travailler sur un projet.',
          created_at: '2023-11-03 11:00:00'
        },
        {id: 5, text: 'Ah intéressant ! De quoi s\'agit-il ?', created_at: '2023-11-03 11:05:00'}
      ],
      newMessage: ''
    };
  },
  mounted() {
    socket.on('chat message', (msg) => {
      this.messages.push({text: msg});
    });
  },
  methods: {
    formatTime(datetime) {
      if (!datetime) {
        const now = new Date();

        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0'); // getMonth() retourne un mois de 0 à 11
        const day = String(now.getDate()).padStart(2, '0');

        const hours = String(now.getHours()).padStart(2, '0');
        const minutes = String(now.getMinutes()).padStart(2, '0');
        const seconds = String(now.getSeconds()).padStart(2, '0');

        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
      }
      return datetime;
    },
    sendMessage() {
      socket.emit('chat message', this.newMessage);
      this.newMessage = '';
    }
  }
};
</script>

<style>
.chat-container {
  max-width: 600px;
  margin: auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
}

.message {
  padding: 10px;
  margin-bottom: 10px;
  background: #f1f1f1;
  border-radius: 8px;
  color: #282828;
}

.message-text {
  margin-bottom: 5px;
  font-size: 16px;
}

.message-time {
  font-size: 12px;
  text-align: right;
  color: #666;
}
</style>
