<template>
  <div class="weui-tab">
    <div class="weui-tab__panel">
      <div class="weui-cells__title" @click="goBack">返回</div>
      <div class="hd">
        <h1 class="page_title">新闻通知</h1>
        <p style="text-align: center;color: lightgrey">新闻信息服务由广东第二师范学院网站提供，本站仅作收录和展示</p>
      </div>
      <div style="margin-top: 0;" class="weui-cells">
        <div
          v-for="item in news[type]"
          :key="item.id"
          :id="item.id"
          @click="showNewDetailInfo(item.id)"
          class="weui-cell weui-cell_access"
          href="javascript:"
        >
          <div class="weui-cell__bd">
            <p style="padding-right: 1rem">{{ item.title }}</p>
          </div>
          <div class="weui-cell__ft">{{ item.publishDate }}</div>
        </div>
      </div>
      <p
        style="text-align: center;color: lightgrey;margin-top: 1rem;margin-bottom: 2rem"
        id="loadMore"
        @click="loadMore"
      >
        点击加载更多新闻通知
      </p>
    </div>

    <div class="weui-tabbar">
      <a
        v-for="(tab, index) in tabs"
        :key="index"
        href="javascript:"
        class="weui-tabbar__item"
        :class="{ 'weui-bar__item_on': type === index }"
        @click="switchNewsType(index)"
      >
        <img :src="tab.icon" class="weui-tabbar__icon">
        <p class="weui-tabbar__label">{{ tab.name }}</p>
      </a>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      news: [[], [], [], [], []],
      type: 0,
      anchor: null,
      tabs: [
        { name: '教学信息', icon: '/img/news/course.png' },
        { name: '考试信息', icon: '/img/news/examine.png' },
        { name: '教务信息', icon: '/img/news/study.png' },
        { name: '行政通知', icon: '/img/news/admin.png' },
        { name: '综合信息', icon: '/img/news/school.png' }
      ]
    };
  },
  mounted() {
    // 加载SessionStorage缓存的分类、瞄点和新闻通知信息列表信息
    let newsData = sessionStorage.getItem('newsData');
    let newsType = sessionStorage.getItem('newsType');
    let newsAnchor = sessionStorage.getItem('newsAnchor');
    if (newsData && newsType) {
      // 从缓存中加载分类和新闻通知信息列表信息
      this.type = parseInt(newsType);
      this.news = JSON.parse(newsData);
      this.loadNewCellReload(this.type);
    }
    // 加载默认分类下的新闻通知信息
    this.switchNewsType(this.type);
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    loadNewCellReload(index) {
      // Vue自动更新视图，无需手动加载
      // 跳转到瞄点位置
      if (this.anchor) {
        this.$nextTick(() => {
          let element = document.getElementById(this.anchor);
          if (element) {
            element.scrollIntoView();
          }
        });
      }
    },
    switchNewsType(index) {
      this.type = index;
      // 若当前分类的缓存列表中没有新闻信息，则主动加载
      if (this.news[index].length === 0) {
        this.loadNews(index, (result) => {
          this.loadNewCellReload(index);
        }, () => {
          // 清空新闻通知信息列表
          this.news[index] = [];
        });
      } else {
        this.loadNewCellReload(index);
      }
    },
    showNewDetailInfo(id) {
      // 缓存瞄点、分类和新闻通知列表信息
      sessionStorage.setItem('newsData', JSON.stringify(this.news));
      sessionStorage.setItem('newsType', this.type);
      sessionStorage.setItem('newsAnchor', id);
      // 显示新闻详细信息
      this.$router.push(`/news/id/${id}`);
    },
    loadMore() {
      this.loadNews(this.type, (result) => {
        // Vue自动更新视图
      }, () => {
        // 加载失败处理
      });
    },
    loadNews(index, onSuccess, onError) {
      let loading = weui.loading('加载中');
      axios
        .get(`/api/schoolnews/news/type/${index}/start/${this.news[index].length}/size/10`)
        .then((response) => {
          loading.hide();
          let result = response.data;
          if (result.success) {
            for (let i = 0; i < result.data.length; i++) {
              this.news[index].push(result.data[i]);
            }
            onSuccess(result.data);
          } else {
            weui.topTips(result.message);
            onError();
          }
        })
        .catch((error) => {
          loading.hide();
          if (error.response) {
            weui.topTips('服务暂不可用，请稍后再试');
          } else {
            weui.topTips('网络连接异常，请检查网络连接');
          }
          onError();
        });
    }
  }
};
</script>

<style scoped>
/* 保持原有样式不变 */
</style>