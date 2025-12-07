<template>
  <div class="close-account-container">
    <!-- 错误提示 -->
    <div v-if="showError" class="weui-toptips weui_warn js_tooltips">{{ errorMessage }}</div>

    <div v-if="!showResult" class="close-account-request">
      <div class="weui-msg">
        <div class="weui-msg__icon-area"><i class="weui-icon-warn weui-icon_msg"></i></div>
        <div class="weui-msg__text-area">
          <h2 class="weui-msg__title">确认删除账号？</h2>
          <p class="weui-msg__desc">删除账号后，您在广东二师助手留存的信息将被清空且无法找回，具体包括：</p>
          <p class="weui-msg__desc">1、个人资料、手机号和邮箱地址等身份信息</p>
          <p class="weui-msg__desc">2、添加的自定义课程以及保存的四六级准考证号信息</p>
          <p class="weui-msg__desc">3、社交功能平台的用户数据和交易记录</p>
        </div>
      </div>

      <!-- 提交的用户信息表单 -->
      <div class="weui-cells">
        <div class="weui-cell">
          <div class="weui-cell__hd">
            <label class="weui-label">密码</label>
          </div>
          <div class="weui-cell__bd weui-cell_primary">
            <input class="weui-input" type="password" maxlength="35" v-model="password" placeholder="请输入你的校园网密码">
          </div>
        </div>
      </div>

      <!-- 注销按钮 -->
      <div class="weui-btn_area">
        <a class="weui-btn weui-btn_warn" href="javascript:" @click="postCloseRequest">注销</a>
      </div>

      <p class="page_desc" style="margin-top: 15px">注销账号前请认真阅读并同意
        <a class="page_desc" @click="window.location.href = '/policy/deletion'">《注销协议》</a>
      </p>

    </div>

    <div v-if="showResult" class="close-account-result">
      <div class="weui-msg">
        <div class="weui-msg__icon-area">
          <i v-if="!resultSuccess" class="weui-icon-warn weui-icon_msg"></i>
          <i v-else class="weui-icon-success weui-icon_msg"></i>
        </div>
        <div class="weui-msg__text-area">
          <h2 class="weui-msg__title">{{ resultTitle }}</h2>
          <p class="weui-msg__desc">{{ resultDesc }}</p>
        </div>
        <div v-if="!resultSuccess && resultData" class="weui-msg__custom-area">
          <div class="weui-cells__group weui-cells__group_form">
            <div v-for="(value, key) in resultData" :key="key" class="weui-cells">
              <a class="weui-cell" href="javascript:">
                <span class="weui-cell__bd">
                  <span>{{ key }}</span>
                  <div class="weui-cell__desc">{{ value }}</div>
                </span>
              </a>
            </div>
          </div>
        </div>
        <div class="weui-msg__opr-area">
          <p class="weui-btn-area">
            <a v-if="resultSuccess" href="javascript:localStorage.clear();window.location.href = '/logout';" role="button" class="weui-btn weui-btn_primary">确定</a>
          </p>
        </div>
        <div class="weui-msg__extra-area">
          <div class="weui-footer">
            <p class="weui-footer__text">如对以上内容有疑问，请联系客服</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 注销中弹框 -->
    <div v-if="loading" role="alert" class="weui-mask_transparent"></div>
    <div v-if="loading" class="weui-toast">
      <span class="weui-primary-loading weui-icon_toast">
        <span class="weui-primary-loading__dot"></span>
      </span>
      <p class="weui-toast__content">注销中</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CloseAccount',
  data() {
    return {
      password: '',
      loading: false,
      showResult: false,
      resultSuccess: false,
      resultTitle: '',
      resultDesc: '',
      resultData: null,
      showError: false,
      errorMessage: ''
    }
  },
  methods: {
    //显示错误提示
    showErrorTip(message) {
      this.errorMessage = message;
      this.showError = true;
      setTimeout(() => {
        this.showError = false;
      }, 2000);
    },

    //提交删除账号请求
    postCloseRequest() {
      if (this.password.length > 0 && this.password.length <= 35) {
        weui.confirm('您将删除您的广东二师助手账号，账号后您的账号相关信息将被清空且无法恢复。删除账号前，请确保您的账号的所有社交功能平台的信息已处理完成无纠纷，同时账号未处于被限制或封禁的状态。点击确认删除则代表您已阅读并同意《用户协议》中注销账号相关的内容。', {
          title: '删除账号',
          buttons: [{
            label: '取消删除',
            type: 'primary',
            onClick: function () {}
          }, {
            label: '确认删除',
            type: 'default',
            onClick: () => {
              this.loading = true;
              this.$axios({
                url: '/api/close/submit',
                method: 'post',
                data: {
                  password: this.password
                }
              })
              .then((response) => {
                this.loading = false;
                this.showResult = true;
                const result = response.data;
                
                if (result.success) {
                  //显示删除账号成功提示信息
                  this.resultSuccess = true;
                  this.resultTitle = '删除账号成功';
                  this.resultDesc = '后续若仍需要使用广东二师助手服务，可以重新进行账号注册';
                } else {
                  //显示删除账号失败提示信息
                  this.resultSuccess = false;
                  if (result.data == null) {
                    //通用类型错误
                    this.resultTitle = '删除账号失败';
                    this.resultDesc = result.message;
                  } else {
                    //删除条件不符合
                    this.resultTitle = '删除账号失败';
                    this.resultDesc = '抱歉，因为以下原因，账号无法注销';
                    this.resultData = result.data;
                  }
                }
              })
              .catch(() => {
                this.loading = false;
                this.showErrorTip('网络连接失败,请检查网络连接');
              });
            }
          }]
        });
      } else {
        this.showErrorTip('账号密码长度不合法');
      }
    }
  }
}
</script>

<style scoped>
/* 保持原有样式不变，这里引入原有的CSS文件 */
@import url('/static/css/common/common.css');
@import url('/static/css/common/weui.min.css');
@import url('/static/css/close/close.css');
</style>