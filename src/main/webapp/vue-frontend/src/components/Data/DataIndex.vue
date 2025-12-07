<template>
  <div class="weui-cells__title">数据查询</div>
  <div class="weui-panel weui-panel_access">
    <div class="weui-panel__hd">常用查询</div>
    <div class="weui-panel__bd">
      <!-- 导航列表 -->
      <div class="weui-cells weui-cells_access">
        <a href="javascript:" class="weui-cell weui-cell_access" @click="NavigateToElectricityFees">
          <div class="weui-cell__hd"><i class="weui-icon-info"></i></div>
          <div class="weui-cell__bd">
            <p>电费查询</p>
          </div>
          <div class="weui-cell__ft weui-cell__ft_in-access"></div>
        </a>
        <a href="javascript:" class="weui-cell weui-cell_access" @click="NavigateToYellowPage">
          <div class="weui-cell__hd"><i class="weui-icon-info"></i></div>
          <div class="weui-cell__bd">
            <p>黄页查询</p>
          </div>
          <div class="weui-cell__ft weui-cell__ft_in-access"></div>
        </a>
      </div>
    </div>
  </div>
  
  <!-- 返回按钮 -->
  <div class="weui-footer">
    <a href="javascript:" class="weui-footer__link" @click="GoBack">返回</a>
  </div>
</template>

<script>
import FastClick from 'fastclick';

export default {
  name: 'DataIndex',
  mounted() {
    // 初始化FastClick
    FastClick.attach(document.body);
  },
  methods: {
    NavigateToElectricityFees() {
      // 跳转到电费查询页面
      this.$router.push('/data/electricityfees');
    },
    NavigateToYellowPage() {
      // 跳转到黄页查询页面
      this.$router.push('/data/yellowpage');
    },
    GoBack() {
      // 返回上一页
      this.$router.back();
    }
  }
};
</script>

<style scoped>
/* 可以添加组件特定的样式 */
.weui-cells__title {
  margin-top: 0;
  margin-bottom: 0;
  padding-left: 15px;
  padding-right: 15px;
  padding-top: 15px;
  color: #888;
  font-size: 14px;
}

.weui-footer {
  margin-top: 20px;
  padding: 20px;
  text-align: center;
}
</style>