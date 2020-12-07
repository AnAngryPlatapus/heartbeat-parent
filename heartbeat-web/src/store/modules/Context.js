import {apolloClient} from '../../main'
import {CURRENT_CONTEXT_QUERY} from '../../graphql'
import router from '../../router'

const state = {
  active: false,
  role: null
}

const getters = {
  getRole: state => state.role,
  isLoggedIn: (state) => state.active
}

const mutations = {
  SET_ACTIVE (state, active) {
    state.active = active
  },
  SET_ROLE (state, role) {
    state.role = role
  }
}

const actions = {
  login ({ commit, dispatch }) {
    apolloClient.query({query: CURRENT_CONTEXT_QUERY}).then(userInfo => {
      commit('SET_ACTIVE', true)
      const role = userInfo.data.currentUser.role
      commit('SET_ROLE', role)
      if (role === 'ADMIN') {
        router.replace('/admin/posts')
      } else if (role === 'USER') {
        router.replace('/')
      }
    }).catch(e => {
      console.log(e)
    })
  },
  logout ({ commit, dispatch }) {
    commit('SET_ACTIVE', false)
    commit('SET_ROLE', null)
    apolloClient.resetStore().then()
    localStorage.setItem('blog-app-token', null)
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
