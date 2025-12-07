<template>
  <div class="weui-msg">
    <div class="weui-msg__icon-area"><i class="weui-icon-warn weui-icon_msg"></i></div>
    <div class="weui-msg__text-area">
      <h2 class="weui-msg__title">{{ errorTitle }}</h2>
      <p class="weui-msg__desc">{{ errorMessage }}</p>
    </div>
    <div class="weui-msg__opr-area" v-if="showBackButton">
      <p class="weui-btn-area">
        <a @click="backToIndex" href="javascript:" class="weui-btn weui-btn_primary">返回首页</a>
      </p>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Error',
  props: {
    errorCode: {
      type: Number,
      default: 404
    },
    errorTitle: {
      type: String,
      default: ''
    },
    errorMessage: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      // 可以根据需要添加数据
    };
  },
  computed: {
    showBackButton() {
      // 401错误不显示返回按钮，其他错误显示
      return this.errorCode !== 401;
    }
  },
  mounted() {
    // 页面加载时的逻辑
  },
  methods: {
    backToIndex() {
      // 返回首页
      this.$router.push('/');
    }
  }
};
</script>

<style scoped>
/* 保留原有的CSS样式，不需要修改 */
</style>