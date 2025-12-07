<template>
  <div class="avatar-container">
    <div class="avatar-header">
      <div class="back-btn" @click="goBack">
        <i class="icon-back"></i>
      </div>
      <div class="header-title">修改头像</div>
    </div>
    
    <div class="avatar-content">
      <div class="avatar-display">
        <img :src="avatarUrl" alt="用户头像" class="current-avatar">
      </div>
      
      <div class="avatar-actions">
        <div class="action-item" @click="selectAvatarImage">
          <i class="icon-upload"></i>
          <div class="action-text">上传头像</div>
        </div>
        <div class="action-item" @click="viewHDAvatar">
          <i class="icon-hd"></i>
          <div class="action-text">查看高清头像</div>
        </div>
        <div class="action-item" @click="deleteAvatar">
          <i class="icon-delete"></i>
          <div class="action-text">删除头像</div>
        </div>
      </div>
      
      <input type="file" id="avatarFileInput" ref="avatarFileInput" accept="image/*" style="display: none;" @change="handleFileSelect">
    </div>
    
    <!-- 裁剪图片弹窗 -->
    <div class="crop-dialog" v-if="showCropDialog">
      <div class="dialog-header">
        <div class="close-btn" @click="closeCropDialog">关闭</div>
        <div class="dialog-title">裁剪头像</div>
        <div class="confirm-btn" @click="drawImageAndUpload">确定</div>
      </div>
      <div class="crop-content">
        <img :src="cropImageSrc" alt="裁剪图片" ref="cropImage">
        <div class="crop-info">拖动裁剪框调整头像范围</div>
      </div>
      <input type="hidden" id="x" ref="x">
      <input type="hidden" id="y" ref="y">
      <input type="hidden" id="width" ref="width">
      <input type="hidden" id="height" ref="height">
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Avatar',
  data() {
    return {
      avatarUrl: '',
      showCropDialog: false,
      cropImageSrc: '',
      image: new Image(),
      image_hd: new Image()
    };
  },
  mounted() {
    this.loadAvatar();
  },
  methods: {
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
    selectAvatarImage() {
      this.$refs.avatarFileInput.click();
    },
    handleFileSelect(event) {
      const files = event.target.files;
      if (files.length === 0) return;
      
      const file = files[0];
      const allowTypes = ['image/jpg', 'image/jpeg', 'image/png', 'image/gif'];
      const maxSize = 1024 * 1024 * 2;
      const maxWidth = 300;
      
      if (allowTypes.indexOf(file.type) === -1) {
        this.$alert('不合法的图片文件类型', '上传错误');
        return;
      }
      
      if (file.size > maxSize) {
        this.$alert('图片文件不能超过2MB', '文件过大');
        return;
      }
      
      const reader = new FileReader();
      reader.readAsDataURL(file);
      
      reader.onload = (e) => {
        const img = new Image();
        img.src = e.target.result;
        
        img.onload = () => {
          // 处理高清原图
          const w_hd = img.width;
          const h_hd = img.height;
          const canvas_hd = document.createElement('canvas');
          const ctx_hd = canvas_hd.getContext('2d');
          canvas_hd.width = w_hd;
          canvas_hd.height = h_hd;
          ctx_hd.drawImage(img, 0, 0, w_hd, h_hd);
          const base64_hd = canvas_hd.toDataURL('image/jpeg', 1);
          this.image_hd.src = base64_hd;
          
          // 处理压缩图片
          const w = Math.min(maxWidth, img.width);
          const h = img.height * (w / img.width);
          const canvas = document.createElement('canvas');
          const ctx = canvas.getContext('2d');
          canvas.width = w;
          canvas.height = h;
          ctx.drawImage(img, 0, 0, w, h);
          const base64 = canvas.toDataURL('image/jpeg', 0.8);
          this.image.src = base64;
          
          this.cropImageSrc = base64;
          this.showCropDialog = true;
          
          // 初始化裁剪功能（这里简化处理，实际项目中可以使用cropper.js等库）
          setTimeout(() => {
            this.initCrop();
          }, 100);
        };
      };
    },
    initCrop() {
      // 这里可以集成裁剪库，如cropper.js
      // 简化处理：假设裁剪框为图片中心的正方形
      const imgElement = this.$refs.cropImage;
      if (!imgElement) return;
      
      const width = Math.min(imgElement.width, imgElement.height);
      const x = (imgElement.width - width) / 2;
      const y = (imgElement.height - width) / 2;
      
      this.$refs.x.value = x;
      this.$refs.y.value = y;
      this.$refs.width.value = width;
      this.$refs.height.value = width;
    },
    drawImageAndUpload() {
      const x = parseFloat(this.$refs.x.value);
      const y = parseFloat(this.$refs.y.value);
      const width = parseFloat(this.$refs.width.value);
      const height = parseFloat(this.$refs.height.value);
      
      // 处理压缩图片
      const canvas = document.createElement('canvas');
      canvas.width = width;
      canvas.height = height;
      const ctx = canvas.getContext('2d');
      ctx.drawImage(this.image, x, y, width, height, 0, 0, width, height);
      const base64 = canvas.toDataURL('image/jpeg', 1);
      
      // 转换为Blob
      const blob = this.dataURLtoBlob(base64);
      
      // 处理高清图片
      const scale = this.image_hd.width / this.image.width;
      const canvas_hd = document.createElement('canvas');
      canvas_hd.width = width * scale;
      canvas_hd.height = height * scale;
      const ctx_hd = canvas_hd.getContext('2d');
      ctx_hd.drawImage(this.image_hd, x * scale, y * scale, width * scale, height * scale, 0, 0, width * scale, height * scale);
      const base64_hd = canvas_hd.toDataURL('image/jpeg', 1);
      const blob_hd = this.dataURLtoBlob(base64_hd);
      
      // 上传头像
      const formData = new FormData();
      formData.append('avatar', blob);
      formData.append('avatar_hd', blob_hd);
      
      axios.post('/api/avatar', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
        .then(response => {
          if (response.data.success) {
            this.closeCropDialog();
            this.loadAvatar();
            this.$toast('头像上传成功');
          } else {
            this.$toast(response.data.message || '头像上传失败');
          }
        })
        .catch(error => {
          console.error('上传头像失败:', error);
          this.$toast('网络连接失败，请检查网络连接');
        });
    },
    dataURLtoBlob(dataURL) {
      const arr = dataURL.split(',');
      const mime = arr[0].match(/:(.*?);/)[1];
      const bstr = atob(arr[1]);
      let n = bstr.length;
      const u8arr = new Uint8Array(n);
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
      }
      return new Blob([u8arr], { type: mime });
    },
    viewHDAvatar() {
      // 查看高清头像功能
      this.$toast('查看高清头像功能开发中');
    },
    deleteAvatar() {
      this.$confirm('将删除用户上传的头像，并恢复为系统默认头像', '删除头像', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.post('/api/avatar/remove')
          .then(response => {
            if (response.data.success) {
              this.loadAvatar();
              this.$toast('头像删除成功');
            } else {
              this.$toast(response.data.message || '头像删除失败');
            }
          })
          .catch(error => {
            console.error('删除头像失败:', error);
            this.$toast('网络连接失败，请检查网络连接');
          });
      }).catch(() => {
        // 取消删除
      });
    },
    closeCropDialog() {
      this.showCropDialog = false;
      this.$refs.avatarFileInput.value = '';
    },
    goBack() {
      this.$router.go(-1);
    }
  }
};
</script>

<style scoped>
.avatar-container {
  background-color: #f5f5f5;
  min-height: 100vh;
}

.avatar-header {
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

.avatar-content {
  padding: 20px;
}

.avatar-display {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
}

.current-avatar {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid #fff;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.avatar-actions {
  display: flex;
  flex-direction: column;
}

.action-item {
  display: flex;
  align-items: center;
  padding: 15px;
  background-color: #fff;
  margin-bottom: 10px;
  border-radius: 5px;
  cursor: pointer;
}

.action-item i {
  font-size: 20px;
  margin-right: 15px;
  color: #1aad19;
}

.action-text {
  font-size: 16px;
  color: #333;
}

.crop-dialog {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.8);
  z-index: 100;
  display: flex;
  flex-direction: column;
}

.dialog-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 15px;
  background-color: #333;
  color: #fff;
}

.close-btn, .confirm-btn {
  cursor: pointer;
  padding: 5px 10px;
}

.confirm-btn {
  color: #1aad19;
}

.dialog-title {
  font-weight: bold;
}

.crop-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.crop-content img {
  max-width: 90%;
  max-height: 70%;
  object-fit: contain;
}

.crop-info {
  color: #fff;
  margin-top: 15px;
}
</style>