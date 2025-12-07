<template>
  <div class="photograph-index">
    <!-- 数据统计 -->
    <div class="am-g" id="statistics">
      <div class="am-u-sm-4">
        <span id="statistics-photos">{{ statistics.photos }}</span>
        <span>照片总数</span>
      </div>
      <div class="am-u-sm-4">
        <span id="statistics-comments">{{ statistics.comments }}</span>
        <span>评论总数</span>
      </div>
      <div class="am-u-sm-4">
        <span id="statistics-likes">{{ statistics.likes }}</span>
        <span>点赞总数</span>
      </div>
    </div>

    <!-- 页面属性-->
    <input type="hidden" id="type" v-model="type" />
    <input type="hidden" id="start" v-model="start" />

    <!-- 信息列表 -->
    <div id="card-box">
      <div v-for="photo in photographs" :key="photo.id" class="card">
        <div class="card-img" @click="browseImage(photo.id)">
          <div :id="'figure-' + photo.id" class="figure-div-box">
            <figure data-am-widget="figure" class="am am-figure card-img-tag am-figure-zoomable" data-am-figure="{ pureview: 'true' }">
              <img class="lazy" :id="'image-' + photo.id + '-1'" :src="getImageUrl(photo.id, 1)" />
              <img v-for="index in photo.count - 1" :key="index" :id="'image-' + photo.id + '-' + (index + 1)" style="display: none" class="lazy" :src="getImageUrl(photo.id, index + 1)" />
            </figure>
          </div>
        </div>
        <div class="tags">
          <span class="am-badge am-badge-warning am-text-sm img-num">{{ photo.count }}图</span>
        </div>
        <div class="card-desc">
          <p class="card-name">{{ photo.title }}</p>
          <p class="card-say">{{ photo.content || '' }}</p>
        </div>
        <div class="card-btn-group">
          <div class="am-btn-group am-btn-group-justify">
            <a class="am-btn am-btn-photo" href="javascript:;" @click="likePhoto(photo.id)" :id="'like-btn-' + photo.id" role="button" :like-count="photo.likeCount" :liked="photo.liked">
              <i :class="photo.liked ? 'am-icon-check-square' : 'am-icon-check-square-o'"></i>{{ photo.likeCount }} {{ photo.liked ? '已点赞' : '点赞' }}
            </a>
            <a class="am-btn am-btn-photo" href="javascript:;" :id="'comment-btn-' + photo.id" @click="showComment(photo.id)" role="button">
              <i class="am-icon-th-list"></i>{{ photo.commentCount }} 评论
            </a>
          </div>
        </div>
        <div :id="'card-comment-' + photo.id" style="display: none">
          <p class="comment-hint">{{ photo.commentCount == 0 ? '目前没有评论！快来抢沙发吧！' : '' }}</p>
          <div class="comment-box" :id="'comment-box-' + photo.id" :comment-count="photo.commentCount">
            <article v-for="comment in comments[photo.id]" :key="comment.commentId" class="am-comment">
              <a :href="'/profile/user/' + comment.username"><img :id="'comment-avatar-' + comment.commentId" :src="getAvatarUrl(comment.username)" class="am-comment-avatar" width="48" height="48"></a>
              <div class="am-comment-main">
                <header class="am-comment-hd">
                  <div class="am-comment-meta">
                    <a :href="'/profile/user/' + comment.username" class="am-comment-author"><time datetime="">{{ comment.createTime }}</time> {{ comment.nickname }}</a>
                  </div>
                </header>
                <div class="am-comment-bd">{{ comment.comment }}</div>
              </div>
            </article>
          </div>
          <div class="am-g" style="text-align: center">
            <div class="am-u-sm-6">
              <button type="button" class="am-btn am-btn-primary" @click="startComment(photo.id)"><i class="am-icon-commenting"></i> 我要评论</button>
            </div>
            <div class="am-u-sm-6">
              <button type="button" class="am-btn am-btn-danger" @click="hideComment(photo.id)"><i class="am-icon-arrow-up"></i> 收起面板</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 加载中弹框 -->
    <div role="alert" id="loadingToast" style="display: none;">
      <div class="weui-mask_transparent"></div>
      <div class="weui-toast">
        <span class="weui-primary-loading weui-icon_toast">
          <span class="weui-primary-loading__dot"></span>
        </span>
        <p class="weui-toast__content">加载中</p>
      </div>
    </div>

    <!-- 加载更多 -->
    <p id="loadmore" style="text-align: center; font-size: 14px; color: #777" @click="loadPhotographs" :disabled="loading">
      {{ loading ? '加载中...' : (hasMore ? '点击查看更多' : '没有更多信息') }}
    </p>

    <!-- 提示信息 -->
    <p style="text-align: center; font-size: 14px; color: red">平台禁止色情、盗图等侵权违规行为
      <br>若有发现请点击<a href="javascript:" @click="report">此处举报</a></p>

    <br><br><br>

    <!-- 底部菜单 -->
    <footer id="toolbar">
      <div class="am-btn-group am-btn-group-justify bottom-btns" style="box-shadow: 0 -1px 5px #989898;">
        <a class="am-btn am-btn-danger" role="button" href="javascript:" @click="switchPhoto(1)">
          <img width="20px" height="20px" src="/img/photograph/life.png"/><br>最美生活照</a>
        <a class="am-btn am-btn-primary" role="button" href="javascript:" @click="switchPhoto(2)">
          <img width="20px" height="20px" src="/img/photograph/school.png"/><br>最美校园照</a>
        <a class="am-btn am-btn-warning" role="button" href="javascript:" @click="switchPhoto(3)">
          <img width="20px" height="20px" src="/img/photograph/graduation.png"/><br>最美毕业照</a>
        <a class="am-btn am-btn-success" role="button" href="javascript:" @click="navigateToUpload">
          <img width="20px" height="20px" src="/img/photograph/upload.png"/><br>我要晒照</a>
      </div>
    </footer>

    <!-- 添加评论弹窗 -->
    <div id="comment-dialog" class="weui-dialog" style="display: none;">
      <div class="weui-dialog__hd"><strong class="weui-dialog__title">发表评论</strong></div>
      <div class="weui-dialog__bd">
        <input type="text" id="comment-input" class="weui-input" placeholder="请输入评论内容">
      </div>
      <div class="weui-dialog__ft">
        <a href="javascript:;" class="weui-dialog__btn weui-dialog__btn_default" @click="closeCommentDialog">取消</a>
        <a href="javascript:;" class="weui-dialog__btn weui-dialog__btn_primary" @click="submitComment">确定</a>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      photographs: [],
      comments: {},
      statistics: {
        photos: 0,
        comments: 0,
        likes: 0
      },
      type: 1,
      start: 0,
      loading: false,
      hasMore: true,
      images: {},
      currentCommentId: null
    };
  },
  mounted() {
    //加载统计数据
    this.loadPhotographStatisticalData();
    //默认加载最新10条照片信息
    this.loadPhotographs();
  },
  methods: {
    //加载统计数据
    loadPhotographStatisticalData() {
      axios.get('/api/photograph/statistics/photos')
        .then(result => {
          if (result.data.success === true) {
            this.statistics.photos = result.data.data;
          }
        });
      axios.get('/api/photograph/statistics/comments')
        .then(result => {
          if (result.data.success === true) {
            this.statistics.comments = result.data.data;
          }
        });
      axios.get('/api/photograph/statistics/likes')
        .then(result => {
          if (result.data.success === true) {
            this.statistics.likes = result.data.data;
          }
        });
    },
    //获取照片图片URL
    getImageUrl(id, index) {
      if (!this.images[id]) {
        this.images[id] = [];
      }
      if (!this.images[id][index - 1]) {
        // 异步加载图片URL
        axios.get(`/api/photograph/id/${id}/index/${index}/image`)
          .then(result => {
            if (result.data.success === true && result.data.data) {
              this.$set(this.images[id], index - 1, result.data.data);
            }
          });
        return '';
      }
      return this.images[id][index - 1];
    },
    //获取用户头像URL
    getAvatarUrl(username) {
      return `/rest/avatar/${username}`;
    },
    //打开图片浏览器
    browseImage(id) {
      // 这里需要实现图片浏览器功能，暂时保留原有逻辑
      $.photoBrowser({
        items: this.images[id],
        onOpen: function () {
          $('.card').hide();
          $('#toolbar').hide();
          $('.photo-container img').css('margin', '0 auto');
        },
        onClose: function () {
          $('.card').show();
          $('#toolbar').show();
          $.scrollTo('#figure-' + id, 500);
        }
      }).open();
    },
    //加载照片列表
    loadPhotographs() {
      if (!this.loading && this.hasMore) {
        this.loading = true;
        $('#loadingToast').show();
        axios.get(`/api/photograph/type/${this.type}/start/${this.start}/size/10`)
          .then(result => {
            $('#loadingToast').hide();
            this.loading = false;
            if (result.data.success) {
              if (result.data.data.length === 0) {
                this.hasMore = false;
              } else {
                this.photographs = this.photographs.concat(result.data.data);
                this.start += result.data.data.length;
              }
            }
          })
          .catch(error => {
            $('#loadingToast').hide();
            this.loading = false;
            $.alert('网络访问异常，请检查网络连接', '网络异常');
          });
      }
    },
    //切换照片类型
    switchPhoto(type) {
      this.type = type;
      this.start = 0;
      this.photographs = [];
      this.hasMore = true;
      this.loadPhotographs();
    },
    //点赞照片
    likePhoto(id) {
      axios.post(`/api/photograph/id/${id}/like`)
        .then(result => {
          if (result.data.success) {
            // 更新本地数据
            const photo = this.photographs.find(p => p.id === id);
            if (photo) {
              photo.liked = !photo.liked;
              photo.likeCount += photo.liked ? 1 : -1;
            }
          }
        })
        .catch(error => {
          if (error.response) {
            $.alert(error.response.data.message, '请求失败');
          } else {
            $.alert('网络访问异常，请检查网络连接', '网络异常');
          }
        });
    },
    //显示评论
    showComment(id) {
      const commentBox = $(`#card-comment-${id}`);
      if (commentBox.css('display') === 'none') {
        commentBox.show();
        // 加载评论列表
        this.loadCommentList(id);
      } else {
        commentBox.hide();
      }
    },
    //隐藏评论
    hideComment(id) {
      $(`#card-comment-${id}`).hide();
    },
    //加载评论列表
    loadCommentList(id) {
      if (!this.comments[id]) {
        axios.get(`/api/photograph/id/${id}/comment`)
          .then(result => {
            if (result.data.success) {
              this.$set(this.comments, id, result.data.data);
            }
          })
          .catch(error => {
            if (error.response) {
              $.alert(error.response.data.message, '请求失败');
            } else {
              $.alert('网络访问异常，请检查网络连接', '网络异常');
            }
          });
      }
    },
    //开始评论
    startComment(id) {
      this.currentCommentId = id;
      $('#comment-dialog').show();
    },
    //关闭评论弹窗
    closeCommentDialog() {
      $('#comment-dialog').hide();
      this.currentCommentId = null;
      $('#comment-input').val('');
    },
    //提交评论
    submitComment() {
      const comment = $('#comment-input').val().trim();
      if (comment) {
        axios.post(`/api/photograph/id/${this.currentCommentId}/comment`, null, {
          params: {
            comment: comment
          }
        })
          .then(result => {
            if (result.data.success) {
              // 更新本地数据
              const photo = this.photographs.find(p => p.id === this.currentCommentId);
              if (photo) {
                photo.commentCount++;
              }
              // 重新加载评论列表
              this.loadCommentList(this.currentCommentId);
              this.closeCommentDialog();
            }
          })
          .catch(error => {
            if (error.response) {
              $.alert(error.response.data.message, '请求失败');
            } else {
              $.alert('网络访问异常，请检查网络连接', '网络异常');
            }
          });
      }
    },
    //跳转到上传页面
    navigateToUpload() {
      this.$router.push('/photograph/upload');
    },
    //举报功能
    report() {
      window.location.href = 'mailto:report@gdeiassistant.cn';
    }
  }
};
</script>

<style scoped>
/* 这里保持原有CSS样式不变 */
</style>