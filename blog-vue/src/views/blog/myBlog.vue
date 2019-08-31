<template>
  <div id="myBlog">
    <el-container>
      <el-main style="width: 75%">
        <myBlogList/>
      </el-main>
      <el-aside id="aside" style="width: 20%" class="hidden-xs-only">
        <div style="margin: 10px">
          <userNewDiscuss/>
        </div>
        <div>
          <myTag/>
        </div>
      </el-aside>
    </el-container>

  </div>
</template>

<script>
  import myBlogList from '@/components/myBlogList'
  import userNewDiscuss from '@/components/userNewDiscuss'
  import myTag from '@/components/myTag'
  export default {
    name: "myBlog",
    components: {myBlogList, userNewDiscuss, myTag},
  }
</script>

<style scoped>

</style>
