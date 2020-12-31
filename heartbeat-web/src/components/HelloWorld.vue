<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <div>
      <grid :cols="cols" :rows="rows"></grid>
    </div>
  </div>
</template>

<script>
  import Grid from 'gridjs-vue'
  import Test from './Test.vue'

  export default {
    name: 'HelloWorld',
    components: {
      Grid
    },
    props: {
      msg: String,
    },
    data() {
      return {
        cols: ['Make', 'Model', 'Year', 'Color'],
        rows: [
          [Test, 'Fusion', '2011', 'Silver'],
          ['Chevrolet', 'Cruz', '2018', 'White']
        ]
      }
    }
  };
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  h3 {
    margin: 40px 0 0;
  }

  ul {
    list-style-type: none;
    padding: 0;
  }

  li {
    display: inline-block;
    margin: 0 10px;
  }

  a {
    color: #42b983;
  }
</style>
