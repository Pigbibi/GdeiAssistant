<template>
  <div>
    <div class="weui-cells__title" @click="$router.go(-1)">返回</div>

    <div class="hd">
      <h1 class="page_title">功能意见反馈</h1>
    </div>

    <div class="weui-cells weui-cells_form">
      <div class="weui-cell">
        <div class="weui-cell__bd">
          <textarea class="weui-textarea" placeholder="请输入功能建议（最多250字）" rows="5" id="feedback-content" v-model="content"></textarea>
          <div class="weui-textarea-counter">{{ content.length }}/250</div>
        </div>
      </div>
      <div class="weui-cell">
        <div class="weui-cell__bd">
          <input type="text" class="weui-input" placeholder="您的联系方式（选填）" v-model="contact" />
        </div>
      </div>
    </div>

    <div class="weui-cells">
      <div class="weui-cell">
        <div class="weui-cell__bd">
          <p>上传图片（最多3张）</p>
        </div>
      </div>
    </div>

    <div class="weui-uploader" id="uploader">
      <div class="weui-uploader__hd">
        <p class="weui-uploader__title">图片上传</p>
        <div class="weui-uploader__info" id="uploaderCount">0/3</div>
      </div>
      <div class="weui-uploader__bd">
        <ul class="weui-uploader__files" id="uploaderFiles">
          <li v-for="(file, index) in uploadedFiles" :key="index" class="weui-uploader__file" :style="{ backgroundImage: `url(${file})` }" @click="previewImage(file)">
            <div class="weui-uploader__file-content">{{ index + 1 }}</div>
            <div class="weui-uploader__file-delete" @click.stop="deleteImage(index)"></div>
          </li>
        </ul>
        <div v-if="uploadedFiles.length < 3" class="weui-uploader__input-box">
          <input id="uploaderInput" class="weui-uploader__input" type="file" accept="image/*" multiple="" @change="onFileChange" />
        </div>
      </div>
    </div>

    <div class="weui-btn-area">
      <button type="button" class="weui-btn weui-btn_primary" id="submitFeedback">提交反馈</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FeedbackFunction',
  data() {
    return {
      content: '',
      contact: '',
      uploadedFiles: []
    };
  },
  mounted() {
    this.initUpload();
    this.initSubmit();
  },
  methods: {
    initUpload() {
      // 图片上传功能已通过v-model和@change事件实现
    },
    initSubmit() {
      // 提交功能已通过@click事件实现
    },
    onFileChange(e) {
      const files = e.target.files;
      if (files.length > 0) {
        for (let i = 0; i < files.length; i++) {
          const file = files[i];
          if (this.validateImage(file)) {
            this.previewImageFile(file);
          }
        }
      }
    },
    validateImage(file) {
      // 验证文件类型
      const imageType = /^image\//;
      if (!imageType.test(file.type)) {
        this.showError('请选择图片文件');
        return false;
      }
      // 验证文件大小（5MB）
      if (file.size > 5 * 1024 * 1024) {
        this.showError('图片大小不能超过5MB');
        return false;
      }
      return true;
    },
    previewImageFile(file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        this.uploadedFiles.push(e.target.result);
      };
      reader.readAsDataURL(file);
    },
    previewImage(imageUrl) {
      // 图片预览功能
      console.log('预览图片:', imageUrl);
    },
    deleteImage(index) {
      this.uploadedFiles.splice(index, 1);
    },
    showError(message) {
      // 使用vant的Toast组件显示错误信息
      this.$toast(message);
    },
    submitFeedback() {
      if (!this.content.trim()) {
        this.showError('请输入功能建议');
        return;
      }

      if (this.content.length > 250) {
        this.showError('功能建议不能超过250字');
        return;
      }

      const formData = new FormData();
      formData.append('content', this.content);
      if (this.contact) {
        formData.append('contact', this.contact);
      }

      // 处理上传的图片
      this.uploadedFiles.forEach((file, index) => {
        // 将base64转换为Blob对象
        const blob = this.base64ToBlob(file);
        formData.append('images', blob, `image_${index}.jpg`);
      });

      // 使用axios提交表单
      this.$axios.post('/api/feedback/function', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
      .then(response => {
        if (response.data.success) {
          this.$toast('反馈提交成功');
          this.$router.go(-1);
        } else {
          this.showError(response.data.message || '反馈提交失败');
        }
      })
      .catch(error => {
        console.error('提交反馈失败:', error);
        this.showError('网络错误，请稍后重试');
      });
    },
    base64ToBlob(base64) {
      const parts = base64.split(';base64,');
      const contentType = parts[0].split(':')[1];
      const raw = window.atob(parts[1]);
      const rawLength = raw.length;
      const uInt8Array = new Uint8Array(rawLength);

      for (let i = 0; i < rawLength; ++i) {
        uInt8Array[i] = raw.charCodeAt(i);
      }

      return new Blob([uInt8Array], { type: contentType });
    }
  }
};
</script>

<style scoped>
/* 保持原有的CSS样式不变 */
.weui-textarea {
  min-height: 120px;
}

.weui-textarea-counter {
  text-align: right;
  color: #888;
  font-size: 14px;
}

.weui-uploader__file {
  position: relative;
  margin-right: 9px;
  margin-bottom: 9px;
  width: 79px;
  height: 79px;
  background-size: cover;
  background-position: center;
}

.weui-uploader__file-delete {
  position: absolute;
  top: -6px;
  right: -6px;
  width: 20px;
  height: 20px;
  background-color: rgba(0, 0, 0, 0.7);
  border-radius: 10px;
  color: #fff;
  text-align: center;
  line-height: 20px;
  font-size: 14px;
}
</style>