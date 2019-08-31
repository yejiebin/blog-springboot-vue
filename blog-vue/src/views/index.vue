<template>
  <el-container>
    <el-main style="width: 75%">
      <indexBlogList/>
    </el-main>
    <el-aside id="aside" style="width: 20%" class="hidden-xs-only">
      <div style="margin: 10px" >
        <introduction/>
        <newDiscuss/>
        <hotBlog/>
        <statisticalBlog/>
      </div>
    </el-aside>
  </el-container>
</template>

<script>
  import indexBlogList from '@/components/indexBlogList'
  import introduction from '@/components/introduction'
  import newDiscuss from '@/components/newDiscuss'
  import hotBlog from '@/components/hotBlog'
  import statisticalBlog from '@/components/statisticalBlog'

  export default {
    name: "index",
    components: {indexBlogList,introduction,newDiscuss,hotBlog,statisticalBlog}
  }
</script>

<style scoped>

</style>
