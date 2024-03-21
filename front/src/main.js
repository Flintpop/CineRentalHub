import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import './style.css';
import utils from './utils/api.js';

const app = createApp(App);

// Utiliser le plugin 'utils' avec Vue 3.
app.use(utils);
app.use(router);
// FontAwesome Import

import { library } from '@fortawesome/fontawesome-svg-core';
import { faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

library.add(faTrashAlt);
app.config.globalProperties.$configureAxios();
app.component('font-awesome-icon', FontAwesomeIcon);
app.mount('#app');
