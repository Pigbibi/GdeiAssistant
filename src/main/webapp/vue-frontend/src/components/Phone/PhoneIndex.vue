<template>
  <div>
    <div class="weui-cells__title" @click="goBack">返回</div>

    <div class="hd">
      <h1 class="page_title">绑定手机</h1>
    </div>

    <!-- 错误提示，显示时用$.show();隐藏时用$.hide(); -->
    <div class="weui-toptips weui_warn js_tooltips" v-if="showError" @click="hideError">{{ errorMessage }}</div>

    <!-- 解绑中弹框 -->
    <div role="alert" id="loadingToast" v-if="showLoading">
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
          <p>手机号</p>
        </div>
        <div class="weui-cell__ft">
          <template v-if="phone">
            +{{ phone.code }}-{{ phone.phone }}
          </template>
          <template v-else>
            未绑定
          </template>
        </div>
      </div>
    </div>

    <!-- 提交按钮 -->
    <div class="weui-btn_area">
      <a class="weui-btn weui-btn_primary" href="javascript:" @click="goToChange">修改</a>
    </div>

    <template v-if="phone">
      <!-- 解绑按钮 -->
      <div class="weui-btn_area">
        <a class="weui-btn weui-btn_default" href="javascript:" @click="unattachPhoneNumber">解绑</a>
      </div>
    </template>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      phone: null,
      showError: false,
      errorMessage: '',
      showLoading: false
    };
  },
  mounted() {
    // 加载用户手机信息
    this.loadUserPhone();
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    goToChange() {
      this.$router.push('/phone/change');
    },
    loadUserPhone() {
      axios
        .get('/api/phone')
        .then((response) => {
          let result = response.data;
          if (result.success) {
            this.phone = result.data;
          } else {
            this.showError = true;
            this.errorMessage = result.message;
            setTimeout(() => this.hideError(), 2000);
          }
        })
        .catch((error) => {
          this.showError = true;
          this.errorMessage = error.response ? error.response.data.message : '网络连接失败，请检查网络连接';
          setTimeout(() => this.hideError(), 2000);
        });
    },
    hideError() {
      this.showError = false;
      this.errorMessage = '';
    },
    unattachPhoneNumber() {
      weui.confirm('解绑手机号不会同步清除你的实名认证信息，且解绑后此手机号将不能接收到系统的通知信息，你是否确认要解绑？', {
        title: '解绑手机号',
        buttons: [{
          label: '取消',
          type: 'default',
          onClick: function() {}
        }, {
          label: 'YES',
          type: 'primary',
          onClick: () => {
            this.showLoading = true;
            axios
              .post('/api/phone/unbind')
              .then((response) => {
                this.showLoading = false;
                let result = response.data;
                if (result.success) {
                  // 重新加载页面
                  this.loadUserPhone();
                } else {
                  this.showError = true;
                  this.errorMessage = result.message;
                  setTimeout(() => this.hideError(), 2000);
                }
              })
              .catch((error) => {
                this.showLoading = false;
                this.showError = true;
                this.errorMessage = error.response ? error.response.data.message : '网络访问异常，请检查网络连接';
                setTimeout(() => this.hideError(), 2000);
              });
          }
        }]
      });
    }
  }
};
</script>

<style scoped>
/* 保持原有样式不变 */
</style>