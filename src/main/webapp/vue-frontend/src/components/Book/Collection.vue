<template>
  <div class="weui-cells__title">馆藏图书查询</div>
  <div class="weui-cells weui-cells_form">
    <div class="weui-cell">
      <div class="weui-cell__hd"><label class="weui-label">图书名称</label></div>
      <div class="weui-cell__bd">
        <input id="bookname" class="weui-input" type="text" v-model="bookname" placeholder="请输入图书名称" />
      </div>
    </div>
  </div>
  <div class="weui-btn-area">
    <button class="weui-btn weui-btn_primary" type="button" @click="postQueryForm">查询馆藏图书</button>
    <button class="weui-btn weui-btn_default" type="button" @click="openAdvancedSearchPage">高级检索</button>
  </div>
  <div id="collectionList">
    <div class="weui-cells">
      <a v-for="(book, index) in collectionList" :key="index" :id="book.detailURL" class="weui-cell" href="javascript:" @click="getDetailInfo(book.detailURL)">
        <div class="weui-cell__bd">
          <p>{{ book.bookname }}</p>
          <p v-if="book.author" style="font-size: 13px;color: #999">{{ book.author }}</p>
          <p v-if="book.publishingHouse" style="font-size:13px;color:#999">{{ book.publishingHouse }}</p>
        </div>
      </a>
    </div>
  </div>
  <div id="bottomLine" class="weui-loadmore weui-loadmore_line">
    <span id="bottomLineText"></span>
  </div>
  <div id="loadingToast" class="weui-toast">
    <i class="weui-loading weui-icon_toast"></i>
    <p class="weui-toast__content">数据加载中</p>
  </div>
  <div class="weui_warn" id="warn"></div>
</template>

<script>
export default {
  data() {
    return {
      bookname: '',
      queriedBookName: '',
      collectionList: [],
      currentPage: 1,
      sumPage: 1
    }
  },
  mounted() {
    // 初始化时隐藏部分元素
    this.$nextTick(() => {
      document.getElementById('collectionList').style.display = 'none';
      document.getElementById('loadingToast').style.display = 'none';
      document.getElementById('bottomLine').style.display = 'none';
    });
    
    // 从本地存储加载缓存的馆藏信息
    this.loadFromSessionStorage();
    
    // 监听返回事件
    window.addEventListener("popstate", this.handleBackEvent);
  },
  beforeDestroy() {
    // 移除事件监听
    window.removeEventListener("popstate", this.handleBackEvent);
  },
  methods: {
    loadFromSessionStorage() {
      const sessionStorageList = sessionStorage.getItem("collectionList");
      const sessionStorageCurrentPage = sessionStorage.getItem("currentPage");
      const sessionStorageSumPage = sessionStorage.getItem("sumPage");
      
      if (sessionStorageCurrentPage !== null) {
        this.currentPage = parseInt(sessionStorageCurrentPage);
      }
      
      if (sessionStorageSumPage !== null) {
        this.sumPage = parseInt(sessionStorageSumPage);
      }
      
      if (sessionStorageList !== null) {
        this.collectionList = JSON.parse(sessionStorageList);
        document.getElementById('collectionList').style.display = 'block';
        
        if (this.currentPage === this.sumPage) {
          // 已经是最后一页
          document.getElementById('bottomLineText').textContent = '别拉了,已经到底了';
          document.getElementById('bottomLine').style.display = 'block';
        } else {
          document.getElementById('bottomLineText').textContent = '点击加载更多信息';
          document.getElementById('bottomLine').style.display = 'block';
        }
      }
    },
    openAdvancedSearchPage() {
      window.location.href = 'http://agentdockingopac.featurelib.libsou.com/showhome/search/showSearch?schoolId=705';
    },
    handleBackEvent() {
      // 退出时删除缓存的馆藏信息
      sessionStorage.removeItem("collectionList");
      sessionStorage.removeItem("currentPage");
      sessionStorage.removeItem("sumPage");
      sessionStorage.removeItem("anchor");
      window.location.href = '/index';
    },
    getDetailInfo(url) {
      sessionStorage.setItem("anchor", url);
      this.$router.push({
        path: '/collectiondetail',
        query: {
          detailURL: url
        }
      });
    },
    postQueryForm() {
      if (this.bookname === '') {
        this.showError('请将信息填写完整！');
      } else {
        // 显示进度条
        this.showLoading();
        
        // 重置分页
        this.currentPage = 1;
        this.sumPage = 1;
        this.queriedBookName = this.bookname;
        this.collectionList = [];
        
        // 发送查询请求
        this.$axios.post('/api/collection/query', {
          bookname: this.bookname,
          page: 1
        })
        .then(response => {
          this.hideLoading();
          const result = response.data;
          if (result && result.collectionList) {
            this.collectionList = result.collectionList;
            this.sumPage = result.sumPage || 1;
            document.getElementById('collectionList').style.display = 'block';
            document.getElementById('bottomLine').style.display = 'block';
            
            // 更新本地存储
            this.updateSessionStorage();
            
            if (this.currentPage === this.sumPage) {
              // 已经是最后一页
              document.getElementById('bottomLineText').textContent = '别拉了,已经到底了';
            } else {
              document.getElementById('bottomLineText').textContent = '点击加载更多信息';
            }
          } else {
            this.showError('查询结果为空');
          }
        })
        .catch(error => {
          this.hideLoading();
          this.showError('查询失败，请检查网络连接');
        });
      }
    },
    queryMore() {
      if (this.currentPage !== this.sumPage) {
        this.showLoading();
        this.$axios.post('/api/collection/query', {
          bookname: this.queriedBookName,
          page: this.currentPage + 1
        })
        .then(response => {
          this.hideLoading();
          const result = response.data;
          if (result && result.collectionList) {
            const newBooks = result.collectionList;
            if (newBooks.length > 0) {
              this.currentPage++;
              this.sumPage = result.sumPage || this.sumPage;
              this.collectionList = [...this.collectionList, ...newBooks];
              
              // 更新本地存储
              this.updateSessionStorage();
              
              if (this.currentPage === this.sumPage) {
                // 已经是最后一页
                document.getElementById('bottomLineText').textContent = '别拉了,已经到底了';
              } else {
                document.getElementById('bottomLineText').textContent = '点击加载更多信息';
              }
            } else {
              this.showError('没有更多图书信息');
            }
          } else {
            this.showError('没有更多图书信息');
          }
        })
        .catch(error => {
          this.hideLoading();
          this.showError('加载失败，请检查网络连接');
        });
      }
    },
    updateSessionStorage() {
      sessionStorage.setItem("collectionList", JSON.stringify(this.collectionList));
      sessionStorage.setItem("currentPage", this.currentPage.toString());
      sessionStorage.setItem("sumPage", this.sumPage.toString());
    },
    showLoading() {
      document.getElementById('loadingToast').style.display = 'block';
    },
    hideLoading() {
      document.getElementById('loadingToast').style.display = 'none';
    },
    showError(message) {
      const warnElement = document.getElementById('warn');
      warnElement.textContent = message;
      warnElement.style.display = 'block';
      setTimeout(() => {
        warnElement.style.display = 'none';
      }, 2000);
    }
  }
}
</script>

<style scoped>
/* 保持原有CSS样式不变 */
</style>