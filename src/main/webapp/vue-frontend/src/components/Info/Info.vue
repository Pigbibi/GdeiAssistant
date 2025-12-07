<template>
  <div>
    <!-- 通知公告 -->
    <div class="announcement" v-if="showAnnouncement">
      <div class="hd">
        <h1 class="page_title">通知公告</h1>
      </div>
      <article class="weui-article">
        <h2>{{ announcement.title }}</h2>
        <h2>时间：{{ announcement.publishTime }}</h2>
        <section>{{ announcement.content }}</section>
      </article>
    </div>

    <!-- 校园公众号 -->
    <div class="wechataccount">
      <div class="hd">
        <h1 class="page_title">校园公众号</h1>
      </div>
      <div>
        <!-- 校园热门公众号列表 -->
        <div v-for="(wechataccount, index) in wechatAccounts" :key="index" class="weui-form-preview" style="margin-bottom: 10px">
          <div class="weui-form-preview__bd">
            <div class="weui-form-preview__item" style="margin-bottom: 0.3rem">
              <label class="weui-form-preview__label">头像</label>
              <img :src="wechataccount.avatar" style="width: 50px; height: auto">
            </div>
            <div class="weui-form-preview__item">
              <label class="weui-form-preview__label">公众号</label>
              <span class="weui-form-preview__value">{{ wechataccount.name }}</span>
            </div>
            <div class="weui-form-preview__item">
              <label class="weui-form-preview__label">功能介绍</label>
              <span class="weui-form-preview__value">{{ wechataccount.description }}</span>
            </div>
            <div v-if="wechataccount.article && wechataccount.article !== ''" class="weui-form-preview__item">
              <label class="weui-form-preview__label">最近文章</label>
              <span class="weui-form-preview__value">{{ wechataccount.article }}</span>
            </div>
          </div>
          <div class="weui-form-preview__ft">
            <a class="weui-form-preview__btn weui-form-preview__btn_primary" :href="'https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=' + wechataccount.biz + '&scene=123#wechat_redirect'">查看</a>
          </div>
        </div>
        <p style="text-align:center;margin-top:0.5rem;font-size:13px;color:#999" @click="$router.push('/wechataccount')">
          点击查看所有热门校园公众号
        </p>
        <br>
      </div>
    </div>

    <!-- 专题阅读 -->
    <div class="recommendation" v-if="recommendations.length > 0">
      <div class="hd">
        <h1 class="page_title">专题阅读</h1>
      </div>
      <div class="weui-cells">
        <!-- 专题阅读内容 -->
        <a v-for="(recommendation, index) in recommendations" :key="index" class="weui-cell weui-cell_access" :href="recommendation.link">
          <div class="weui-cell__bd">
            <p>{{ recommendation.title }}</p>
            <p style="font-size:13px;color:#999">{{ recommendation.description }}</p>
          </div>
          <div class="weui-cell__ft"></div>
        </a>
      </div>
      <p style="text-align:center;margin-top:0.5rem;font-size:13px;color:#999" @click="$router.push('/reading')">
        点击查看所有专题阅读内容
      </p>
      <br>
    </div>

    <!-- 世界上的今日 -->
    <div class="today" v-if="festival">
      <div class="hd">
        <h1 class="page_title">世界上的今日</h1>
      </div>
      <article class="weui-article">
        <h2>你知道吗？每年的{{ todayMonth }}月{{ todayDay }}日，是{{ festival.name }}</h2>
        <section>
          <p v-for="(description, index) in festival.description" :key="index">{{ description.trim() }}</p>
        </section>
      </article>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Info',
  data() {
    return {
      announcement: {
        title: '',
        publishTime: '',
        content: ''
      },
      showAnnouncement: false,
      wechatAccounts: [
        // 默认数据，实际应该从后端获取
        { avatar: '', name: '', description: '', article: '', biz: '' }
      ],
      recommendations: [],
      festival: null,
      todayMonth: '',
      todayDay: ''
    };
  },
  created() {
    // 设置当前日期
    const now = new Date();
    this.todayMonth = now.getMonth() + 1;
    this.todayDay = now.getDate();
    
    // 加载数据
    this.loadAnnouncement();
    this.loadReadingInfo();
    
    // 模拟获取校园公众号数据
    this.loadWechatAccounts();
    
    // 模拟获取节日信息
    this.loadFestivalInfo();
  },
  methods: {
    // 加载通知公告
    loadAnnouncement() {
      this.$axios.get('/api/announcement')
        .then(response => {
          const result = response.data;
          if (result.success) {
            if (result.data) {
              this.announcement.title = result.data.title;
              this.announcement.publishTime = result.data.publishTime;
              this.announcement.content = result.data.content;
              this.showAnnouncement = true;
            }
          } else {
            this.$toast.fail(result.message || '加载通知公告失败');
          }
        })
        .catch(error => {
          if (error.response) {
            this.$toast.fail(error.response.data.message || '加载通知公告失败');
          } else {
            this.$toast.fail('网络连接异常，请检查并重试');
          }
          console.error('加载通知公告时发生错误:', error);
        });
    },
    
    // 加载专题阅读信息
    loadReadingInfo() {
      this.$axios.get('/api/reading')
        .then(response => {
          const result = response.data;
          if (result.success) {
            if (result.data.length > 0) {
              this.recommendations = result.data;
            }
          } else {
            this.$toast.fail(result.message || '加载专题阅读信息失败');
          }
        })
        .catch(error => {
          if (error.response) {
            this.$toast.fail(error.response.data.message || '加载专题阅读信息失败');
          } else {
            this.$toast.fail('网络连接异常，请检查并重试');
          }
          console.error('加载专题阅读信息时发生错误:', error);
        });
    },
    
    // 加载校园公众号数据
    loadWechatAccounts() {
      // 这里应该从后端API获取数据，但原代码使用了JSTL标签，所以需要后端提供对应的API
      // 目前使用模拟数据
      this.wechatAccounts = [
        { avatar: '/static/image/wechat/school.jpg', name: '广东第二师范学院', description: '广东第二师范学院官方公众号', article: '我校召开2023年工作会议', biz: 'MzA3MzA0NjQ0Mw==' },
        { avatar: '/static/image/wechat/student.jpg', name: '广二师学生处', description: '学生工作信息发布平台', article: '关于2023年奖学金评定的通知', biz: 'MzA3MzA0NjQ0Nw==' },
        { avatar: '/static/image/wechat/library.jpg', name: '广二师图书馆', description: '图书馆服务与资源介绍', article: '2023年新书推荐', biz: 'MzA3MzA0NjQ0OQ==' }
      ];
    },
    
    // 加载节日信息
    loadFestivalInfo() {
      // 这里应该从后端API获取数据，但原代码使用了自定义标签，所以需要后端提供对应的API
      // 目前使用模拟数据
      this.festival = {
        name: '测试节日',
        description: ['这是一个测试节日', '用于展示节日信息功能']
      };
    }
  }
};
</script>

<style scoped>
/* 保留原有的CSS样式 */
.announcement {
  padding: 15px;
  background-color: #fff;
  margin-bottom: 10px;
}

.wechataccount {
  padding: 15px;
  background-color: #fff;
  margin-bottom: 10px;
}

.recommendation {
  padding: 15px;
  background-color: #fff;
  margin-bottom: 10px;
}

.today {
  padding: 15px;
  background-color: #fff;
  margin-bottom: 10px;
}

.page_title {
  font-size: 18px;
  font-weight: bold;
  text-align: center;
  margin: 0;
  padding: 15px 0;
}

.weui-article h2 {
  font-size: 16px;
  font-weight: bold;
  margin: 15px 0;
}

.weui-article section {
  font-size: 14px;
  line-height: 1.8;
}

.weui-form-preview {
  margin-bottom: 10px;
}

.weui-form-preview__bd {
  padding: 10px;
}

.weui-form-preview__item {
  margin-bottom: 10px;
}

.weui-form-preview__label {
  font-weight: bold;
  width: 70px;
}

.weui-form-preview__value {
  font-size: 14px;
  color: #333;
}

.weui-cells {
  margin-top: 0;
}

.weui-cell {
  padding: 15px;
}

.weui-cell__bd p {
  margin: 0;
}
</style>