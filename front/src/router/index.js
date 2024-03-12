import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue';
import Login from '../views/Login.vue';
import Register from '../views/Register.vue';
import HomeConnected  from "../views/HomeConnected.vue";
import GestionMembre from "../views/GestionMembre.vue";
import GestionEvenement from "../views/GestionEvenement.vue";
import GestionLieu from "../views/GestionLieu.vue";
import About from "../views/About.vue";

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home,
    },
    {
        path: '/login',
        name: 'Login',
        component: Login,
    },
    {
        path: '/register',
        name: 'Register',
        component: Register,
    },
    {
        path: '/homeConnected',
        name: 'homeConnected',
        component: HomeConnected,
    },
    {
        path: '/gestionMembre',
        name: 'gestionMembre',
        component: GestionMembre,
    },
    {
        path:'/gestionEvenement',
        name:'gestionEvenement',
        component: GestionEvenement,
    },
    {
        path: '/gestionLieu',
        name: 'gestionLieu',
        component: GestionLieu,
    },
    {
        path: '/about',
        name: 'about',
        component: About,
    }
    // ...autres routes
];

const router = createRouter({
    history: createWebHistory('/'),
    routes,
});

export default router;
