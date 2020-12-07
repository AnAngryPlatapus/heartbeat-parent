<template>
  <section class="section">
    <div class="container">
      <div class="columns">
        <div class="column is-3">
          <Menu />
        </div>
        <div class="column is-9" >
            <h2 class="title">Add Post</h2>
            <form method="post" @submit.prevent="addPost">
              <div class="field">
                <label class="label">Title</label>

                <p class="control">
                  <input
                    class="input"
                    v-model="title"
                    placeholder="Post title">
                </p>
              </div>

              <div class="field">
                <label class="label">Content</label>

                <p class="control">
                  <textarea
                    class="textarea"
                    rows="10"
                    v-model="content"
                    placeholder="Post content" />
                </p>
              </div>
              <p class="control">
                <button class="button is-primary">Add Post</button>
              </p>
            </form>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import Menu from './Menu'
import {ADD_POST_MUTATION, ALL_POSTS_QUERY} from '../../graphql'

export default {
  name: 'AddPost',
  components: {Menu},
  data () {
    return {
      title: '',
      content: ''
    }
  },
  methods: {
    addPost () {
      this.$apollo.mutate({
        mutation: ADD_POST_MUTATION,
        variables: {
          title: this.title,
          content: this.content
        },
        update: (store, {data: {addPost}}) => {
          const data = store.readQuery({query: ALL_POSTS_QUERY})
          data.allPosts.push(addPost)
          store.writeQuery({ query: ALL_POSTS_QUERY, data })
        }
      })
        .then(() => {
          this.$router.replace('/admin/posts')
        })
    }
  }
}
</script>
l
