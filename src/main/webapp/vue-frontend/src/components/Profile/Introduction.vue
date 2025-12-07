<template>
  <div class="introduction-container">
    <div class="introduction-header">
      <div class="back-btn" @click="goBack">
        <i class="icon-back"></i>
      </div>
      <div class="header-title">个人简介</div>
      <div class="save-btn" @click="changeIntroduction">保存</div>
    </div>
    
    <div class="introduction-content">
      <div class="introduction-display">
        <p v-if="introduction === ''">这个人很懒，什么都没写_(:3 」∠)_</p>
        <p v-else>{{ introduction }}</p>
      </div>
      
      <div class="introduction-edit">
        <textarea 
          v-model="introductionInput" 
          placeholder="请输入个人简介（最多80字）" 
          maxlength="80"
          @input="inputLengthCheck"
        ></textarea>
        <div class="introduction-count">{{ introductionInput.length }}/80</div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Introduction',
  data() {
    return {
      introduction: '',
      introductionInput: '',
      introductionCache: ''
    };
  },
  mounted() {
    this.getIntroduction();
  },
  methods: {
    getIntroduction() {
      axios.get('/api/introduction')
        .then(response => {
          if (response.data.success) {
            this.introduction = response.data.data || '';
            this.introductionInput = this.introduction;
            this.introductionCache = this.introduction;
          }
        })
        .catch(error => {
          console.error('获取个人简介失败:', error);
          this.showNetworkErrorTip();
        });
    },
    inputLengthCheck() {
      // 自动限制长度，无需额外处理
    },
    changeIntroduction() {
      if (this.introductionInput !== this.introductionCache) {
        axios.post('/api/introduction', {
          introduction: this.introductionInput
        })
          .then(response => {
            if (response.data.success) {
              this.introduction = this.introductionInput;
              this.introductionCache = this.introductionInput;
              this.$toast('更新成功');
            } else {
              this.showCustomErrorTip(response.data.message);
            }
          })
          .catch(error => {
            console.error('更新个人简介失败:', error);
            this.showNetworkErrorTip();
          });
      } else {
        this.goBack();
      }
    },
    showNetworkErrorTip() {
      this.$toast('网络连接失败，请检查网络连接');
    },
    showCustomErrorTip(message) {
      this.$toast(message);
    },
    goBack() {
      this.$router.go(-1);
    }
  }
};
</script>

<style scoped>
.introduction-container {
  background-color: #f5f5f5;
  min-height: 100vh;
}

.introduction-header {
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

.save-btn {
  color: #1aad19;
  cursor: pointer;
  font-weight: bold;
}

.introduction-content {
  padding: 20px;
}

.introduction-display {
  background-color: #fff;
  padding: 20px;
  border-radius: 5px;
  margin-bottom: 20px;
  min-height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  color: #666;
  font-size: 16px;
}

.introduction-edit {
  background-color: #fff;
  padding: 20px;
  border-radius: 5px;
  position: relative;
}

.introduction-edit textarea {
  width: 100%;
  height: 150px;
  border: 1px solid #eee;
  border-radius: 5px;
  padding: 10px;
  font-size: 16px;
  resize: none;
  outline: none;
}

.introduction-count {
  position: absolute;
  bottom: 10px;
  right: 20px;
  color: #999;
  font-size: 14px;
}
</style>