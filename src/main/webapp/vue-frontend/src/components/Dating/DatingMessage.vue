<template>
  <div class="panel-overlay"></div>
  <div class="warp">
    <div class="head" @click="goToIndex">
      <div class="logo"><span class="t">卖室友</span></div>
    </div>

    <div class="content">
      <div class="record">
        <p class="record-t"><i class="iconfont"></i>卖室友消息</p>
        <!-- 消息列表 -->
        <div 
          v-for="message in messages" 
          :key="message.messageId"
          @click="handleMessageClick(message)"
          class="MessageBox"
        >
          <div class="yuan" style="top: -5.5px; left: -5.5px;"></div>
          <div class="yuan" style="top: -5.5px; right: -5.5px;"></div>
          <div class="movieName">
            <span v-if="message.state === 0">[未读]</span>
            {{ getMessageTitle(message) }}
          </div>
          <div class="Message">{{ getMessageContent(message) }}</div>
        </div>
      </div>

      <div style="text-align: center">
        <span id="loadMore" @click="loadMore">{{ hasMore ? '点击加载更多' : '没有更多消息了' }}</span>
      </div>
    </div>
  </div>

  <!-- 错误提示 -->
  <div class="weui-toptips weui_warn" v-if="showError" ref="errorTip">{{ errorMessage }}</div>
</template>

<script>
import axios from 'axios'
import weui from 'weui'

export default {
  name: 'DatingMessage',
  data() {
    return {
      messageIndex: 0,
      hasMore: true,
      messages: [],
      showError: false,
      errorMessage: ''
    }
  },
  mounted() {
    
    // 加载消息
    this.loadMore()
  },
  methods: {
    // 加载更多消息
    loadMore() {
      if (this.hasMore) {
        // 显示等待动画
        const loading = weui.loading('加载中')
        
        axios.get(`/api/dating/message/start/${this.messageIndex}`)
          .then(response => {
            loading.hide()
            if (response.data.success) {
              if (response.data.data.length === 0) {
                this.hasMore = false
              } else {
                this.messageIndex += response.data.data.length
                this.messages.push(...response.data.data)
              }
            } else {
              this.showErrorMsg(response.data.message)
            }
          })
          .catch(error => {
            loading.hide()
            console.error('加载消息失败:', error)
            this.showErrorMsg('网络连接异常，请重试')
          })
      }
    },
    
    // 获取消息标题
    getMessageTitle(message) {
      if (message.type === 0) {
        // 撩一下通知
        return '被撩通知'
      } else {
        // 撩一下处理通知
        if (message.datingPick.state === -1) {
          // 被拒绝通知
          return '撩一下请求被拒绝'
        } else {
          // 被接受通知
          return '撩一下请求被接受'
        }
      }
    },
    
    // 获取消息内容
    getMessageContent(message) {
      if (message.type === 0) {
        // 撩一下通知
        return `你被用户${message.datingPick.username}撩了一下，点击查看`
      } else {
        // 撩一下处理通知
        if (message.datingPick.state === -1) {
          // 被拒绝通知
          return `${message.datingPick.datingProfile.nickname}拒绝了你的撩一下请求`
        } else {
          // 被接受通知
          return `${message.datingPick.datingProfile.nickname}接受了你的撩一下请求`
        }
      }
    },
    
    // 处理消息点击
    handleMessageClick(message) {
      if (message.state === 0) {
        // 标记消息为已读
        this.markMessageAsRead(message.messageId, message)
      } else {
        // 直接跳转
        this.navigateToMessageTarget(message)
      }
    },
    
    // 标记消息为已读
    markMessageAsRead(messageId, message) {
      axios.post(`/api/dating/message/id/${messageId}/read`)
        .then(response => {
          if (response.data.success) {
            // 更新消息状态
            message.state = 1
            // 跳转到目标页面
            this.navigateToMessageTarget(message)
          } else {
            this.showErrorMsg(response.data.message)
          }
        })
        .catch(error => {
          console.error('标记消息为已读失败:', error)
          this.showErrorMsg('网络连接异常，请重试')
        })
    },
    
    // 跳转到消息目标页面
    navigateToMessageTarget(message) {
      if (message.type === 0) {
        // 撩一下通知，跳转到撩一下详情
        this.$router.push(`/dating/pick/id/${message.datingPick.pickId}`)
      } else {
        // 撩一下处理通知，跳转到用户资料
        this.$router.push(`/dating/profile/id/${message.datingPick.datingProfile.profileId}`)
      }
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