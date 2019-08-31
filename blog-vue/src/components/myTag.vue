<template>
  <el-card id="myTag">
    <p>
      <span style="color:#67C23A" class="el-icon-collection-tag">个人标签</span>
    </p>
    <hr/>

    <div v-for="tag in tags">
      <el-tag type="success" closable @close="deleteTag(tag.id)" style="margin-top: 10px" @click="editTag(tag.id)">
        <el-link type="success" :underline="false">{{tag.name}}</el-link>
      </el-tag>
    </div>
    <div style="margin-top: 15%">
      <el-button class="el-icon-plus" size="mini" @click="addTag">新增标签</el-button>
    </div>

  </el-card>

</template>

<script>
  import tag from '@/api/tag'

  export default {
    name: "myTag",
    data () {
      return {
        tags: []
      }
    },
    created () {
      this.load();
    },
    methods: {
      load() {
        tag.getTag().then(res => {
          this.tags = res.data;
        })
      },
      deleteTag(id) {
        this.$confirm('是否删除此标签', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          tag.deleteTag(id).then(res => {
            this.load();
            this.$message({
              message: '删除成功',
              type: 'success'
            })
          })
        })
      },
      editTag(id) {
        this.$prompt('请输入修改后的标签名', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        }).then(({value}) => {
          if (value == null || value.trim().length <= 0) {
            this.$message({
              message: '请输入新的标签名',
              type: 'error'
            });
            return ;
          }
          tag.updateTag(id, value).then(res => {
            console.log(this);
            this.load();
            this.$message({
              message: '修改成功',
              type: 'success'
            });
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
            this.load();
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

</style>
