<template>
  <el-card id="hotBlog">
    <p>
      <span style="color:#67C23A" class="el-icon-document">热门文章</span>
    </p>
    <hr />
    <div v-for="blog in hotBlog">
      <el-link type="info" style="margin: 5px 0" :underline="false"  @click="router(blog.id)">
        《{{blog.title}}》&nbsp;浏览数:&nbsp;{{blog.blogViews}}
      </el-link>
    </div>
    <br/>
  </el-card>
</template>

<script>
  import blog from '@/api/blog'
  export default {
    name: "hotBlog",
    data () {
      return {
        hotBlog: []
      }
    },
    created () {
      // this.hotBlog = [
      //   {id: 1, title: '天亮了', blogViews: 1000},
      //   {id: 2, title: '天亮了2', blogViews: 500},
      //   {id: 3, title: '天亮了3', blogViews: 300},
      //   {id: 4, title: '天亮了4', blogViews: 200}
      // ]
      blog.getHotBlog().then(response => {
        this.hotBlog = response.data;
      })
    },
    methods: {
      router(id) {
        this.$router.push({
          path: '/blog/'+id
        })
      }
    }
  }
</script>

<style scoped>
  #hotBlog {
    /*-moz-box-shadow: 0px 6px 0px #333333;*/
    /*-webkit-box-shadow: 0px 6px 0px #333333;*/
    /*box-shadow: 0px 3px 10px #333333;*/
    text-align: center;

    margin: 20px 0;
  }
</style>
