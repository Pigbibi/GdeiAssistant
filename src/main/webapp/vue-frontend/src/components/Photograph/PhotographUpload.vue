<template>
  <div class="photograph-upload">
    <!-- 图片文件上传表单 -->
    <form id="uploadForm" style="display: none" enctype="multipart/form-data">
      <input id="imageFileInput_1" accept="image/*" type="file" hidden="hidden" @change="handleFileChange($event, 1)">
      <input id="imageFileInput_2" accept="image/*" type="file" hidden="hidden" @change="handleFileChange($event, 2)">
      <input id="imageFileInput_3" accept="image/*" type="file" hidden="hidden" @change="handleFileChange($event, 3)">
      <input id="imageFileInput_4" accept="image/*" type="file" hidden="hidden" @change="handleFileChange($event, 4)">
    </form>

    <section id="upload">
      <div class="am-form-group">
        <label>标题/名字<span class="red-text">*</span> </label>
        <input type="text" maxlength="25" id="title" v-model="title" class="am-form-field am-round" placeholder="输入照片标题或你的名字">
      </div>
      <div class="am-form-group">
        <label>照片类型</label>
        <input type="radio" class="" name="type" value="1" v-model="type" checked>生活照
        <input type="radio" class="" name="type" value="2" v-model="type">校园照
        <input type="radio" class="" name="type" value="3" v-model="type">毕业照
      </div>
      <div style="margin-bottom: 10px">
        <i class="am-icon-picture-o"></i>选择主图<span class="red-text">*</span>
      </div>
      <div id="main-photo-box" class="am-vertical-align" @click="$('#imageFileInput_1').click()">
        <div class="am-vertical-align am-vertical-align-middle preview-box">
          <i class="am-icon-plus-circle add-picture" style="font-size: 40px;line-height: 120px;color: #17bcd4;"></i>
        </div>
      </div>

      <br>

      <div style="margin-bottom: 10px">
        <i class="am-icon-picture-o"></i>选择副图(选填，可多选，最多三张)
      </div>
      <div class="am-g" id="second-photo-box">
        <div class="am-u-sm-4" @click="$('#imageFileInput_2').click()">
          <div id="second-photo-box-1" class="am-vertical-align">
            <div class="am-vertical-align-middle">
              <i class="am-icon-plus-circle"></i>
            </div>
          </div>
        </div>
        <div class="am-u-sm-4" @click="$('#imageFileInput_3').click()">
          <div id="second-photo-box-2" class="am-vertical-align">
            <div class="am-vertical-align-middle">
              <i class="am-icon-plus-circle"></i>
            </div>
          </div>
        </div>
        <div class="am-u-sm-4" @click="$('#imageFileInput_4').click()">
          <div id="second-photo-box-3" class="am-vertical-align">
            <div class="am-vertical-align-middle">
              <i class="am-icon-plus-circle"></i>
            </div>
          </div>
        </div>
      </div>

      <br>

      <button class="am-btn am-btn-default" id="clearSecondPicture" @click="clearPictures">
        <i class="am-icon-trash"></i>清空图片
      </button>

      <div class="am-form-group" id="say-something">
        <label>说点什么吧</label>
        <textarea class="am-form-field am-radius" @keyup="inputLengthCheck" rows="4" id="content" v-model="content" placeholder="分享照片背后的故事..."></textarea>
        <p class="am-form-help" id="word-count">{{ content.length }}/150字</p>
      </div>

      <br>

      <button type="button" class="am-btn am-btn-primary am-round" id="submit" @click="submitUpload">
        <i class="am-icon-upload"></i>上传照片
      </button>
    </section>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      title: '',
      type: 1,
      content: '',
      // 允许上传的图片类型
      allowTypes: ['image/jpg', 'image/jpeg', 'image/png', 'image/gif'],
      // 图片最大大小为5MB
      maxSize: 1024 * 1024 * 5,
      // 存储选中的图片
      images: {
        1: null, // 主图
        2: null, // 副图1
        3: null, // 副图2
        4: null  // 副图3
      }
    };
  },
  methods: {
    // 处理文件选择事件
    handleFileChange(event, index) {
      const files = event.target.files;
      // 如果没有选中文件，直接返回
      if (files.length === 0) {
        return;
      }
      const file = files[0];
      this.loadImage(file, index);
    },
    // 清空图片
    clearPictures() {
      this.images = {
        1: null,
        2: null,
        3: null,
        4: null
      };
      // 清空预览图
      $('#main-photo-box').css('background-image', '');
      $('#second-photo-box-1').css('background-image', '');
      $('#second-photo-box-2').css('background-image', '');
      $('#second-photo-box-3').css('background-image', '');
    },
    // 检测Input文字长度是否超过限制并进行实时字数反馈
    inputLengthCheck() {
      if (this.content.length > 150) {
        this.content = this.content.substr(0, 150);
      }
    },
    // 加载图片并显示预览图
    loadImage(file, index) {
      // 如果类型不在允许的类型范围内
      if (this.allowTypes.indexOf(file.type) === -1) {
        $.alert('不合法的图片文件类型', '上传错误');
        return;
      }

      if (file.size > this.maxSize) {
        $.alert('图片文件不能超过5MB', '文件过大');
        return;
      }

      const reader = new FileReader();
      reader.readAsDataURL(file);

      reader.onload = (e) => {
        const img = new Image();
        img.src = e.target.result;
        img.onload = () => {
          // 宽度和高度为原图高度
          const w = img.width;
          const h = img.height;
          const canvas = document.createElement('canvas');
          const ctx = canvas.getContext('2d');

          // 设置 canvas 的宽度和高度
          canvas.width = w;
          canvas.height = h;
          ctx.drawImage(img, 0, 0, w, h);

          // 返回一个包含图片展示的 Data URI
          const base64 = canvas.toDataURL('image/jpeg', 0.8);

          // 存储图片
          this.images[index] = base64;

          if (index === 1) {
            // 设置主图预览图
            $('#main-photo-box').css('background-repeat', 'round');
            $('#main-photo-box').css('background-image', `url(${base64})`);
          } else {
            // 设置副图预览图
            $(`#second-photo-box-${index - 1}`).css('background-repeat', 'round');
            $(`#second-photo-box-${index - 1}`).css('background-image', `url(${base64})`);
          }
        };
      };
    },
    // 提交照片信息
    submitUpload() {
      if (this.title.length === 0) {
        weui.alert('标题不能为空', {
          title: '错误提示',
          buttons: [{
            label: '确定',
            type: 'primary'
          }]
        });
      } else if (this.title.length > 25) {
        weui.alert('标题长度过长', {
          title: '错误提示',
          buttons: [{
            label: '确定',
            type: 'primary'
          }]
        });
      } else if (!this.images[1]) {
        weui.alert('请上传至少一张图片', {
          title: '错误提示',
          buttons: [{
            label: '确定',
            type: 'primary'
          }]
        });
      } else {
        const loading = weui.loading('提交中');
        $('#submit').attr('disabled', true);
        
        const formData = new FormData();
        // 基本属性
        formData.append('title', this.title);
        if (this.content) {
          formData.append('content', this.content);
        }
        
        let count = 1;
        formData.append('type', this.type);
        
        // 处理主图
        if (this.images[1]) {
          const blob = this.base64ToBlob(this.images[1]);
          formData.append('image1', blob);
        }
        
        // 处理副图
        for (let index = 2; index <= 4; index++) {
          if (this.images[index]) {
            const blob = this.base64ToBlob(this.images[index]);
            formData.append(`image${index}`, blob);
            count++;
          }
        }
        
        // 图片数量
        formData.append('count', count);
        
        // 发送请求
        axios.post('/api/photograph', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        .then(result => {
          $('#submit').attr('disabled', false);
          loading.hide();
          if (result.data.success === true) {
            // 上传成功，跳转到照片列表页面
            this.$router.push('/photograph');
          } else {
            weui.alert(result.data.message, {
              title: '请求失败',
              buttons: [{
                label: '确定',
                type: 'primary'
              }]
            });
          }
        })
        .catch(error => {
          $('#submit').attr('disabled', false);
          loading.hide();
          if (error.response) {
            weui.alert(error.response.data.message, {
              title: '请求失败',
              buttons: [{
                label: '确定',
                type: 'primary'
              }]
            });
          } else {
            weui.alert('网络访问异常，请检查网络连接', {
              title: '网络异常',
              buttons: [{
                label: '确定',
                type: 'primary'
              }]
            });
          }
        });
      }
    },
    // 将base64转换为Blob对象
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
/* 这里保持原有CSS样式不变 */
</style>