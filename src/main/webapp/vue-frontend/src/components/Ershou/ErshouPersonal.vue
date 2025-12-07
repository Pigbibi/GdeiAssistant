<template>
  <div class="ershouPersonal">
    <div class="header">
      <div class="tit">我的二手交易</div>
      <div class="op">
        <a href="javascript:;" @click="goBack"><i class="i iback"></i></a>
      </div>
    </div>
    <div class="main">
      <div class="tabs">
        <div class="tab" :class="{ active: activeTab === 'available' }" @click="switchTab('available')">
          待出售
        </div>
        <div class="tab" :class="{ active: activeTab === 'solded' }" @click="switchTab('solded')">
          已出售
        </div>
        <div class="tab" :class="{ active: activeTab === 'notAvailable' }" @click="switchTab('notAvailable')">
          已下架
        </div>
      </div>
      <div class="content">
        <div v-if="activeTab === 'available'" class="lis">
          <div v-for="item in personalItems.available" :key="item.id" class="li">
            <a href="javascript:;" @click="goToDetail(item.id)">
              <i class="img">
                <img :id="item.id" @load="checkimg" :src="getPreviewPictureUrl(item.id)">
              </i>
              <h5 class="tit">{{ item.name }}</h5>
              <p class="site"><i class="i isite"></i>{{ item.location }}</p>
              <em class="price">￥{{ item.price }}</em>
              <div class="actions">
                <button class="btn btn-small btn-edit" @click.stop="goToEdit(item.id)">编辑</button>
                <button class="btn btn-small btn-sold" @click.stop="markAsSold(item.id)">已出售</button>
                <button class="btn btn-small btn-delete" @click.stop="removeItem(item.id)">下架</button>
              </div>
            </a>
          </div>
        </div>
        <div v-else-if="activeTab === 'solded'" class="lis">
          <div v-for="item in personalItems.solded" :key="item.id" class="li">
            <a href="javascript:;" @click="goToDetail(item.id)">
              <i class="img">
                <img :id="item.id" @load="checkimg" :src="getPreviewPictureUrl(item.id)">
              </i>
              <h5 class="tit">{{ item.name }}</h5>
              <p class="site"><i class="i isite"></i>{{ item.location }}</p>
              <em class="price">￥{{ item.price }}</em>
              <div class="state">已出售</div>
            </a>
          </div>
        </div>
        <div v-else-if="activeTab === 'notAvailable'" class="lis">
          <div v-for="item in personalItems.notAvailable" :key="item.id" class="li">
            <a href="javascript:;" @click="goToDetail(item.id)">
              <i class="img">
                <img :id="item.id" @load="checkimg" :src="getPreviewPictureUrl(item.id)">
              </i>
              <h5 class="tit">{{ item.name }}</h5>
              <p class="site"><i class="i isite"></i>{{ item.location }}</p>
              <em class="price">￥{{ item.price }}</em>
              <div class="state">已下架</div>
            </a>
          </div>
        </div>
      </div>
    </div>
    <div class="weui_warn"></div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'ErshouPersonal',
  data() {
    return {
      activeTab: 'available',
      personalItems: {
        available: [],
        solded: [],
        notAvailable: []
      },
      previewImages: {}
    }
  },
  mounted() {
    this.loadPersonalItems()
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
      document.body.style.minHeight = window.innerHeight + 'px'
    },
    loadPersonalItems() {
      axios.get('/api/ershou/personal/items')
        .then(response => {
          if (response.data.success === true) {
            this.personalItems = response.data.data
          } else {
            this.showErrorTip(response.data.message)
          }
        })
        .catch(error => {
          this.showErrorTip("获取个人二手交易信息失败")
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
    switchTab(tab) {
      this.activeTab = tab
    },
    markAsSold(id) {
      if (confirm('确定将该商品标记为已出售吗？')) {
        axios.post(`/api/ershou/item/state/id/${id}`, null, {
          params: { state: 2 }
        })
        .then(response => {
          if (response.data.success === true) {
            this.showErrorTip('标记成功')
            this.loadPersonalItems()
          } else {
            this.showErrorTip(response.data.message)
          }
        })
        .catch(error => {
          this.showErrorTip('操作失败')
        })
      }
    },
    removeItem(id) {
      if (confirm('确定下架该商品吗？')) {
        axios.post(`/api/ershou/item/state/id/${id}`, null, {
          params: { state: 0 }
        })
        .then(response => {
          if (response.data.success === true) {
            this.showErrorTip('下架成功')
            this.loadPersonalItems()
          } else {
            this.showErrorTip(response.data.message)
          }
        })
        .catch(error => {
          this.showErrorTip('操作失败')
        })
      }
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
    },
    goToEdit(id) {
      this.$router.push(`/ershou/edit/${id}`)
    }
  }
}
</script>

<style scoped>
/* 这里需要添加原JSP文件中对应的CSS样式 */
</style>