<template>
  <div class="panel-overlay"></div>
  <div class="warp">
    <div class="head" @click="goToIndex">
      <div class="logo"><span class="t">卖室友</span></div>
    </div>

    <div class="conterInfo">
      <div class="releaseBox">
        <div class="releaseName">发布信息
          <div class="yuan" style="top: -5.5px; left: -5.5px;"></div>
          <div class="yuan" style="top: -5.5px; right: -5.5px;"></div>
          <div class="yuan" style="top: -5.5px; left: 50%; margin-left: -5.5px;"></div>
        </div>

        <form>
          <div class="Photo" id="imgshow">
            <img :src="showImageUrl" id="showimg" v-if="showImageUrl"/>
            <div class="uploading" style="top:11rem; width:100%;position:absolute;">
              <input type="button" class="circleBtn" value="上传"/>
              <input name="image" id="image" @change="handleImageUpload" accept="image/*"
                     style="opacity:0; height: 5rem;position: absolute;top: 0rem; width: 100%;" type="file" class="upload"/>
            </div>
          </div>

          <div class="formInput">
            <p><input type="text" class="inputBox" id="nickname" v-model="nickname" placeholder="请输入你的昵称"/></p>
            <p>
              <input type="hidden" id="grade_value" v-model="gradeValue"/>
              <input type="text" readonly class="inputBox" id="grade" v-model="grade" placeholder="请选择你的年级" @click="showGradeSelect"/>
            </p>
            <p>
              <input type="hidden" id="area_value" v-model="areaValue"/>
              <input type="text" readonly class="inputBox" id="area" v-model="area" placeholder="请选择你的性别" @click="showAreaSelect"/>
            </p>
            <p><input type="text" class="inputBox" id="faculty" v-model="faculty" placeholder="请输入你的专业"/></p>
            <p><input type="text" class="inputBox" id="hometown" v-model="hometown" placeholder="请输入你的家乡"/></p>
            <p><input type="text" class="inputBox" id="qq" v-model="qq" placeholder="请输入你的QQ"/></p>
            <p><input type="text" class="inputBox" id="wechat" v-model="wechat" placeholder="请输入你的微信"/></p>
          </div>
          <div style="text-align: center;margin-bottom: 10px;margin-left: 15px;margin-right: 15px">
            <span>在接受撩一下请求前，QQ和微信不会公开显示</span><br>
            <span style="color:red">请勿违规盗用他人照片或冒充他人，欢迎举报监督</span>
          </div>
          <div class="textBox">
            <textarea class="textarea" id="content" v-model="content" placeholder="什么样的TA会让你心动呢？谈谈你的理想对象，不超过100字"></textarea>
            <input type="button" class="circleBtn" :disabled="isSubmitting" value="发布" @click="publishDating"/>
            <div class="yuan" style="top: -5.5px; left: -5.5px;"></div>
            <div class="yuan" style="top: -5.5px; right: -5.5px;"></div>
          </div>
        </form>
      </div>
    </div>
  </div>

  <!-- 错误提示 -->
  <div class="weui-toptips weui_warn" v-if="showError" ref="errorTip">{{ errorMessage }}</div>
</template>

<script>
import axios from 'axios'
import weui from 'weui'

export default {
  name: 'DatingPublish',
  data() {
    return {
      imageBase64: '',
      showImageUrl: '',
      nickname: '',
      gradeValue: '',
      grade: '',
      areaValue: '',
      area: '',
      faculty: '',
      hometown: '',
      qq: '',
      wechat: '',
      content: '',
      isSubmitting: false,
      showError: false,
      errorMessage: ''
    }
  },
  mounted() {

  },
  methods: {
    // 处理图片上传
    handleImageUpload(event) {
      const files = event.target.files
      
      // 如果没有选中文件，直接返回
      if (files.length === 0) {
        return
      }
      
      const file = files[0]
      
      // 允许上传的图片类型
      const allowTypes = ['image/jpg', 'image/jpeg', 'image/png', 'image/gif']
      
      // 图片最大大小为5MB
      const maxSize = 1024 * 1024 * 5
      
      // 图片最大宽度
      const maxWidth = 720
      
      // 如果类型不在允许的类型范围内
      if (allowTypes.indexOf(file.type) === -1) {
        this.showErrorMsg("不合法的图片文件类型")
        return
      }
      
      if (file.size > maxSize) {
        this.showErrorMsg("图片文件不能超过5MB")
      } else {
        const reader = new FileReader()
        reader.readAsDataURL(file)
        reader.onload = (e) => {
          const img = new Image()
          img.src = e.target.result
          img.onload = () => {
            // 不要超出最大宽度
            const w = Math.min(maxWidth, img.width)
            // 高度按比例计算
            const h = img.height * (w / img.width)
            const canvas = document.createElement('canvas')
            const ctx = canvas.getContext('2d')
            // 设置 canvas 的宽度和高度
            canvas.width = w
            canvas.height = h
            ctx.drawImage(img, 0, 0, w, h)
            // 返回一个包含图片展示的 Data URI
            this.imageBase64 = canvas.toDataURL('image/jpeg', 0.8)
            // 显示图片预览图
            this.showImageUrl = this.imageBase64
          }
        }
      }
    },
    
    // 显示错误消息
    showErrorMsg(message) {
      this.errorMessage = message
      this.showError = true
      setTimeout(() => {
        this.showError = false
      }, 2000)
    },
    
    // 发布卖室友信息
    publishDating() {
      if (!this.imageBase64) {
        this.showErrorMsg("上传一张美美的照片吧")
      } else if (this.nickname.length === 0 || this.nickname.length > 15) {
        this.showErrorMsg("昵称长度不合法")
      } else if (this.gradeValue === '') {
        this.showErrorMsg("年级未选择")
      } else if (this.areaValue === '') {
        this.showErrorMsg("性别未选择")
      } else if (this.faculty.length === 0 || this.faculty.length > 12) {
        this.showErrorMsg("专业长度不合法")
      } else if (this.hometown.length === 0 || this.hometown.length > 10) {
        this.showErrorMsg("家乡长度不合法")
      } else if (this.qq.length === 0 && this.wechat.length === 0) {
        this.showErrorMsg("QQ号码和微信至少填写一个")
      } else if (this.qq.length > 15 || this.wechat.length > 20) {
        this.showErrorMsg("联系方式长度不合法")
      } else if (this.content.length === 0) {
        this.showErrorMsg("填一下你心目中的那个TA吧")
      } else if (this.content.length > 100) {
        this.showErrorMsg("心动条件长度不合法")
      } else {
        // 转换base64为blob
        const byteString = atob(this.imageBase64.split(',')[1])
        const mimeType = this.imageBase64.split(',')[0].split(':')[1].split(';')[0]
        const ab = new ArrayBuffer(byteString.length)
        const ia = new Uint8Array(ab)
        for (let j = 0; j < byteString.length; j++) {
          ia[j] = byteString.charCodeAt(j)
        }
        const blob = new Blob([ab], { type: mimeType })
        
        // 创建表单数据
        const formData = new FormData()
        formData.append('image', blob)
        formData.append('nickname', this.nickname)
        formData.append('grade', this.gradeValue)
        formData.append('area', this.areaValue)
        formData.append('faculty', this.faculty)
        formData.append('hometown', this.hometown)
        formData.append('content', this.content)
        if (this.qq) {
          formData.append('qq', this.qq)
        }
        if (this.wechat) {
          formData.append('wechat', this.wechat)
        }
        
        // 设置提交状态
        this.isSubmitting = true
        
        // 显示等待动画
        const loading = weui.loading('提交中')
        
        // 提交数据
        axios.post('/api/dating/profile', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        .then(response => {
          loading.hide()
          if (response.data.success) {
            this.$router.push('/dating')
          } else {
            this.isSubmitting = false
            this.showErrorMsg(response.data.message)
          }
        })
        .catch(error => {
          this.isSubmitting = false
          loading.hide()
          this.showErrorMsg("网络异常，请检查网络连接")
        })
      }
    },
    
    // 显示性别选择框
    showAreaSelect() {
      const areaPicker = [
        { label: '小姐姐', value: 0 },
        { label: '小哥哥', value: 1 }
      ]
      
      weui.picker(areaPicker, {
        defaultValue: ['1'],
        onConfirm: (area) => {
          this.areaValue = area[0].value
          this.area = area[0].label
        }
      })
    },
    
    // 显示年级选择框
    showGradeSelect() {
      const gradePicker = [
        { label: '大一', value: 1 },
        { label: '大二', value: 2 },
        { label: '大三', value: 3 },
        { label: '大四', value: 4 }
      ]
      
      weui.picker(gradePicker, {
        defaultValue: ['1'],
        onConfirm: (grade) => {
          this.gradeValue = grade[0].value
          this.grade = grade[0].label
        }
      })
    },
    
    // 跳转到首页
    goToIndex() {
      this.$router.push('/dating')
    }
  }
}
</script>

<style scoped>
/* 引入外部CSS文件 */
@import '/css/dating/global.css';
@import '/css/dating/layout.css';

/* 错误提示样式 */
.weui-toptips {
  position: fixed;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  z-index: 9999;
  padding: 10px;
  background-color: #f43530;
  color: #fff;
  border-radius: 0 0 4px 4px;
  display: none;
}

.weui-toptips.show {
  display: block;
}
</style>