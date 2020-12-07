import Vue from 'vue'
import Vuex from 'vuex'
import Context from './modules/Context'
import createPersistedState from 'vuex-persistedstate'

Vue.use(Vuex)

export const store = new Vuex.Store({
  plugins: [createPersistedState({
    storage: window.sessionStorage
  })],
  modules: {
    Context
  }
})
