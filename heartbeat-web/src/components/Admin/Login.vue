<template>
 <section class="section">
   <div class="columns">
     <div class="column is-4 is-offset-4">
       <h2 class="title has-text-centered">Login</h2>

       <form method="post" @submit.prevent="loginUser">
         <div class="field">
           <label class="label">Username</label>

           <p class="control">
             <input type="text"
                    class="input"
                    v-model="username">
           </p>
         </div>

         <div class="field">
           <label class="label">Password</label>

           <p class="control">
             <input type="password"
                    class="input"
                    v-model="password">
           </p>
         </div>

         <p class="control">
           <button class="button is-primary is-fullwidth is-uppercase">Login</button>
         </p>

       </form>
     </div>
   </div>
 </section>
</template>

<script>
import { mapActions } from 'vuex'
export default {
  name: 'Login',
  data () {
    return {
      username: '',
      password: ''
    }
  },
  methods: {
    ...mapActions(['login']),
    loginUser () {
      const requestOptions = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({username: this.username, password: this.password})
      }
      // TODO add url constants to app config and change to gql
      fetch('http://localhost:8080/auth/signIn', requestOptions)
        .then(response => response.json())
        .then(data => {
          console.log('set app token')
          localStorage.setItem('blog-app-token', `Bearer ${data.token}`)
          this.login()
        })
    }
  }
}
</script>
