import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue';
import Login from '../views/Login.vue';
import Register from '../views/Register.vue';
import About from "../views/About.vue";
import manageUsers from "../views/ManageUsers.vue";
import HomeUser from "../views/HomeUser.vue";
import MyMovies from "../views/MyMovies.vue";
import MyHistory from "../views/MyHistory.vue";
import MyAccount from "../views/MyAccount.vue";
import MyCart from "../views/MyCart.vue";
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
        path: '/HomeUser',
        name: 'HomeUser',
        component: HomeUser,
    },
    {
        path: '/MyMovies',
        name: 'MyMovies',
        component: MyMovies,
    },
    {
        path: '/MyHistory',
        name: 'MyHistory',
        component: MyHistory,
    },
    {
        path: '/MyAccount',
        name: 'MyAccount',
        component: MyAccount,
    },
    {
        path: '/MyCart',
        name: 'MyCart',
        component: MyCart,
    },
    // ...autres routes
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
