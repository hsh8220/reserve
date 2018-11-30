import Vue from 'vue'
import store from '@/store'
import Router from 'vue-router'
import Login from '@/components/Login'
import Join from '@/components/Join'
import Home from '@/components/Home'
import SpotList from '@/components/SpotList'
import State from '@/components/State'
import Setting from '@/components/Setting'
import UserAdd from '@/components/UserAdd'
import UserEdit from '@/components/UserEdit'
import SpotAdd from '@/components/SpotAdd'

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
        },
        {
          path: 'setting',
          component: Setting
        },
        {
          path: 'userAdd',
          component: UserAdd
        },
        {
          path: 'spotAdd',
          component: SpotAdd
        },
        {
          path: 'userEdit',
          component: UserEdit
        }
      ],
      beforeEnter: requireAuth()
    }
  ]
})
