<template>
  <div id="indexBlogList" v-loading="loading">
    <div>
      <blogOverView v-for="blog in blogList" :key="blog.id"
                    :id="blog.id"
                    :title="blog.title"
                    :body="blog.body"
                    :time="getTime(blog.time)"
                    :blogViews="blog.blogViews"
                    :discussCount="blog.discussCount"
                    :tags="catchTagName(blog.tags)"
                    :name="blog.user.name"
                    @reload="reloadBlogData"/>
    </div>

    <div v-if="loading" style="margin: 35% 0">

    </div>


    <div>
      <el-pagination
        :page-size="pageSize"
        background
        layout="prev, pager, next"
        :total="total"
        @current-change="currentChange"
        :current-page="currentPage"
        @prev-click="currentPage=currentPage-1"
        @next-click="currentPage=currentPage+1">
      </el-pagination>
    </div>
  </div>

</template>

<script>
  import blogOverView from '@/components/blogOverView'
  import date from '@/utils/date'
  import blog from '@/api/blog'

  export default {
    name: "indexBlogList",
    components: {blogOverView},
    data () {
      return {
        total: 0,        //数据总数
        blogList: [],
          // [
        //   {
        //     "id": 1,
        //     "title": "文章一",
        //     "body": "你好",
        //     "time": 1566388964000,
        //     "blogViews": 10,
        //     "discussCount": 0,
        //     "tags": [{"name": "Spring"}, {"name": "Spring Boot"}],
        //     "user": {
        //       "name": "木水杰"
        //     }
        //   },
        //   {
        //     "id": 2,
        //     "title": "文章2",
        //     "body": "你好",
        //     "time": 1566368964000,
        //     "blogViews": 10,
        //     "discussCount": 0,
        //     "tags": [{"name": "Spring"}, {"name": "SpringMVC"}],
        //     "user": {
        //       "name": "木水杰"
        //     }
        //   },
        //   {
        //     "id": 3,
        //     "title": "文章3",
        //     "body": "你好",
        //     "time": 1566368964000,
        //     "blogViews": 10,
        //     "discussCount": 0,
        //     "tags": [{"name": "Spring"}, {"name": "SpringMVC"}],
        //     "user": {
        //       "name": "木水杰"
        //     }
        //   }
        // ],   //当前页数据
        pageSize: 5,    //每页显示数量
        currentPage: 1,   //当前页数
        loading: false   //true
      }
    },

    created () {
      this.loadBlog()
    },

    methods: {
      loadBlog () { //加载数据
        blog.getBlogHome(this.currentPage, this.pageSize).then(responese => {
          this.total = responese.data.total;
          this.blogList = responese.data.rows;
          this.loading = false;
        });
      },
      reloadBlogData (data) {
        // console.log(data);
        this.loadBlog();
      },
      getTime (time) {  //将时间戳转化为几分钟前，几小时前
        return date.timeago(time)
      },
      currentChange (currentPage) {//页码更改事件处理
        this.currentPage = currentPage;
        this.loadBlog();
        scrollTo(0, 0);
      },
      catchTagName (tags) {
        var tagNames = [];
        for (var i = 0; i < tags.length; i++) {
          tagNames.push(tags[i].name);
        }
        return tagNames;
      }
    }
  }
</script>

<style scoped>

</style>
