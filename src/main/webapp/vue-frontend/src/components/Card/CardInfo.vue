<template>
  <div class="card-info-container">
    <div class="card-info-header">
      <h2>校园卡信息</h2>
    </div>
    <div class="card-info-content" v-if="cardInfo">
      <div class="info-group">
        <label>姓名:</label>
        <span id="name">{{ cardInfo.name }}</span>
      </div>
      <div class="info-group">
        <label>学号:</label>
        <span id="number">{{ cardInfo.number }}</span>
      </div>
      <div class="info-group">
        <label>卡号:</label>
        <span id="cardNumber">{{ cardInfo.cardNumber }}</span>
      </div>
      <div class="info-group">
        <label>余额:</label>
        <span id="cardBalance">{{ cardInfo.cardBalance }}</span>
      </div>
      <div class="info-group">
        <label>临时余额:</label>
        <span id="cardInterimBalance">{{ cardInfo.cardInterimBalance }}</span>
      </div>
      <div class="info-group">
        <label>挂失状态:</label>
        <span id="cardLostState">{{ cardInfo.cardLostState }}</span>
      </div>
      <div class="info-group">
        <label>冻结状态:</label>
        <span id="cardFreezeState">{{ cardInfo.cardFreezeState }}</span>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'CardInfo',
  data() {
    return {
      cardInfo: null
    }
  },
  mounted() {
    this.queryCardInfo()
  },
  methods: {
    queryCardInfo() {
      const loading = weui.loading('查询中')
      axios.post('/api/cardinfoquery')
        .then(response => {
          loading.hide()
          if (response.data.success) {
            this.cardInfo = response.data.data
          } else {
            weui.topTips(response.data.message)
          }
        })
        .catch(error => {
          loading.hide()
          if (error.response) {
            weui.topTips(error.response.data.message)
          } else {
            weui.topTips('网络连接异常，请检查网络连接')
          }
        })
    }
  }
}
</script>

<style scoped>
.card-info-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.card-info-header {
  text-align: center;
  margin-bottom: 20px;
}

.card-info-content {
  background-color: #f5f5f5;
  padding: 20px;
  border-radius: 8px;
}

.info-group {
  margin-bottom: 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-group label {
  font-weight: bold;
  flex: 1;
}

.info-group span {
  flex: 2;
  text-align: right;
}
</style>