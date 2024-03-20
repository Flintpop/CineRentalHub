import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import './style.css';
import utils from './utils/api.js';

const app = createApp(App);

// Utiliser le plugin 'utils' avec Vue 3.
app.use(utils);
app.use(router);

app.config.globalProperties.$configureAxios();

app.mount('#app');
