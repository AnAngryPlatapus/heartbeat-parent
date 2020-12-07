<template>
  <article class="media">
    <figure class="media-left">
      <!--              TODO user image -->
      <p class="image is-64x64">
        <img src="https://bulma.io/images/placeholders/128x128.png">
      </p>
    </figure>
    <div class="media-content">
      <div class="content">
        <p>
          <strong>{{ comment.blogUser.username }}</strong> <small>{{ comment.blogUser.email }}</small>
          <br>
          {{ comment.content }}
        </p>
      </div>
      <!--              TODO Add functionality to comment component-->
      <nav class="level is-mobile">
        <div class="level-left">
          <a class="level-item">
            <span class="icon is-small"><i class="fas fa-reply"></i></span>
          </a>
          <a class="level-item">
            <span class="icon is-small"><i class="fas fa-retweet"></i></span>
          </a>
          <a class="level-item">
            <span class="icon is-small"><iAppServiceStack class="fas fa-heart"></iAppServiceStack></span>
          </a>
        </div>
      </nav>
    </div>
  </article>
</template>
<script>
export default {
  name: 'SingleComment',
  props: {
    comment: Object
  }
}
</script>
