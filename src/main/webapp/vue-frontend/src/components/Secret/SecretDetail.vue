<template>
  <div id="secret_detail">
    <div class="header">
      <div class="back" @click="goBack"><img width="25px" height="25px" src="/img/secret/back.png" /></div>
      <div class="title">树洞详情</div>
    </div>
    <div class="secret_content">
      <div class="theme" :class="'theme' + secretDetail.theme"></div>
      <div class="content">
        <div v-if="secretDetail.type === 0" class="text">{{ secretDetail.content }}</div>
        <div v-else class="voice">
          <img width="40px" height="40px" src="/img/secret/voice.png">
          <div class="play" @click="playVoice"></div>
        </div>
      </div>
      <div class="detail">
        <div class="publish_time">{{ formatTime(secretDetail.publishTime) }}</div>
        <div class="like" @click="updateLikeState">
          <img width="20px" height="20px" :src="secretDetail.liked ? '/img/secret/like_pressed.png' : '/img/secret/like.png'">
          <span>{{ secretDetail.likeCount || 0 }}</span>
        </div>
      </div>
    </div>
    <div class="comment_list">
      <div class="comment_header">评论 ({{ secretDetail.commentCount || 0 }})</div>
      <div v-for="(comment, index) in secretDetail.secretCommentList" :key="index" class="comment_item">
        <div class="avatar"></div>
        <div class="comment_content">
          <div class="comment_text">{{ comment.comment }}</div>
          <div class="comment_time">{{ formatTime(comment.publishTime) }}</div>
        </div>
      </div>
    </div>
    <div class="comment_input">
      <input type="text" v-model="commentContent" placeholder="写下你的评论..." @keyup.enter="submitComment">
      <button @click="submitComment">发送</button>
    </div>
    <audio id="audio" ref="audio" :src="secretDetail.voiceURL"></audio>
  </div>
</template>

<script>
export default {
  name: 'secretDetail',
  data() {
    return {
      secretDetail: {},
      commentContent: ''
    }
  },
  mounted() {
    this.loadSecretDetail()
  },
  methods: {
    loadSecretDetail() {
      const id = this.$route.params.id
      this.$axios.get(`/api/secret/detail/id/${id}`)
        .then(response => {
          if (response.data.success) {
            this.secretDetail = response.data.data
          } else {
            console.error('加载树洞详情失败:', response.data.message)
          }
        })
        .catch(error => {
          console.error('请求失败:', error)
        })
    },
    goBack() {
      this.$router.go(-1)
    },
    updateLikeState() {
      const id = this.$route.params.id
      const like = this.secretDetail.liked ? 0 : 1
      this.$axios.post(`/api/secret/id/${id}/like`, { like })
        .then(response => {
          if (response.data.success) {
            this.secretDetail.liked = like
            this.secretDetail.likeCount += like ? 1 : -1
          } else {
            console.error('更新点赞状态失败:', response.data.message)
          }
        })
        .catch(error => {
          console.error('请求失败:', error)
        })
    },
    submitComment() {
      if (!this.commentContent.trim()) return
      const id = this.$route.params.id
      this.$axios.post(`/api/secret/id/${id}/comment`, { comment: this.commentContent })
        .then(response => {
          if (response.data.success) {
            // 清空输入框
            this.commentContent = ''
            // 重新加载评论列表
            this.loadSecretDetail()
          } else {
            console.error('发表评论失败:', response.data.message)
          }
        })
        .catch(error => {
          console.error('请求失败:', error)
        })
    },
    playVoice() {
      const audio = this.$refs.audio
      if (audio.paused) {
        audio.play()
      } else {
        audio.pause()
      }
    },
    formatTime(timeStr) {
      if (!timeStr) return ''
      const date = new Date(timeStr)
      return date.toLocaleString()
    }
  }
}
</script>

<style scoped>
@import '../../../static/css/css/secret/secret.css';
</style>