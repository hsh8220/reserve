import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'

Vue.use(Vuex)

const enhanceAccessToeken = () => {
  const {accessToken} = sessionStorage
  if (!accessToken) return
  axios.defaults.headers.common['accessToken'] = accessToken;
}
enhanceAccessToeken()

export default new Vuex.Store({
    state: {
      accessToken: null,
      userId: null,
      name: null,
      result: null,
    },
    getters: {
      getUserId: state => state.userId,
      getName: state => state.name,
      getAccessToken: state => state.accessToken,
      getResult: state => state.result
    },
    mutations: {
      LOGIN(state, member) {
        state.accessToken = member.accessToken,
          state.userId = member.member.userId,
          state.name = member.member.name,
          state.result = member.result,
          sessionStorage.accessToken = member.accessToken,
          sessionStorage.userId = member.member.userId
      },
      LOGOUT(state) {
        state.accessToken = null,
          state.userId = null,
          state.name = null,
          state.result = null,
          sessionStorage.clear();
      }
    },
    actions: {
      LOGIN({commit}, member) {
        let result =  axios.post('login', member)
          .then(({data}) => {
            commit('LOGIN', data)
            axios.defaults.headers.common['accessToken'] = data.accessToken;
          });
        return result;
      },
      LOGOUT({commit}) {
        axios.defaults.headers.common['accessToken'] = undefined
        commit('LOGOUT')
      }
    }
  }
)
