<template>
  <div id="edit">
    <div class="hd">
      <h1 class="page_title">四六级成绩查询</h1>
      <p class="page_desc">成绩仅供参考，请以成绩单为准</p>
    </div>
    <div class="bd">
      <div class="weui-cells__title">
        英语四六级成绩查询
      </div>
      <div class="weui-cells weui-cells_form">
        <div class="weui-cell">
          <div class="weui-cell__hd"><label class="weui-label">姓名</label></div>
          <div class="weui-cell__bd">
            <input class="weui-input" id="name" v-model="formData.name" placeholder="请输入姓名" type="text" maxlength="20">
          </div>
        </div>
        <div class="weui-cell">
          <div class="weui-cell__hd"><label class="weui-label">准考证号</label></div>
          <div class="weui-cell__bd">
            <input class="weui-input" id="number" v-model="formData.number" placeholder="请输入准考证号" type="text" maxlength="15" @input="inputLengthCheck($event, 15)">
          </div>
          <div class="weui-cell__ft">
            <a class="weui-vcode-btn" @click="importNumber">导入</a>
            <a class="weui-vcode-btn" @click="saveNumber">保存</a>
          </div>
        </div>
        <div class="weui-cell weui-cell_vcode">
          <div class="weui-cell__hd">
            <label class="weui-label">验证码</label>
          </div>
          <div class="weui-cell__bd">
            <input class="weui-input" id="checkcode" v-model="formData.checkcode" placeholder="请输入验证码" type="text">
          </div>
          <div class="weui-cell__ft">
            <img class="weui-vcode-img" id="checkcode-img" :src="checkcodeUrl" @click="updateCheckCode">
          </div>
        </div>
      </div>
      <div class="weui-btn-area">
        <a class="weui-btn weui-btn_primary" href="javascript:" @click="postQueryForm">查询</a>
      </div>

      <!-- 错误提示 -->
      <div class="weui-toptips weui_warn js_tooltips" v-if="showError">{{ errorMessage }}</div>

      <br><br>

      <div class="weui-cells__title">
        备用查询入口
      </div>
      <div class="weui-cells">
        <a class="weui-cell weui-cell_access" href="javascript:" @click="window.location.href = 'http://www.chsi.com.cn/cet/'">
          <div class="weui-cell__bd">
            <p>学信网四六级查分</p>
          </div>
          <div class="weui-cell__ft">
          </div>
        </a>
        <a class="weui-cell weui-cell_access" href="javascript:" @click="window.location.href = 'http://cet.neea.edu.cn/cet/'">
          <div class="weui-cell__bd">
            <p>中国教育考试网查询</p>
          </div>
          <div class="weui-cell__ft">
          </div>
        </a>
      </div>
    </div>

    <!-- 查询结果页面 -->
    <div id="result" v-if="showResult">
      <div class="hd">
        <h1 class="page_title">查询结果</h1>
        <p class="page_desc">成绩仅供参考,请以成绩单为准</p>
      </div>

      <div class="weui-msg">
        <div class="weui_text_area">
          <h2 id="cetName" class="weui-msg_title">{{ resultData.name }}</h2>
          <p id="cetType" class="weui-msg_desc">{{ resultData.type }}</p>
          <p id="cetSchool" class="weui-msg_desc">{{ resultData.school }}</p>
          <br>
          <p id="cetTotalScore" class="weui-msg_desc" style="color: red">总分: {{ resultData.totalScore }}</p>
          <p id="cetListeningScore" class="weui-msg_desc">听力: {{ resultData.listeningScore }}</p>
          <p id="cetReadingScore" class="weui-msg_desc">阅读: {{ resultData.readingScore }}</p>
          <p id="cetWritingAndTranslatingScore" class="weui-msg_desc">写作和翻译: {{ resultData.writingAndTranslatingScore }}</p>
        </div>
        <br>
        <div class="weui_opr_area">
          <p class="weui-btn_area">
            <a @click="reQuery" class="weui-btn weui-btn_primary" href="javascript:">重新查询</a>
            <a @click="$router.go(-1)" class="weui-btn weui-btn_default" href="javascript:">返回主页</a>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Cet',
  data() {
    return {
      formData: {
        name: '',
        number: '',
        checkcode: ''
      },
      checkcodeUrl: '',
      showResult: false,
      showError: false,
      errorMessage: '',
      resultData: {}
    }
  },
  mounted() {
    this.updateCheckCode()
  },
  methods: {
    inputLengthCheck(event, maxLength) {
      let value = event.target.value
      if (value.length > maxLength) {
        event.target.value = value.substring(0, maxLength)
        this.formData.number = value.substring(0, maxLength)
      }
    },
    importNumber() {
      axios.get('/api/cet/number')
        .then(response => {
          if (response.data.success && response.data.data) {
            this.formData.number = response.data.data.toString()
          }
        })
        .catch(error => {
          console.error('导入考号失败:', error)
        })
    },
    saveNumber() {
      this.$router.push('/cet/save')
    },
    updateCheckCode() {
      this.checkcodeUrl = '/api/cet/checkcode?time=' + Date.now()
    },
    postQueryForm() {
      if (!this.formData.name || !this.formData.number || !this.formData.checkcode) {
        this.showError = true
        this.errorMessage = '请填写完整信息'
        setTimeout(() => {
          this.showError = false
        }, 3000)
        return
      }

      // 显示加载中
      weui.loading('查询中', { duration: 0 })

      axios.post('/rest/cetquery', this.formData, {
        params: {
          token: localStorage.getItem('token') || ''
        }
      })
        .then(response => {
          weui.hideLoading()
          if (response.data.success) {
            this.resultData = response.data.data
            this.showResult = true
          } else {
            this.showError = true
            this.errorMessage = response.data.message || '查询失败'
            setTimeout(() => {
              this.showError = false
            }, 3000)
            this.updateCheckCode()
          }
        })
        .catch(error => {
          weui.hideLoading()
          console.error('查询失败:', error)
          if (error.response) {
            if (error.response.status === 503) {
              this.showError = true
              this.errorMessage = '四六级查询系统维护中，请稍后再试'
            } else {
              this.showError = true
              this.errorMessage = '查询失败，请检查网络连接'
            }
          } else {
            this.showError = true
            this.errorMessage = '查询失败，请检查网络连接'
          }
          setTimeout(() => {
            this.showError = false
          }, 3000)
          this.updateCheckCode()
        })
    },
    reQuery() {
      this.formData.name = ''
      this.formData.checkcode = ''
      this.showResult = false
      this.updateCheckCode()
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