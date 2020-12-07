import Vue from 'vue'
import router from './router'
import App from './App'
import { store } from './store'

import {HttpLink} from 'apollo-link-http'
import {setContext} from 'apollo-link-context'
import {ApolloClient} from 'apollo-client'
import {InMemoryCache} from 'apollo-cache-inmemory'
import VueApollo from 'vue-apollo'

const httpLink = new HttpLink({
  uri: 'http://localhost:8080/graphql',
  withCredentials: true
})

const authLink = setContext((_, { headers }) => {
  // get the auth token from local storage if it exists
  const token = localStorage.getItem('blog-app-token')
  return {
    headers: {
      ...headers,
      authorization: token || null
    }
  }
})

export const apolloClient = new ApolloClient({
  link: authLink.concat(httpLink),
  cache: new InMemoryCache()
})

Vue.use(VueApollo)

const apolloProvider = new VueApollo({
  defaultClient: apolloClient
})

// eslint-disable-next-line no-new
new Vue({
  el: '#app',
  store,
  router,
  apolloProvider,
  template: '<App/>',
  components: { App }
})
