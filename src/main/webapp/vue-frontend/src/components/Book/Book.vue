<template>
  <div class="weui-cells__title">图书借阅信息查询</div>
  <div class="weui-cells weui-cells_form">
    <div class="weui-cell">
      <div class="weui-cell__hd"><label class="weui-label">查询密码</label></div>
      <div class="weui-cell__bd">
        <input id="password" class="weui-input" type="password" v-model="password" placeholder="请输入查询密码" />
      </div>
    </div>
  </div>
  <div class="weui-btn-area">
    <button class="weui-btn weui-btn_primary" type="button" @click="postQueryForm">查询图书借阅信息</button>
  </div>
  
  <!-- 初始提示信息 -->
  <div v-if="showInput" class="weui-msg weui-msg_access">
    <div class="weui-msg__icon-area">
      <i class="weui-icon-info-circle weui-icon_msg"></i>
    </div>
    <div class="weui-msg__text-area">
      <h2 class="weui-msg__title">图书借阅查询</h2>
      <p class="weui-msg__desc">图书借阅密码默认为读者证号后六位，如遗忘请联系图书馆重置密码。</p>
    </div>
  </div>
  
  <!-- 查询结果标题 -->
  <div v-if="showResult" class="weui-cells__title">图书借阅信息</div>
  
  <!-- 图书列表 -->
  <div v-if="showBookList" class="weui-cells">
    <a v-for="(book, index) in bookList" :key="index" class="weui-cell weui-cell_access" href="javascript:" @click="showBookDetail(index)">
      <div class="weui-cell__bd">
        <p>{{ book.name }}</p>
        <p style="font-size:13px;color:#999">借阅时间：{{ book.borrowDate }}</p>
        <p style="font-size:13px;color:#999">应还时间：{{ book.returnDate }}</p>
      </div>
      <div class="weui-cell__ft"></div>
    </a>
  </div>
  
  <!-- 空记录提示 -->
  <div v-if="showEmpty" class="weui-msg weui-msg_access">
    <div class="weui-msg__icon-area">
      <i class="weui-icon-success-circle weui-icon_msg"></i>
    </div>
    <div class="weui-msg__text-area">
      <h2 class="weui-msg__title">暂无借阅记录</h2>
      <p class="weui-msg__desc">当前没有任何图书借阅记录。</p>
    </div>
  </div>
  
  <!-- 图书详情弹窗 -->
  <div v-if="showDialog" class="weui-popup__container">
    <div class="weui-popup__overlay"></div>
    <div class="weui-popup__modal">
      <div class="toolbar">
        <div class="toolbar-inner">
          <a href="javascript:" class="picker-button close-popup" @click="closePopup">关闭</a>
          <h1 class="title">图书详细信息</h1>
          <a href="javascript:" class="picker-button" @click="renewBook">续借</a>
        </div>
      </div>
      <div class="modal-content">
        <div class="weui-form-preview">
          <div class="weui-form-preview__hd">
            <label class="weui-form-preview__label">图书名称</label>
            <em class="weui-form-preview__value">{{ currentBook.name }}</em>
          </div>
          <div class="weui-form-preview__bd">
            <div class="weui-form-preview__item">
              <label class="weui-form-preview__label">图书ID</label>
              <span class="weui-form-preview__value">{{ currentBook.id }}</span>
            </div>
            <div class="weui-form-preview__item">
              <label class="weui-form-preview__label">图书SN</label>
              <span class="weui-form-preview__value">{{ currentBook.sn }}</span>
            </div>
            <div class="weui-form-preview__item">
              <label class="weui-form-preview__label">图书条码</label>
              <span class="weui-form-preview__value">{{ currentBook.code }}</span>
            </div>
            <div class="weui-form-preview__item">
              <label class="weui-form-preview__label">图书作者</label>
              <span class="weui-form-preview__value">{{ currentBook.author }}</span>
            </div>
            <div class="weui-form-preview__item">
              <label class="weui-form-preview__label">借阅时间</label>
              <span class="weui-form-preview__value">{{ currentBook.borrowDate }}</span>
            </div>
            <div class="weui-form-preview__item">
              <label class="weui-form-preview__label">应还时间</label>
              <span class="weui-form-preview__value">{{ currentBook.returnDate }}</span>
            </div>
            <div class="weui-form-preview__item">
              <label class="weui-form-preview__label">续借次数</label>
              <span class="weui-form-preview__value">{{ currentBook.renewTime }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  
  <!-- 加载提示 -->
  <div v-if="isLoading" class="weui-toast">
    <i class="weui-loading weui-icon_toast"></i>
    <p class="weui-toast__content">数据加载中</p>
  </div>
  
  <!-- 错误提示 -->
  <div v-if="errorMessage" class="weui_warn" id="warn">{{ errorMessage }}</div>
</template>

<script>
import axios from 'axios'
import weui from 'weui.js'

export default {
  data() {
    return {
      password: '',
      bookList: [],
      currentBook: {},
      isLoading: false,
      showInput: true,
      showResult: false,
      showBookList: false,
      showEmpty: false,
      showDialog: false,
      errorMessage: ''
    }
  },
  methods: {
    postQueryForm() {
      if (this.password === '') {
        this.showError('请将信息填写完整！');
      } else {
        this.isLoading = true;
        this.bookList = [];
        axios.post('/api/bookquery', {
          password: this.password
        })
        .then(response => {
          this.isLoading = false;
          if (response.data.success) {
            if (response.data.data.length > 0) {
              this.bookList = response.data.data;
              this.showInput = false;
              this.showResult = true;
              this.showBookList = true;
              this.showEmpty = false;
            } else {
              this.showInput = false;
              this.showResult = true;
              this.showBookList = false;
              this.showEmpty = true;
            }
          } else {
            this.showError(response.data.message);
          }
        })
        .catch(error => {
          this.isLoading = false;
          if (error.response && error.response.status === 503) {
            this.showError(error.response.data.message);
          } else {
            this.showError('网络连接异常，请检查网络连接');
          }
        });
      }
    },
    showBookDetail(index) {
      this.currentBook = this.bookList[index];
      this.showDialog = true;
    },
    renewBook() {
      let sn = this.currentBook.sn;
      let code = this.currentBook.code;
      
      weui.loading('续借图书中', 0);
      
      axios.post('/api/bookrenew', {
        sn: sn,
        code: code
      })
      .then(response => {
        weui.hideLoading();
        if (response.data.success) {
          weui.topTips('续借图书成功', 3000);
          this.closePopup();
        } else {
          weui.topTips(response.data.message, 3000);
        }
      })
      .catch(error => {
        weui.hideLoading();
        if (error.response && error.response.status === 503) {
          weui.topTips(error.response.data.message, 3000);
        } else {
          weui.topTips('网络连接异常，请检查网络连接', 3000);
        }
      });
    },
    closePopup() {
      this.showDialog = false;
    },
    showError(message) {
      this.errorMessage = message;
      setTimeout(() => {
        this.errorMessage = '';
      }, 2000);
    }
  }
}
</script>

<style scoped>
/* 保持原有CSS样式不变 */
</style>