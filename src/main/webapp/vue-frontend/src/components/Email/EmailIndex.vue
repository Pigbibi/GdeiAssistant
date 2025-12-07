<template>
  <div>
    <div class="weui-cells__title" @click="goBack">返回</div>

    <div class="hd">
      <h1 class="page_title">绑定邮箱</h1>
    </div>

    <!-- 错误提示，显示时用$.show();隐藏时用$.hide(); -->
    <div class="weui-toptips weui_warn js_tooltips" :class="{ 'show': showError }" ref="errorTip">{{ errorMessage }}</div>

    <!-- 解绑中弹框 -->
    <div role="alert" id="loadingToast" v-if="loading" style="display: block;">
      <div class="weui-mask_transparent"></div>
      <div class="weui-toast">
            <span class="weui-primary-loading weui-icon_toast">
              <span class="weui-primary-loading__dot"></span>
            </span>
        <p class="weui-toast__content">解绑中</p>
      </div>
    </div>

    <div class="weui-cells">
      <div class="weui-cell" href="javascript:" @click="">
        <div class="weui-cell__bd">
          <p>电子邮箱</p>
        </div>
        <div class="weui-cell__ft">
          {{ email ? email : '未绑定' }}
        </div>
      </div>
    </div>

    <!-- 提交按钮 -->
    <div class="weui-btn_area">
      <a class="weui-btn weui-btn_primary" href="javascript:" @click="goToChange">修改</a>
    </div>

    <div v-if="email" class="weui-btn_area">
      <!-- 解绑按钮 -->
      <div class="weui-btn_area">
        <a class="weui-btn weui-btn_default" href="javascript:" @click="unBindEmail">解绑</a>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'EmailIndex',
  data() {
    return {
      email: null,
      loading: false,
      showError: false,
      errorMessage: ''
    };
  },
  mounted() {
    this.loadUserEmail();
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    goToChange() {
      this.$router.push('/email/change');
    },
    loadUserEmail() {
      axios.get('/api/email')
        .then(response => {
          if (response.data.success) {
            this.email = response.data.data.email;
          }
        })
        .catch(error => {
          if (error.response) {
            this.showErrorMessage(error.response.data.message);
          } else {
            this.showErrorMessage('网络访问异常，请检查网络连接');
          }
        });
    },
    //解绑电子邮件地址
    unBindEmail() {
      if (confirm('你是否确认要解绑电子邮件地址？')) {
        this.loading = true;
        axios.post('/api/email/unbind')
          .then(response => {
            this.loading = false;
            if (response.data.success) {
              alert('解绑成功');
              this.email = null;
            } else {
              this.showErrorMessage(response.data.message);
            }
          })
          .catch(error => {
            this.loading = false;
            if (error.response) {
              this.showErrorMessage(error.response.data.message);
            } else {
              this.showErrorMessage('网络访问异常，请检查网络连接');
            }
          });
      }
    },
    showErrorMessage(message) {
      this.errorMessage = message;
      this.showError = true;
      setTimeout(() => {
        this.showError = false;
      }, 2000);
    }
  }
};
</script>

<style scoped>
.show {
  display: block;
}
</style>