import gql from 'graphql-tag'

export const ALL_USERS_QUERY = gql`query AllUsersQuery {
  allUsers {
    blogUserId
    username
    email
  }
}`

export const USER_QUERY = gql`query UserQuery($id: Long!) {
  user(id: $id) {
    blogUserId
    username
    email
    posts {
      postId
    }
  }
}`

export const ADD_POST_MUTATION = gql`mutation AddPostMutation($title: String!, $content: String!) {
  addPost(
    title: $title,
    content: $content
  ) {
    postId
    slug
    title
    content
    blogUser {
      blogUserId
      username
      email
    }
  }
}`

export const ALL_POSTS_QUERY = gql`query AllPostsQuery {
  allPosts {
    postId
    title
    slug
    blogUser {
      username
    }
  }
}`

export const POST_QUERY = gql`query PostQuery($slug: String!) {
  post(slug: $slug) {
    postId
    title
    slug
    content
    blogUser {
      blogUserId
      username
      email
    }
    comments {
      commentId
      blogUser {
        blogUserId
        username
      }
      content
    }
  }
}`

export const ADD_COMMENT_MUTATION = gql`mutation AddCommentMutation($slug: String!, $content: String!){
  addComment(postSlug: $slug, content: $content) {
    commentId
    content
    blogUser {
      username
      blogUserId
    }
  }
}`

export const CURRENT_CONTEXT_QUERY = gql`query CurrentContextQuery {
  currentUser {
    username
    blogUserId
    role
  }
}`
