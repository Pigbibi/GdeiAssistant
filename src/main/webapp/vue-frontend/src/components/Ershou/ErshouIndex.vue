<template>
  <div class="ershouIndex">
    <div class="header">
      <div class="tit">二手交易</div>
      <div class="op">
        <a href="javascript:;" @click="goToSearch"><i class="i isearch"></i></a>
        <a href="javascript:;" @click="goToPublish"><i class="i ipublish"></i></a>
      </div>
    </div>
    <div class="type">
      <ul>
        <li @click="goToType(0)"><i class="i itype0"></i><span>全部</span></li>
        <li @click="goToType(1)"><i class="i itype1"></i><span>书籍</span></li>
        <li @click="goToType(2)"><i class="i itype2"></i><span>电子产品</span></li>
        <li @click="goToType(3)"><i class="i itype3"></i><span>生活用品</span></li>
        <li @click="goToType(4)"><i class="i itype4"></i><span>服饰</span></li>
        <li @click="goToType(5)"><i class="i itype5"></i><span>运动器材</span></li>
        <li @click="goToType(6)"><i class="i itype6"></i><span>美妆</span></li>
        <li @click="goToType(7)"><i class="i itype7"></i><span>文具</span></li>
        <li @click="goToType(8)"><i class="i itype8"></i><span>乐器</span></li>
        <li @click="goToType(9)"><i class="i itype9"></i><span>自行车</span></li>
        <li @click="goToType(10)"><i class="i itype10"></i><span>其他</span></li>
        <li @click="goToPersonal"><i class="i imy"></i><span>我的</span></li>
      </ul>
    </div>
    <div class="main">
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
  name: 'ErshouIndex',
  data() {
    return {
      ershouItems: [],
      first: 10,
      previewImages: {}
    }
  },
  mounted() {
    this.loadErshouInfo(0)
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
    loadErshouInfo(start) {
      axios.get(`/api/ershou/item/start/${start}`)
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
    goToSearch() {
      this.$router.push('/ershou/search')
    },
    goToPublish() {
      this.$router.push('/ershou/publish')
    },
    goToType(type) {
      this.$router.push(`/ershou/type/${type}`)
    },
    goToPersonal() {
      this.$router.push('/ershou/personal')
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