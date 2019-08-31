<template>
  <el-card id="announcement" v-loading="loading">
    <br/>
    <div v-if="loading" style="margin: 35% 0"></div>
    <div v-for="announcement in announcementList"
         style="text-align: left;padding-left: 2%;">
      <div class="top" :class="(announcement.top==1)?'normal':'subscript'" v-if="announcement.top==0">
        置顶
      </div>

      <p style="color:#409EFF" class="el-icon-data-board">
        &nbsp;{{announcement.title}}
      </p>
      <div style="color: #303133" :id="announcement.id" v-html="announcement.body"/>

      <el-divider/>
    </div>


    <div style="padding-bottom: 4%">
      <el-pagination
        :page-size="pageSize"
        background
        layout="prev, pager, next"
        :total="total"
        @current-change="currentChange"
        :current-page="currentPage"
        @prev-click="currentPage=currentPage-1"
        @next-click="currentPage=currentPage+1"
        :hide-on-single-page="true">
      </el-pagination>
    </div>

  </el-card>
</template>

<script>
  import announcement from '@/api/announcement'
  export default {
    name: "announcement",
    data () {
      return {
        total: 0,        //数据总数
        announcementList: [],   //当前页数据防止空页面的突兀
        pageSize: 5,    //每页显示数量
        currentPage: 1,   //当前页数
        loading: true //是否加载中
      }
    },
    created () {
      // this.announcementList = [
      //   {
      //     id: 1,
      //     title: '天亮了',
      //     body: '今天的天很亮了，今天的天很亮了',
      //     top: 1
      //   },
      //   {
      //     id: 2,
      //     title: '天亮2',
      //     body: '今天的天很亮了，今天的天很亮了',
      //     top: 0
      //   },
      //   {
      //     id: 3,
      //     title: '天亮了3',
      //     body: '今天的天很亮了，今天的天很亮了',
      //     top: 0
      //   },
      //   {
      //     id: 4,
      //     title: '天亮了4',
      //     body: '今天的天很亮了，今天的天很亮了',
      //     top: 0
      //   }
      // ]
      this.loadAnnouncement();
    },
    updated: function () {
      var w = document.documentElement.offsetWidth || document.body.offsetWidth;
      // console.log(w);
      if (w < 768) {  //对应xs
        var tops = document.getElementsByClassName('subscript');
        for (var i = 0; i < tops.length; i++) {
          tops[i].style.marginLeft = '80%'
        }
      }
    },
    methods: {
      currentChange(currentPage) { //页码更改事件处理
        this.currentPage = currentPage;
        this.loadAnnouncement();
      },
      loadAnnouncement() {
        announcement.getAnnouncement(this.currentPage, this.pageSize).then(res => {
          this.total = res.data.total;
          this.announcementList = res.data.rows;
          this.loading = false
        })
      },
    }


  }
</script>

<style scoped>
  #announcement {
    margin: 10px 5% 0 5%;
  }

  .subscript {
    color: #fff;
    height: 30px;
    width: 80px;
    margin-top: -25px;
    margin-left: 94%;
    text-align: center;
    line-height: 30px;
    background-color: #67C23A;
  }
</style>
