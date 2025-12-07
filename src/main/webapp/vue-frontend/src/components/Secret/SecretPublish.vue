<template>
  <div id="secret_publish">
    <!-- 树洞发布框 -->
    <div class="form">
      <form>
        <header>
          <i class="back" @click="history.back()"></i>
          <span>小秘密</span>
          <label class="btn" @click="publishSecret">发布</label>
        </header>
        <div class="edit" style="text-align: center">
          <!-- 语音树洞 -->
          <div id="voice" v-show="showVoice">
            <!-- 语音图标 -->
            <img id="record" width="50px" height="50px" src="/img/secret/voice_normal.png" @mousedown="startRecord" @mouseup="stopRecord" @mouseleave="stopRecord" @touchstart="startRecord" @touchend="stopRecord">
            <br>
            <text id="voice_tip">{{ voiceTip }}</text>
          </div>
          <!-- 文字树洞 -->
          <div id="word" v-show="showWord">
            <!-- 文本输入框 -->
            <textarea name="content" id="text" v-model="secretContent" maxlength="100" autofocus placeholder="说个小秘密" v-align="center" max-lenght="100"></textarea>
            <!-- 剩余可输入字符数 -->
            <div class="length">{{ 100 - secretContent.length }}</div>
          </div>
        </div>
      </form>
    </div>

    <div class="bar">
      <div style="height:30px">
        <div @click="replayRecord">
          <img id="voice_button" width="20px" height="20px" :style="{display: showVoiceButton ? 'block' : 'none'}" :src="voiceButtonSrc" />
          <p id="voice_state" :style="{display: showVoiceButton ? 'block' : 'none'}">{{ voiceState }}</p>
        </div>
        <i style="float: right;position:relative;top:5px"></i>
        <div id="voice_volume" :style="{display: showVoiceButton ? 'block' : 'none'}">
          <div id="volume" :style="{width: volumeWidth}"></div>
        </div>
      </div>
      <div style="float:right;margin-top:15px">
        <input id="timer" type="checkbox" v-model="timer"> 24小时后删除</input>
      </div>
    </div>

    <!-- 主题选择 -->
    <div class="themes">
      <div v-for="i in 6" :key="i" class="theme" :class="'theme' + i" :class="{selected: selectedTheme === i}" @click="selectTheme(i)">
        <i v-if="selectedTheme === i" class="selected"></i>
      </div>
    </div>

    <div class="themes">
      <div v-for="i in 6" :key="i + 6" class="theme" :class="'theme' + (i + 6)" :class="{selected: selectedTheme === i + 6}" @click="selectTheme(i + 6)">
        <i v-if="selectedTheme === i + 6" class="selected"></i>
      </div>
    </div>
    <div class="attach"></div>

    <!-- 切换到文字树洞 -->
    <div id="switchToWord" :style="{display: showSwitchToWord ? 'block' : 'none'}">
      切换到<p style="display:inline;color: deepskyblue" @click="switchToWord()">文字树洞</p>
      ，用文字分享你的小秘密
    </div>

    <!-- 切换到语音树洞 -->
    <div id="switchToVoice" :style="{display: showSwitchToVoice ? 'block' : 'none'}">
      切换到<p style="display:inline;color: deepskyblue" @click="switchToVoice()">语音树洞</p>
      ，用语音分享你的小秘密
    </div>

    <!-- 错误提示 -->
    <div class="weui-toptips weui_warn js_tooltips" ref="toast"></div>
  </div>
</template>

<script>
export default {
  name: 'secretPublish',
  data() {
    return {
      // 树洞内容
      secretContent: '',
      // 树洞类型：0-文字，1-普通语音，2-微信语音
      secretType: 0,
      // 主题选择
      selectedTheme: 1,
      // 定时删除
      timer: false,
      // 语音相关
      showVoice: false,
      showWord: true,
      showSwitchToWord: false,
      showSwitchToVoice: true,
      showVoiceButton: false,
      voiceButtonSrc: '/img/secret/init.png',
      voiceState: '未录音',
      voiceTip: '长按开始录音，最长不超过60秒',
      volumeWidth: '0',
      // 录音相关变量
      record: null,
      voiceFile: null,
      isRecording: false
    }
  },
  mounted() {
    // 初始化录音功能（如果需要）
    this.initRecord()
  },
  methods: {
    initRecord() {
      // 如果不是微信环境且是语音类型，获取麦克风权限
      if (!this.isWechat() && this.secretType === 1) {
        // 这里可以添加录音初始化代码
      }
    },
    isWechat() {
      return navigator.userAgent.toLowerCase().match(/MicroMessenger/i) == "micromessenger"
    },
    isIOS() {
      return /(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)
    },
    switchToWord() {
      this.showSwitchToWord = false
      this.showSwitchToVoice = true
      this.showWord = true
      this.showVoice = false
      this.showVoiceButton = false
      this.secretType = 0
    },
    switchToVoice() {
      this.showSwitchToVoice = false
      this.showSwitchToWord = true
      this.showWord = false
      this.showVoice = true
      this.showVoiceButton = true
      
      if (this.isIOS() && this.isWechat()) {
        this.secretType = 2
      } else {
        this.secretType = 1
      }
      
      // 获取麦克风权限
      if (!this.isIOS() && !this.isWechat()) {
        // 这里可以添加获取麦克风权限的代码
      }
    },
    selectTheme(theme) {
      this.selectedTheme = theme
    },
    startRecord() {
      this.isRecording = true
      this.voiceTip = '松开结束录音'
      // 这里可以添加开始录音的代码
    },
    stopRecord() {
      if (this.isRecording) {
        this.isRecording = false
        this.voiceTip = '长按开始录音，最长不超过60秒'
        // 这里可以添加结束录音的代码
        this.voiceState = '录音完成'
        this.voiceButtonSrc = '/img/secret/play.png'
      }
    },
    replayRecord() {
      // 这里可以添加播放录音的代码
      this.voiceState = '正在播放'
    },
    publishSecret() {
      // 验证内容
      if (this.secretType === 0 && !this.secretContent.trim()) {
        this.showToast('树洞信息不能为空')
        return
      }
      
      // 构造请求数据
      const data = {
        content: this.secretContent,
        type: this.secretType,
        theme: this.selectedTheme,
        timer: this.timer ? 1 : 0
      }
      
      // 如果是语音类型，需要处理文件上传
      if (this.secretType === 1 && this.voiceFile) {
        const formData = new FormData()
        formData.append('content', this.secretContent)
        formData.append('type', this.secretType)
        formData.append('theme', this.selectedTheme)
        formData.append('timer', this.timer ? 1 : 0)
        formData.append('voice', this.voiceFile)
        
        this.$axios.post('/api/secret/info', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        }).then(response => {
          this.handlePublishResponse(response)
        }).catch(error => {
          console.error('发布树洞失败:', error)
          this.showToast('发布失败，请稍后重试')
        })
      } else if (this.secretType === 2) {
        // 微信语音处理
        data.voiceId = this.voiceId
        this.$axios.post('/api/secret/info', data)
          .then(response => {
            this.handlePublishResponse(response)
          })
          .catch(error => {
            console.error('发布树洞失败:', error)
            this.showToast('发布失败，请稍后重试')
          })
      } else {
        // 文字树洞
        this.$axios.post('/api/secret/info', data)
          .then(response => {
            this.handlePublishResponse(response)
          })
          .catch(error => {
            console.error('发布树洞失败:', error)
            this.showToast('发布失败，请稍后重试')
          })
      }
    },
    handlePublishResponse(response) {
      if (response.data.success) {
        // 发布成功，返回树洞列表页
        this.$router.push('/secret')
      } else {
        this.showToast(response.data.message || '发布失败，请稍后重试')
      }
    },
    showToast(message) {
      const toast = this.$refs.toast
      toast.textContent = message
      toast.style.display = 'block'
      setTimeout(() => {
        toast.style.display = 'none'
      }, 2000)
    }
  }
}
</script>

<style scoped>
@import '../../../static/css/css/secret/secret-publish.css';
</style>