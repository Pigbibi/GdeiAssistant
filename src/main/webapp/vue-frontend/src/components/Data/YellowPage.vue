<template>
  <div>
    <div class="weui-panel weui-panel_access">
      <div class="weui-panel__hd">黄页查询</div>
      <div class="weui-panel__bd">
        <!-- 黄页列表 -->
        <div v-for="(item, index) in yellowPageList" :key="index" class="weui-media-box weui-media-box_text">
          <h4 class="weui-media-box__title">{{ item.name }}</h4>
          <p class="weui-media-box__desc">{{ item.category }} - {{ item.region }}</p>
          <ul class="weui-media-box__info">
            <li class="weui-media-box__info__meta">{{ item.telephone }}</li>
            <li class="weui-media-box__info__meta weui-media-box__info__meta_extra">{{ item.address }}</li>
          </ul>
          <div class="weui-cell__ft">
            <a href="javascript:" class="weui-cell__ft" @click="ShowDetail(item)">
              <i class="weui-icon-info-circle"></i>
            </a>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 详情弹窗 -->
    <div v-if="showModal" class="weui-mask" @click="CloseModal"></div>
    <div v-if="showModal" class="weui-dialog" style="width: 85%; margin-left: -42.5%;">
      <div class="weui-dialog__hd">
        <strong class="weui-dialog__title">{{ detailInfo.name }}</strong>
      </div>
      <div class="weui-dialog__bd" style="max-height: 300px; overflow-y: auto;">
        <table class="weui-table">
          <tr>
            <td class="weui-table_td">类别</td>
            <td class="weui-table_td">{{ detailInfo.category }}</td>
          </tr>
          <tr>
            <td class="weui-table_td">区域</td>
            <td class="weui-table_td">{{ detailInfo.region }}</td>
          </tr>
          <tr>
            <td class="weui-table_td">电话</td>
            <td class="weui-table_td">
              <a :href="'tel:' + detailInfo.telephone" class="weui-link">{{ detailInfo.telephone }}</a>
              <a href="javascript:" class="weui-link" style="margin-left: 10px;" @click="SendSMS(detailInfo.telephone)">发送短信</a>
            </td>
          </tr>
          <tr>
            <td class="weui-table_td">地址</td>
            <td class="weui-table_td">{{ detailInfo.address }}</td>
          </tr>
          <tr v-if="detailInfo.email">
            <td class="weui-table_td">邮箱</td>
            <td class="weui-table_td">
              <a :href="'mailto:' + detailInfo.email" class="weui-link">{{ detailInfo.email }}</a>
            </td>
          </tr>
          <tr v-if="detailInfo.website">
            <td class="weui-table_td">网址</td>
            <td class="weui-table_td">
              <a :href="detailInfo.website" class="weui-link" target="_blank">{{ detailInfo.website }}</a>
            </td>
          </tr>
        </table>
      </div>
      <div class="weui-dialog__ft">
        <a href="javascript:" class="weui-dialog__btn weui-dialog__btn_default" @click="CloseModal">取消</a>
        <a href="javascript:" class="weui-dialog__btn weui-dialog__btn_primary" @click="CopyTelephone(detailInfo.telephone)">复制电话</a>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import FastClick from 'fastclick';

export default {
  name: 'YellowPage',
  data() {
    return {
      yellowPageList: [],
      yellowPageTypeList: [],
      showModal: false,
      detailInfo: {}
    };
  },
  mounted() {
    // 初始化FastClick
    FastClick.attach(document.body);
    
    // 加载黄页数据
    this.LoadYellowPageData();
  },
  methods: {
    LoadYellowPageData() {
      // 显示加载提示
      weui.loading('加载中', {className: 'custom-classname'});
      
      // 发送请求获取黄页数据
      axios.get('/api/data/yellowpage')
        .then(response => {
          // 隐藏加载提示
          weui.hideLoading();
          
          const result = response.data;
          if (result.success) {
            this.yellowPageList = result.data.data;
            this.yellowPageTypeList = result.data.type;
          } else {
            weui.topTips('加载失败');
          }
        })
        .catch(error => {
          // 隐藏加载提示
          weui.hideLoading();
          
          weui.topTips('网络异常，请重试');
          console.error('Error:', error);
        });
    },
    ShowDetail(item) {
      // 显示详情弹窗
      this.detailInfo = item;
      this.showModal = true;
    },
    CloseModal() {
      // 关闭详情弹窗
      this.showModal = false;
    },
    SendSMS(telephone) {
      // 发送短信功能
      window.location.href = 'sms:' + telephone;
    },
    CopyTelephone(telephone) {
      // 复制电话号码到剪贴板
      if (document.execCommand) {
        // 创建临时元素
        const tempElement = document.createElement('input');
        tempElement.value = telephone;
        document.body.appendChild(tempElement);
        tempElement.select();
        
        // 执行复制命令
        if (document.execCommand('copy')) {
          weui.toast('复制成功');
        } else {
          weui.topTips('复制失败');
        }
        
        // 移除临时元素
        document.body.removeChild(tempElement);
      } else {
        weui.topTips('当前浏览器不支持复制功能');
      }
      
      // 关闭弹窗
      this.CloseModal();
    }
  }
};
</script>

<style scoped>
/* 可以添加组件特定的样式 */
</style>