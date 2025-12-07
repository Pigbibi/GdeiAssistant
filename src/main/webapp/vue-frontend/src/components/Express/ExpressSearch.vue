<template>
  <div>
    <div id="Header" class="Header">
      <h1>广东第二师范学院表白墙</h1>
    </div>

    <!-- 搜索按钮 -->
    <div class="main-body ui-content" id="search-bars">
      <div class="ui-field-contain">
        <label for="search-bars-input">搜索：</label>
        <input type="search" name="search" maxlength="10" id="search-bars-input" v-model="keyword" placeholder="请输入关键词">
      </div>
      <input id="search-bars-submit" style="text-align:center;display:block;margin:0 auto;" type="submit" data-inline="true" value="搜索" @click="searchKeyword">

      <!-- 搜索结果 -->
      <div class="main-body" id="main">
        <div v-for="data in expressList" :key="data.id" :class="['post', 'post-' + data.id]">
          <div :class="['post-title', 'post-title-' + data.id]">
            <ul>
              <li :class="getGenderCodeByIndex(data.selfGender)">{{ data.nickname }}</li>
              <li><img src="/img/express/to.png"/></li>
              <li :class="getGenderCodeByIndex(data.personGender)">{{ data.name }}</li>
            </ul>
          </div>
          <div :class="['post-body', 'post-body-' + data.id]">
            <p class="post-body-content">{{ data.content }}</p>
            <p class="post-body-time">{{ data.publishTime }}</p>
          </div>
          <div :class="['post-actions', 'action', 'ui-navbar', 'post-actions-' + data.id]">
            <ul :class="['ui-grid-c', 'post-actions-ul-' + data.id]">
              <li class="ui-block-a">
                <a :class="['ui-link', 'ui-btn', 'ui-icon-like', 'ui-btn-icon-left', { 'ui-icon-liked': data.liked }]" href="#" :post="data.id" @click="likeExpress(data.id, $event)">{{ data.likeCount }}</a>
              </li>
              <li class="ui-block-b">
                <a class="ui-link ui-btn ui-icon-guess ui-btn-icon-left" href="#guess-Name-Popup" data-rel="popup" data-position-to="window" data-transition="pop" :post="data.id" @click="openGuessPopup(data.id, data.guessCount, data.guessSum)">{{ data.guessCount }}/{{ data.guessSum }}</a>
              </li>
              <li class="ui-block-c">
                <a class="ui-link ui-btn ui-icon-comment ui-btn-icon-left" href="#comment-Popup" data-rel="popup" data-position-to="window" data-transition="pop" :post="data.id" @click="openCommentPopup(data.id)">{{ data.commentCount }}</a>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>

    <!-- 加载更多 -->
    <section v-show="showLoadMore" id="loadmore" class="loadmore" @click="loadExpress">
      <span>{{ loadMoreText }}</span>
    </section>

    <!-- 错误提示 -->
    <div class="layui-m-layer layui-m-layer0" :style="{ display: showError ? 'block' : 'none' }">
      <div class="layui-m-layermain">
        <div class="layui-m-layersection">
          <div class="layui-m-layerchild layui-m-layer-msg layui-m-anim-up">
            <div id="warning" class="layui-m-layercont">{{ errorMessage }}</div>
          </div>
        </div>
      </div>
    </div>

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
    <div data-role="popup" class="ui-content" data-overlay-theme="b" id="guess-Name-Popup">
      <a href="#" data-rel="back" class="ui-btn ui-corner-all ui-shadow ui-btn ui-icon-delete ui-btn-icon-notext ui-btn-right">关闭</a>
      <h4>猜名字</h4>
      <p>
        已猜中 <span id="guess_right">{{ guessRight }}</span> 次, 被猜 <span id="guess_all">{{ guessAll }}</span> 次.
      </p>
      <div class="ui-field-contain">
        <label for="guess-input">猜一猜发起者的名字：</label>
        <input type="search" name="search" id="guess-input" v-model="guessName" placeholder="名字">
      </div>
      <input id="guess-submit" style="text-align:center;display:block;margin:0 auto;" type="submit" data-inline="true" value="猜！" @click="submitGuess">
      <span id="guess-hint">{{ guessHint }}</span>
    </div>

    <!-- 评论列表和发表评论模块 -->
    <div data-role="popup" class="ui-content" data-overlay-theme="b" id="comment-Popup">
      <a href="#" data-rel="back" class="ui-btn ui-corner-all ui-shadow ui-btn ui-icon-delete ui-btn-icon-notext ui-btn-right">关闭</a>
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
      <input id="comment-submit" style="text-align:center;display:block;margin:0 auto;" type="submit" data-inline="true" value="评论" @click="submitComment">
      <span id="comment-hint">{{ commentHint }}</span>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'ExpressSearch',
  data() {
    return {
      keyword: '',
      start: 0,
      expressList: [],
      showLoadMore: false,
      loadMoreText: '点击加载更多',
      showError: false,
      errorMessage: '',
      // 猜名字相关
      currentGuessPostId: null,
      guessRight: 0,
      guessAll: 0,
      guessName: '',
      guessHint: '',
      // 评论相关
      currentCommentPostId: null,
      commentList: [],
      newComment: '',
      commentHint: ''
    };
  },
  methods: {
    // 搜索关键词
    searchKeyword() {
      if (this.keyword.trim() !== '') {
        // 显示加载更多选项按钮
        this.showLoadMore = true;
        this.loadMoreText = '点击加载更多';
        // 更新关键词和当前表白信息数量
        this.start = 0;
        // 清空已加载的表白信息
        this.expressList = [];
        // 加载表白信息
        this.loadExpress();
      } else {
        this.showWarningMessage('请输入搜索关键词');
      }
    },
    
    // 加载表白信息
    loadExpress() {
      if (this.keyword) {
        axios.get(`/api/express/keyword/${this.keyword}/start/${this.start}/size/10`)
          .then(response => {
            const result = response.data;
            if (result.success === true) {
              if (result.data.length === 0) {
                this.showLoadMore = true;
                this.loadMoreText = '没有更多信息了';
              } else {
                this.start += result.data.length;
                this.expressList = [...this.expressList, ...result.data];
              }
            } else {
              this.showWarningMessage(result.message);
            }
          })
          .catch(error => {
            this.showWarningMessage('网络异常，请检查网络连接');
          });
      }
    },
    
    // 点赞表白
    likeExpress(postId, event) {
      event.preventDefault();
      // 查找当前表白信息
      const express = this.expressList.find(item => item.id === postId);
      if (express && !express.liked) {
        // 提交点赞请求
        axios.post(`/api/express/id/${postId}/like`)
          .then(response => {
            const result = response.data;
            if (result.success === true) {
              // 添加已点赞图标，点赞数+1
              express.liked = true;
              express.likeCount += 1;
            } else {
              this.showWarningMessage(result.message);
            }
          })
          .catch(error => {
            this.showWarningMessage('网络异常，请检查网络连接');
          });
      }
    },
    
    // 打开猜名字弹窗
    openGuessPopup(postId, guessCount, guessSum) {
      this.currentGuessPostId = postId;
      this.guessRight = guessCount;
      this.guessAll = guessSum;
      this.guessName = '';
      this.guessHint = '';
    },
    
    // 提交猜名字信息
    submitGuess() {
      if (this.currentGuessPostId && this.guessName.trim() !== '') {
        axios.post(`/api/express/id/${this.currentGuessPostId}/guess`, {
          name: this.guessName
        })
        .then(response => {
          const result = response.data;
          if (result.success === true) {
            if (result.data === true) {
              this.guessHint = '恭喜你，猜中了！';
              this.guessRight += 1;
              this.guessAll += 1;
              // 更新列表中的猜名字比例
              const express = this.expressList.find(item => item.id === this.currentGuessPostId);
              if (express) {
                express.guessCount += 1;
                express.guessSum += 1;
              }
            } else {
              this.guessHint = '很遗憾，没有猜中，再试试吧！';
              this.guessAll += 1;
              // 更新列表中的猜名字总数
              const express = this.expressList.find(item => item.id === this.currentGuessPostId);
              if (express) {
                express.guessSum += 1;
              }
            }
          } else {
            this.showWarningMessage(result.message);
          }
        })
        .catch(error => {
          this.showWarningMessage('网络异常，请检查网络连接');
        });
      } else {
        this.showWarningMessage('真实姓名不能为空');
      }
    },
    
    // 打开评论弹窗
    openCommentPopup(postId) {
      this.currentCommentPostId = postId;
      this.newComment = '';
      this.commentHint = '';
      // 加载评论列表
      this.loadComments(postId);
    },
    
    // 加载评论列表
    loadComments(postId) {
      axios.get(`/api/express/id/${postId}/comment`)
        .then(response => {
          const result = response.data;
          if (result.success === true) {
            this.commentList = result.data;
          } else {
            this.showWarningMessage(result.message);
          }
        })
        .catch(error => {
          this.showWarningMessage('网络异常，请检查网络连接');
        });
    },
    
    // 提交评论
    submitComment() {
      if (this.currentCommentPostId && this.newComment.trim() !== '') {
        axios.post(`/api/express/id/${this.currentCommentPostId}/comment`, {
          comment: this.newComment
        })
        .then(response => {
          const result = response.data;
          if (result.success === true) {
            // 清空评论输入表单
            this.newComment = '';
            // 表白信息评论数+1
            const express = this.expressList.find(item => item.id === this.currentCommentPostId);
            if (express) {
              express.commentCount += 1;
            }
            // 重新加载评论
            this.loadComments(this.currentCommentPostId);
          } else {
            this.showWarningMessage(result.message);
          }
        })
        .catch(error => {
          this.showWarningMessage('网络异常，请检查网络连接');
        });
      } else {
        this.showWarningMessage('评论信息不能为空');
      }
    },
    
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