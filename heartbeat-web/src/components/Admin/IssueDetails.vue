<template>
  <section class="section">
    <div class="container">
      <div class="columns">
        <div class="column is-3">
          <Menu />
        </div>
        <div class="column is-9">
          <h2 class="title">User Details</h2>

          <div class="field is-horizontal">
            <div class="field-label is-normal">
              <label class="label">Username</label>
            </div>
            <div class="field-body">
              <div class="field">
                <div class="control">
                  <input class="input is-static" :value="user.username" readonly>
                </div>
              </div>
            </div>
          </div>

          <div class="field is-horizontal">
            <div class="field-label is-normal">
              <label class="label">Email Address</label>
            </div>
            <div class="field-body">
              <div class="field">
                <div class="control">
                  <input class="input is-static" :value="user.email" readonly>
                </div>
              </div>
            </div>
          </div>

          <div class="field is-horizontal">
            <div class="field-label is-normal">
              <label class="label">Number of posts</label>
            </div>
            <div class="field-body">
              <div class="field">
                <div class="control">
                  <input class="input is-static" :value="user.posts.length" readonly>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import Menu from './Menu'
import {USER_QUERY} from '../../graphql'
export default {
  name: 'UserDetails',
  components: {Menu},
  data () {
    return {
      user: {
        id: '',
        posts: [],
        email: '',
        username: ''
      },
      id: this.$route.params.id
    }
  },
  apollo: {
    user: {
      query: USER_QUERY,
      variables () {
        return {
          id: this.id
        }
      }
    }
  }
}
</script>
