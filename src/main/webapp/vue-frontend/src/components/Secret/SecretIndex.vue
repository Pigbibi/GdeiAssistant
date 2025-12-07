<template>
  <div id="secret">
    <div class="header">
      <div class="title">校园树洞</div>
      <div class="publish" @click="toPublish">
        <img width="25px" height="25px" src="/img/secret/add.png" />
      </div>
    </div>
    <div class="info" id="secret_list">
      <div v-for="(item, index) in secretList" :key="index" class="msg" @click="showSecretDetail(item.id)">
        <div class="theme theme1" :class="'theme' + item.theme"></div>
        <div class="content">
          <div v-if="item.type === 0" class="text">{{ item.content }}</div>
          <div v-else class="voice">
            <img width="35px" height="35px" src="/img/secret/voice.png">
            <span>语音消息</span>
          </div>
        </div>
        <div class="detail">
          <div class="comment">
            <img width="18px" height="18px" src="/img/secret/comment.png">
            <span>{{ item.commentCount || 0 }}</span>
          </div>
          <div class="like" @click.stop="updateLikeState(item)">
            <img width="18px" height="18px" :src="item.liked ? '/img/secret/like_pressed.png' : '/img/secret/like.png'">
            <span>{{ item.likeCount || 0 }}</span>
          </div>
        </div>
      </div>
    </div>
    <div class="load-more" @click="loadMore" v-if="hasMore">加载更多</div>
    <div class="no-more" v-else>没有更多了</div>
  </div>
</template>

<script>
export default {
  name: 'secretIndex',
  data() {
    return {
      secretList: [],
      start: 0,
      size: 10,
      hasMore: true
    }
  },
  mounted() {
    this.loadSecretInfo()
  },
  methods: {
    loadSecretInfo() {
      this.$axios.get(`/api/secret/info/start/${this.start}/size/${this.size}`)
        .then(response => {
          if (response.data.success) {
            const newList = response.data.data
            if (newList.length < this.size) {
              this.hasMore = false
            }
            this.secretList = [...this.secretList, ...newList]
            this.start += this.size
          } else {
            console.error('加载树洞信息失败:', response.data.message)
          }
        })
        .catch(error => {
          console.error('请求失败:', error)
        })
    },
    loadMore() {
      if (this.hasMore) {
        this.loadSecretInfo()
      }
    },
    showSecretDetail(id) {
      this.$router.push(`/secret/detail/id/${id}`)
    },
    toPublish() {
      this.$router.push('/secret/publish')
    },
    updateLikeState(item) {
      const like = item.liked ? 0 : 1
      this.$axios.post(`/api/secret/id/${item.id}/like`, { like })
        .then(response => {
          if (response.data.success) {
            item.liked = like
            item.likeCount += like ? 1 : -1
          } else {
            console.error('更新点赞状态失败:', response.data.message)
          }
        })
        .catch(error => {
          console.error('请求失败:', error)
        })
    }
  }
}
</script>

<style scoped>
@import '../../../static/css/css/secret/secret.css';
</style>