<template>
  <div class="weui-panel weui-panel_access">
    <div class="weui-panel__hd">电费查询</div>
    <div class="weui-panel__bd">
      <div class="weui-cells weui-cells_form">
        <div class="weui-cell">
          <div class="weui-cell__hd">
            <label for="select" class="weui-label">年份</label>
          </div>
          <div class="weui-cell__bd">
            <select class="weui-select" id="select" v-model="year" @change="YearListChange">
              <option v-for="item in YearList" :value="item" :key="item">{{ item }}</option>
            </select>
          </div>
        </div>
        <div class="weui-cell">
          <div class="weui-cell__hd">
            <label for="name" class="weui-label">姓名</label>
          </div>
          <div class="weui-cell__bd">
            <input class="weui-input" type="text" id="name" v-model="name" maxlength="5" placeholder="请输入姓名" @input="NameInput" />
          </div>
        </div>
        <div class="weui-cell">
          <div class="weui-cell__hd">
            <label for="number" class="weui-label">学号</label>
          </div>
          <div class="weui-cell__bd">
            <input class="weui-input" type="number" id="number" v-model="number" maxlength="10" placeholder="请输入学号" @input="NumberInput" />
          </div>
        </div>
      </div>
      <div class="weui-btn-area">
        <button class="weui-btn weui-btn_primary" @click="QueryElectricFees">查询</button>
      </div>
    </div>
  </div>
  <!-- 结果展示区域 -->
  <div v-if="electricFeesData" class="weui-panel weui-panel_access">
    <div class="weui-panel__hd">查询结果</div>
    <div class="weui-panel__bd">
      <div class="weui-cells">
        <div class="weui-cell">
          <div class="weui-cell__hd"><span class="weui-label">年份</span></div>
          <div class="weui-cell__bd">{{ electricFeesData.year }}</div>
        </div>
        <div class="weui-cell">
          <div class="weui-cell__hd"><span class="weui-label">宿舍</span></div>
          <div class="weui-cell__bd">{{ electricFeesData.dormitory }}</div>
        </div>
        <div class="weui-cell">
          <div class="weui-cell__hd"><span class="weui-label">入住人数</span></div>
          <div class="weui-cell__bd">{{ electricFeesData.livedNumber }}</div>
        </div>
        <div class="weui-cell">
          <div class="weui-cell__hd"><span class="weui-label">用电数额</span></div>
          <div class="weui-cell__bd">{{ electricFeesData.electricityNumber }}</div>
        </div>
        <div class="weui-cell">
          <div class="weui-cell__hd"><span class="weui-label">需缴费用</span></div>
          <div class="weui-cell__bd">{{ electricFeesData.paymentNumber }}</div>
        </div>
        <div class="weui-cell">
          <div class="weui-cell__hd"><span class="weui-label">收费时间</span></div>
          <div class="weui-cell__bd">{{ electricFeesData.paymentTime }}</div>
        </div>
        <div class="weui-cell">
          <div class="weui-cell__hd"><span class="weui-label">备注</span></div>
          <div class="weui-cell__bd">{{ electricFeesData.remark }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import FastClick from 'fastclick';

export default {
  name: 'ElectricityFees',
  data() {
    return {
      name: '',
      number: '',
      year: new Date().getFullYear(),
      YearList: [],
      electricFeesData: null
    };
  },
  mounted() {
    // 初始化FastClick
    FastClick.attach(document.body);
    
    // 初始化年份列表
    this.InitYearList();
    
    // 设置默认年份为当前年份
    let currentYear = new Date().getFullYear();
    for (let i = 0; i < this.YearList.length; i++) {
      if (this.YearList[i] === currentYear) {
        this.year = currentYear;
        break;
      }
    }
  },
  methods: {
    InitYearList() {
      // 生成2016到当前年份的列表
      let currentYear = new Date().getFullYear();
      for (let i = 2016; i <= currentYear; i++) {
        this.YearList.push(i);
      }
    },
    YearListChange() {
      // 年份改变时的处理
    },
    NameInput() {
      // 检查姓名输入长度
      if (this.name.length > 5) {
        this.name = this.name.substring(0, 5);
      }
    },
    NumberInput() {
      // 检查学号输入长度
      if (this.number.toString().length > 10) {
        this.number = parseInt(this.number.toString().substring(0, 10));
      }
    },
    QueryElectricFees() {
      // 表单验证
      if (!this.name.trim()) {
        weui.topTips('请输入姓名');
        return;
      }
      if (!this.number.toString().trim()) {
        weui.topTips('请输入学号');
        return;
      }
      if (this.number.toString().length < 10) {
        weui.topTips('学号长度不足');
        return;
      }
      
      // 显示加载提示
      weui.loading('查询中', {className: 'custom-classname'});
      
      // 发送查询请求
      axios.post('/api/data/electricfees', {
        name: this.name,
        number: this.number,
        year: this.year
      })
      .then(response => {
        // 隐藏加载提示
        weui.hideLoading();
        
        const result = response.data;
        if (result.success) {
          this.electricFeesData = result.data;
        } else {
          weui.topTips(result.message || '查询失败');
        }
      })
      .catch(error => {
        // 隐藏加载提示
        weui.hideLoading();
        
        if (error.response && error.response.status === 404) {
          weui.topTips('查无此人信息');
        } else {
          weui.topTips('查询失败，请重试');
        }
        console.error('Error:', error);
      });
    }
  }
};
</script>

<style scoped>
/* 可以添加组件特定的样式 */
</style>