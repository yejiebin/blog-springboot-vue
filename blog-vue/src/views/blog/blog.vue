<template>
  <div>
    <el-card id="blog">
      <el-link :underline="false" @click="back()"><i class="el-icon-back">Back</i></el-link>
      <!--为了blogId值改变事件会被watch到-->
      <p style="display: none">{{blogId = this.$route.params.blogId}}</p>

      <div id="title">
        <h2 style="text-align: center">{{title}}</h2>
      </div>
      <div style="text-align: center">
        <p>
          <span class="el-icon-time hidden-xs-only">&nbsp;{{getTime(time)}}</span>
          <span class="el-icon-view hidden-xs-only" style="margin-left: 100px">&nbsp;{{blogViews}}</span>
          <span class="el-icon-chat-line-square hidden-xs-only" style="margin-left: 100px">&nbsp;{{discussCount}}</span>
          <span class="el-icon-user-solid hidden-xs-only" style="margin-left: 150px">&nbsp;{{userName}}</span>
        </p>
        <p>
          <span>
            <span v-for="tag in catchTagName(tags)">
              <el-tag type="success" style="margin-left: 5px">{{tag}}</el-tag>
            </span>
          </span>
        </p>
      </div>
      <mavon-editor v-model="body" id="editor" :toolbarsFlag="false" :subfield="false" defaultOpen="preview"/>
      <!-- 以下是预览模式配置 -->
      <!--:toolbarsFlag="false"  :subfield="false" defaultOpen="preview"-->

      <el-divider></el-divider>
      <div id="discuss" class="hidden-xs-only">
        <div style="width: 50%;margin-left: 2.5%;padding-top: 2%" v-if="getStoreName()!=''">
          <el-input v-model="discussBody" placeholder="请输入评论内容" style="width: 40%" size="mini"></el-input>
          <el-button type="primary" style="width: 10%" size="mini" @click="sendDiscuss">评论</el-button>
        </div>
      </div>

      <div v-for="discuss in discussList" id="discussList">
        <!-- 评论部分 -->
        <p style="margin: -5px " @mouseenter="pEnterDiscuss(discuss.id)" @mouseleave="pLeave()">
          <el-button type="text">{{discuss.user.name}}&nbsp;&nbsp;:</el-button>
          <span style="margin-left: 10px">{{discuss.body}}</span>
            <span style="color: #909399;margin-left: 50px" class="el-icon-time">{{getTime(discuss.time)}}</span>
          <el-button type="text" style="margin-left: 5%"
                     v-if="(discuss.user.name==getStoreName()||(getStoreRoles().indexOf('ADMIN') > -1))&&showDiscussReplyId==discuss.id"
                     @click="deleteDiscuss(discuss.id)">删除
          </el-button>
          <el-button type="text" style="margin-left: 1%" @click="sendReply(discuss.id,null)"
                     v-if="getStoreName()!=''&&showDiscussReplyId==discuss.id">回复
          </el-button>

        </p>
        <!-- 评论下的回复部分 -->
        <p v-if="discuss.replyList.length>0" v-for="reply in discuss.replyList" style="margin: -5px"
           @mouseenter="pEnterReply(reply.id)" @mouseleave="pLeave()">
          <span style="margin-left: 5%" class="el-icon-arrow-right"></span>
          <el-button type="text">{{reply.user.name}}&nbsp;&nbsp;:</el-button>
          <span v-if="reply.reply != null">回复:</span>
          <el-button type="text" v-if="reply.reply != null">{{reply.reply.user.name}}</el-button>


          <span style="margin-left: 10px">{{reply.body}}</span>
          <span style="color: #909399;margin-left: 50px" class="el-icon-time">{{getTime(reply.time)}}</span>

          <el-button type="text" style="margin-left: 5%"
                     v-if="(reply.user.name==getStoreName()||(getStoreRoles().indexOf('ADMIN') > -1))&&showReplyId==reply.id"
                     @click="deleteReply(reply.id)">删除
          </el-button>
          <el-button type="text" style="margin-left: 1%" @click="sendReply(discuss.id,reply.id)"
                     v-if="getStoreName()!=''&&showReplyId==reply.id">回复
          </el-button>
        </p>
      </div>
      <br/>
      <div>
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
  </div>
</template>

<script>
  import date from '@/utils/date'
  import discuss from '@/api/discuss'
  import blog from '@/api/blog'
  import reply from '@/api/reply'

  export default {
    name: "blog",
    data () {
      return {
        blogId: -1,//博文id
        title: '',//博文标题
        body: '',//博文内容
        discussCount: 0,//评论数
        blogViews: 0,//浏览数
        time: 0, //发布事件
        userName: '',//博客用户名
        tags: [],  //博文标签

        total: 0,        //数据总数
        discussList: [],   //当前页数据
        pageSize: 5,    //每页显示数量
        currentPage: 1,   //当前页数

        discussBody: '',//评论内容
        replyBody: '',   //回复内容
        showDiscussReplyId: -1,// 是否显示评论的回复按钮
        showReplyId: -1// 是否显示回复的回复按钮
      }
    },
    // created() {
    //   this.title = '文章标题';
    //   this.time = 1566368964000;
    //   this.userName = '水木杰';
    //   this.tags = [{"name": "Spring"}, {"name": "SpringMVC"}];
    //   this.body = '##这是内容！这是内容！这是内容！这是内容！这是内容！这是内容！这是内容！这是内容！这是内容！这是内容！这是内容！这是内容！'
    //   this.discussList = [
    //     {
    //       id: 1,
    //       body: '这是评论！这是评论！这是评论！这是评论！这是评论！这是评论！这是评论！这是评论！这是评论！这是评论！',
    //       time: 1566368964000,
    //       user: {name: '木水杰'},
    //       replyList: [
    //         {
    //           id: 1,
    //           body: '你好你好',
    //           time: 1566368964000,
    //           user: {name: '木水杰'},
    //           reply: null
    //         },{
    //           id: 2,
    //           body: '你好',
    //           time: 1566368964000,
    //           user: {name: '水木杰'},
    //           reply: {
    //               user: {name: '阿牛'}
    //           }
    //         }
    //       ]
    //
    //     },
    //     {
    //       id: 2,
    //       body: '这是评论！这是评论！这是评论！这是评论！这是评论！这是评论！这是评论！这是评论！这是评论！这是评论！',
    //       time: 1566368964000,
    //       user: {name: '水木杰'},
    //       replyList: [
    //         {
    //           id: 3,
    //           body: '你好你好',
    //           time: 1566368964000,
    //           user: {name: '木水杰'},
    //           reply: null
    //         },{
    //           id: 4,
    //           body: '你好',
    //           time: 1566368964000,
    //           user: {name: '水木杰'},
    //           reply: {
    //             user: {name: '阿红'}
    //           }
    //         }
    //       ]
    //     },
    //     {
    //       id: 3,
    //       body: '这是评论！这是评论！这是评论！这是评论！这是评论！这是评论！这是评论！这是评论！这是评论！这是评论！',
    //       time: 1566368964000,
    //       user: {name: '水木杰'}
    //     }
    //   ]
    // },
    watch: {
      blogId() {//在此调用接口
        this.loadBlog();
        var w = document.documentElement.offsetWidth || document.body.offsetWidth;
        if (w < 768) {  //对应xs
          document.getElementById('editor').style.margin = '0% -4.5%';
          document.getElementById('editor').style.margin = '0% 0%'
          document.getElementById('blog').style.margin = '20px 0% 0 0%'
        }
      }
    },
    methods: {
      loadBlog() { //加载数据
        blog.getBlogById(this.blogId).then(res => {
          this.title = res.data.title;
          this.body = res.data.body;
          this.discussCount = res.data.discussCount;
          this.blogViews = res.data.blogViews;
          this.time = res.data.time;
          this.userName = res.data.user.name;
          this.tags = res.data.tags;
        });

        discuss.getDiscussByBlogId(this.blogId, this.currentPage, this.pageSize).then(responese => {
          this.total = responese.data.total;
          this.discussList = responese.data.rows;
        });
      },
      currentChange(currentPage) { //页码更改事件处理
        this.currentPage = currentPage;
        //获取评论
        discuss.getDiscussByBlogId(this.blogId, this.currentPage, this.pageSize).then(responese => {
          this.total = responese.data.total;
          this.discussList = responese.data.rows;
        });
      },
      back() {
        history.back();
      },
      getTime(time) {
        return date.timeago(time);
      },
      catchTagName (tags) {
        var tagNames = [];
        for (var i = 0; i < tags.length; i++) {
          tagNames.push(tags[i].name);
        }
        return tagNames;
      },
      getStoreName() {//获取store中存储的name
        return this.$store.state.name;
      },
      getStoreRoles() { //获取store中存储的roles
        return this.$store.state.roles;
      },
      pEnterDiscuss(discussId) {
        this.showDiscussReplyId = discussId;
      },
      pEnterReply(replyId) {
        this.showReplyId = replyId;
      },
      pLeave() {
        this.showDiscussReplyId = -1;
        this.showReplyId = -1;
      },
      sendDiscuss() {//发送评论
        if (this.discussBody.trim().length <= 0) {
          this.$message({
            type: 'error',
            message: '内容不能为空'
          });
          return;
        }

        discuss.sendDiscuss(this.blogId, this.discussBody).then(res => {
          this.$message({
            type: 'success',
            message: '评论成功'
          });
          this.discussBody = '';
          //页面评论数+1
          this.discussCount = this.discussCount+1;
          //获取评论
          discuss.getDiscussByBlogId(this.blogId, this.currentPage, this.pageSize).then(responese => {
            this.total = responese.data.total;
            this.discussList = responese.data.rows;
          });
        })
      },
      sendReply(discussId, replyId) {  //发送回复
        this.$prompt('请输入回复内容', '提示', {
          confirmButtonText: '回复',
          cancelButtonText: '取消'
        }).then(({value}) => {
          if (value == null || value.trim().length <= 0) {
            this.$message({
              type: 'error',
              message: '字段不完整'
            });
            return;
          }
          reply.sendReply(discussId, value, replyId).then(res => {
            this.$message({
              type: 'success',
              message: '回复成功'
            });
            //页面评论数+1
            this.discussCount = this.discussCount+1;
            //获取评论
            discuss.getDiscussByBlogId(this.blogId, this.currentPage, this.pageSize).then(responese => {
              this.total = responese.data.total;
              this.discussList = responese.data.rows;
            });
          })
        }).catch(() => {
        });
      },
      deleteDiscuss(discussId) {  //删除评论  判断是用户还是管理员 走不一样的api
        this.$confirm('是否删除此评论?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          if (this.$store.state.roles.indexOf('ADMIN') > -1) {
            //管理员
            discuss.adminDeleteDiscuss(discussId).then(res => {
              this.$message({
                type: 'success',
                message: '删除成功'
              });

              //页面评论数-1-replys
              this.discussCount = this.discussCount-1;
              var delDiscuss = this.discussList.filter(function(item){
                return item.id == discussId;
              });
              if(delDiscuss.length!=0){
                var replysOfThisDiscuss = delDiscuss[0].replyList==null ? 0 : delDiscuss[0].replyList.length;
                this.discussCount = this.discussCount-replysOfThisDiscuss;
              }
              //获取评论
              discuss.getDiscussByBlogId(this.blogId, this.currentPage, this.pageSize).then(responese => {
                this.total = responese.data.total;
                this.discussList = responese.data.rows;
              });
            })
          } else {
            //普通用户
            discuss.userDeleteDiscuss(discussId).then(res => {
              this.$message({
                type: 'success',
                message: '删除成功'
              });
              //页面评论数-1-replys
              this.discussCount = this.discussCount-1;
              var delDiscuss = this.discussList.filter(function(item){
                return item.id == discussId;
              });
              if(delDiscuss.length!=0){
                var replysOfThisDiscuss = delDiscuss[0].replyList==null ? 0 : delDiscuss[0].replyList.length;
                this.discussCount = this.discussCount-replysOfThisDiscuss;
              }
              //获取评论
              discuss.getDiscussByBlogId(this.blogId, this.currentPage, this.pageSize).then(responese => {
                this.total = responese.data.total;
                this.discussList = responese.data.rows;
              });
            })
          }
        }).catch(() => {
        });
      },
      deleteReply(replyId) {  //删除回复  判断是用户还是管理员 走不一样的api
        this.$confirm('是否删除此回复?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          if (this.$store.state.roles.indexOf('ADMIN') > -1) {
            //管理员
            discuss.adminDeleteReply(replyId).then(res => {
              this.$message({
                type: 'success',
                message: '删除成功'
              });
              //页面评论数+1
              this.discussCount = this.discussCount-1;
              //获取评论
              discuss.getDiscussByBlogId(this.blogId, this.currentPage, this.pageSize).then(responese => {
                this.total = responese.data.total;
                this.discussList = responese.data.rows;
              });
            })
          } else {
            //普通用户
            discuss.userDeleteReply(replyId).then(res => {
              this.$message({
                type: 'success',
                message: '删除成功'
              });
              //页面评论数-1
              this.discussCount = this.discussCount-1;
              //获取评论
              discuss.getDiscussByBlogId(this.blogId, this.currentPage, this.pageSize).then(responese => {
                this.total = responese.data.total;
                this.discussList = responese.data.rows;
              });
            })
          }

        }).catch(() => {
        });
      },
    }
  }
</script>

<style scoped>
  #blog {
    margin: 20px 5% 0 5%;
    padding: 20px;
    text-align: left;
  }

  #editor {
    margin: 2% 2%;
    height: 100%;
  }

  #discuss {
    margin: 15px 5% 0 5%;
  }

  #discussList {
    text-align: left;
    padding: 2% 1% 1% 3%;
  }
</style>
