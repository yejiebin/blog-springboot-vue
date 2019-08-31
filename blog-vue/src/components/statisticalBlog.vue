<template>
  <el-card id="statisticalBlog">
    <!--<hr/>-->
    <p>
      <span style="color:#67C23A" class="el-icon-moon">文章归档</span>
    </p>
    <hr />
    <div v-for="count in blogCount">
      <p style="margin: 5px 0;color:#909399">{{count.year}}年{{count.month}}月&nbsp;&nbsp;{{count.count}}篇</p>
    </div>
    <br/>
  </el-card>
</template>

<script>

  import blog from '@/api/blog'
  export default {
    name: "statisticalBlog",
    data () {
      return {
        blogCount: []
      }
    },
    created () {
      // this.blogCount = [
      //   {year: 2019, month: 8, count: 200},
      //   {year: 2019, month: 7, count: 90},
      //   {year: 2019, month: 6, count: 100},
      //   {year: 2019, month: 5, count: 80}
      // ]
      blog.getStatisticalBlogByMonth().then(response => {
        this.blogCount = response.data;
      })
    }
  }
</script>

<style scoped>
  #statisticalBlog {
    /*-moz-box-shadow: 0px 6px 0px #333333;*/
    /*-webkit-box-shadow: 0px 6px 0px #333333;*/
    /*box-shadow: 0px 3px 10px #333333;*/
    text-align: center;

    margin: 20px 0;
  }
</style>
