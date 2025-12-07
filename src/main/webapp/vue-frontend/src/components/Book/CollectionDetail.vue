<template>
  <div class="weui-cells__title">馆藏图书详细信息</div>
  
  <div class="weui-cells" v-if="collectionDetail">
    <div class="weui-cell">
      <div class="weui-cell__hd"><label class="weui-label">书名</label></div>
      <div class="weui-cell__bd">
        <span>{{ collectionDetail.bookname }}</span>
      </div>
    </div>
    <div class="weui-cell">
      <div class="weui-cell__hd"><label class="weui-label">作者</label></div>
      <div class="weui-cell__bd">
        <span>{{ collectionDetail.author }}</span>
      </div>
    </div>
    <div class="weui-cell">
      <div class="weui-cell__hd"><label class="weui-label">题名/责任者</label></div>
      <div class="weui-cell__bd">
        <span>{{ collectionDetail.principal }}</span>
      </div>
    </div>
    <div class="weui-cell">
      <div class="weui-cell__hd"><label class="weui-label">出版社/出版年</label></div>
      <div class="weui-cell__bd">
        <span>{{ collectionDetail.publishingHouse }}</span>
      </div>
    </div>
    <div class="weui-cell">
      <div class="weui-cell__hd"><label class="weui-label">ISBN/定价</label></div>
      <div class="weui-cell__bd">
        <span>{{ collectionDetail.price }}</span>
      </div>
    </div>
    <div class="weui-cell">
      <div class="weui-cell__hd"><label class="weui-label">载体形态项</label></div>
      <div class="weui-cell__bd">
        <span>{{ collectionDetail.physicalDescriptionArea }}</span>
      </div>
    </div>
    <div class="weui-cell">
      <div class="weui-cell__hd"><label class="weui-label">个人责任者</label></div>
      <div class="weui-cell__bd">
        <span>{{ collectionDetail.personalPrincipal }}</span>
      </div>
    </div>
    <div class="weui-cell">
      <div class="weui-cell__hd"><label class="weui-label">学科主题</label></div>
      <div class="weui-cell__bd">
        <span>{{ collectionDetail.subjectTheme }}</span>
      </div>
    </div>
    <div class="weui-cell">
      <div class="weui-cell__hd"><label class="weui-label">中图法分类号</label></div>
      <div class="weui-cell__bd">
        <span>{{ collectionDetail.chineseLibraryClassification }}</span>
      </div>
    </div>
  </div>
  
  <div class="weui-cells__title">馆藏分布</div>
  <div class="weui-cells" v-if="collectionDetail && collectionDetail.collectionDistributionList">
    <div v-for="(item, index) in collectionDetail.collectionDistributionList" :key="index" class="weui-cell">
      <div class="weui-cell__bd">
        <p><strong>{{ item.location }}</strong></p>
        <p>索取号：{{ item.callNumber }}</p>
        <p>条码号：{{ item.barcode }}</p>
        <p>状态：{{ item.state }}</p>
      </div>
    </div>
  </div>
  
  <div class="weui-msg" v-if="!collectionDetail">
    <div class="weui-msg__icon-area"><i class="weui-icon-warn weui-icon_msg"></i></div>
    <div class="weui-msg__text-area">
      <h2 class="weui-msg__title">查询详细信息失败</h2>
      <p class="weui-msg__desc">{{ queryErrorMessage }}</p>
    </div>
  </div>
  
  <div class="weui-btn-area">
    <button class="weui-btn weui-btn_default" type="button" @click="goBack">返回</button>
  </div>
  
  <div id="loadingToast" class="weui-toast">
    <i class="weui-loading weui-icon_toast"></i>
    <p class="weui-toast__content">数据加载中</p>
  </div>
  <div class="weui_warn" id="warn"></div>
</template>

<script>
import axios from 'axios'
import weui from 'weui.js'

export default {
  data () {
    return {
      collectionDetail: null,
      queryErrorMessage: '',
      loading: false
    }
  },
  mounted () {
    // 从路由参数获取详情URL
    const detailURL = this.$route.query.detailURL
    if (detailURL) {
      this.getDetailInfo(detailURL)
    } else {
      this.queryErrorMessage = '未找到详细信息URL'
    }
  },
  methods: {
    // 获取详细信息
    getDetailInfo (detailURL) {
      this.loading = true
      weui.loading('加载中', 0)
      axios.get('/api/collection/detail', {
        params: {
          url: detailURL
        }
      }).then(response => {
        this.loading = false
        weui.hideLoading()
        if (response.data) {
          // 成功获取数据
          this.collectionDetail = response.data
        } else {
          this.queryErrorMessage = '获取图书详细信息失败'
          weui.topTips('获取图书详细信息失败', 3000)
        }
      }).catch(error => {
        this.loading = false
        weui.hideLoading()
        this.queryErrorMessage = '获取图书详细信息失败'
        weui.topTips('获取图书详细信息失败', 3000)
        console.error('Error fetching collection detail:', error)
      })
    },
    // 返回上一页
    goBack () {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
/* 保持原有CSS样式不变 */
</style>