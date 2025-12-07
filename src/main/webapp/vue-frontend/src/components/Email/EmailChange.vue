<template>
  <div>
    <div class="weui-cells__title" @click="goBack">返回</div>

    <div class="hd">
      <h1 class="page_title">修改绑定电子邮件</h1>
    </div>

    <!-- 错误提示，显示时用$.show();隐藏时用$.hide(); -->
    <div class="weui-toptips weui_warn js_tooltips" :class="{ 'show': showError }" ref="errorTip">{{ errorMessage }}</div>

    <div class="weui-cells">
      <div class="weui-cell">
        <div class="weui-cell__hd">
          <label class="weui-label" style="width: 3rem">地址</label>
        </div>
        <div class="weui-cell__bd">
          <input id="email" v-model="email" class="weui-input" type="email" placeholder="请输入电子邮件地址">
        </div>
      </div>
      <div class="weui-cell weui-cell_vcode">
        <div class="weui-cell__hd">
          <label class="weui-label" style="width: 3rem">验证码</label>
        </div>
        <div class="weui-cell__bd">
          <input id="verification_code" v-model="verificationCode" class="weui-input" type="number" maxlength="6" placeholder="请输入验证码" />
        </div>
        <div class="weui-cell__ft">
          <button id="verfication_button" class="weui-btn weui-btn_default weui-vcode-btn" :disabled="buttonDisabled" @click="getVerificationCode">
            {{ buttonText }}
          </button>
        </div>
      </div>
    </div>

    <div class="weui-btn_area">
      <a class="weui-btn weui-btn_primary close-popup" href="javascript:" @click="saveEmail">保存</a>
    </div>

    <!-- 提交中弹框 -->
    <div role="alert" id="loadingToast" v-if="loading" style="display: block;">
      <div class="weui-mask_transparent"></div>
      <div class="weui-toast">
            <span class="weui-primary-loading weui-icon_toast">
              <span class="weui-primary-loading__dot"></span>
            </span>
        <p class="weui-toast__content">保存中</p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'EmailChange',
  data() {
    return {
      email: '',
      verificationCode: '',
      loading: false,
      showError: false,
      errorMessage: '',
      buttonDisabled: false,
      buttonText: '获取验证码'
    };
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    //获取邮件验证码
    getVerificationCode() {
      if (this.email === '') {
        this.showErrorMessage('电子邮件地址不能为空');
      } else if (!(/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/).test(this.email)) {
        this.showErrorMessage('电子邮件地址格式不合法');
      } else {
        let loading = weui.loading('正在请求验证码');
        this.buttonDisabled = true;
        axios.post('/api/email/verification', {
          email: this.email
        })
        .then(response => {
          loading.hide();
          if (response.data.success) {
            //设置验证码获取等待间隔
            this.buttonDisabled = true;
            document.getElementById("verfication_button").style.color = "#bfbfbf";
            let second = 59;
            let waittingInterval = setInterval(() => {
              this.buttonText = `等待${second}秒`;
              second = second - 1;
            }, 1000);
            let waittingTimeout = setTimeout(() => {
              clearInterval(waittingInterval);
              this.buttonDisabled = false;
              this.buttonText = "获取验证码";
              document.getElementById("verfication_button").style.color = "#3cc51f";
            }, 60000);
          } else {
            this.buttonDisabled = false;
            this.showErrorMessage(response.data.message);
          }
        })
        .catch(error => {
          loading.hide();
          this.buttonDisabled = false;
          if (error.response) {
            this.showErrorMessage(error.response.data.message);
          } else {
            this.showErrorMessage('网络连接异常，请检查网络连接');
          }
        });
      }
    },
    //提交绑定电子邮件地址请求
    saveEmail() {
      if (this.email === '') {
        this.showErrorMessage('电子邮件地址不能为空');
      } else if (!(/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/).test(this.email)) {
        this.showErrorMessage('电子邮件地址格式不合法');
      } else if (this.verificationCode === '') {
        this.showErrorMessage('验证码不能为空');
      } else if (this.verificationCode.length !== 6 || !/^\d+$/.test(this.verificationCode)) {
        this.showErrorMessage('验证码必须为6位的数字');
      } else {
        this.loading = true;
        axios.post('/api/email/bind', {
          email: this.email,
          randomCode: this.verificationCode
        })
        .then(response => {
          this.loading = false;
          if (response.data.success) {
            weui.alert('你已成功绑定电子邮件地址', {
              title: '绑定成功',
              buttons: [{
                label: '确定',
                type: 'primary',
                onClick: () => {
                  this.$router.go(-1);
                }
              }]
            });
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