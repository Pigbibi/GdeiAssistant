<template>
  <div id="secret_profile">
    <div class="header">
      <div class="back" @click="goBack"><img width="25px" height="25px" src="/img/secret/back.png" /></div>
      <div class="title">我的树洞</div>
    </div>
    <div class="notice"><i class="inotice"></i>我的树洞</div>
    <div class="msg_list">
      <div v-for="(item, index) in secretList" :key="index" class="msg" @click="showSecretDetail(item.id)">
        <a href="#">
          <p>{{ item.type === 0 ? item.content : "语音消息" }}</p>
        </a>
        <i class="toggle"></i>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'secretProfile',
  data() {
    return {
      secretList: []
    }
  },
  mounted() {
    this.loadSecretProfile()
  },
  methods: {
    loadSecretProfile() {
      this.$axios.get('/api/secret/profile/items')
        .then(response => {
          if (response.data.success) {
            this.secretList = response.data.data
          } else {
            console.error('加载我的树洞失败:', response.data.message)
          }
        })
        .catch(error => {
          console.error('请求失败:', error)
        })
    },
    goBack() {
      this.$router.go(-1)
    },
    showSecretDetail(id) {
      this.$router.push(`/secret/detail/id/${id}`)
    }
  }
}
</script>

<style scoped>
@import '../../../static/css/css/secret/secret-profile.css';
</style>