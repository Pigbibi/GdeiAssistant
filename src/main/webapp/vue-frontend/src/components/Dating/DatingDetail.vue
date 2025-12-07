<template>
  <div class="warp">
    <!-- 返回按钮 -->
    <div class="head" @click="goBack">
      <div class="logo"><span class="t">卖室友</span></div>
    </div>

    <div class="conterInfo">
      <!-- 加载中 -->
      <div v-if="loading" class="loading-container">
        <div class="weui_loading"></div>
        <p>加载中...</p>
      </div>

      <!-- 错误提示 -->
      <div v-else-if="error" class="error-container">
        <p>{{ error }}</p>
        <button @click="loadProfileDetail">重试</button>
      </div>

      <!-- 用户信息 -->
      <div v-else-if="profile" class="releaseBox">
        <div class="releaseName">{{ profile.nickname }}
          <div class="yuan" style="top: -5.5px; left: -5.5px;"></div>
          <div class="yuan" style="top: -5.5px; right: -5.5px;"></div>
          <div class="yuan" style="top: -5.5px; left: 50%; margin-left: -5.5px;"></div>
        </div>
        <div class="Photo">
          <img :src="profileImage" alt="头像">
        </div>
        <div class="miaoshu">
          {{ profile.content }}
        </div>
        <div class="yuepian">
          <ul>
            <li><span>年级：</span>{{ getGradeText(profile.grade) }}</li>
            <li><span>专业：</span>{{ profile.faculty }}</li>
            <li><span>家乡：</span>{{ profile.hometown }}</li>
            <li v-if="isContactVisible"><span>QQ：</span>{{ profile.qq }}</li>
            <li v-else><span>QQ：</span>对方接受了撩一下后才可见哦</li>
            <li v-if="isContactVisible"><span>微信：</span>{{ profile.wechat }}</li>
            <li v-else><span>微信：</span>对方接受了撩一下后才可见哦</li>
          </ul>
        </div>

        <!-- 撩一下表单 -->
        <div v-if="!isPickNotAvailable" class="textBox">
          <textarea class="textarea" id="content" v-model="content" placeholder="来说点什么吧，不超过50字"></textarea>
          <input type="button" @click="submitPickInfo" class="circleBtn btnclick" value="撩一下"/>
          <div class="yuan" style="top: -5.5px; left: -5.5px;"></div>
          <div class="yuan" style="top: -5.5px; right: -5.5px;"></div>
        </div>
      </div>
    </div>

    <!-- 撩一下信息发送成功弹框 -->
    <div id="toast" v-if="showToast">
      <div class="weui-mask_transparent"></div>
      <div class="weui-toast">
        <i class="weui-icon-success-no-circle weui-icon_toast"></i>
        <p class="weui-toast__content">发送成功，请耐心等待对方回复</p>
      </div>
    </div>

    <!-- 错误提示 -->
    <div class="weui-toptips weui_warn js_tooltips" v-if="errorMessage">{{ errorMessage }}</div>
  </div>
</template>

<script>
import axios from 'axios';
import FastClick from 'fastclick';

export default {
  name: 'DatingDetail',
  data() {
    return {
      loading: true,
      error: '',
      profile: null,
      profileId: '',
      profileImage: '',
      isContactVisible: false,
      isPickNotAvailable: false,
      content: '',
      showToast: false,
      errorMessage: ''
    };
  },
  mounted() {
    // 初始化FastClick
    FastClick.attach(document.body);
    
    // 获取profileId并加载详情
    this.profileId = this.$route.params.id;
    if (this.profileId) {
      this.loadProfileDetail(this.profileId);
    }
  },
  methods: {
    goBack() {
      this.$router.push('/dating');
    },
    loadProfileDetail(profileId) {
      this.loading = true;
      this.error = '';

      // 发送API请求获取用户详情
      const url = `/api/dating/profile/id/${profileId}`;
      
      axios.get(url)
        .then(response => {
          this.loading = false;
          const result = response.data;
          if (result.success) {
            this.profile = result.data;
            this.isContactVisible = result.isContactVisible;
            this.isPickNotAvailable = result.isPickNotAvailable;
            
            // 加载头像
            this.loadProfileImage(profileId);
          } else {
            this.error = result.message || '加载失败';
          }
        })
        .catch(error => {
          this.loading = false;
          this.error = '网络连接异常，请重试';
          console.error('Error:', error);
        });
    },
    loadProfileImage(id) {
      const url = `/api/dating/profile/id/${id}/picture`;
      
      axios.get(url)
        .then(response => {
          const result = response.data;
          if (result.success) {
            this.profileImage = result.data;
          }
        })
        .catch(error => {
          console.error('Error loading image:', error);
        });
    },
    submitPickInfo() {
      if (this.content.length > 50) {
        this.errorMessage = '撩一下输入的内容太长了';
        this.hideError();
      } else if (this.content.length === 0) {
        this.errorMessage = '请输入撩一下的留言信息';
        this.hideError();
      } else {
        axios.post('/api/dating/pick', {
          profileId: this.profileId,
          content: this.content
        })
        .then(response => {
          const result = response.data;
          if (result.success) {
            this.showToast = true;
            setTimeout(() => {
              this.showToast = false;
            }, 1000);
            this.content = '';
          } else {
            this.errorMessage = result.message;
            this.hideError();
          }
        })
        .catch(error => {
          this.errorMessage = '网络连接异常，请重试';
          this.hideError();
          console.error('Error:', error);
        });
      }
    },
    hideError() {
      setTimeout(() => {
        this.errorMessage = '';
      }, 2000);
    },
    getGradeText(grade) {
      const gradeMap = {
        1: '大一',
        2: '大二',
        3: '大三',
        4: '大四'
      };
      return gradeMap[grade] || '';
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

.weui-header {
  background-color: #ff6b6b;
  padding: 15px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  color: #fff;
}

.weui-header-left a {
  color: #fff;
  font-size: 20px;
}

.weui-header-title {
  margin: 0;
  font-size: 18px;
  font-weight: bold;
}

.content {
  padding: 15px;
}

.loading-container, .error-container {
  text-align: center;
  padding: 40px 0;
}

.weui_loading {
  width: 40px;
  height: 40px;
  margin: 0 auto 10px;
  border: 2px solid #ff6b6b;
  border-radius: 50%;
  border-top-color: transparent;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.profile-container {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.profile-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 20px;
  border: 3px solid #ff6b6b;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.profile-info {
  flex: 1;
}

.nickname {
  font-size: 24px;
  font-weight: bold;
  margin: 0 0 10px 0;
  color: #333;
}

.basic-info {
  font-size: 16px;
  color: #666;
  margin: 0 0 5px 0;
}

.hometown {
  font-size: 14px;
  color: #999;
  margin: 0;
}

.profile-content {
  margin-bottom: 20px;
}

.content-item {
  margin-bottom: 20px;
}

.content-item h3 {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin: 0 0 10px 0;
  border-left: 3px solid #ff6b6b;
  padding-left: 10px;
}

.content-item p {
  font-size: 14px;
  color: #666;
  margin: 0;
  line-height: 1.6;
}

.contact-info {
  background-color: #f0f0f0;
  padding: 10px;
  border-radius: 4px;
  font-size: 14px;
  color: #333;
}

.action-buttons {
  margin-top: 20px;
}

.weui_btn {
  width: 100%;
  padding: 12px;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.weui_btn_primary {
  background-color: #ff6b6b;
  color: #fff;
}

.weui_btn_primary:hover {
  background-color: #ff5252;
}
</style>