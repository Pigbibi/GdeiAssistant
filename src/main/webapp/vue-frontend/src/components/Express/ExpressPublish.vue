<template>
  <div>
    <div id="Header" class="Header">
      <h1>广东第二师范学院表白墙</h1>
    </div>

    <!-- 信息填写框 -->
    <div class="main-body" id="saylove-box">
      <img id="loading" style="display:none;position: fixed;top: 50%;left: 45%" src="/img/express/ajax-loader.gif" width="50px" height="auto" />

      <!-- 表白者信息 -->
      <fieldset>
        <legend>你的信息</legend>
        <label for="nickname">你的昵称</label>
        <input type="text" name="nickname" id="nickname" v-model="nickname" value="" maxlength="10">
        <label for="realname">你的真名</label>
        <input type="text" name="realname" id="realname" v-model="realname" value="" maxlength="10">
        <p class="guess-hint">
          注：真实姓名为可选填写项，默认保密不显示！填写即可参加紧张刺激的猜名字游戏！
        </p>
        <select class="gender-Type" id="self_gender" name="type" v-model="selfGender">
          <option value="0" selected>男</option>
          <option value="1">女</option>
          <option value="2">其他或保密</option>
        </select>
      </fieldset>

      <!-- 被表白者信息 -->
      <fieldset>
        <legend>TA的信息</legend>
        <label for="name">TA的名字、选择性别</label>
        <input type="text" name="name" id="name" v-model="name" value="" maxlength="10">
        <select class="itsGender-Type" id="person_gender" name="type" v-model="personGender">
          <option value="0" selected>男</option>
          <option value="1">女</option>
          <option value="2">其他或保密</option>
        </select>
      </fieldset>

      <!-- 表白内容 -->
      <label for="content" style=" text-align: left; clear: both ">表白的内容</label>
      <textarea style="height: 250px" name="content" id="content" v-model="content" @keyup="inputLengthCheck"></textarea>
      <p id="textCountBox">
        剩余<span id="textCount">{{ remainingTextCount }}</span>字
      </p>

      <!-- 提交按钮 -->
      <a href="javascript:" id="submit" class="ui-btn ui-btn-inline ui-corner-all" @click="publishExpress">提交</a>

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
    </div>

    <!-- 底部按钮 -->
    <div id="footer">
      <div class="ui-navbar" role="navigation">
        <ul>
          <li><a href="#" class="ui-icon-article" @click="$router.push('/express')">首页</a></li>
          <li><a href="#" class="ui-icon-heart" @click="$router.push('/express/publish')">表白</a></li>
          <li><a href="#" class="ui-icon-search" @click="$router.push('/express/search')">搜索</a></li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'ExpressPublish',
  data() {
    return {
      nickname: '',
      realname: '',
      selfGender: '0',
      name: '',
      personGender: '0',
      content: '',
      maxContentLength: 250,
      remainingTextCount: 250,
      showError: false,
      errorMessage: ''
    };
  },
  methods: {
    // 检测Input文字长度是否超过限制并进行实时字数反馈
    inputLengthCheck() {
      if (this.content.length > this.maxContentLength) {
        this.content = this.content.substr(0, this.maxContentLength);
      }
      this.remainingTextCount = this.maxContentLength - this.content.length;
    },
    
    // 发布表白信息
    publishExpress() {
      if (this.nickname === '') {
        this.showWarningMessage("你的昵称不能为空");
      } else if (this.nickname.length > 10) {
        this.showWarningMessage("你的昵称长度不能超过10字");
      } else if (this.realname.length > 10) {
        this.showWarningMessage("你的真实姓名长度不能超过10字");
      } else if (this.name === '') {
        this.showWarningMessage("对方名字不能为空");
      } else if (this.name.length > 10) {
        this.showWarningMessage("对方名字长度不能超过10字");
      } else if (this.content === '') {
        this.showWarningMessage("表白内容不能为空");
      } else if (this.content.length > this.maxContentLength) {
        this.showWarningMessage("表白内容长度不能超过250字");
      } else {
        // 显示加载动画
        document.getElementById('loading').style.display = 'block';
        
        axios.post('/api/express', {
          nickname: this.nickname,
          realname: this.realname,
          selfGender: this.selfGender,
          name: this.name,
          personGender: this.personGender,
          content: this.content
        })
        .then(response => {
          // 隐藏加载动画
          document.getElementById('loading').style.display = 'none';
          
          const result = response.data;
          if (result.success === true) {
            // 发布成功，跳转到首页
            this.$router.push('/express');
          } else {
            this.showWarningMessage(result.message);
          }
        })
        .catch(error => {
          // 隐藏加载动画
          document.getElementById('loading').style.display = 'none';
          this.showWarningMessage("网络异常，请检查网络连接");
        });
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