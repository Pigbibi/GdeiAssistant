<template>
  <div>
    <div>
      <div class="weui-cells__title" @click="goBack">返回</div>
      <div class="hd">
        <h1 class="page_title" style="margin-top: 15px">交易单信息</h1>
      </div>
    </div>

    <div v-if="deliveryTrade">
      <div class="weui-cells__title">全民快递交易信息</div>
      <div class="weui-cells">
        <div class="weui-cell" href="javascript:">
          <div class="weui-cell__bd">
            <p>交易号</p>
          </div>
          <div class="weui-cell__ft">{{ deliveryTrade.tradeId }}</div>
        </div>
        <a class="weui-cell weui-cell_access" href="javascript:" @click="goToOrderDetail(deliveryTrade.orderId)">
          <div class="weui-cell__bd">
            <p>订单号</p>
          </div>
          <div class="weui-cell__ft">{{ deliveryTrade.orderId }}</div>
        </a>
        <div class="weui-cell" href="javascript:">
          <div class="weui-cell__bd">
            <p>交易时间</p>
          </div>
          <div class="weui-cell__ft">{{ formatDate(deliveryTrade.tradeTime) }}</div>
        </div>
        <a class="weui-cell weui-cell_access" href="javascript:" @click="goToUserProfile(deliveryTrade.helperUsername)">
          <div class="weui-cell__bd">
            <p>接单人</p>
          </div>
          <div class="weui-cell__ft">{{ deliveryTrade.helperUsername }}</div>
        </a>
        <a class="weui-cell weui-cell_access" href="javascript:" @click="goToUserProfile(deliveryTrade.username)">
          <div class="weui-cell__bd">
            <p>发布者</p>
          </div>
          <div class="weui-cell__ft">{{ deliveryTrade.username }}</div>
        </a>
        <div class="weui-cell" href="javascript:">
          <div class="weui-cell__bd">
            <p>交易状态</p>
          </div>
          <div class="weui-cell__ft">
            {{ deliveryTrade.state === 0 ? '交易未完成' : '交易已完成' }}
          </div>
        </div>
        <div class="weui-cell" href="javascript:">
          <div class="weui-cell__bd">
            <p>报酬</p>
          </div>
          <div class="weui-cell__ft">￥{{ deliveryTrade.price.toFixed(2) }}</div>
        </div>
      </div>

      <!-- 如果交易未完成，则显示确认交付按钮 -->
      <div v-if="deliveryTrade.state === 0 && canFinishTrade">
        <p class="weui-btn-area">
          <a href="javascript:" @click="finishTrade(deliveryTrade.tradeId)" class="weui-btn weui-btn_primary">确认交付</a>
        </p>
      </div>
    </div>

    <div v-else-if="!hasPermission">
      <div class="weui-msg">
        <div class="weui-msg__icon-area"><i class="weui-icon-warn weui-icon_msg"></i></div>
        <div class="weui-msg__text-area">
          <h2 class="weui-msg__title">没有权限查看</h2>
          <p class="weui-msg__desc">你没有权限查看该交易信息</p>
        </div>
      </div>
    </div>

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
  name: 'DeliveryTradeDetail',
  data() {
    return {
      deliveryTrade: null,
      hasPermission: false,
      canFinishTrade: false,
      loading: false,
      showError: false,
      errorMessage: ''
    };
  },
  mounted() {
    this.loadTradeDetail();
  },
  methods: {
    goBack() {
      this.$router.go(-1);
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
    goToOrderDetail(orderId) {
      this.$router.push(`/delivery/order/id/${orderId}`);
    },
    goToUserProfile(username) {
      window.location.href = `/profile/user/${username}`;
    },
    loadTradeDetail() {
      const tradeId = this.$route.params.id;
      this.loading = true;
      
      axios.get(`/api/delivery/trade/id/${tradeId}`)
        .then(response => {
          this.loading = false;
          const data = response.data;
          this.deliveryTrade = data.deliveryTrade;
          this.hasPermission = data.hasPermission;
          this.canFinishTrade = data.canFinishTrade;
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
    //完成交易
    finishTrade(tradeId) {
      if (confirm('是否确认已完成交付？确认后将完成该交易')) {
        this.loading = true;
        axios.post(`/api/delivery/trade/id/${tradeId}/finishtrade`)
          .then(response => {
            this.loading = false;
            if (response.data.success) {
              alert('交易完成，点击确定刷新页面');
              window.location.reload();
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