<template>
  <div>
    <div>
      <div class="weui-cells__title" @click="goBack">返回</div>
      <div class="hd">
        <h1 class="page_title" style="margin-top: 15px">个人中心</h1>
      </div>
    </div>

    <div v-if="orderList.length === 0 && acceptedOrderList.length === 0">
      <div class="weui-msg">
        <div class="weui-msg__icon-area"><i class="weui-icon-warn weui-icon_msg"></i></div>
        <div class="weui-msg__text-area">
          <h2 class="weui-msg__title">没有任何记录</h2>
          <p class="weui-msg__desc">没有找到任何下单和接单记录</p>
        </div>
      </div>
    </div>

    <div v-if="orderList.length > 0">
      <div class="weui-cells__title">我的下单</div>
      <div class="weui-cells">
        <a v-for="order in orderList" :key="order.orderId" class="weui-cell weui-cell_access" :href="'javascript:void(0)'" @click="goToOrderDetail(order.orderId)">
          <div class="weui-cell__bd">
            <p>订单号：{{ order.orderId }}</p>
            <p style="font-size:13px;color:#999">下单时间：{{ formatDate(order.orderTime) }}</p>
            <p style="font-size:13px;color:#999">订单状态：{{ getOrderStateText(order.state) }}</p>
          </div>
          <div class="weui-cell__ft"></div>
        </a>
      </div>
    </div>

    <div v-if="acceptedOrderList.length > 0">
      <div class="weui-cells__title">我的接单</div>
      <div class="weui-cells">
        <a v-for="order in acceptedOrderList" :key="order.orderId" class="weui-cell weui-cell_access" :href="'javascript:void(0)'" @click="goToOrderDetail(order.orderId)">
          <div class="weui-cell__bd">
            <p>订单号：{{ order.orderId }}</p>
            <p style="font-size:13px;color:#999">下单时间：{{ formatDate(order.orderTime) }}</p>
            <p style="font-size:13px;color:#999">订单状态：{{ getOrderStateText(order.state) }}</p>
          </div>
          <div class="weui-cell__ft"></div>
        </a>
      </div>
    </div>

    <!-- 提交中弹框 -->
    <div role="alert" id="loadingToast" v-if="loading" style="display: block;">
      <div class="weui-mask_transparent"></div>
      <div class="weui-toast">
            <span class="weui-primary-loading weui-icon_toast">
              <span class="weui-primary-loading__dot"></span>
            </span>
        <p class="weui-toast__content">加载中</p>
      </div>
    </div>

    <!-- 错误提示 -->
    <div class="weui-toptips weui_warn js_tooltips" :class="{ 'show': showError }" ref="errorTip">{{ errorMessage }}</div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'DeliveryPersonal',
  data() {
    return {
      orderList: [],
      acceptedOrderList: [],
      loading: false,
      showError: false,
      errorMessage: ''
    };
  },
  mounted() {
    this.loadPersonalData();
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    goToOrderDetail(orderId) {
      this.$router.push(`/delivery/order/id/${orderId}`);
    },
    formatDate(date) {
      if (!date) return '';
      const d = new Date(date);
      const year = d.getFullYear();
      const month = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      const hours = String(d.getHours()).padStart(2, '0');
      const minutes = String(d.getMinutes()).padStart(2, '0');
      const seconds = String(d.getSeconds()).padStart(2, '0');
      return `${year}年${month}月${day}日 ${hours}:${minutes}:${seconds}`;
    },
    getOrderStateText(state) {
      switch (state) {
        case 0:
          return '待接单';
        case 1:
          return '已接单';
        case 2:
          return '已删除';
        default:
          return '未知状态';
      }
    },
    loadPersonalData() {
      this.loading = true;
      
      // 加载我的下单记录
      axios.get('/api/delivery/personal/orders')
        .then(response => {
          if (response.data.success) {
            this.orderList = response.data.data;
          } else {
            this.showErrorMessage(response.data.message);
          }
          
          // 加载我的接单记录
          return axios.get('/api/delivery/personal/acceptedorders');
        })
        .then(response => {
          this.loading = false;
          if (response.data.success) {
            this.acceptedOrderList = response.data.data;
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