<template>
  <div class="panel-overlay"></div>
  <div class="warp">
    <div class="head" @click="goToIndex">
      <div class="logo"><span class="t">卖室友</span></div>
    </div>

    <div class="content" v-if="datingPick">
      <div class="record">
        <p class="record-t"><i class="iconfont"></i>撩一下记录</p>
        <div class='MessageBox'>
          <div class='yuan' style='top: -5.5px; left: -5.5px;'></div>
          <div class='yuan' style='top: -5.5px; right: -5.5px;'></div>
          <div class='movieName'>
            用户{{ datingPick.username }}撩了你一下
          </div>
          <div class='Message'>对方留言：{{ datingPick.content }}</div>
        </div>
      </div>

      <div class="action">
        <div style="background-color:white;color:black" @click="viewUserProfile">
          查看对方个人资料
        </div>
        <div v-if="datingPick.state === 0">
          <br>
          <div style="background-color:limegreen;" @click="acceptPick">接受</div>
          <br>
          <div style="background-color:red;" @click="rejectPick">拒绝</div>
        </div>
        <div v-else style="color:black">你已处理该撩一下请求</div>
      </div>
    </div>
  </div>

  <!-- 错误提示 -->
  <div class="weui-toptips weui_warn" v-if="showError" ref="errorTip">{{ errorMessage }}</div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'DatingPick',
  data() {
    return {
      pickId: '',
      datingPick: null,
      showError: false,
      errorMessage: ''
    }
  },
  mounted() {
    // 获取路径参数中的pickId
    this.pickId = this.$route.params.id
    
    // 加载撩一下记录
    this.loadPickDetail()
  },
  methods: {
    // 加载撩一下记录
    loadPickDetail() {
      axios.get(`/api/dating/pick/id/${this.pickId}`)
        .then(response => {
          if (response.data.success) {
            this.datingPick = response.data.data
          } else {
            this.showErrorMsg(response.data.message)
          }
        })
        .catch(error => {
          console.error('加载撩一下记录失败:', error)
          this.showErrorMsg('网络连接异常，请重试')
        })
    },
    
    // 接受撩一下请求
    acceptPick() {
      axios.post(`/api/dating/pick/id/${this.pickId}`, {
        state: 1
      })
      .then(response => {
        if (response.data.success) {
          // 刷新页面
          location.reload()
        } else {
          this.showErrorMsg(response.data.message)
        }
      })
      .catch(error => {
        console.error('接受撩一下请求失败:', error)
        this.showErrorMsg('网络连接异常，请重试')
      })
    },
    
    // 拒绝撩一下请求
    rejectPick() {
      axios.post(`/api/dating/pick/id/${this.pickId}`, {
        state: -1
      })
      .then(response => {
        if (response.data.success) {
          // 刷新页面
          location.reload()
        } else {
          this.showErrorMsg(response.data.message)
        }
      })
      .catch(error => {
        console.error('拒绝撩一下请求失败:', error)
        this.showErrorMsg('网络连接异常，请重试')
      })
    },
    
    // 查看对方个人资料
    viewUserProfile() {
      this.$router.push(`/profile/user/${this.datingPick.username}`)
    },
    
    // 跳转到首页
    goToIndex() {
      this.$router.push('/dating')
    },
    
    // 显示错误消息
    showErrorMsg(message) {
      this.errorMessage = message
      this.showError = true
      setTimeout(() => {
        this.showError = false
      }, 2000)
    }
  }
}
</script>

<style scoped>
/* 引入外部CSS文件 */
@import '/css/dating/global.css';
@import '/css/dating/layout.css';

/* 错误提示样式 */
.weui-toptips {
  position: fixed;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  z-index: 9999;
  padding: 10px;
  background-color: #f43530;
  color: #fff;
  border-radius: 0 0 4px 4px;
  display: none;
}

.weui-toptips.show {
  display: block;
}
</style>