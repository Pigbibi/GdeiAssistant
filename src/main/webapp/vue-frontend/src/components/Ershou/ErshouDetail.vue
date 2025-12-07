<template>
  <div class="ershouDetail">
    <div class="header">
      <div class="tit">商品详情</div>
      <div class="op">
        <a href="javascript:;" @click="goBack"><i class="i iback"></i></a>
      </div>
    </div>
    <div class="main">
      <div class="hd">
        <h4 class="tit">{{ ershouInfo?.ershouItem?.name }}</h4>
        <p class="price">￥{{ ershouInfo?.ershouItem?.price }}</p>
        <p class="site"><i class="i isite"></i>{{ ershouInfo?.ershouItem?.location }}</p>
        <p class="time">{{ ershouInfo?.ershouItem?.publishTime }}</p>
      </div>
      <div class="content">
        <div class="picture">
          <div class="images">
            <ul id="imageBox">
              <li v-for="(image, index) in imageUrls" :key="index">
                <img :src="image" @load="checkimg">
              </li>
            </ul>
          </div>
        </div>
        <div class="des">
          <p class="tit">商品描述</p>
          <div class="ct">
            {{ ershouInfo?.ershouItem?.description }}
          </div>
        </div>
        <div class="contact">
          <p class="tit">联系方式</p>
          <div class="ct">
            <i class="i iphone"></i>
            <span>{{ ershouInfo?.ershouItem?.phone }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'ErshouDetail',
  data() {
    return {
      ershouInfo: null,
      imageUrls: []
    }
  },
  mounted() {
    this.initPage()
    this.loadErshouDetail()
    this.initSlider()
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
      document.body.style.minHeight = window.innerHeight + 'px'
    },
    loadErshouDetail() {
      const id = this.$route.params.id
      axios.get(`/api/ershou/item/id/${id}/detail`)
        .then(response => {
          if (response.data.success === true) {
            this.ershouInfo = response.data.data
            this.loadImages()
          } else {
            this.showErrorTip(response.data.message)
          }
        })
        .catch(error => {
          this.showErrorTip("获取商品详情失败")
        })
    },
    loadImages() {
      const id = this.$route.params.id
      // 这里需要根据后端API获取所有图片
      // 假设后端有获取所有图片的API
      axios.get(`/api/ershou/item/id/${id}/images`)
        .then(response => {
          if (response.data.success === true) {
            this.imageUrls = response.data.data
          }
        })
    },
    initSlider() {
      // 初始化图片轮播功能
      // 由于原JSP中使用了yxMobileSlider.js，这里需要用Vue的方式实现类似功能
      // 或者使用第三方轮播组件
    },
    showErrorTip(message) {
      const warnElement = document.querySelector('.weui_warn')
      if (warnElement) {
        warnElement.textContent = message
        warnElement.style.display = 'block'
        setTimeout(() => {
          warnElement.style.display = 'none'
        }, 2000)
      }
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