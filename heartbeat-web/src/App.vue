<template>
  <div id="app">
    <nav class="navbar is-primary" role="navigation" aria-label="main navigation">
      <div class="container">
        <div class="navbar-brand">
          <router-link class="navbar-item" to="/"><strong>Blog App</strong></router-link>

          <button class="button navbar-burger">
            <span></span>
            <span></span>
            <span></span>
          </button>
        </div>

        <div class="navbar-end">
          <div class="navbar-item">
            <div v-if="!isLoggedIn" class="buttons">
              <router-link to="/signup">
                <a class="button is-primary"><strong>Sign up</strong></a>
              </router-link>
              <router-link to="/login">
                <a class="button is-light">Log in</a>
              </router-link>
            </div>
            <div v-else class="buttons">
              <a v-on:click="logoutUser" class="button is-primary">
                <strong>Logout</strong>
              </a>
            </div>
          </div>
        </div>
      </div>
    </nav>
    <router-view/>
  </div>
</template>

<script>
import {mapGetters, mapActions} from 'vuex'

export default {
  name: 'app',
  computed: {
    ...mapGetters(['getRole', 'isLoggedIn'])
  },
  methods: {
    ...mapActions(['logout']),
    logoutUser () {
      this.logout()
      this.$router.replace('/login')
    }
  }
}
</script>
