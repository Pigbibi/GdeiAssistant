<template>
  <div class="authentication-index">
    <div class="weui-header">
      <div class="weui-header-left">
        <a href="#" class="iconfont icon-return" @click="goBack"></a>
      </div>
      <h1 class="weui-header-title">实名认证</h1>
      <div class="weui-header-right"></div>
    </div>
    <div class="weui-content">
      <div class="weui-msg">
        <div class="weui-msg__icon-area">
          <i class="weui-icon-success weui-icon_msg" v-if="authenticationStatus"></i>
          <i class="weui-icon-info weui-icon_msg" v-else></i>
        </div>
        <div class="weui-msg__text-area">
          <h2 class="weui-msg__title" v-if="authenticationStatus">您已通过实名认证</h2>
          <h2 class="weui-msg__title" v-else>您尚未进行实名认证</h2>
          <p class="weui-msg__desc" v-if="authenticationStatus">
            认证信息：{{ authenticationInfo.name }}，{{ authenticationInfo.idCardNumber }}
          </p>
          <p class="weui-msg__desc" v-else>
            实名认证有助于提高账号安全性，保障您的权益
          </p>
        </div>
        <div class="weui-msg__opr-area">
          <p class="weui-btn-area">
            <a href="#" class="weui-btn weui-btn_primary" v-if="!authenticationStatus" @click="goToUpdate">进行实名认证</a>
            <a href="#" class="weui-btn weui-btn_warn" v-else @click="deleteAuthentication">取消认证</a>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'AuthenticationIndex',
  data() {
    return {
      authenticationStatus: false,
      authenticationInfo: {
        name: '',
        idCardNumber: ''
      }
    }
  },
  mounted() {
    // 获取认证状态
    this.getAuthenticationStatus()
  },
  methods: {
    // 返回上一页
    goBack() {
      this.$router.go(-1)
    },
    // 跳转到认证页面
    goToUpdate() {
      this.$router.push('/authentication/update')
    },
    // 获取认证状态
    getAuthenticationStatus() {
      axios.get('/api/authentication/status')
        .then(response => {
          if (response.data.code === 0) {
            this.authenticationStatus = response.data.data.status
            if (this.authenticationStatus) {
              this.authenticationInfo = response.data.data.authenticationInfo
            }
          } else {
            // 处理错误
            console.error('获取认证状态失败:', response.data.message)
          }
        })
        .catch(error => {
          console.error('获取认证状态时发生错误:', error)
        })
    },
    // 取消认证
    deleteAuthentication() {
      weui.confirm('确定要取消实名认证吗？', {
        title: '提示',
        buttons: [
          { label: '取消', type: 'default' },
          { label: '确定', type: 'primary' }
        ]
      }, (result) => {
        if (result.index === 1) {
          axios.delete('/api/authentication/delete')
            .then(response => {
              if (response.data.code === 0) {
                weui.toast('取消认证成功', {
                  duration: 2000,
                  callback: () => {
                    this.authenticationStatus = false
                  }
                })
              } else {
                weui.alert(response.data.message)
              }
            })
            .catch(error => {
              weui.alert('取消认证失败，请稍后重试')
              console.error('取消认证时发生错误:', error)
            })
        }
      })
    }
  }
}
</script>

<style scoped>
/* 保留原JSP页面的样式 */
.weui-header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background-color: #1AAD19;
  color: #FFFFFF;
  z-index: 1000;
  height: 44px;
  line-height: 44px;
  text-align: center;
  font-size: 18px;
}

.weui-header-left,
.weui-header-right {
  position: absolute;
  top: 0;
  width: 44px;
  height: 44px;
}

.weui-header-left {
  left: 0;
}

.weui-header-right {
  right: 0;
}

.weui-header-title {
  margin: 0 44px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.weui-content {
  margin-top: 44px;
  padding: 20px;
}

.weui-msg {
  text-align: center;
}

.weui-msg__icon-area {
  margin-bottom: 20px;
}

.weui-icon_msg {
  font-size: 80px;
  color: #1AAD19;
}

.weui-msg__title {
  font-size: 20px;
  margin-bottom: 10px;
}

.weui-msg__desc {
  font-size: 14px;
  color: #999;
  margin-bottom: 30px;
}

.weui-btn-area {
  margin: 20px auto;
  width: 90%;
}

.weui-btn {
  display: block;
  width: 100%;
  height: 44px;
  line-height: 44px;
  text-align: center;
  border-radius: 22px;
  text-decoration: none;
  font-size: 16px;
}

.weui-btn_primary {
  background-color: #1AAD19;
  color: #FFFFFF;
}

.weui-btn_warn {
  background-color: #E64340;
  color: #FFFFFF;
}
</style>