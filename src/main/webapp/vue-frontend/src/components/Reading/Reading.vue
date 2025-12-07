<template>
  <div>
    <div class="weui-cells__title" @click="goBack">返回</div>
    
    <div class="hd">
      <h1 class="page_title">专题阅读</h1>
      <p class="page_desc">每期为你提供优质、有收获的专题阅读内容</p>
    </div>
    
    <div v-if="loading" class="loading-container">
      <div class="weui-primary-loading">
        <span class="weui-primary-loading__dot"></span>
      </div>
      <p class="loading-text">正在加载专题阅读内容</p>
    </div>
    
    <div v-else-if="readingList.length > 0" class="weui-cells">
      <!-- 专题阅读内容 -->
      <a class="weui-cell weui-cell_access" v-for="reading in readingList" :key="reading.id" :href="reading.link" target="_blank">
        <div class="weui-cell__bd">
          <p>{{ reading.title }}</p>
          <p style='font-size:13px;color:#999'>{{ reading.description }}</p>
        </div>
        <div class="weui-cell__ft"></div>
      </a>
    </div>
    
    <div v-else class="no-content">
      <p>暂无专题阅读内容</p>
    </div>
    
    <p class="page_desc">专题阅读信息服务由微信公众订阅号提供</p>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'ReadingIndex',
  data() {
    return {
      readingList: [],
      loading: true
    }
  },
  methods: {
    goBack() {
      this.$router.back()
    },
    
    // 获取专题阅读列表
    getReadingList() {
      // 使用axios调用后端API获取专题阅读列表
      axios.get('/api/reading')
      .then(result => {
        this.loading = false
        if (result.data.success === true) {
          this.readingList = result.data.data
        }
      })
      .catch(error => {
        this.loading = false
        console.error('获取专题阅读列表失败:', error)
      })
    }
  },
  mounted() {
    this.getReadingList()
  }
}
</script>

<style scoped>
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 0;
}

.loading-text {
  margin-top: 10px;
  color: #888;
}

.no-content {
  text-align: center;
  padding: 40px 0;
  color: #888;
}
</style>