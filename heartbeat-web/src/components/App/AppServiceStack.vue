<template>
  <section class="section">
    <div class="columns">
      <div class="column is-6 is-offset-3">
        <router-link class="button is-link is-small" to="/">Back Home</router-link>

        <h1 class="title">
          {{ post.title }}
        </h1>

        <div class="content">
          {{ post.content }}
        </div>

        <div class="content is-small">
          <h2 class="subtitle">Comments</h2>

          <div class="container"
                v-for="comment in post.comments"
                :key="comment.commentId">
            <SingleComment :comment="comment" />
          </div>

          <div class="column is-9">
            <h3 class="title">Add Comment</h3>
              <form method="post" @submit.prevent="addComment">
                <div class="field">
                  <label class="label">Content</label>

                  <p class="control">
                    <textarea
                      class="textarea"
                      rows="7"
                      v-model="content"
                      placeholder="Comment content" />
                  </p>
                  <p class="control">
                    <button class="button is-primary">Add Comment</button>
                  </p>
                </div>
              </form>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import {ADD_COMMENT_MUTATION, POST_QUERY} from '../../graphql'
import SingleComment from './SingleComment'

export default {
  name: 'SinglePost',
  components: {SingleComment},
  data () {
    return {
      post: '',
      slug: this.$route.params.slug,
      content: ''
    }
  },
  methods: {
    addComment () {
      this.$apollo.mutate({
        mutation: ADD_COMMENT_MUTATION,
        variables: {
          slug: this.slug,
          content: this.content
        },
        update: (store, {data: {addComment}}) => {
          const data = store.readQuery({query: POST_QUERY, variables: { slug: this.slug }})
          data.post.comments.push(addComment)
          store.writeQuery({ query: POST_QUERY, variables: { slug: this.slug }, data })
        }
      })
      this.content = ''
    }
  },
  apollo: {
    post: {
      query: POST_QUERY,
      variables () {
        return {
          slug: this.slug
        }
      }
    }
  }
}
</script>
