<template>
  <div>
    <div class="weui-cells__title" @click="$router.go(-1)">返回</div>

    <div class="hd">
      <h1 class="page_title">功能管理</h1>
      <p class="page_desc">注意：功能入口配置将被保存至浏览器本地</p>
    </div>

    <div class="weui-cells__title">打开/关闭首页的功能入口</div>
    <div class="weui-cells weui-cells_form">
      <div class="weui-cell weui-cell_switch" v-for="func in functions" :key="func.id">
        <div class="weui-cell__bd">{{ func.name }}</div>
        <div class="weui-cell__ft">
          <input :id="func.id" class="weui-switch" type="checkbox" v-model="func.enabled" @change="changeFunctionDisplaySetting(func.id, func.enabled)">
        </div>
      </div>
    </div>

    <div class="weui-btn-area">
      <button type="button" class="weui-btn weui-btn_default" @click="resetFunctionDisplaySetting">重置功能显示设置</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FunctionManage',
  data() {
    return {
      functions: [
        { id: 'grade', name: '成绩查询', enabled: true },
        { id: 'schedule', name: '课表查询', enabled: true },
        { id: 'cet', name: '四六级查询', enabled: true },
        { id: 'collection', name: '馆藏查询', enabled: true },
        { id: 'book', name: '我的图书馆', enabled: true },
        { id: 'bill', name: '消费查询', enabled: true },
        { id: 'card', name: '我的饭卡', enabled: true },
        { id: 'evaluate', name: '教学评价', enabled: true },
        { id: 'spare', name: '教室查询', enabled: true },
        { id: 'kaoyan', name: '考研查询', enabled: true },
        { id: 'tice', name: '体测查询', enabled: true },
        { id: 'news', name: '新闻通知', enabled: true },
        { id: 'data', name: '信息查询', enabled: true },
        { id: 'ershou', name: '二手交易', enabled: true },
        { id: 'lostandfound', name: '失物招领', enabled: true },
        { id: 'secret', name: '校园树洞', enabled: true },
        { id: 'photograph', name: '拍好校园', enabled: true },
        { id: 'express', name: '表白墙', enabled: true },
        { id: 'dating', name: '卖室友', enabled: true },
        { id: 'topic', name: '话题', enabled: true },
        { id: 'delivery', name: '快递代拿', enabled: true }
      ]
    };
  },
  mounted() {
    this.loadFunctionDisplaySetting();
  },
  methods: {
    loadFunctionDisplaySetting() {
      const displaySetting = localStorage.getItem("functionDisplaySetting");
      if (displaySetting) {
        const setting = JSON.parse(displaySetting);
        this.functions.forEach(func => {
          func.enabled = (setting[func.id] !== undefined) ? setting[func.id] : true;
        });
      }
    },
    changeFunctionDisplaySetting(id, state) {
      const displaySetting = {};
      this.functions.forEach(func => {
        displaySetting[func.id] = func.enabled;
      });
      localStorage.setItem("functionDisplaySetting", JSON.stringify(displaySetting));
      this.$toast('保存成功');
    },
    resetFunctionDisplaySetting() {
      localStorage.removeItem("functionDisplaySetting");
      this.functions.forEach(func => {
        func.enabled = true;
      });
      this.$toast('重置成功');
    }
  }
};
</script>

<style scoped>
/* 保持原有的CSS样式不变 */
.hd {
  padding: 20px;
  text-align: center;
}

.page_title {
  margin: 0;
  font-size: 20px;
}

.page_desc {
  margin: 10px 0 0 0;
  font-size: 14px;
  color: #888;
}

.weui-cells__title {
  padding: 10px 15px;
  color: #888;
  font-size: 14px;
}

.weui-cell {
  padding: 10px 15px;
  display: flex;
  align-items: center;
  border-bottom: 1px solid #E2E0E3;
}

.weui-cell__bd {
  flex: 1;
}

.weui-switch {
  position: relative;
  width: 52px;
  height: 32px;
  background-color: #DFDFDF;
  border-radius: 16px;
  transition: all .3s;
}

.weui-switch:checked {
  background-color: #04BE02;
}

.weui-switch::before {
  content: '';
  position: absolute;
  top: 2px;
  left: 2px;
  width: 28px;
  height: 28px;
  background-color: #FFFFFF;
  border-radius: 14px;
  transition: all .3s;
}

.weui-switch:checked::before {
  transform: translateX(20px);
}

.weui-btn-area {
  padding: 15px;
}

.weui-btn {
  display: block;
  width: 100%;
  padding: 12px;
  font-size: 16px;
  border-radius: 4px;
  border: none;
  outline: none;
  cursor: pointer;
}

.weui-btn_default {
  background-color: #F2F2F2;
  color: #333;
}
</style>