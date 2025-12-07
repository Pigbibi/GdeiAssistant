<template>
  <div>
    <div>
      <div class="weui-cells__title" @click="goBack">返回</div>
      <div class="hd">
        <h1 class="page_title" style="margin-top: 15px">接单中心</h1>
      </div>
    </div>

    <div class="weui-cells">
      <a v-for="order in orderList" :key="order.orderId" class="weui-cell weui-cell_access" :href="'javascript:void(0)'" @click="goToOrderDetail(order.orderId)">
        <div class="weui-cell__bd">
          <label class="weui-label">
            <p>{{ order.company }}</p>
            <p style="overflow: hidden;text-overflow:ellipsis;white-space: nowrap;font-size:13px;color:#999">{{ order.address }}</p>
          </label>
        </div>
        <div class="weui-cell__ft"> {{ order.price.toFixed(2) }}元</div>
      </a>
    </div>

    <div id='bottomLine' v-if="showLoadMore" style='display:block;text-align: center;color: #888;font-size: 14px;margin: 15px' @click="loadDeliveryOrder">
      {{ loadMoreText }}
    </div>

    <!-- 加载中弹框 -->
    <div role="alert" id="loadingToast" v-if="loading" style="display: block;">
      <div class="weui-mask_transparent"></div>
      <div class="weui-toast">
            <span class="weui-primary-loading weui-icon_toast">
              <span class="weui-primary-loading__dot"></span>
            </span>
        <p class="weui-toast__content">{{ loadingText }}</p>
      </div>
    </div>

    <!-- 错误提示 -->
    <div class="weui-toptips weui_warn js_tooltips" :class="{ 'show': showError }" ref="errorTip">{{ errorMessage }}</div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'DeliveryAccept',
  data() {
    return {
      orderList: [],
      loading: false,
      loadingText: '加载中',
      showLoadMore: true,
      loadMoreText: '点击加载更多',
      showError: false,
      errorMessage: ''
    };
  },
  mounted() {
    this.loadDeliveryOrder();
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    goToOrderDetail(orderId) {
      this.$router.push(`/delivery/order/id/${orderId}`);
    },
    //加载快递代收订单信息
    loadDeliveryOrder() {
      this.loading = true;
      this.loadingText = '加载中';
      
      axios.get(`/api/delivery/order/start/${this.orderList.length}/size/10`)
        .then(response => {
          this.loading = false;
          if (response.data.success) {
            if (response.data.data.length == 0) {
              this.loadMoreText = '已经到底了';
              this.showLoadMore = false;
            } else {
              this.orderList = [...this.orderList, ...response.data.data];
            }
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