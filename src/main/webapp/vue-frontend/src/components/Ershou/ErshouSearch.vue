<template>
  <div class="ershouSearch">
    <div class="header">
      <div class="tit">二手交易搜索</div>
      <div class="op">
        <a href="javascript:;" @click="goBack"><i class="i iback"></i></a>
      </div>
    </div>
    <div class="main">
      <div class="search">
        <input type="text" id="keyword" placeholder="搜索二手商品" v-model="keyword">
        <button class="search-btn" @click="searchErshou">搜索</button>
      </div>
      <div class="lis">
        <div v-for="item in ershouItems" :key="item.id" class="li">
          <a href="javascript:;" @click="goToDetail(item.id)">
            <i class="img">
              <img :id="item.id" @load="checkimg" :src="getPreviewPictureUrl(item.id)">
            </i>
            <h5 class="tit">{{ item.name }}</h5>
            <p class="site"><i class="i isite"></i>{{ item.location }}</p>
            <em class="price">￥{{ item.price }}</em>
          </a>
        </div>
      </div>
    </div>
    <div class="weui_warn"></div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'ErshouSearch',
  data() {
    return {
      ershouItems: [],
      keyword: '',
      first: 10,
      previewImages: {}
    }
  },
  mounted() {
    this.initPage()
    this.bindScrollEvent()
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
    bindScrollEvent() {
      window.addEventListener('scroll', () => {
        if (window.scrollY + window.innerHeight >= document.body.offsetHeight - 40 && this.ershouItems.length === this.first) {
          this.loadErshouInfo(this.ershouItems.length)
        }
      })
    },
    searchErshou() {
      if (!this.keyword.trim()) {
        this.showErrorTip("请输入搜索关键词")
        return
      }
      // 重置数据
      this.ershouItems = []
      this.first = 10
      this.previewImages = {}
      // 开始搜索
      this.loadErshouInfo(0)
    },
    loadErshouInfo(start) {
      axios.get(`/api/ershou/keyword/${encodeURIComponent(this.keyword)}/start/${start}`)
        .then(response => {
          if (response.data.success === true) {
            if (start === 0) {
              this.ershouItems = response.data.data
            } else {
              this.ershouItems = this.ershouItems.concat(response.data.data)
            }
            this.first = this.first + 10
          } else {
            this.showErrorTip(response.data.message)
          }
        })
        .catch(error => {
          this.showErrorTip("网络异常，请检查网络连接")
        })
    },
    getPreviewPictureUrl(id) {
      if (!this.previewImages[id]) {
        axios.get(`/api/ershou/item/id/${id}/preview`)
          .then(response => {
            if (response.data.success === true) {
              this.$set(this.previewImages, id, response.data.data)
            }
          })
        return ''
      }
      return this.previewImages[id]
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
    },
    goToDetail(id) {
      this.$router.push(`/ershou/detail/${id}`)
    }
  },
  beforeDestroy() {
    window.removeEventListener('scroll', this.bindScrollEvent)
  }
}
</script>

<style scoped>
/* 这里需要添加原JSP文件中对应的CSS样式 */
</style>