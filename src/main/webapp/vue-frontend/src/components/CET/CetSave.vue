<template>
  <div id="edit">
    <div class="hd">
      <h1 class="page_title">准考证号保存</h1>
      <p class="page_desc">保存后可在查询页面直接导入准考证号</p>
    </div>
    <div class="bd">
      <div class="weui-cells__title">
        英语四六级准考证号
      </div>
      <div class="weui-cells weui-cells_form">
        <div class="weui-cell">
          <div class="weui-cell__hd"><label class="weui-label">准考证号</label></div>
          <div class="weui-cell__bd">
            <input class="weui-input" id="number" v-model="cetNumber" placeholder="请输入准考证号" type="text" maxlength="15" @input="inputLengthCheck($event, 15)">
          </div>
        </div>
      </div>
      <div class="weui-btn-area">
        <a class="weui-btn weui-btn_primary" href="javascript:" @click="saveCetNumber">保存</a>
        <a class="weui-btn weui-btn_default" href="javascript:" @click="$router.go(-1)">返回</a>
      </div>

      <!-- 错误提示 -->
      <div class="weui-toptips weui_warn js_tooltips" v-if="showError">{{ errorMessage }}</div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'CetSave',
  data() {
    return {
      cetNumber: '',
      showError: false,
      errorMessage: ''
    }
  },
  mounted() {
    // 页面加载时获取已保存的考号
    this.getSavedCetNumber()
  },
  methods: {
    inputLengthCheck(event, maxLength) {
      let value = event.target.value
      if (value.length > maxLength) {
        event.target.value = value.substring(0, maxLength)
        this.cetNumber = value.substring(0, maxLength)
      }
    },
    getSavedCetNumber() {
      axios.get('/api/cet/number')
        .then(response => {
          if (response.data.success && response.data.data) {
            this.cetNumber = response.data.data.toString()
          }
        })
        .catch(error => {
          console.error('获取保存的考号失败:', error)
        })
    },
    saveCetNumber() {
      if (!this.cetNumber) {
        this.showError = true
        this.errorMessage = '请输入准考证号'
        setTimeout(() => {
          this.showError = false
        }, 3000)
        return
      }

      if (this.cetNumber.length !== 15) {
        this.showError = true
        this.errorMessage = '准考证号格式不正确'
        setTimeout(() => {
          this.showError = false
        }, 3000)
        return
      }

      // 显示加载中
      weui.loading('保存中', { duration: 0 })

      axios.post('/api/cet/number', null, {
        params: {
          number: this.cetNumber
        }
      })
        .then(response => {
          weui.hideLoading()
          if (response.data.success) {
            weui.toast('保存成功', () => {
              this.$router.go(-1)
            })
          } else {
            this.showError = true
            this.errorMessage = response.data.message || '保存失败'
            setTimeout(() => {
              this.showError = false
            }, 3000)
          }
        })
        .catch(error => {
          weui.hideLoading()
          console.error('保存考号失败:', error)
          this.showError = true
          this.errorMessage = '保存失败，请检查网络连接'
          setTimeout(() => {
            this.showError = false
          }, 3000)
        })
    }
  }
}
</script>

<style scoped>
/* 保持原有样式不变 */
</style>

<style>
@import "~@/assets/css/weui.min.css";
@import "~@/assets/css/common.css";
</style>