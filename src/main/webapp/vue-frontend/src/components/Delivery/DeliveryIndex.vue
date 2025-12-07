<template>
  <div>
    <div class="weui-cells__title" @click="goBack">返回</div>

    <div class="avatarDiv">
      <img class="avatar" :src="avatarURL || '/img/avatar/default.png'">
    </div>

    <div class="nickname">
      <h2 class="weui-msg__title">{{ nickname }}</h2>
    </div>

    <div class="phone">
      <div class="links">
        <div @click="$router.push('/delivery/order')">
          <i style="background: url(/img/delivery/order.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
          <p>我要下单</p>
        </div>
        <div @click="$router.push('/delivery/accept')">
          <i style="background: url(/img/delivery/take.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
          <p>我要接单</p>
        </div>
        <div @click="$router.push('/delivery/personal')">
          <i style="background: url(/img/delivery/personal.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
          <p>个人中心</p>
        </div>
      </div>
    </div>

    <!-- 错误提示 -->
    <div class="weui-toptips weui_warn js_tooltips" :class="{ 'show': showError }" ref="errorTip">{{ errorMessage }}</div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'DeliveryIndex',
  data() {
    return {
      avatarURL: '',
      nickname: '',
      showError: false,
      errorMessage: ''
    };
  },
  mounted() {
    this.loadIndexData();
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    loadIndexData() {
      axios.get('/api/delivery/index/data')
        .then(response => {
          const data = response.data;
          this.avatarURL = data.avatarURL;
          this.nickname = data.nickname;
        })
        .catch(error => {
          this.showErrorMessage(error.response?.data?.message || '加载数据失败');
        });
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
.avatar {
  width: 100%;
  height: 100%;
  border-radius: 50px;
}

.avatarDiv {
  width: 100px;
  height: 100px;
  margin: 25px auto;
}

.nickname {
  width: 60%;
  text-align: center;
  margin: 0 auto 1.5rem;
}

.nickname h2 {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.show {
  display: block;
}
</style>