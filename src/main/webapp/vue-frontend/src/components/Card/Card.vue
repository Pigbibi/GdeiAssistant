<template>
  <div class="card-query-container">
    <div class="card-query-header">
      <h2>饭卡消费查询</h2>
    </div>
    <div class="card-query-form">
      <div class="form-group">
        <label for="year">年份</label>
        <select id="year" v-model="cardQuery.year">
          <!-- 年份选项将通过JavaScript动态生成 -->
        </select>
      </div>
      <div class="form-group">
        <label for="month">月份</label>
        <select id="month" v-model="cardQuery.month">
          <!-- 月份选项将通过JavaScript动态生成 -->
        </select>
      </div>
      <div class="form-group">
        <label for="date">日期</label>
        <select id="date" v-model="cardQuery.date">
          <!-- 日期选项将通过JavaScript动态生成 -->
        </select>
      </div>
      <button @click="submitQuery" class="query-btn">查询</button>
    </div>
    <div id="result-container" class="result-container" v-if="cardQueryResult">
      <div class="card-info">
        <h3>校园卡信息</h3>
        <p>姓名: {{ cardQueryResult.cardInfo.name }}</p>
        <p>学号: {{ cardQueryResult.cardInfo.number }}</p>
        <p>卡号: {{ cardQueryResult.cardInfo.cardNumber }}</p>
        <p>余额: {{ cardQueryResult.cardInfo.cardBalance }}</p>
        <p>临时余额: {{ cardQueryResult.cardInfo.cardInterimBalance }}</p>
        <p>挂失状态: {{ cardQueryResult.cardInfo.cardLostState }}</p>
        <p>冻结状态: {{ cardQueryResult.cardInfo.cardFreezeState }}</p>
      </div>
      <div class="card-transactions">
        <h3>消费记录</h3>
        <table class="transaction-table">
          <thead>
            <tr>
              <th>交易时间</th>
              <th>商户名称</th>
              <th>交易名称</th>
              <th>交易金额</th>
              <th>账户余额</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(card, index) in cardQueryResult.cardList" :key="index">
              <td>{{ card.tradeTime }}</td>
              <td>{{ card.merchantName }}</td>
              <td>{{ card.tradeName }}</td>
              <td>{{ card.tradePrice }}</td>
              <td>{{ card.accountBalance }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Card',
  data() {
    return {
      cardQuery: {
        year: new Date().getFullYear(),
        month: new Date().getMonth() + 1,
        date: new Date().getDate()
      },
      cardQueryResult: null
    }
  },
  mounted() {
    this.initDateSelectors()
  },
  methods: {
    initDateSelectors() {
      // 初始化年份选择器
      const yearSelect = document.getElementById('year')
      const currentYear = new Date().getFullYear()
      for (let i = currentYear - 5; i <= currentYear; i++) {
        const option = document.createElement('option')
        option.value = i
        option.textContent = i + '年'
        if (i === currentYear) {
          option.selected = true
        }
        yearSelect.appendChild(option)
      }

      // 初始化月份选择器
      const monthSelect = document.getElementById('month')
      for (let i = 1; i <= 12; i++) {
        const option = document.createElement('option')
        option.value = i
        option.textContent = i + '月'
        if (i === new Date().getMonth() + 1) {
          option.selected = true
        }
        monthSelect.appendChild(option)
      }

      // 初始化日期选择器
      this.updateDateOptions()

      // 监听年份和月份变化，更新日期选项
      yearSelect.addEventListener('change', () => {
        this.updateDateOptions()
      })

      monthSelect.addEventListener('change', () => {
        this.updateDateOptions()
      })
    },

    updateDateOptions() {
      const year = parseInt(document.getElementById('year').value)
      const month = parseInt(document.getElementById('month').value)
      const dateSelect = document.getElementById('date')
      const currentDate = new Date().getDate()

      // 清空现有日期选项
      dateSelect.innerHTML = ''

      // 计算当月天数
      const daysInMonth = new Date(year, month, 0).getDate()

      // 添加日期选项
      for (let i = 1; i <= daysInMonth; i++) {
        const option = document.createElement('option')
        option.value = i
        option.textContent = i + '日'
        if (i === currentDate && year === new Date().getFullYear() && month === new Date().getMonth() + 1) {
          option.selected = true
        }
        dateSelect.appendChild(option)
      }

      // 更新Vue模型中的日期值
      this.cardQuery.date = Math.min(this.cardQuery.date, daysInMonth)
    },

    submitQuery() {
      const loading = weui.loading('查询中')
      axios.post('/api/cardquery', this.cardQuery)
        .then(response => {
          loading.hide()
          if (response.data.success) {
            this.cardQueryResult = response.data.data
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
.card-query-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.card-query-header {
  text-align: center;
  margin-bottom: 20px;
}

.card-query-form {
  background-color: #f5f5f5;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.form-group select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.query-btn {
  width: 100%;
  padding: 10px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.query-btn:hover {
  background-color: #45a049;
}

.result-container {
  background-color: #f5f5f5;
  padding: 20px;
  border-radius: 8px;
}

.card-info {
  margin-bottom: 20px;
}

.card-info h3 {
  margin-top: 0;
  margin-bottom: 15px;
}

.card-info p {
  margin: 5px 0;
}

.card-transactions h3 {
  margin-top: 0;
  margin-bottom: 15px;
}

.transaction-table {
  width: 100%;
  border-collapse: collapse;
}

.transaction-table th,
.transaction-table td {
  padding: 8px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.transaction-table th {
  background-color: #4CAF50;
  color: white;
}
</style>