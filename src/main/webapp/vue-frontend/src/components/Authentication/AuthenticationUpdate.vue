<template>
  <div class="authentication-update">
    <div class="weui-header">
      <div class="weui-header-left">
        <a href="#" class="iconfont icon-return" @click="goBack"></a>
      </div>
      <h1 class="weui-header-title">实名认证</h1>
      <div class="weui-header-right"></div>
    </div>
    <div class="weui-content">
      <!-- 中国居民身份证认证 -->
      <div class="weui-form" v-if="isMainland">
        <div class="weui-cell weui-cell_access">
          <div class="weui-cell__hd"><label class="weui-label">姓名</label></div>
          <div class="weui-cell__bd">
            <input class="weui-input" type="text" placeholder="请输入真实姓名" v-model="realName">
          </div>
        </div>
        <div class="weui-cell weui-cell_access">
          <div class="weui-cell__hd"><label class="weui-label">身份证号</label></div>
          <div class="weui-cell__bd">
            <input class="weui-input" type="text" placeholder="请输入身份证号码" v-model="idCardNumber">
          </div>
        </div>
      </div>
      <!-- 港澳台及海外用户认证 -->
      <div class="weui-form" v-else>
        <div class="weui-cell weui-cell_access">
          <div class="weui-cell__hd"><label class="weui-label">姓名</label></div>
          <div class="weui-cell__bd">
            <input class="weui-input" type="text" placeholder="请输入真实姓名" v-model="realName">
          </div>
        </div>
        <div class="weui-cell weui-cell_access">
          <div class="weui-cell__hd"><label class="weui-label">证件号码</label></div>
          <div class="weui-cell__bd">
            <input class="weui-input" type="text" placeholder="请输入证件号码" v-model="idCardNumber">
          </div>
        </div>
        <div class="weui-cell weui-cell_access">
          <div class="weui-cell__hd"><label class="weui-label">证件类型</label></div>
          <div class="weui-cell__bd">
            <input class="weui-input" type="text" placeholder="请输入证件类型" v-model="idCardType">
          </div>
        </div>
        <div class="weui-uploader">
          <div class="weui-uploader__hd">
            <p class="weui-uploader__title">证件照片</p>
            <div class="weui-uploader__info">{{ images.length }}/3</div>
          </div>
          <div class="weui-uploader__bd">
            <ul class="weui-uploader__files" id="uploaderFiles">
              <li class="weui-uploader__file" v-for="(image, index) in images" :key="index" :style="{backgroundImage: 'url(' + image + ')'}" @click="previewImage(image, index)">
                <i class="weui-uploader__file-delete" @click.stop="deleteImage(index)"></i>
              </li>
            </ul>
            <div class="weui-uploader__input-box" v-if="images.length < 3">
              <input id="uploaderInput" class="weui-uploader__input" type="file" accept="image/*" multiple @change="onFileChange">
            </div>
          </div>
        </div>
      </div>
      <!-- 认证类型切换 -->
      <div class="weui-cells weui-cells_checkbox">
        <label class="weui-cell weui-check__label" for="mainland">
          <div class="weui-cell__hd">
            <input type="radio" class="weui-check" name="authenticationType" id="mainland" v-model="isMainland" :checked="isMainland" @change="switchAuthenticationType">
            <i class="weui-icon-checked"></i>
          </div>
          <div class="weui-cell__bd">
            <p>中国居民身份证</p>
          </div>
        </label>
        <label class="weui-cell weui-check__label" for="other">
          <div class="weui-cell__hd">
            <input type="radio" class="weui-check" name="authenticationType" id="other" v-model="isMainland" :checked="!isMainland" @change="switchAuthenticationType">
            <i class="weui-icon-checked"></i>
          </div>
          <div class="weui-cell__bd">
            <p>港澳台及海外用户</p>
          </div>
        </label>
      </div>
      <!-- 提交按钮 -->
      <div class="weui-btn-area">
        <a href="#" class="weui-btn weui-btn_primary" @click="submitAuthentication">提交认证</a>
      </div>
    </div>
    <!-- 错误提示 -->
    <div class="weui_warn" v-if="showError">
      <i class="weui-icon-warn"></i>
      <p>{{ errorMessage }}</p>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import FastClick from 'fastclick'

export default {
  name: 'AuthenticationUpdate',
  data() {
    return {
      isMainland: true,
      realName: '',
      idCardNumber: '',
      idCardType: '',
      images: [],
      showError: false,
      errorMessage: ''
    }
  },
  mounted() {
    // 初始化FastClick
    FastClick.attach(document.body)
  },
  methods: {
    // 返回上一页
    goBack() {
      this.$router.go(-1)
    },
    // 切换认证类型
    switchAuthenticationType() {
      this.isMainland = !this.isMainland
      // 重置表单
      this.realName = ''
      this.idCardNumber = ''
      this.idCardType = ''
      this.images = []
    },
    // 文件上传处理
    onFileChange(e) {
      const files = e.target.files
      if (files.length === 0) return
      
      for (let i = 0; i < files.length; i++) {
        const file = files[i]
        // 检查文件类型
        if (!/^image\/(jpeg|jpg|png|gif)$/.test(file.type)) {
          this.showError = true
          this.errorMessage = '只支持JPG、PNG、GIF格式的图片'
          setTimeout(() => {
            this.showError = false
          }, 2000)
          continue
        }
        // 检查文件大小
        if (file.size > 5 * 1024 * 1024) {
          this.showError = true
          this.errorMessage = '图片大小不能超过5MB'
          setTimeout(() => {
            this.showError = false
          }, 2000)
          continue
        }
        // 处理图片
        this.handleImage(file)
      }
      // 清空input
      e.target.value = ''
    },
    // 处理图片
    handleImage(file) {
      const reader = new FileReader()
      reader.onload = (e) => {
        // 压缩图片
        this.compressImage(e.target.result, (compressedDataUrl) => {
          this.images.push(compressedDataUrl)
        })
      }
      reader.readAsDataURL(file)
    },
    // 压缩图片
    compressImage(dataUrl, callback) {
      const img = new Image()
      img.onload = () => {
        const canvas = document.createElement('canvas')
        const ctx = canvas.getContext('2d')
        
        // 设置压缩后的尺寸
        const maxWidth = 800
        const maxHeight = 800
        let width = img.width
        let height = img.height
        
        if (width > maxWidth) {
          height = height * (maxWidth / width)
          width = maxWidth
        }
        if (height > maxHeight) {
          width = width * (maxHeight / height)
          height = maxHeight
        }
        
        canvas.width = width
        canvas.height = height
        ctx.drawImage(img, 0, 0, width, height)
        
        // 压缩质量
        const compressedDataUrl = canvas.toDataURL('image/jpeg', 0.8)
        callback(compressedDataUrl)
      }
      img.src = dataUrl
    },
    // 预览图片
    previewImage(image, index) {
      // 这里可以实现图片预览功能
      console.log('预览图片:', image)
    },
    // 删除图片
    deleteImage(index) {
      this.images.splice(index, 1)
    },
    // 提交认证
    submitAuthentication() {
      // 表单验证
      if (!this.realName) {
        this.showError = true
        this.errorMessage = '请输入姓名'
        setTimeout(() => {
          this.showError = false
        }, 2000)
        return
      }
      
      if (!this.idCardNumber) {
        this.showError = true
        this.errorMessage = '请输入证件号码'
        setTimeout(() => {
          this.showError = false
        }, 2000)
        return
      }
      
      if (!this.isMainland) {
        if (!this.idCardType) {
          this.showError = true
          this.errorMessage = '请输入证件类型'
          setTimeout(() => {
            this.showError = false
          }, 2000)
          return
        }
        
        if (this.images.length === 0) {
          this.showError = true
          this.errorMessage = '请上传证件照片'
          setTimeout(() => {
            this.showError = false
          }, 2000)
          return
        }
      }
      
      // 构建请求参数
      const params = {
        isMainland: this.isMainland,
        realName: this.realName,
        idCardNumber: this.idCardNumber,
        idCardType: this.isMainland ? '' : this.idCardType,
        images: this.isMainland ? [] : this.images
      }
      
      // 提交认证
      weui.loading('提交中...')
      
      axios.post('/api/authentication/update', params)
        .then(response => {
          weui.hideLoading()
          if (response.data.code === 0) {
            weui.toast('认证成功', {
              duration: 2000,
              callback: () => {
                this.$router.push('/authentication')
              }
            })
          } else {
            weui.alert(response.data.message)
          }
        })
        .catch(error => {
          weui.hideLoading()
          weui.alert('认证失败，请稍后重试')
          console.error('认证失败:', error)
        })
    }
  }
}
</script>

<style scoped>
/* 保留原JSP页面的样式 */
.weui-header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background-color: #1AAD19;
  color: #FFFFFF;
  z-index: 1000;
  height: 44px;
  line-height: 44px;
  text-align: center;
  font-size: 18px;
}

.weui-header-left,
.weui-header-right {
  position: absolute;
  top: 0;
  width: 44px;
  height: 44px;
}

.weui-header-left {
  left: 0;
}

.weui-header-right {
  right: 0;
}

.weui-header-title {
  margin: 0 44px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.weui-content {
  margin-top: 44px;
  padding: 15px;
}

.weui-form {
  margin-bottom: 20px;
}

.weui-cell {
  padding: 10px 0;
  border-bottom: 1px solid #E5E5E5;
}

.weui-cell__hd {
  width: 60px;
}

.weui-label {
  display: block;
  width: 100%;
  color: #333;
}

.weui-input {
  width: 100%;
  border: none;
  outline: none;
  font-size: 14px;
  color: #333;
}

.weui-cells {
  margin-top: 0;
}

.weui-cells_checkbox {
  background-color: #FFFFFF;
}

.weui-cell {
  position: relative;
  padding: 10px 15px;
  display: flex;
  align-items: center;
}

.weui-check {
  position: absolute;
  left: -9999px;
}

.weui-icon-checked {
  display: inline-block;
  width: 20px;
  height: 20px;
  background-color: #FFFFFF;
  border: 1px solid #D9D9D9;
  border-radius: 50%;
  margin-right: 10px;
}

.weui-check:checked + .weui-icon-checked {
  background-color: #1AAD19;
  border-color: #1AAD19;
  background-image: url("data:image/svg+xml;charset=utf-8,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 13 10'%3E%3Cpolygon fill='%23FFFFFF' points='11.6,0.7 5.8,6.5 1.4,2.1 0,3.5 5.8,9.3 13,2.1 '/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: center;
  background-size: 13px 10px;
}

.weui-uploader {
  margin-top: 20px;
}

.weui-uploader__hd {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.weui-uploader__title {
  font-size: 14px;
  color: #333;
}

.weui-uploader__info {
  font-size: 14px;
  color: #999;
}

.weui-uploader__files {
  display: flex;
  flex-wrap: wrap;
  margin: 0;
  padding: 0;
  list-style: none;
}

.weui-uploader__file {
  position: relative;
  width: 80px;
  height: 80px;
  margin-right: 10px;
  margin-bottom: 10px;
  background-size: cover;
  background-position: center;
}

.weui-uploader__file-delete {
  position: absolute;
  top: -5px;
  right: -5px;
  width: 20px;
  height: 20px;
  background-color: rgba(255, 0, 0, 0.8);
  border-radius: 50%;
  color: #FFFFFF;
  text-align: center;
  line-height: 20px;
  font-size: 14px;
}

.weui-uploader__input-box {
  position: relative;
  width: 80px;
  height: 80px;
  border: 1px dashed #D9D9D9;
  border-radius: 4px;
  overflow: hidden;
}

.weui-uploader__input {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0;
}

.weui-btn-area {
  margin: 20px auto;
  width: 90%;
}

.weui-btn {
  display: block;
  width: 100%;
  height: 44px;
  line-height: 44px;
  text-align: center;
  border-radius: 22px;
  text-decoration: none;
  font-size: 16px;
  background-color: #1AAD19;
  color: #FFFFFF;
  border: none;
  outline: none;
}

.weui_warn {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  padding: 15px;
  background-color: rgba(0, 0, 0, 0.7);
  color: #FFFFFF;
  border-radius: 4px;
  z-index: 1001;
  display: flex;
  align-items: center;
}

.weui-icon-warn {
  margin-right: 10px;
}
</style>