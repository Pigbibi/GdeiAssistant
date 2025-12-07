<template>
  <div class="public-profile-container">
    <div class="profile-header">
      <div class="back-btn" @click="goBack">
        <i class="icon-back"></i>
      </div>
      <div class="header-title">个人资料</div>
    </div>
    
    <div class="profile-content">
      <div class="avatar-section">
        <img :src="avatarUrl" alt="用户头像" class="avatar">
        <div class="username">{{ username }}</div>
        <div class="nickname">{{ nickname }}</div>
        <div class="ip-area">IP属地: {{ ipArea || '-' }}</div>
      </div>
      
      <div class="info-section">
        <div class="info-item" v-if="showFaculty">
          <div class="item-label">院系</div>
          <div class="item-value">{{ faculty || '-' }}</div>
        </div>
        <div class="info-item" v-if="showMajor">
          <div class="item-label">专业</div>
          <div class="item-value">{{ major || '-' }}</div>
        </div>
        <div class="info-item" v-if="showLocation">
          <div class="item-label">所在地</div>
          <div class="item-value">{{ location || '-' }}</div>
        </div>
        <div class="info-item" v-if="showHometown">
          <div class="item-label">家乡</div>
          <div class="item-value">{{ hometown || '-' }}</div>
        </div>
        <div class="info-item" v-if="showIntroduction">
          <div class="item-label">个人简介</div>
          <div class="item-value intro">{{ introduction || '这个人很懒，什么都没写_(:3 」∠)_' }}</div>
        </div>
        <div class="info-item" v-if="showEnrollment">
          <div class="item-label">入学年份</div>
          <div class="item-value">{{ enrollment || '-' }}</div>
        </div>
        <div class="info-item" v-if="showAge">
          <div class="item-label">年龄</div>
          <div class="item-value">{{ age || '-' }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'PublicProfile',
  data() {
    return {
      avatarUrl: '',
      username: '',
      nickname: '',
      ipArea: '',
      faculty: '',
      major: '',
      location: '',
      hometown: '',
      introduction: '',
      enrollment: '',
      age: '',
      showFaculty: true,
      showMajor: true,
      showLocation: true,
      showHometown: true,
      showIntroduction: true,
      showEnrollment: true,
      showAge: true
    };
  },
  mounted() {
    this.loadProfile();
    this.loadAvatar();
    this.loadIPAddressArea();
  },
  methods: {
    loadProfile() {
      // 这里需要根据路由参数获取用户名
      const username = this.$route.params.username || '';
      if (username) {
        axios.get(`/api/profile/user/${username}`)
          .then(response => {
            if (response.data.success) {
              const profile = response.data.data;
              this.username = profile.username;
              this.nickname = profile.nickname;
              this.faculty = profile.faculty;
              this.major = profile.major;
              this.location = profile.location;
              this.hometown = profile.hometown;
              this.introduction = profile.introduction;
              this.enrollment = profile.enrollment;
              this.age = profile.age;
              // 这里需要根据隐私设置决定是否显示某些信息
              // 由于是公开资料，假设都可以显示
            }
          })
          .catch(error => {
            console.error('加载个人资料失败:', error);
          });
      } else {
        // 如果没有用户名参数，加载当前用户的个人资料
        this.loadCurrentUserProfile();
      }
    },
    loadCurrentUserProfile() {
      axios.get('/api/profile')
        .then(response => {
          if (response.data.success) {
            const profile = response.data.data;
            this.username = profile.username;
            this.nickname = profile.nickname;
            this.faculty = profile.faculty;
            this.major = profile.major;
            this.location = profile.location;
            this.hometown = profile.hometown;
            this.introduction = profile.introduction;
            this.enrollment = profile.enrollment;
            this.age = profile.age;
          }
        })
        .catch(error => {
          console.error('加载个人资料失败:', error);
        });
    },
    loadAvatar() {
      axios.get('/api/avatar')
        .then(response => {
          if (response.data.success) {
            this.avatarUrl = response.data.data || '/static/images/default-avatar.png';
          }
        })
        .catch(error => {
          console.error('加载头像失败:', error);
          this.avatarUrl = '/static/images/default-avatar.png';
        });
    },
    loadIPAddressArea() {
      axios.get('/api/ip/area')
        .then(response => {
          if (response.data.success) {
            this.ipArea = response.data.data;
          }
        })
        .catch(error => {
          console.error('加载IP属地失败:', error);
        });
    },
    goBack() {
      this.$router.go(-1);
    }
  }
};
</script>

<style scoped>
.public-profile-container {
  background-color: #f5f5f5;
  min-height: 100vh;
}

.profile-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 50px;
  background-color: #fff;
  padding: 0 15px;
  border-bottom: 1px solid #eee;
  position: sticky;
  top: 0;
  z-index: 10;
}

.back-btn {
  cursor: pointer;
  font-size: 20px;
  color: #333;
}

.header-title {
  font-size: 18px;
  font-weight: bold;
}

.profile-content {
  padding: 20px;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 30px;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 15px;
  border: 3px solid #fff;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.username {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 5px;
}

.nickname {
  color: #666;
  margin-bottom: 10px;
}

.ip-area {
  color: #999;
  font-size: 12px;
}

.info-section {
  background-color: #fff;
  border-radius: 5px;
  padding: 10px 0;
}

.info-item {
  display: flex;
  padding: 15px;
  border-bottom: 1px solid #eee;
}

.info-item:last-child {
  border-bottom: none;
}

.item-label {
  width: 80px;
  color: #666;
}

.item-value {
  flex: 1;
  color: #333;
}

.item-value.intro {
  white-space: pre-line;
}
</style>