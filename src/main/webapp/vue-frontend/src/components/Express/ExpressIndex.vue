<template>
  <div>
    <!-- 页头的信息 -->
    <div id="Header" class="Header">
      <h1>广东第二师范学院表白墙</h1>
    </div>

    <!-- 主体 -->
    <div class="main-body" id="main">
      <!-- 表白信息列表 -->
      <div v-for="data in expressList" :key="data.id" class="post post-${data.id}">
        <div class="post-title post-title-${data.id}">
          <ul>
            <li :class="getGenderCodeByIndex(data.selfGender)">{{ data.nickname }}</li>
            <li><img src="/img/express/to.png"/></li>
            <li :class="getGenderCodeByIndex(data.personGender)">{{ data.name }}</li>
          </ul>
        </div>
        <div class="post-body post-body-${data.id}">
          <p class="post-body-content">{{ data.content }}</p>
          <p class="post-body-time">{{ data.publishTime }}</p>
        </div>
        <div class="post-actions action ui-navbar post-actions-${data.id}" role="navigation">
          <ul class="ui-grid-c post-actions-ul-${data.id}">
            <li class="ui-block-a">
              <a 
                class="ui-link ui-btn ui-icon-like ui-btn-icon-left" 
                :class="{ 'ui-icon-liked': data.liked }" 
                href="#" 
                :post="data.id" 
                data-icon="like"
                @click="handleLike(data.id, $event)"
              >
                {{ data.likeCount }}
              </a>
            </li>
            <li class="ui-block-b">
              <a 
                class="ui-link ui-btn ui-icon-guess ui-btn-icon-left" 
                href="#guess-Name-Popup"  
                data-rel="popup" 
                data-position-to="window"
                data-transition="pop" 
                :post="data.id" 
                data-icon="guess"
                @click="openGuessPopup(data.id, data.guessCount, data.guessSum)"
              >
                {{ data.guessCount }}/{{ data.guessSum }}
              </a>
            </li>
            <li class="ui-block-c">
              <a 
                class="ui-link ui-btn ui-icon-comment ui-btn-icon-left" 
                href="#comment-Popup" 
                data-rel="popup" 
                data-position-to="window"
                data-transition="pop" 
                :post="data.id" 
                data-icon="comment"
                @click="loadComments(data.id)"
              >
                {{ data.commentCount }}
              </a>
            </li>
          </ul>
        </div>
      </div>
    </div>

    <!-- 错误提示 -->
    <div class="layui-m-layer layui-m-layer0" :style="{ display: showError ? 'block' : 'none' }">
      <div class="layui-m-layermain">
        <div class="layui-m-layersection">
          <div class="layui-m-layerchild layui-m-layer-msg  layui-m-anim-up">
            <div id="warning" class="layui-m-layercont">{{ errorMessage }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 加载更多 -->
    <section id="loadmore" class="loadmore" @click="loadExpress" :style="{ display: hasMore ? 'block' : 'none' }">
      <span>{{ loadMoreText }}</span>
    </section>

    <!-- 颜色提示信息 -->
    <p style="text-align:center;color:#9e9e9e;font-size:12px;">
      蓝色下划线：男生 / 红色下划线：女生 / 黑色下划线：其他或保密
    </p>

    <!-- 提示信息 -->
    <p style="text-align:center;color:#9e9e9e;font-size:12px;">
      交友需谨慎，请注意保护个人隐私。
    </p>
    <p style="text-align:center;color:#9e9e9e;font-size:12px;">
      抵制粗俗语言，共创文明网络环境。
    </p>

    <!-- 底部菜单 -->
    <div id="footer">
      <div class="ui-navbar" role="navigation">
        <ul>
          <li><a href="#" class="ui-icon-article" @click="$router.push('/express')">首页</a></li>
          <li><a href="#" class="ui-icon-heart" @click="$router.push('/express/publish')">表白</a></li>
          <li><a href="#" class="ui-icon-search" @click="$router.push('/express/search')">搜索</a></li>
        </ul>
      </div>
    </div>

    <!-- 猜名字模块 -->
    <div id="guess-Name-Popup">
      <a href="#" @click="closeGuessPopup">关闭</a>
      <h4>猜名字</h4>
      <p>
        已猜中 <span id="guess_right">{{ currentGuessRight }}</span> 次, 被猜 <span id="guess_all">{{ currentGuessAll }}</span> 次.
      </p>
      <div class="ui-field-contain">
        <label for="guess-input">猜一猜发起者的名字：</label>
        <input type="search" name="search" id="guess-input" v-model="guessName" placeholder="名字">
      </div>
      <input id="guess-submit" style="text-align:center;display:block;margin:0 auto;" type="submit" value="猜！" @click="submitGuess">
      <span id="guess-hint">{{ guessHint }}</span>
    </div>

    <!-- 评论列表和发表评论模块 -->
    <div id="comment-Popup">
      <a href="#" @click="closeCommentPopup">关闭</a>
      <h4>评论列表</h4>
      <div id="comment-lists">
        <ul id="comment-lists-ul">
          <li v-if="commentList.length === 0">暂无评论，快来抢沙发吧！</li>
          <li v-for="(comment, index) in commentList" :key="index">
            <span class="comment-floor">{{ index + 1 }}楼</span>
            <span class="comment-nickname">{{ comment.nickname }}</span>
            <p>{{ comment.comment }}</p>
            <span class="comment-time">{{ comment.publishTime }}</span>
          </li>
        </ul>
      </div>
      <div class="ui-field-contain">
        <label for="comment-input">评论：</label>
        <input type="search" name="search" id="comment-input" v-model="newComment" placeholder="我想说...">
      </div>
      <input id="comment-submit" style="text-align:center;display:block;margin:0 auto;" type="submit" value="评论" @click="submitComment">
      <span id="comment-hint">{{ commentHint }}</span>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'ExpressIndex',
  data() {
    return {
      start: 0,
      expressList: [],
      hasMore: true,
      loadMoreText: '点击加载更多',
      showError: false,
      errorMessage: '',
      
      // 猜名字相关
      currentGuessPostId: null,
      currentGuessRight: 0,
      currentGuessAll: 0,
      guessName: '',
      guessHint: '',
      
      // 评论相关
      currentCommentPostId: null,
      commentList: [],
      newComment: '',
      commentHint: ''
    };
  },
  mounted() {
    // 默认加载最新10条的表白信息
    this.loadExpress();
  },
  methods: {
    // 根据性别下标值获取性别参数值
    getGenderCodeByIndex(index) {
      switch (index) {
        case 0:
          //男性
          return "male";
        case 1:
          //女性
          return "female";
        case 2:
          //其他或保密
          return "secrecy";
      }
    },
    
    // 加载表白信息
    loadExpress() {
      axios.get(`/api/express/start/${this.start}/size/10`)
        .then(response => {
          const result = response.data;
          if (result.success === true) {
            if (result.data.length === 0) {
              this.hasMore = false;
              this.loadMoreText = '没有更多信息了';
            } else {
              this.expressList = this.expressList.concat(result.data);
              this.start += result.data.length;
            }
          } else {
            this.showWarningMessage(result.message);
          }
        })
        .catch(error => {
          this.showWarningMessage("网络异常，请检查网络连接");
        });
    },
    
    // 点赞
    handleLike(postID, event) {
      const post = this.expressList.find(item => item.id === postID);
      if (!post.liked) {
        //提交点赞请求
        axios.post(`/api/express/id/${postID}/like`)
          .then(response => {
            const result = response.data;
            if (result.success === true) {
              //添加已点赞图标，点赞数+1
              post.liked = true;
              post.likeCount += 1;
            } else {
              this.showWarningMessage(result.message);
            }
          })
          .catch(error => {
            this.showWarningMessage("网络异常，请检查网络连接");
          });
      }
    },
    
    // 弹出猜名字窗口
    openGuessPopup(postID, guessRight, guessAll) {
      this.currentGuessPostId = postID;
      this.currentGuessRight = guessRight;
      this.currentGuessAll = guessAll;
      this.guessName = '';
      this.guessHint = '';
      // 显示弹窗的逻辑，这里需要根据项目的弹窗组件来实现
      // 暂时使用简单的显示方式
      document.getElementById('guess-Name-Popup').style.display = 'block';
    },
    
    // 关闭猜名字窗口
    closeGuessPopup() {
      document.getElementById('guess-Name-Popup').style.display = 'none';
    },
    
    // 提交猜名字信息
    submitGuess() {
      if (this.guessName !== '') {
        axios.post(`/api/express/id/${this.currentGuessPostId}/guess`, {
          name: this.guessName
        })
          .then(response => {
            const result = response.data;
            if (result.success === true) {
              if (result.data === true) {
                this.guessHint = "恭喜你，猜中了！";
                this.currentGuessRight += 1;
                this.currentGuessAll += 1;
                // 更新列表中的猜名字比例
                const post = this.expressList.find(item => item.id === this.currentGuessPostId);
                if (post) {
                  post.guessCount = this.currentGuessRight;
                  post.guessSum = this.currentGuessAll;
                }
              } else {
                this.guessHint = "很遗憾，没有猜中，再试试吧！";
                this.currentGuessAll += 1;
                // 更新列表中的猜名字比例
                const post = this.expressList.find(item => item.id === this.currentGuessPostId);
                if (post) {
                  post.guessSum = this.currentGuessAll;
                }
              }
            } else {
              this.showWarningMessage(result.message);
            }
          })
          .catch(error => {
            this.showWarningMessage("网络异常，请检查网络连接");
          });
      } else {
        this.showWarningMessage("真实姓名不能为空");
      }
    },
    
    // 加载评论信息
    loadComments(postID) {
      this.currentCommentPostId = postID;
      this.commentList = [];
      this.newComment = '';
      this.commentHint = '';
      
      axios.get(`/api/express/id/${postID}/comment`)
        .then(response => {
          const result = response.data;
          if (result.success === true) {
            this.commentList = result.data;
            // 显示弹窗
            document.getElementById('comment-Popup').style.display = 'block';
          } else {
            this.showWarningMessage(result.message);
          }
        })
        .catch(error => {
          this.showWarningMessage("网络异常，请检查网络连接");
        });
    },
    
    // 关闭评论窗口
    closeCommentPopup() {
      document.getElementById('comment-Popup').style.display = 'none';
    },
    
    // 提交评论
    submitComment() {
      if (this.newComment !== '') {
        axios.post(`/api/express/id/${this.currentCommentPostId}/comment`, {
          comment: this.newComment
        })
          .then(response => {
            const result = response.data;
            if (result.success === true) {
              // 清空评论输入表单
              this.newComment = '';
              // 重新加载评论
              this.loadComments(this.currentCommentPostId);
              // 更新列表中的评论数
              const post = this.expressList.find(item => item.id === this.currentCommentPostId);
              if (post) {
                post.commentCount += 1;
              }
            } else {
              this.showWarningMessage(result.message);
            }
          })
          .catch(error => {
            this.showWarningMessage("网络异常，请检查网络连接");
          });
      } else {
        this.showWarningMessage("评论信息不能为空");
      }
    },
    
    // 显示错误信息
    showWarningMessage(text) {
      this.errorMessage = text;
      this.showError = true;
      setTimeout(() => {
        this.showError = false;
      }, 2000);
    }
  }
};
</script>

<style scoped>
/* 保持原有的CSS样式不变 */
</style>