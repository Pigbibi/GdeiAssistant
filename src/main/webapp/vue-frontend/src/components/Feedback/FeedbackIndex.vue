<template>
  <div>
    <div class="weui-cells__title" @click="$router.go(-1)">返回</div>

    <div class="hd">
      <h1 class="page_title">帮助与反馈</h1>
    </div>

    <div class="weui-cells">
      <a class="weui-cell weui-cell_access" href="javascript:" @click="$router.push('/feedback/faq')">
        <div class="weui-cell__bd">
          <p>常见问题解答</p>
        </div>
        <div class="weui-cell__ft">
        </div>
      </a>
      <a class="weui-cell weui-cell_access" href="javascript:" @click="$router.push('/feedback/function')">
        <div class="weui-cell__bd">
          <p>功能意见反馈</p>
        </div>
        <div class="weui-cell__ft">
        </div>
      </a>
      <a class="weui-cell weui-cell_access" href="javascript:" @click="$router.push('/feedback/ticket')">
        <div class="weui-cell__bd">
          <p>故障工单提交</p>
        </div>
        <div class="weui-cell__ft">
        </div>
      </a>
      <a class="weui-cell weui-cell_access" href="mailto:report@gdeiassistant.cn">
        <div class="weui-cell__bd">
          <p>社区违规举报</p>
        </div>
        <div class="weui-cell__ft">
        </div>
      </a>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FeedbackIndex'
};
</script>

<style scoped>
/* 保持原有的CSS样式不变 */
</style>