<template>
  <div class="container">
    <div class="head" @click="goToDating">
      <div class="logo"><span class="t">卖室友</span></div>
      <div class="account">
        <div class="loginWin">
          <router-link to="/dating/message">
            <img v-if="!hasUnReadMessage" src="/static/img/dating/message.png" style="width: 25px;height: 25px;margin-top: 1rem">
            <img v-else src="/static/img/dating/unread_message.png" style="width: 30px;height: 30px;margin-top: 1rem">
          </router-link>
        </div>
      </div>
    </div>

    <div class="content">
      <div id="banner">
        <div class="pagination"></div>
      </div>
      <div class="sexTab">
        <ul>
          <li :class="{ 'female': true, 'selected': currentArea === 0 }" @click="switchArea(0)">
            <a href="javascript:">小姐姐</a>
          </li>
          <li :class="{ 'male': true, 'selected': currentArea === 1 }" @click="switchArea(1)">
            <a href="javascript:">小哥哥</a>
          </li>
        </ul>
      </div>
      <div class="sexWrapper">
        <div :class="{ 'cards': true, 'male': true, 'active': currentArea === 1 }">
          <ul class="cardsBox">
            <li v-for="item in maleProfiles" :key="item.profileId" @click="showDetailInfo(item.profileId)">
              <div class="Peopleimg">
                <a class="click" href="javascript:">
                  <img :id="item.profileId" :src="getProfileImage(item.profileId)" alt="">
                </a>
              </div>
              <h1 class="P-title"><a class="click" href="javascript:">{{ item.nickname }}</a></h1>
              <div class="p-info">{{ item.faculty }} {{ getGradeText(item.grade) }}学生</div>
              <div class="p-info">来自{{ item.hometown }}</div>
            </li>
          </ul>
          <section style="clear: both;text-align: center">
            <span id="loadMore_male" @click="loadMore(1)">
              {{ maleHasMore ? '点击加载更多' : '已经到底了' }}
            </span>
          </section>
        </div>
        <div :class="{ 'cards': true, 'female': true, 'active': currentArea === 0 }">
          <ul class="cardsBox">
            <li v-for="item in femaleProfiles" :key="item.profileId" @click="showDetailInfo(item.profileId)">
              <div class="Peopleimg">
                <a class="click" href="javascript:">
                  <img :id="item.profileId" :src="getProfileImage(item.profileId)" alt="">
                </a>
              </div>
              <h1 class="P-title"><a class="click" href="javascript:">{{ item.nickname }}</a></h1>
              <div class="p-info">{{ item.faculty }} {{ getGradeText(item.grade) }}学生</div>
              <div class="p-info">来自{{ item.hometown }}</div>
            </li>
          </ul>
          <section style="clear: both;text-align: center">
            <span id="loadMore_female" @click="loadMore(0)">
              {{ femaleHasMore ? '点击加载更多' : '已经到底了' }}
            </span>
          </section>
        </div>
      </div>
    </div>

    <div class="skypub" @click="goToPublish">
      <i></i>
    </div>

    <!-- 错误提示 -->
    <div class="weui-toptips weui_warn js_tooltips">{{ errorMessage }}</div>
  </div>
</template>

<script>
import axios from 'axios';
import FastClick from 'fastclick';

export default {
  name: 'DatingIndex',
  data() {
    return {
      currentArea: 0, // 0: female, 1: male
      femaleIndex: 0,
      maleIndex: 0,
      femaleHasMore: true,
      maleHasMore: true,
      femaleProfiles: [],
      maleProfiles: [],
      hasUnReadMessage: false,
      errorMessage: '',
      profileImages: {} // 存储用户头像URL
    };
  },
  mounted() {
    // 初始化FastClick
    FastClick.attach(document.body);
    
    // 加载数据
    this.loadMore(0);
    this.loadMore(1);
    
    // 检查未读消息
    this.checkUnReadMessage();
  },
  methods: {
    goToDating() {
      this.$router.push('/dating');
    },
    switchArea(index) {
      this.currentArea = index;
    },
    showDetailInfo(id) {
      this.$router.push(`/dating/profile/id/${id}`);
    },
    goToPublish() {
      this.$router.push('/dating/publish');
    },
    loadMore(index) {
      let hasMore;
      let currentIndex;
      let profiles;
      let type;

      if (index === 0) {
        hasMore = this.femaleHasMore;
        currentIndex = this.femaleIndex;
        profiles = this.femaleProfiles;
        type = 'female';
      } else {
        hasMore = this.maleHasMore;
        currentIndex = this.maleIndex;
        profiles = this.maleProfiles;
        type = 'male';
      }

      if (hasMore) {
        const url = `/api/dating/profile/area/${index}/start/${currentIndex}`;
        weui.loading('加载中', {className: 'custom-classname'});

        // 发送真实API请求
        axios.get(url)
          .then(response => {
            weui.hideLoading();
            
            const result = response.data;
            if (result.success) {
              const data = result.data;
              if (data.length > 0) {
                if (type === 'female') {
                  this.femaleProfiles = [...this.femaleProfiles, ...data];
                  this.femaleIndex += data.length;
                } else {
                  this.maleProfiles = [...this.maleProfiles, ...data];
                  this.maleIndex += data.length;
                }

                // 加载头像
                data.forEach(item => {
                  this.getProfileImage(item.profileId);
                });
              } else {
                if (type === 'female') {
                  this.femaleHasMore = false;
                } else {
                  this.maleHasMore = false;
                }
              }
            } else {
              this.showError(result.message || '加载失败');
            }
          })
          .catch(error => {
            weui.hideLoading();
            this.showError('网络连接异常，请重试');
            console.error('Error:', error);
          });
      }
    },
    getProfileImage(id) {
      if (!this.profileImages[id]) {
        // 发送请求获取头像
        const url = `/api/dating/profile/id/${id}/picture`;
        
        axios.get(url)
          .then(response => {
            const result = response.data;
            if (result.success) {
              this.$set(this.profileImages, id, result.data);
            }
          })
          .catch(error => {
            console.error('Error loading image:', error);
          });
        
        return '/static/img/loading.gif';
      }
      return this.profileImages[id];
    },
    getGradeText(grade) {
      const gradeMap = {
        1: '大一',
        2: '大二',
        3: '大三',
        4: '大四'
      };
      return gradeMap[grade] || '';
    },
    showError(message) {
      this.errorMessage = message;
      setTimeout(() => {
        this.errorMessage = '';
      }, 2000);
    },
    checkUnReadMessage() {
      // 检查未读消息数量
      const url = `/api/dating/message/unreadCount`;
      
      axios.get(url)
        .then(response => {
          const result = response.data;
          if (result.success) {
            this.hasUnReadMessage = result.data > 0;
          }
        })
        .catch(error => {
          console.error('Error checking unread messages:', error);
        });
    }
  }
};
</script>

<style scoped>
@import '../../assets/css/weui.min.css';
@import '../../assets/css/common.css';

.container {
  position: relative;
}

.head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.logo .t {
  font-size: 24px;
  font-weight: bold;
  color: #ff6b6b;
}

.account {
  display: flex;
  align-items: center;
}

.loginWin {
  margin-left: 15px;
}

.content {
  padding-bottom: 80px;
}

#banner {
  height: 120px;
  background-color: #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 15px;
}

.sexTab {
  background-color: #fff;
  margin-bottom: 15px;
}

.sexTab ul {
  display: flex;
  list-style: none;
  padding: 0;
  margin: 0;
}

.sexTab li {
  flex: 1;
  text-align: center;
  padding: 15px 0;
  cursor: pointer;
}

.sexTab li.selected {
  border-bottom: 2px solid #ff6b6b;
}

.sexTab li a {
  text-decoration: none;
  color: #333;
  font-size: 16px;
  font-weight: bold;
}

.cards {
  display: none;
  padding: 15px;
  background-color: #f8f8f8;
}

.cards.active {
  display: block;
}

.cardsBox {
  display: flex;
  flex-wrap: wrap;
  list-style: none;
  padding: 0;
  margin: 0;
}

.cardsBox li {
  width: 48%;
  margin-right: 4%;
  margin-bottom: 15px;
  background-color: #fff;
  border-radius: 8px;
  padding: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.2s;
}

.cardsBox li:nth-child(2n) {
  margin-right: 0;
}

.cardsBox li:hover {
  transform: translateY(-2px);
}

.Peopleimg {
  margin-bottom: 10px;
}

.Peopleimg img {
  width: 100%;
  border-radius: 8px;
}

.P-title {
  font-size: 16px;
  font-weight: bold;
  margin: 0 0 5px 0;
}

.P-title a {
  color: #333;
  text-decoration: none;
}

.p-info {
  font-size: 14px;
  color: #666;
  margin-bottom: 5px;
}

.skypub {
  position: fixed;
  bottom: 20px;
  right: 20px;
  width: 60px;
  height: 60px;
  background-color: #ff6b6b;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  cursor: pointer;
  z-index: 100;
}

.skypub i {
  color: #fff;
  font-size: 24px;
}

.weui-toptips {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  padding: 10px;
  text-align: center;
  background-color: #e74c3c;
  color: #fff;
  display: none;
}

.weui-toptips.show {
  display: block;
}
</style>