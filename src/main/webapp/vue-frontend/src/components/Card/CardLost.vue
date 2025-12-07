<template>
  <div class="card-lost-container">
    <div class="card-lost-header">
      <h2>校园卡挂失</h2>
    </div>
    <div class="card-lost-content">
      <p class="warning-text">校园卡挂失后将无法使用，请谨慎操作！</p>
      <div class="form-group">
        <label for="cardPassword">校园卡密码</label>
        <input 
          type="password" 
          id="cardPassword" 
          v-model="cardPassword"
          placeholder="请输入校园卡密码"
          maxlength="6"
          @input="checkPasswordLength"
        >
      </div>
      <button @click="submitCardLost" class="lost-btn">确认挂失</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'CardLost',
  data() {
    return {
      cardPassword: ''
    }
  },
  methods: {
    checkPasswordLength() {
      // 确保密码不超过6位
      if (this.cardPassword.length > 6) {
        this.cardPassword = this.cardPassword.substring(0, 6)
      }
    },
    submitCardLost() {
      if (!this.cardPassword) {
        weui.topTips('请输入校园卡密码')
        return
      }

      if (this.cardPassword.length !== 6) {
        weui.topTips('校园卡密码应为6位数字')
        return
      }

      const loading = weui.loading('挂失中')
      axios.post('/api/cardlost', { cardPassword: this.cardPassword })
        .then(response => {
          loading.hide()
          if (response.data.success) {
            weui.confirm('校园卡挂失成功！', {
              title: '提示',
              buttons: [{
                label: '确定',
                type: 'primary'
              }]
            })
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
.card-lost-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.card-lost-header {
  text-align: center;
  margin-bottom: 20px;
}

.card-lost-content {
  background-color: #f5f5f5;
  padding: 20px;
  border-radius: 8px;
}

.warning-text {
  color: red;
  margin-bottom: 20px;
  text-align: center;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.form-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
}

.lost-btn {
  width: 100%;
  padding: 10px;
  background-color: #ff4444;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.lost-btn:hover {
  background-color: #cc0000;
}
</style>