import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue';
import Login from '../views/Login.vue';
import Register from '../views/Register.vue';
import About from "../views/About.vue";
import manageUsers from "../views/ManageUsers.vue";
import MovieDetailsPage from "../views/MovieDetailsPage.vue";

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
        path: '/about',
        name: 'about',
        component: About,
    },
    {
        path: '/ManageUsers',
        name: 'manageUsers',
        component: manageUsers,
    },
    {
        path: '/MovieDetailsPage/:movieId',
        name: 'MovieDetailsPage',
        component: MovieDetailsPage
    }
];

const router = createRouter({
    history: createWebHistory('/'),
    routes,
});

export default router;
