<template>
  <div id="editBlog">
    <!--为了blogId值改变事件会被watch到-->
    <p style="display: none">{{blogId = this.$route.params.blogId}}</p>

    <el-card style="height: 700px;">
      <el-input
        type="text"
        placeholder="请输入标题"
        v-model="title"
        maxlength="100"
        show-word-limit/>
      <br/><br/>
      <mavon-editor ref=md @save="save()" v-model="body" id="editor" @imgAdd="$uploadImg" @imgDel="$imgDel"
                    placeholder="## Start"/>
      <!-- 以下是预览模式配置 -->
      <!--:toolbarsFlag="false"  :subfield="false" defaultOpen="preview"-->

    </el-card>

    <el-card id="tags" v-if="tags.length>0">
      <div>
        <p class="el-icon-mouse">选择一个以上标签</p>
        <el-checkbox-group v-model="checkboxGroup">
          <el-checkbox size="mini" v-for="tag in tags" :key="tag.id" :label="tag.id" border
                       style="margin-top: 10px">
            {{tag.name}}
          </el-checkbox>
          <el-button class="el-icon-plus" size="mini" @click="addTag" style="margin: 10px 0 0 30px">新增标签</el-button>
        </el-checkbox-group>
      </div>

      <el-button style="margin-top: 3%;" type="primary" plain class="el-icon-document-checked" @click="editBlog">
        保存修改
      </el-button>
    </el-card>

    <el-card v-if="tags.length<=0">
      没有标签,请先添加标签
      <el-button class="el-icon-plus" size="mini" @click="addTag" style="margin: 10px 0 0 30px">新增标签</el-button>
    </el-card>
  </div>
</template>

<script>
  import blog from '@/api/blog'
  import tag from '@/api/tag'
  import file from '@/utils/file'

  export default {
    name: "editBlog",
    data () {
      return {
        title: '',
        body: '',
        tags: [],
        checkboxGroup: [],
        blogId: ''
      }
    },
    watch: {
      blogId() {
        //加载数据
        tag.getTag().then(res => {
          this.tags = res.data;
        });
        blog.getBlogById(this.blogId).then(res => {
          this.title = res.data.title;
          this.body = res.data.body;
          this.checkboxGroup = res.data.tags.map(t => t.id) // 填充标签
        });

      }
    },
    created () {
      // this.tags = [
      //   {
      //     id: 1,
      //     name: 'Spring'
      //   },
      //   {
      //     id: 2,
      //     name: 'Spring Boot'
      //   },
      //   {
      //     id: 3,
      //     name: 'SpringMVC'
      //   }
      // ]
      tag.getTag().then(res => {
        this.tags = res.data;
      })
    },
    methods: {
      $uploadImg(pos, $file){
        // 第一步.将图片上传到服务器.
        var formdata = new FormData();
        formdata.append('file', $file);
        blog.uploadImg(formdata).then(res => {
          // 第二步.将返回的url替换到文本原位置![...](0) -> ![...](url)
          this.$refs.md.$img2Url(pos, res.data);
        });
      },
      $imgDel(pos) {
        delete this.img_file[pos];
      },
      save() {
        if (this.title.length > 0 && this.body.length > 0) {
          file.generateTxt(this.title, this.body + '\n' + new Date().toLocaleString());
        }
      },
      editBlog() {
        if (this.title.length <= 0) {
          this.$message({
            message: '标题不能为空',
            type: 'warning'
          });
          return ;
        }
        if (this.body.length <= 0) {
          this.$message({
            message: '内容不能为空',
            type: 'warning'
          });
          return ;
        }
        if (this.checkboxGroup.length <= 0) {
          this.$message({
            message: '请选择标签',
            type: 'warning'
          });
          return ;
        }
        var tags = this.checkboxGroup;
        var tagStr = '';
        for (var i = 0; i < tags.length; i++) {
          if (i != tags.length-1) {
            tagStr = tagStr + tags[i] + ',';
          }else {
            tagStr = tagStr + tags[i]
          }
        }
        blog.editBlog(this.blogId, this.title, this.body, tagStr).then(res => {
          this.$alert('修改成功', '提示', {
            confirmButtonText: '确定',
            callback: action => {
              if (action == 'confirm') {
                scrollTo(0, 0);
                history.back()
              }
            }
          })
        })
      },
      addTag() {
        this.$prompt('请输入新标签名', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        }).then(({value}) => {
          if (value == null || value.trim().length <= 0) {
            this.$message({
              message: '请输入新标签名',
              type: 'error'
            });
            return;
          }
          tag.addTag(value).then(res => {
            this.tags.push(res.data);
            this.$message({
              message: '新增成功',
              type: 'success'
            });
          })
        })
      }
    }
  }
</script>

<style scoped>
  #editBlog {
    margin: 20px 5% 0 5%;
  }

  #tags {
    margin-top: 1%;
  }

  #editor {
    height: 600px;
  }

  .el-checkbox__input.is-checked + .el-checkbox__label {
    color: #67C23A;
  }

  .el-checkbox.is-bordered.is-checked {
    border-color: #67C23A;
  }

  .el-checkbox__input.is-checked .el-checkbox__inner, .el-checkbox__input.is-indeterminate .el-checkbox__inner {
    background-color: #67C23A;
    border-color: #67C23A;
  }
</style>
