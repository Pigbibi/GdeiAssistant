<template>
  <div class="ershouEdit">
    <div class="header">
      <div class="tit">编辑二手交易</div>
      <div class="op">
        <a href="javascript:;" @click="goBack"><i class="i iback"></i></a>
      </div>
    </div>
    <div class="main">
      <form id="editForm" enctype="multipart/form-data">
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
        <!-- 图片编辑部分可能需要特殊处理，这里暂时保留基本结构 -->
        <div class="form-group btn">
          <button type="button" class="submit" @click="submitForm">保存修改</button>
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
  name: 'ErshouEdit',
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
    this.loadItemDetail()
  },
  methods: {
    loadItemDetail() {
      const id = this.$route.params.id
      axios.get(`/api/ershou/item/id/${id}/detail`)
        .then(response => {
          if (response.data.success === true) {
            const item = response.data.data.ershouItem
            this.formData = {
              name: item.name,
              price: item.price,
              location: item.location,
              phone: item.phone,
              description: item.description,
              type: item.type
            }
          } else {
            this.showErrorTip(response.data.message)
          }
        })
        .catch(error => {
          this.showErrorTip('获取商品详情失败')
        })
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
      return true
    },
    submitForm() {
      if (!this.validateForm()) {
        return
      }
      const id = this.$route.params.id
      axios.post(`/api/ershou/item/id/${id}`, this.formData)
        .then(response => {
          if (response.data.success === true) {
            this.showErrorTip('修改成功')
            setTimeout(() => {
              this.$router.push('/ershou/personal')
            }, 1500)
          } else {
            this.showErrorTip(response.data.message)
          }
        })
        .catch(error => {
          this.showErrorTip('修改失败，请检查网络连接')
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