<template>
  <div class="ershouPublish">
    <div class="header">
      <div class="tit">发布二手交易</div>
      <div class="op">
        <a href="javascript:;" @click="goBack"><i class="i iback"></i></a>
      </div>
    </div>
    <div class="main">
      <form id="publishForm" enctype="multipart/form-data">
        <div class="form-group">
          <label class="lab">商品名称</label>
          <input type="text" name="name" placeholder="请输入商品名称" v-model="formData.name">
        </div>
        <div class="form-group">
          <label class="lab">商品价格</label>
          <input type="number" name="price" placeholder="请输入商品价格" v-model.number="formData.price">
        </div>
        <div class="form-group">
          <label class="lab">交易地点</label>
          <input type="text" name="location" placeholder="请输入交易地点" v-model="formData.location">
        </div>
        <div class="form-group">
          <label class="lab">联系方式</label>
          <input type="tel" name="phone" placeholder="请输入联系方式" v-model="formData.phone">
        </div>
        <div class="form-group">
          <label class="lab">商品分类</label>
          <div class="sel" id="selectType" @click="showTypeModal">
            <span class="txt">{{ selectedTypeName }}</span>
            <i class="i arrowdown"></i>
          </div>
        </div>
        <div class="form-group">
          <label class="lab">商品描述</label>
          <textarea name="description" placeholder="请输入商品描述" v-model="formData.description"></textarea>
        </div>
        <div class="form-group picture">
          <label class="lab">商品图片</label>
          <div class="images">
            <div class="img-item" v-for="(image, index) in images" :key="index">
              <img v-if="image" :src="image" @load="checkimg">
              <input type="file" :name="'image' + (index + 1)" accept="image/*" @change="uploadImage($event, index)">
            </div>
          </div>
        </div>
        <div class="form-group btn">
          <button type="button" class="submit" @click="submitForm">发布</button>
        </div>
      </form>
    </div>
    <!-- 分类选择弹窗 -->
    <div class="sky" v-if="showType">
      <div class="mark"></div>
      <div class="typemw mw">
        <div class="tit">商品分类</div>
        <div class="typelist">
          <ul>
            <li v-for="type in types" :key="type.value" @click="selectType(type)">
              {{ type.name }}
            </li>
          </ul>
        </div>
        <div class="mwclose" @click="hideTypeModal">关闭</div>
      </div>
    </div>
    <div class="weui_warn"></div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'ErshouPublish',
  data() {
    return {
      formData: {
        name: '',
        price: 0,
        location: '',
        phone: '',
        description: '',
        type: 10 // 默认分类为其他
      },
      images: [null, null, null, null],
      imagesCount: 0,
      showType: false,
      types: [
        { value: 1, name: '书籍' },
        { value: 2, name: '电子产品' },
        { value: 3, name: '生活用品' },
        { value: 4, name: '服饰' },
        { value: 5, name: '运动器材' },
        { value: 6, name: '美妆' },
        { value: 7, name: '文具' },
        { value: 8, name: '乐器' },
        { value: 9, name: '自行车' },
        { value: 10, name: '其他' }
      ]
    }
  },
  computed: {
    selectedTypeName() {
      const type = this.types.find(t => t.value === this.formData.type)
      return type ? type.name : '其他'
    }
  },
  mounted() {
    this.initPage()
  },
  methods: {
    checkimg(event) {
      const obj = event.target
      if (obj.width > obj.height) {
        obj.style.width = 'auto'
        obj.style.height = '100%'
      }
    },
    initPage() {
      const ua = window.navigator.userAgent
      if (!(/\(i[^;]+;( U;)? CPU.+Mac OS X/).test(ua) && ua.indexOf('Android') === -1 && ua.indexOf('Linux') === -1) {
        document.body.classList.add('isPC')
      }
      if (window.innerWidth < 375) {
        document.querySelector('.picture .images').classList.add('forp4')
      }
    },
    uploadImage(event, index) {
      const file = event.target.files[0]
      if (file) {
        const reader = new FileReader()
        reader.onload = (e) => {
          this.images[index] = e.target.result
          this.imagesCount++
        }
        reader.readAsDataURL(file)
      }
    },
    showTypeModal() {
      this.showType = true
      document.body.style.overflow = 'hidden'
    },
    hideTypeModal() {
      this.showType = false
      document.body.style.overflow = 'auto'
    },
    selectType(type) {
      this.formData.type = type.value
      this.hideTypeModal()
    },
    validateForm() {
      if (!this.formData.name.trim()) {
        this.showErrorTip('请输入商品名称')
        return false
      }
      if (this.formData.price <= 0 || this.formData.price > 9999.99) {
        this.showErrorTip('商品价格不合法')
        return false
      }
      if (!this.formData.location.trim()) {
        this.showErrorTip('请输入交易地点')
        return false
      }
      if (!this.formData.phone.trim()) {
        this.showErrorTip('请输入联系方式')
        return false
      }
      if (!this.formData.description.trim()) {
        this.showErrorTip('请输入商品描述')
        return false
      }
      if (this.imagesCount < 1) {
        this.showErrorTip('请至少选择一张图片')
        return false
      }
      return true
    },
    submitForm() {
      if (!this.validateForm()) {
        return
      }
      const formData = new FormData()
      formData.append('name', this.formData.name)
      formData.append('price', this.formData.price)
      formData.append('location', this.formData.location)
      formData.append('phone', this.formData.phone)
      formData.append('type', this.formData.type)
      formData.append('description', this.formData.description)
      
      // 添加图片文件
      this.images.forEach((image, index) => {
        if (image) {
          const fileInput = document.querySelector(`input[name='image${index + 1}']`)
          if (fileInput.files.length > 0) {
            formData.append(`image${index + 1}`, fileInput.files[0])
          }
        }
      })
      
      axios.post('/api/ershou/item', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
      .then(response => {
        if (response.data.success === true) {
          this.showErrorTip('发布成功')
          setTimeout(() => {
            this.$router.push('/ershou')
          }, 1500)
        } else {
          this.showErrorTip(response.data.message)
        }
      })
      .catch(error => {
        this.showErrorTip('发布失败，请检查网络连接')
      })
    },
    showErrorTip(message) {
      const warnElement = document.querySelector('.weui_warn')
      warnElement.textContent = message
      warnElement.style.display = 'block'
      setTimeout(() => {
        warnElement.style.display = 'none'
      }, 2000)
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
/* 这里需要添加原JSP文件中对应的CSS样式 */
</style>