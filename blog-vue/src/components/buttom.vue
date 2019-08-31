<template>
  <div id="bottom">
    <div class="line"></div>
    <p id="txt">Blog Copyright@2019</p>
    <p>有bug请发邮件至 <el-link :underline="false" href="mailto:jiebinye@126.com">jiebinye@126.com</el-link></p>
    <div class="line"></div>
  </div>
</template>

<script>
    export default {
        name: "buttom"
    }
</script>

<style scoped>
  .line {
    width:100%;
    height: 4px;
  }
  #bottom{
    color: #303133;
    background: #F2F6FC;
    margin-top: 90px;
  }
</style>
