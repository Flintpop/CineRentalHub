import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/NoConnected/Home.vue';
import Login from '../views/NoConnected/Login.vue';
import Register from '../views/NoConnected/Register.vue';
import About from "../views/NoConnected/About.vue";
import manageUsers from "../views/Admin/ManageUsers.vue";
import HomeUser from "../views/User/HomeUser.vue";
import MyMovies from "../views/User/MyMovies.vue";
import MyHistory from "../views/User/MyHistory.vue";
import MyAccount from "../views/User/MyAccount.vue";
import MyCart from "../views/MyCart.vue";
import MovieDetailsPage from "../views/MovieDetailsPage.vue";
import ManageUsers from "../views/Admin/ManageUsers.vue";
import ManageMovies from "../views/Admin/ManageMovies.vue";
import HomeAdmin from "../views/Admin/HomeAdmin.vue";
import MyMessages from "../views/User/MyMessages.vue";




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
    },
    {
        path: '/HomeAdmin',
        name: 'HomeAdmin',
        component: HomeAdmin
    },
    {
        path: '/ManageUsers',
        name: 'ManageUsers',
        component: ManageUsers
    },
    {
        path: '/ManageMovies',
        name: 'ManageMovies',
        component: ManageMovies,
    },
    {
        path: '/MyMessages',
        name: 'MyMessages',
        component: MyMessages,
    }
];

const router = createRouter({
    history: createWebHistory('/'),
    routes,
});

export default router;
