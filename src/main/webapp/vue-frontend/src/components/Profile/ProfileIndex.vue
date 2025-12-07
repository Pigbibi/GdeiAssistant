<template>
  <div class="profile-container">
    <div class="profile-header">
      <div class="user-info">
        <div class="avatar">
          <img :src="avatarUrl" alt="用户头像" @click="goToAvatarPage">
        </div>
        <div class="user-details">
          <div class="username">{{ username }}</div>
          <div class="nickname">{{ nickname }}</div>
        </div>
      </div>
    </div>
    
    <div class="profile-content">
      <div class="profile-section">
        <div class="section-header">个人资料</div>
        <div class="section-item">
          <div class="item-label">昵称</div>
          <div class="item-value">{{ nickname }}</div>
          <div class="item-action" @click="editNickname">修改</div>
        </div>
        <div class="section-item">
          <div class="item-label">年龄</div>
          <div class="item-value">{{ age || '-' }}</div>
          <div class="item-action" @click="editAge">修改</div>
        </div>
        <div class="section-item">
          <div class="item-label">院系</div>
          <div class="item-value">{{ faculty || '-' }}</div>
          <div class="item-action" @click="editFaculty">修改</div>
        </div>
        <div class="section-item">
          <div class="item-label">专业</div>
          <div class="item-value">{{ major || '-' }}</div>
          <div class="item-action" @click="editMajor">修改</div>
        </div>
        <div class="section-item">
          <div class="item-label">所在地</div>
          <div class="item-value">{{ location || '-' }}</div>
          <div class="item-action" @click="editLocation">修改</div>
        </div>
        <div class="section-item">
          <div class="item-label">家乡</div>
          <div class="item-value">{{ hometown || '-' }}</div>
          <div class="item-action" @click="editHometown">修改</div>
        </div>
        <div class="section-item">
          <div class="item-label">入学年份</div>
          <div class="item-value">{{ enrollment || '-' }}</div>
          <div class="item-action" @click="editEnrollment">修改</div>
        </div>
        <div class="section-item">
          <div class="item-label">个人简介</div>
          <div class="item-value intro">{{ introduction || '这个人很懒，什么都没写_(:3 」∠)_' }}</div>
          <div class="item-action" @click="goToIntroductionPage">修改</div>
        </div>
      </div>
      
      <div class="profile-section">
        <div class="section-header">隐私设置</div>
        <div class="section-item" @click="goToPrivacyPage">
          <div class="item-label">隐私设置</div>
          <div class="item-value">管理个人信息隐私</div>
          <div class="item-action">></div>
        </div>
      </div>
      
      <div class="profile-section">
        <div class="section-header">其他</div>
        <div class="section-item" @click="goToPersonalProfilePage">
          <div class="item-label">查看个人资料页访客视角</div>
          <div class="item-value"></div>
          <div class="item-action">></div>
        </div>
      </div>
    </div>
    
    <!-- 底部菜单 -->
    <div class="bottom-menu">
      <div class="menu-item" @click="goToHome">
        <i class="icon-home"></i>
        <div class="menu-text">首页</div>
      </div>
      <div class="menu-item" @click="goToSecret">
        <i class="icon-secret"></i>
        <div class="menu-text">树洞</div>
      </div>
      <div class="menu-item active" @click="goToProfile">
        <i class="icon-profile"></i>
        <div class="menu-text">我的</div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'ProfileIndex',
  data() {
    return {
      avatarUrl: '',
      username: '',
      nickname: '',
      age: '',
      faculty: '',
      major: '',
      location: '',
      hometown: '',
      enrollment: '',
      introduction: ''
    };
  },
  mounted() {
    this.loadProfile();
    this.loadAvatar();
  },
  methods: {
    loadProfile() {
      axios.get('/api/profile')
        .then(response => {
          if (response.data.success) {
            const profile = response.data.data;
            this.username = profile.username;
            this.nickname = profile.nickname;
            this.age = profile.age;
            this.faculty = profile.faculty;
            this.major = profile.major;
            this.location = profile.location;
            this.hometown = profile.hometown;
            this.enrollment = profile.enrollment;
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
    goToAvatarPage() {
      this.$router.push('/profile/avatar');
    },
    goToIntroductionPage() {
      this.$router.push('/profile/introduction');
    },
    goToPrivacyPage() {
      this.$router.push('/profile/privacy');
    },
    goToPersonalProfilePage() {
      this.$router.push('/profile/personal');
    },
    editNickname() {
      // 实现修改昵称功能
    },
    editAge() {
      // 实现修改年龄功能
    },
    editFaculty() {
      // 实现修改院系功能
    },
    editMajor() {
      // 实现修改专业功能
    },
    editLocation() {
      // 实现修改所在地功能
    },
    editHometown() {
      // 实现修改家乡功能
    },
    editEnrollment() {
      // 实现修改入学年份功能
    },
    goToHome() {
      this.$router.push('/');
    },
    goToSecret() {
      this.$router.push('/secret');
    },
    goToProfile() {
      this.$router.push('/profile');
    }
  }
};
</script>

<style scoped>
.profile-container {
  padding-bottom: 60px;
}

.profile-header {
  background-color: #fff;
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.user-info {
  display: flex;
  align-items: center;
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 20px;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  cursor: pointer;
}

.user-details {
  flex: 1;
}

.username {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 5px;
}

.nickname {
  color: #666;
  font-size: 14px;
}

.profile-content {
  margin-top: 10px;
}

.profile-section {
  background-color: #fff;
  margin-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.section-header {
  padding: 15px;
  font-weight: bold;
  color: #999;
  border-bottom: 1px solid #eee;
}

.section-item {
  display: flex;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #eee;
}

.section-item:last-child {
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
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.item-action {
  color: #1aad19;
  cursor: pointer;
}

.bottom-menu {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 50px;
  background-color: #fff;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: space-around;
  align-items: center;
}

.menu-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #666;
  cursor: pointer;
}

.menu-item.active {
  color: #1aad19;
}

.icon-home, .icon-secret, .icon-profile {
  font-size: 20px;
  margin-bottom: 3px;
}

.menu-text {
  font-size: 12px;
}
</style>