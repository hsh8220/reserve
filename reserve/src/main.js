// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import 'babel-polyfill'
import 'event-source-polyfill'
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import Vuetify from 'vuetify'
import axios from 'axios'
import moment from 'moment'
import VueMomentJS from 'vue-momentjs'
import Loader from './components/Loader.vue'
import firebase from 'firebase/app';
import 'firebase/app';
import 'firebase/messaging';

import 'vuetify/dist/vuetify.min.css'
import 'material-design-icons-iconfont/dist/material-design-icons.css'

let config = {
  apiKey: "AIzaSyDvNwohFBDR3C6B6lr6M50Pb681NohFEnA",
  authDomain: "reserve-c602b.firebaseapp.com",
  databaseURL: "https://reserve-c602b.firebaseio.com",
  projectId: "reserve-c602b",
  storageBucket: "reserve-c602b.appspot.com",
  messagingSenderId: "1048874769144"
};
firebase.initializeApp(config)

const messaging = firebase.messaging()


navigator.serviceWorker.register('/firebase-messaging-sw.js')
  .then((registration) => {
    messaging.useServiceWorker(registration)
  }).catch(err => {
    console.log(err)
})

messaging.usePublicVapidKey("BDQdI8m3vMBYjHdKnPOXJY2AOJ5EDMnl1BOrFggY7B5YBpVLHDt1p81UHUejobuNXrawYIb_ZeIynfhtkOnG1Kk");

messaging.requestPermission().then(function () {
  console.log('Notification permission grated.')
}).catch(function (err) {
  console.log('Unable to get permission to notify.', err)
})

Vue.config.productionTip = false

Vue.prototype.$http = axios
Vue.prototype.$messaging = messaging

Vue.use(Vuetify)
Vue.use(VueMomentJS, moment)
Vue.component('Loader', Loader)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App }
})
