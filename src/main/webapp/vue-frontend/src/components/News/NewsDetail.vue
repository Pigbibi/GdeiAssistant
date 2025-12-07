<template>
  <div>
    <div class="weui-cells__title" @click="goBack">返回</div>
    <div class="hd">
      <h1 class="page_title" style="margin-top: 15px">{{ newsDetail.title }}</h1>
    </div>
    <div v-html="newsDetail.content"></div>

    <!-- 错误提示，显示时用$.show();隐藏时用$.hide(); -->
    <div class="weui-toptips weui_warn js_tooltips">{{ errorMessage }}</div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      newsDetail: {
        title: '',
        content: ''
      },
      errorMessage: ''
    };
  },
  mounted() {
    // 获取新闻ID并加载详细信息
    const id = this.$route.params.id;
    this.loadNewsDetail(id);
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    loadNewsDetail(id) {
      let loading = weui.loading('加载中');
      axios
        .get(`/api/schoolnews/news/id/${id}/detail`)
        .then((response) => {
          loading.hide();
          let result = response.data;
          if (result.success) {
            this.newsDetail = result.data;
          } else {
            weui.topTips(result.message);
          }
        })
        .catch((error) => {
          loading.hide();
          if (error.response) {
            weui.topTips('服务暂不可用，请稍后再试');
          } else {
            weui.topTips('网络连接异常，请检查网络连接');
          }
        });
    },
    downloadFile(url) {
      // 根据实际情况实现文件下载功能
      window.location.href = url;
    }
  },
  created() {
    // 将downloadFile方法挂载到window对象，以便在HTML内容中调用
    window.downloadFile = this.downloadFile;
  },
  beforeDestroy() {
    // 组件销毁时移除window上的方法
    delete window.downloadFile;
  }
};
</script>

<style scoped>
/* 保持原有样式不变 */
</style>