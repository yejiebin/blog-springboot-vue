<template>
  <div id="app">
    <bar/>
    <router-view/>
    <buttom/>
  </div>
</template>

<script>
  import bar from '@/components/bar'
  import buttom from '@/components/buttom'
export default {
  name: 'App',
  components: {bar, buttom}

}
</script>

<style>
#app {
  font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
  /*-webkit-font-smoothing: antialiased;*/
  /*-moz-osx-font-smoothing: grayscale;*/
  text-align: center;
  /*color: #2c3e50;*/
  margin-top: 0px;
  background-color: rgba(242, 245, 252, 0.3);
}
</style>
