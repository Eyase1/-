import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/AboutView.vue'),
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/user/LoginRegisterView.vue'),
    },
    {
      path: '/dishes',
      name: 'dishes',
      component: () => import('../views/DishesView.vue'),
    },
    {
      path: '/analysis',
      name: 'analysis',
      component: () => import('../views/AnalysisView.vue'),
    },
    {
      path: '/review',
      name: 'review',
      component: () => import('../views/ReviewView.vue'),
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('../views/ProfileView.vue'),
    },
    {
      path: '/home',
      name: 'home',
      component: () => import('../views/HomeMainView.vue'),
    },
    {
      path: '/user-management',
      name: 'UserManagement',
      component: () => import('../views/user/UserManagementView.vue'),
      meta: {
        requiresAuth: true,
        requiresAdmin: true
      }
    },
    {
      path:'/password-setup',
      component:()=>import('../views/user/PasswordSetupView.vue')
    }
  ],
})

export default router
