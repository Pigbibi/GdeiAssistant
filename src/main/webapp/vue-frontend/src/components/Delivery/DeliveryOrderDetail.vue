<template>
  <div>
    <div>
      <div class="weui-cells__title" @click="goBack">返回</div>
      <div class="hd">
        <h1 class="page_title" style="margin-top: 15px">订单信息</h1>
      </div>
    </div>

    <div v-if="detailType === 0 || detailType === 1 || detailType === 3">
      <div class="weui-cells__title">全民快递订单基础信息</div>
      <div class="weui-cells">
        <div class="weui-cell" href="javascript:">
          <div class="weui-cell__bd">
            <p>订单号</p>
          </div>
          <div class="weui-cell__ft">{{ deliveryOrder.orderId }}</div>
        </div>
        <a class="weui-cell weui-cell_access" href="javascript:" @click="goToUserProfile(deliveryOrder.username)">
          <div class="weui-cell__bd">
            <p>发布者</p>
          </div>
          <div class="weui-cell__ft">{{ deliveryOrder.username }}</div>
        </a>
        <div class="weui-cell" href="javascript:">
          <div class="weui-cell__bd">
            <p>下单时间</p>
          </div>
          <div class="weui-cell__ft">{{ formatDate(deliveryOrder.orderTime) }}</div>
        </div>
        <div class="weui-cell" href="javascript:">
          <div class="weui-cell__bd">
            <p>报酬</p>
          </div>
          <div class="weui-cell__ft">￥{{ deliveryOrder.price.toFixed(2) }}</div>
        </div>
        <div class="weui-cell" href="javascript:">
          <div class="weui-cell__bd">
            <p>送往地址</p>
          </div>
          <div class="weui-cell__ft">{{ deliveryOrder.address }}</div>
        </div>
      </div>

      <div v-if="detailType === 0 || detailType === 3">
        <div class="weui-cells__title">全民快递订单详细信息</div>
        <div class="weui-cells">
          <div class="weui-cell" href="javascript:">
            <div class="weui-cell__bd">
              <p>姓名</p>
            </div>
            <div class="weui-cell__ft">{{ deliveryOrder.name }}</div>
          </div>
          <div class="weui-cell" href="javascript:">
            <div class="weui-cell__bd">
              <p>学号</p>
            </div>
            <div class="weui-cell__ft">{{ deliveryOrder.number }}</div>
          </div>
          <div class="weui-cell" href="javascript:">
            <div class="weui-cell__bd">
              <p>手机号</p>
            </div>
            <div class="weui-cell__ft">{{ deliveryOrder.phone }}</div>
          </div>
          <div class="weui-cell" href="javascript:">
            <div class="weui-cell__bd">
              <p>快递公司</p>
            </div>
            <div class="weui-cell__ft">{{ deliveryOrder.company }}</div>
          </div>
          <div class="weui-cell" href="javascript:">
            <div class="weui-cell__bd">
              <p>备注</p>
            </div>
            <div class="weui-cell__ft">{{ deliveryOrder.remarks }}</div>
          </div>
        </div>

        <div v-if="detailType === 0 && deliveryOrder.state === 0">
          <p class="weui-btn-area">
            <a href="javascript:" @click="deleteOrder(deliveryOrder.orderId)" class="weui-btn weui-btn_warn">删除</a>
          </p>
        </div>

        <div v-if="deliveryOrder.state === 1 && deliveryTrade">
          <div class="weui-cells__title">全民快递订单交易信息</div>
          <div class="weui-cells">
            <a class="weui-cell weui-cell_access" :href="'javascript:void(0)'" @click="goToTradeDetail(deliveryTrade.tradeId)">
              <div class="weui-cell__bd">
                <p>交易详细信息</p>
              </div>
              <div class="weui-cell__ft"></div>
            </a>
          </div>
        </div>
      </div>

      <div v-else>
        <p class="weui-btn-area">
          <a href="javascript:" @click="acceptOrder(deliveryOrder.orderId)" class="weui-btn weui-btn_primary">接单</a>
        </p>
      </div>
    </div>

    <div v-else>
      <div class="weui-msg">
        <div class="weui-msg__icon-area"><i class="weui-icon-warn weui-icon_msg"></i></div>
        <div class="weui-msg__text-area">
          <h2 class="weui-msg__title">没有权限查看</h2>
          <p class="weui-msg__desc">你没有权限查看该订单信息</p>
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
  name: 'DeliveryOrderDetail',
  data() {
    return {
      deliveryOrder: {},
      deliveryTrade: null,
      detailType: 0,
      loading: false,
      showError: false,
      errorMessage: ''
    };
  },
  mounted() {
    this.loadOrderDetail();
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
    goToUserProfile(username) {
      window.location.href = `/profile/user/${username}`;
    },
    goToTradeDetail(tradeId) {
      this.$router.push(`/delivery/trade/id/${tradeId}`);
    },
    loadOrderDetail() {
      const orderId = this.$route.params.id;
      this.loading = true;
      
      axios.get(`/api/delivery/order/id/${orderId}`)
        .then(response => {
          this.loading = false;
          const data = response.data;
          this.deliveryOrder = data.deliveryOrder;
          this.deliveryTrade = data.deliveryTrade;
          this.detailType = data.detailType;
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
    //删除订单
    deleteOrder(orderId) {
      if (confirm('将删除该订单，订单信息无法恢复，你确定吗？')) {
        this.loading = true;
        axios.delete(`/api/delivery/order/id/${orderId}`)
          .then(response => {
            this.loading = false;
            if (response.data.success) {
              alert('删除订单成功');
              this.$router.go(-1);
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
    //用户接单
    acceptOrder(orderId) {
      this.loading = true;
      axios.post('/api/delivery/acceptorder', {
        orderId: orderId
      })
      .then(response => {
        this.loading = false;
        if (response.data.success) {
          alert('接单成功，点击确定刷新订单信息');
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