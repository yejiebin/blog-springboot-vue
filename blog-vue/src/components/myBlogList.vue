<template>
  <div id="myBlogList">
    <div v-for="blog in blogList">
      <blogOverView :id="blog.id"
                    :title="blog.title"
                    :body="blog.body"
                    :time="getTime(blog.time)"
                    :blogViews="blog.blogViews"
                    :discussCount="blog.discussCount"
                    :tags="catchTagName(blog.tags)"
                    :name="blog.user.name"
                    @reload="reloadBlogData"/>
    </div>

    <el-card class="box-card" style="margin: 20% " v-if="blogList.length <= 0">
      <div >
        还没发布过博客
      </div>
    </el-card>

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
  import blog from '@/api/blog'
  import date from '@/utils/date'

  export default {
    name: "myBlogList",
    components: {blogOverView},
    data () {
      return {
        total: 100,       //数据总数
        currentPage: 1, //当前页数
        pageSize: 3,    //每页显示数量
        blogList: []//当前页数据
      }
    },
    created () {
      this.loadBlog();
    },
    methods: {
      loadBlog () {
        blog.getMyBlog(this.currentPage, this.pageSize).then(res => {
          this.total = res.data.total;
          this.blogList = res.data.rows;
        })
      },  //加载数据
      reloadBlogData() {
        this.loadBlog();
      },
      currentChange(currentPage) {  //页码更改事件处理
        this.currentPage = currentPage;
        this.loadBlog();
        scrollTo(0, 0);
      },
      getTime(time) {   //将时间戳转化为几分钟前，几小时前
        return date.timeago(time);
      },
      catchTagName(tags) {
        var tagName = [];
        for (var i = 0; i < tags.length; i++) {
          tagName.push(tags[i].name);
        }
        return tagName;
      }
    }
  }
</script>

<style scoped>
  #myBlogList {
    margin-top: -13px;
  }
</style>
