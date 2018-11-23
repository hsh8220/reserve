import Vue from 'vue'
import store from '@/store'
import Router from 'vue-router'
import Login from '@/components/Login'
import Join from '@/components/Join'
import Home from '@/components/Home'
import SpotList from '@/components/SpotList'
import State from '@/components/State'

Vue.use(Router)

const requireAuth = () => (from, to, next) => {
  if (sessionStorage.accessToken) return next()
  next('/login')
}

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Login',
      component: Login
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/join',
      name: 'Join',
      component: Join
    },
    {
      path: '/home',
      name: 'Home',
      component: Home,
      children: [
        {
          path: 'spotList',
          component: SpotList
        },
        {
          path: 'state/:id',
          component: State
        }
      ],
      beforeEnter: requireAuth()
    }
  ]
})
