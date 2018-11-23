// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import Vuetify from 'vuetify'
import axios from 'axios'
import moment from "moment"
import VueMomentJS from "vue-momentjs"

import 'vuetify/dist/vuetify.min.css'
import 'material-design-icons-iconfont/dist/material-design-icons.css'

Vue.config.productionTip = false

Vue.prototype.$http = axios

Vue.use(Vuetify)
Vue.use(VueMomentJS, moment)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App }
})
