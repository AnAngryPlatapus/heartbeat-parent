<template>
  <section class="section">
    <div class="columns">
      <div class="column is-4 is-offset-4">
        <h2 class="title has-text-centered">Sign Up</h2>

        <form method="POST" @submit.prevent="signUp">
          <div class="field">
            <label class="label">Username</label>
<!--             TODO check refactor -->
            <p class="control">
              <input
                type="text"
                class="input"
                v-model="username">
            </p>
          </div>

          <div class="field">
            <label class="label">Email Address</label>

            <p class="control">
              <input
                type="email"
                class="input"
                v-model="email">
            </p>
          </div>

          <div class="field">
            <label class="label">Password</label>

            <p class="control">
              <input
                type="password"
                class="input"
                v-model="password">
            </p>
          </div>
          <p class="control">
            <button class="button is-primary is-fullwidth is-uppercase">SignUp</button>
          </p>
        </form>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  name: 'SignUp',
  data () {
    return {
      username: '',
      email: '',
      password: ''
    }
  },
  methods: {
    signUp () {
      const requestOptions = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          username: this.username,
          email: this.email,
          password: this.password
        })
      }
      fetch('http://localhost:8080/auth/signUp', requestOptions)
        .then(response => {
          this.$router.replace('/login')
        })
    }
  }
}
</script>
