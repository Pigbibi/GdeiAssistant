<template>
  <div>
    <div class="weui-cells__title" @click="goBack">返回</div>

    <div class="hd">
      <h1 class="page_title" style="margin-top: 15px">修改绑定手机</h1>
    </div>

    <!-- 错误提示，显示时用$.show();隐藏时用$.hide(); -->
    <div class="weui-toptips weui_warn js_tooltips" v-if="showError" @click="hideError">{{ errorMessage }}</div>

    <div class="weui-cells">
      <div class="weui-cell weui-cell_access weui-cell_select-before">
        <div class="weui-cell__hd">
          <input id="phone_code" type="hidden" :value="phoneCode">
          <label id="phone_flag" class="weui-label" style="width: 5rem" @click="changePhoneCode()">{{ phoneFlag }}</label>
        </div>
        <div class="weui-cell__bd">
          <input id="phone" v-model="phone" class="weui-input" type="number" pattern="[0-9]*" placeholder="请输入手机号">
        </div>
      </div>
      <div class="weui-cell weui-cell_vcode">
        <div class="weui-cell__hd">
          <label class="weui-label" style="width: 3rem">验证码</label>
        </div>
        <div class="weui-cell__bd">
          <input id="verification_code" v-model="verificationCode" class="weui-input" type="number" maxlength="6" placeholder="请输入验证码"/>
        </div>
        <div class="weui-cell__ft">
          <button id="verfication_button" :disabled="verificationDisabled" class="weui-btn weui-btn_default weui-vcode-btn" @click="getVerificationCode()">{{ verificationButtonText }}</button>
        </div>
      </div>
    </div>

    <p class="page_desc" style="margin-top: 25px">运营商可能会收取短信或通话费用<br>为了保证你的账号安全，请仅绑定本人使用的手机号</p>

    <div class="weui-btn_area">
      <a class="weui-btn weui-btn_primary close-popup" href="javascript:" @click="savePhoneNumber()">保存</a>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      phoneCode: '86',
      phone: '',
      verificationCode: '',
      phoneFlag: '🇨🇳 +86',
      attributionPickerItems: [],
      attributionMap: new Map(),
      showError: false,
      errorMessage: '',
      verificationDisabled: false,
      verificationButtonText: '获取验证码'
    };
  },
  mounted() {
    // 加载国际手机代号表
    this.loadPhoneAttributionMap();
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    loadPhoneAttributionMap() {
      axios
        .get('/rest/phone/attribution')
        .then((response) => {
          let result = response.data;
          if (result.success === true) {
            result.data.forEach((item) => {
              this.attributionMap.set(item.code, item);
              this.attributionPickerItems.push({
                label: item.flag + " " + item.name + " +" + item.code,
                value: item.code
              });
            });
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
    changePhoneCode() {
      weui.picker(this.attributionPickerItems,
        {
          defaultValue: ['86'],
          container: 'body',
          onConfirm: (result) => {
            //设置国际区号和国家/地区旗帜
            this.phoneCode = result[0].value;
            this.phoneFlag = this.attributionMap.get(result[0].value).flag + " +" + result[0].value;
          }
        });
    },
    getVerificationCode() {
      if (this.phone === '') {
        this.showError = true;
        this.errorMessage = "手机号不能为空";
        setTimeout(() => this.hideError(), 2000);
      } else if (this.phone.length < 7 || this.phone.length > 11) {
        this.showError = true;
        this.errorMessage = "手机号格式不合法";
        setTimeout(() => this.hideError(), 2000);
      } else {
        let loading = weui.loading('正在请求验证码');
        this.verificationDisabled = true;
        axios
          .post('/api/phone/verification', {
            code: this.phoneCode,
            phone: this.phone
          })
          .then((response) => {
            loading.hide();
            let result = response.data;
            if (result.success === true) {
              //设置验证码获取等待间隔
              this.verificationDisabled = true;
              let second = 59;
              this.verificationButtonText = `等待${second}秒`;
              let waittingInterval = setInterval(() => {
                second--;
                this.verificationButtonText = `等待${second}秒`;
              }, 1000);
              let waittingTimeout = setTimeout(() => {
                clearInterval(waittingInterval);
                this.verificationDisabled = false;
                this.verificationButtonText = "获取验证码";
              }, 60000);
            } else {
              this.verificationDisabled = false;
              this.showError = true;
              this.errorMessage = result.message;
              setTimeout(() => this.hideError(), 2000);
            }
          })
          .catch((error) => {
            loading.hide();
            this.verificationDisabled = false;
            this.showError = true;
            this.errorMessage = error.response ? error.response.data.message : '网络连接异常，请检查网络连接';
            setTimeout(() => this.hideError(), 2000);
          });
      }
    },
    savePhoneNumber() {
      if (this.phone === '') {
        this.showError = true;
        this.errorMessage = "手机号不能为空";
        setTimeout(() => this.hideError(), 2000);
      } else if (this.phone.length < 7 || this.phone.length > 11) {
        this.showError = true;
        this.errorMessage = "手机号格式不合法";
        setTimeout(() => this.hideError(), 2000);
      } else if (this.verificationCode === '') {
        this.showError = true;
        this.errorMessage = "验证码不能为空";
        setTimeout(() => this.hideError(), 2000);
      } else if (this.verificationCode.length !== 6 || !/^\d+$/.test(this.verificationCode)) {
        this.showError = true;
        this.errorMessage = "验证码必须为6位的数字";
        setTimeout(() => this.hideError(), 2000);
      } else {
        let loading = weui.loading('绑定中');
        axios
          .post('/api/phone/attach', {
            code: this.phoneCode,
            phone: this.phone,
            randomCode: this.verificationCode
          })
          .then((response) => {
            loading.hide();
            let result = response.data;
            if (result.success) {
              weui.alert('你已成功绑定手机号', () => {
                this.$router.go(-1);
              }, {
                title: '绑定成功'
              });
            } else {
              this.showError = true;
              this.errorMessage = result.message;
              setTimeout(() => this.hideError(), 2000);
            }
          })
          .catch((error) => {
            loading.hide();
            this.showError = true;
            this.errorMessage = error.response ? error.response.data.message : '网络访问异常，请检查网络连接';
            setTimeout(() => this.hideError(), 2000);
          });
      }
    },
    hideError() {
      this.showError = false;
      this.errorMessage = '';
    }
  }
};
</script>

<style scoped>
/* 保持原有样式不变 */
</style>