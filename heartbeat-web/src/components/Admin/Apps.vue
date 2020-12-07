<template>
  <section class="section">
    <div class="container">
      <div class="columns">
        <div class="column is-3">
          <Menu />
        </div>
        <div class="column is-9">
          <h2 class="title">Posts</h2>

          <table class="table is-striped is-narrow is-hoverable is-fullwidth">
            <thead>
              <tr>
                <th>title</th>
                <th>User</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="post in allPosts"
                  :key="post.postId">
                <td>{{ post.title }}</td>
                <td>{{ post.blogUser.username }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import Menu from './Menu'
import {ALL_POSTS_QUERY} from '../../graphql'

export default {
  components: {Menu},
  data () {
    return {
      allPosts: []
    }
  },
  apollo: {
    allPosts: {
      query: ALL_POSTS_QUERY
    }
  }
}

</script>
