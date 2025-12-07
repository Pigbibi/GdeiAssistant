<template>
  <div class="privacy-container">
    <div class="privacy-header">
      <div class="back-btn" @click="goBack">
        <i class="icon-back"></i>
      </div>
      <div class="header-title">隐私设置</div>
    </div>
    
    <div class="privacy-content">
      <div class="privacy-section">
        <div class="section-header">个人资料可见性</div>
        <div class="section-item">
          <div class="item-label">院系</div>
          <div class="item-action">
            <input type="checkbox" id="faculty" v-model="privacy.facultyOpen" @change="changePrivacySetting('faculty')">
            <label for="faculty" class="weui-switch"></label>
          </div>
        </div>
        <div class="section-item">
          <div class="item-label">专业</div>
          <div class="item-action">
            <input type="checkbox" id="major" v-model="privacy.majorOpen" @change="changePrivacySetting('major')">
            <label for="major" class="weui-switch"></label>
          </div>
        </div>
        <div class="section-item">
          <div class="item-label">所在地</div>
          <div class="item-action">
            <input type="checkbox" id="location" v-model="privacy.locationOpen" @change="changePrivacySetting('location')">
            <label for="location" class="weui-switch"></label>
          </div>
        </div>
        <div class="section-item">
          <div class="item-label">家乡</div>
          <div class="item-action">
            <input type="checkbox" id="hometown" v-model="privacy.hometownOpen" @change="changePrivacySetting('hometown')">
            <label for="hometown" class="weui-switch"></label>
          </div>
        </div>
        <div class="section-item">
          <div class="item-label">个人简介</div>
          <div class="item-action">
            <input type="checkbox" id="introduction" v-model="privacy.introductionOpen" @change="changePrivacySetting('introduction')">
            <label for="introduction" class="weui-switch"></label>
          </div>
        </div>
        <div class="section-item">
          <div class="item-label">入学年份</div>
          <div class="item-action">
            <input type="checkbox" id="enrollment" v-model="privacy.enrollmentOpen" @change="changePrivacySetting('enrollment')">
            <label for="enrollment" class="weui-switch"></label>
          </div>
        </div>
        <div class="section-item">
          <div class="item-label">年龄</div>
          <div class="item-action">
            <input type="checkbox" id="age" v-model="privacy.ageOpen" @change="changePrivacySetting('age')">
            <label for="age" class="weui-switch"></label>
          </div>
        </div>
        <div class="section-item">
          <div class="item-label">学历</div>
          <div class="item-action">
            <input type="checkbox" id="degree" v-model="privacy.degreeOpen" @change="changePrivacySetting('degree')">
            <label for="degree" class="weui-switch"></label>
          </div>
        </div>
        <div class="section-item">
          <div class="item-label">职业</div>
          <div class="item-action">
            <input type="checkbox" id="profession" v-model="privacy.professionOpen" @change="changePrivacySetting('profession')">
            <label for="profession" class="weui-switch"></label>
          </div>
        </div>
        <div class="section-item">
          <div class="item-label">小学</div>
          <div class="item-action">
            <input type="checkbox" id="primary_school" v-model="privacy.primarySchoolOpen" @change="changePrivacySetting('primary_school')">
            <label for="primary_school" class="weui-switch"></label>
          </div>
        </div>
        <div class="section-item">
          <div class="item-label">初中</div>
          <div class="item-action">
            <input type="checkbox" id="junior_high_school" v-model="privacy.juniorHighSchoolOpen" @change="changePrivacySetting('junior_high_school')">
            <label for="junior_high_school" class="weui-switch"></label>
          </div>
        </div>
        <div class="section-item">
          <div class="item-label">高中</div>
          <div class="item-action">
            <input type="checkbox" id="high_school" v-model="privacy.highSchoolOpen" @change="changePrivacySetting('high_school')">
            <label for="high_school" class="weui-switch"></label>
          </div>
        </div>
        <div class="section-item">
          <div class="item-label">大学</div>
          <div class="item-action">
            <input type="checkbox" id="colleges" v-model="privacy.colleges" @change="changePrivacySetting('colleges')">
            <label for="colleges" class="weui-switch"></label>
          </div>
        </div>
      </div>
      
      <div class="privacy-section">
        <div class="section-header">数据设置</div>
        <div class="section-item">
          <div class="item-label">允许缓存数据</div>
          <div class="item-action">
            <input type="checkbox" id="cache" v-model="privacy.cacheAllow" @change="changePrivacySetting('cache')">
            <label for="cache" class="weui-switch"></label>
          </div>
        </div>
        <div class="section-item">
          <div class="item-label">允许搜索引擎收录</div>
          <div class="item-action">
            <input type="checkbox" id="robots" v-model="privacy.robotsIndexAllow" @change="changePrivacySetting('robots')">
            <label for="robots" class="weui-switch"></label>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Privacy',
  data() {
    return {
      privacy: {
        facultyOpen: false,
        majorOpen: false,
        locationOpen: false,
        hometownOpen: false,
        introductionOpen: false,
        enrollmentOpen: false,
        ageOpen: false,
        degreeOpen: false,
        professionOpen: false,
        primarySchoolOpen: false,
        juniorHighSchoolOpen: false,
        highSchoolOpen: false,
        colleges: false,
        cacheAllow: false,
        robotsIndexAllow: false
      }
    };
  },
  mounted() {
    this.getPrivacySettings();
  },
  methods: {
    getPrivacySettings() {
      axios.get('/api/privacy')
        .then(response => {
          if (response.data.success) {
            this.privacy = response.data.data;
          } else {
            this.$toast(response.data.message);
          }
        })
        .catch(error => {
          console.error('获取隐私设置失败:', error);
          this.$toast('网络连接失败，请检查网络连接');
        });
    },
    changePrivacySetting(tag) {
      const state = this.privacy[tag.replace(/_([a-z])/g, (g) => g[1].toUpperCase()) + 'Open'] || this.privacy[tag];
      
      axios.post('/api/privacy', {
        tag: tag,
        state: state
      })
        .then(response => {
          if (response.data.success) {
            this.$toast('更新成功');
          } else {
            // 恢复原状态
            this.privacy[tag.replace(/_([a-z])/g, (g) => g[1].toUpperCase()) + 'Open'] = !state;
            this.privacy[tag] = !state;
            this.$toast(response.data.message);
          }
        })
        .catch(error => {
          console.error('更新隐私设置失败:', error);
          // 恢复原状态
          this.privacy[tag.replace(/_([a-z])/g, (g) => g[1].toUpperCase()) + 'Open'] = !state;
          this.privacy[tag] = !state;
          this.$toast('网络连接失败，请检查网络连接');
        });
    },
    goBack() {
      this.$router.go(-1);
    }
  }
};
</script>

<style scoped>
.privacy-container {
  background-color: #f5f5f5;
  min-height: 100vh;
}

.privacy-header {
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

.privacy-content {
  margin-top: 10px;
}

.privacy-section {
  background-color: #fff;
  margin-bottom: 10px;
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
  justify-content: space-between;
  padding: 15px;
  border-bottom: 1px solid #eee;
}

.section-item:last-child {
  border-bottom: none;
}

.item-label {
  color: #333;
  font-size: 16px;
}

.item-action {
  position: relative;
}

.weui-switch {
  display: inline-block;
  position: relative;
  width: 52px;
  height: 32px;
  border: 1px solid #dfdfdf;
  outline: 0;
  border-radius: 16px;
  box-sizing: border-box;
  background-color: #dfdfdf;
  transition: background-color 0.3s, border 0.3s;
  cursor: pointer;
}

.weui-switch:before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 50px;
  height: 30px;
  border-radius: 15px;
  background-color: #fdfdfd;
  transition: transform 0.3s;
}

.weui-switch:after {
  content: "";
  position: absolute;
  top: -1px;
  left: -1px;
  width: 32px;
  height: 32px;
  border-radius: 16px;
  background-color: #fff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.3);
  transition: transform 0.3s;
}

input[type="checkbox"]:checked + .weui-switch {
  border-color: #1aad19;
  background-color: #1aad19;
}

input[type="checkbox"]:checked + .weui-switch:before {
  transform: scale(0);
}

input[type="checkbox"]:checked + .weui-switch:after {
  transform: translateX(20px);
}

input[type="checkbox"] {
  position: absolute;
  opacity: 0;
  width: 0;
  height: 0;
}
</style>