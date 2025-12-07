<template>
  <div>
    <div>
      <div class="weui-cells__title" @click="goBack">返回</div>
      <div class="hd">
        <h1 class="page_title" style="margin-top: 15px">在线下单</h1>
      </div>
    </div>

    <div class="weui-cells__title">个人信息</div>
    <div class="weui-cells weui-cells_form">
      <div class="weui-cell">
        <div class="weui-cell__hd">
          <label class="weui-label">姓名</label>
        </div>
        <div class="weui-cell__bd weui-cell_primary">
          <input id="name" class="weui-input" type="text" maxlength="10" v-model="formData.name"
                 placeholder="请输入姓名">
        </div>
      </div>
      <div class="weui-cell">
        <div class="weui-cell__hd">
          <label class="weui-label">学号</label>
        </div>
        <div class="weui-cell__bd weui-cell_primary">
          <input id="number" class="weui-input" type="number" maxlength="11"
                 v-model="formData.number" pattern="[0-9]*" placeholder="请输入学号" @keyup="inputLengthCheck($event, 11)">
        </div>
      </div>
      <div class="weui-cell">
        <div class="weui-cell__hd">
          <label class="weui-label">手机号</label>
        </div>
        <div class="weui-cell__bd weui-cell_primary">
          <input id="phone" class="weui-input" type="number" maxlength="11"
                 v-model="formData.phone" pattern="[0-9]*" placeholder="请输入手机号" @keyup="inputLengthCheck($event, 11)">
        </div>
      </div>
    </div>

    <div class="weui-cells__title">交易信息</div>
    <div class="weui-cells weui-cells_form">
      <div class="weui-cell">
        <div class="weui-cell__hd">
          <label class="weui-label">报酬</label>
        </div>
        <div class="weui-cell__bd weui-cell_primary">
          <input id="price" class="weui-input" type="number" v-model="formData.price" placeholder="不超过99元，数值将四舍五入">
        </div>
      </div>
      <div class="weui-cell">
        <div class="weui-cell__hd">
          <label class="weui-label">地址</label>
        </div>
        <div class="weui-cell__bd weui-cell_primary">
          <input id="address" class="weui-input" type="text" maxlength="50" v-model="formData.address" placeholder="请输入送往地址">
        </div>
      </div>
    </div>

    <div class="weui-cells__title">快递信息</div>
    <div class="weui-cells weui-cells_form">
      <div class="weui-cell">
        <div class="weui-cell__hd">
          <label class="weui-label">公司</label>
        </div>
        <div class="weui-cell__bd weui-cell_primary">
          <input id="company" class="weui-input" type="text" maxlength="10" v-model="formData.company" placeholder="请输入快递公司名称">
        </div>
      </div>
      <div class="weui-cell">
        <div class="weui-cell__hd">
          <label class="weui-label">备注</label>
        </div>
        <div class="weui-cell__bd weui-cell_primary">
          <textarea id="remarks" @keyup="textAreaInputLengthCheck($event, 100)"
                   placeholder="请输入备注" type="text" v-model="formData.remarks"
                   class="weui-textarea"></textarea>
          <div class="weui-textarea-counter">
            <span id="length">{{ formData.remarks.length }}</span>/100
          </div>
        </div>
      </div>
    </div>

    <p class="weui-btn-area">
      <a href="javascript:" @click="submitForm" class="weui-btn weui-btn_primary">提交</a>
    </p>

    <br>

    <!-- 提交中弹框 -->
    <div role="alert" id="loadingToast" v-if="loading" style="display: block;">
      <div class="weui-mask_transparent"></div>
      <div class="weui-toast">
            <span class="weui-primary-loading weui-icon_toast">
              <span class="weui-primary-loading__dot"></span>
            </span>
        <p class="weui-toast__content">提交中</p>
      </div>
    </div>

    <!-- 错误提示 -->
    <div class="weui-toptips weui_warn js_tooltips" :class="{ 'show': showError }" ref="errorTip">{{ errorMessage }}</div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'DeliveryOrder',
  data() {
    return {
      formData: {
        name: '',
        number: '',
        phone: '',
        price: '',
        company: '',
        address: '',
        remarks: ''
      },
      loading: false,
      showError: false,
      errorMessage: ''
    };
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    //检测Input文字长度是否超过限制
    inputLengthCheck(event, maxLen) {
      const str = event.target;
      if (str.value.length > maxLen) {
        str.value = str.value.substr(0, maxLen);
        this.formData[str.id] = str.value;
      }
    },
    //检测Input文字长度是否超过限制并进行实时字数反馈
    textAreaInputLengthCheck(event, maxLen) {
      this.inputLengthCheck(event, maxLen);
    },
    //检查下单属性并提交
    submitForm() {
      if (this.formData.name === '' || this.formData.name.length > 10) {
        this.showErrorMessage('姓名长度不合法');
      } else if (this.formData.number === '' || this.formData.number.length !== 11) {
        this.showErrorMessage('学号长度不合法');
      } else if (this.formData.phone === '' || this.formData.phone.length > 11) {
        this.showErrorMessage('手机号长度不合法');
      } else if (this.formData.price === '' || Number(parseFloat(this.formData.price)) === 0 || Number(parseFloat(this.formData.price)).toFixed(2) > 99) {
        this.showErrorMessage('请输入正确的报酬');
      } else if (this.formData.company === '' || this.formData.company.length > 10) {
        this.showErrorMessage('快递公司名称长度不合法');
      } else if (this.formData.address === '' || this.formData.address.length > 50) {
        this.showErrorMessage('收件地址长度不合法');
      } else if (this.formData.remarks.length > 100) {
        this.showErrorMessage('备注长度不合法');
      } else {
        this.loading = true;
        axios.post('/api/delivery/order', {
          name: this.formData.name,
          number: this.formData.number,
          phone: this.formData.phone,
          price: parseFloat(this.formData.price),
          company: this.formData.company,
          address: this.formData.address,
          remarks: this.formData.remarks
        })
        .then(response => {
          this.loading = false;
          if (response.data.success) {
            this.$router.push('/delivery');
          } else {
            this.showErrorMessage(response.data.message);
          }
        })
        .catch(error => {
          this.loading = false;
          if (error.response) {
            this.showErrorMessage(error.response.data.message);
          } else {
            this.showErrorMessage('网络连接异常，请检查网络连接');
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
.weui-cells {
  margin-top: 0;
}

.show {
  display: block;
}
</style>