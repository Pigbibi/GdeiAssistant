<template>
  <div>
    <div class="weui-cells__title" style="float: left" @click="$router.back()">返回</div>
    <div class="weui-cells__title" style="float: right" @click="showOptionMenu()">更多</div>
    <div class="hd">
      <h1 class="page_title" style="clear:both;margin-top: 35px">我的成绩单</h1>
    </div>

    <div class="outter">
      <div class="navbar">
        <div id="one" @click="postQueryForm(0)" :class="{ selected: currentYear === 0 }">大一</div>
        <div id="two" @click="postQueryForm(1)" :class="{ selected: currentYear === 1 }">大二</div>
        <div id="three" @click="postQueryForm(2)" :class="{ selected: currentYear === 2 }">大三</div>
        <div id="four" @click="postQueryForm(3)" :class="{ selected: currentYear === 3 }">大四</div>
      </div>
    </div>

    <div class="grades">
      <div class="term" style="padding-top: 15px">第一学期</div>
      <p class="page_desc">{{ firstTermGPA ? '平均学分绩点：' + firstTermGPA : '' }}</p>
      <div class="table">
        <table>
          <thead>
          <tr>
            <th width="65%">课程</th>
            <th>学分</th>
            <th>成绩</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(grade, index) in firstTermGradeList" :key="index">
            <td style="width='65%';">{{ grade.gradeName }}</td>
            <td>{{ grade.gradeCredit }}</td>
            <td>{{ grade.gradeScore }}</td>
          </tr>
          </tbody>
        </table>
      </div>
      <div class="term">第二学期</div>
      <p class="page_desc">{{ secondTermGPA ? '平均学分绩点：' + secondTermGPA : '' }}</p>
      <div class="table">
        <table>
          <thead>
          <tr>
            <th width="65%">课程</th>
            <th>学分</th>
            <th>成绩</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(grade, index) in secondTermGradeList" :key="index">
            <td style="width='65%';">{{ grade.gradeName }}</td>
            <td>{{ grade.gradeCredit }}</td>
            <td>{{ grade.gradeScore }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import weui from 'weui'

export default {
  name: 'GradeQuery',
  data() {
    return {
      currentYear: 0,
      firstTermGPA: '',
      secondTermGPA: '',
      firstTermGradeList: [],
      secondTermGradeList: []
    }
  },
  mounted() {
    this.postQueryForm()
  },
  methods: {
    // 清除原有的成绩信息
    clearGradeInfo() {
      this.firstTermGPA = ''
      this.secondTermGPA = ''
      this.firstTermGradeList = []
      this.secondTermGradeList = []
    },
    
    // 处理成绩信息
    handleGradeInfo(data) {
      this.currentYear = data.year
      this.firstTermGPA = data.firstTermGPA
      this.secondTermGPA = data.secondTermGPA
      this.firstTermGradeList = data.firstTermGradeList
      this.secondTermGradeList = data.secondTermGradeList
    },
    
    // 提交异步查询请求
    postQueryForm(year) {
      let loading = weui.loading('加载中')
      
      if (year === undefined) {
        axios.post('/api/gradequery')
          .then(result => {
            loading.hide()
            this.clearGradeInfo()
            if (result.data.success === true) {
              this.handleGradeInfo(result.data.data)
            } else {
              this.showCustomErrorTip(result.data.message)
            }
          })
          .catch(error => {
            loading.hide()
            if (error.response) {
              this.showCustomErrorTip(error.response.data.message)
            } else {
              this.showNetworkErrorTip()
            }
          })
      } else {
        axios.post('/api/gradequery', { year: year })
          .then(result => {
            loading.hide()
            this.clearGradeInfo()
            if (result.data.success === true) {
              this.handleGradeInfo(result.data.data)
            } else {
              this.showCustomErrorTip(result.data.message)
            }
          })
          .catch(error => {
            loading.hide()
            if (error.response) {
              this.showCustomErrorTip(error.response.data.message)
            } else {
              this.showNetworkErrorTip()
            }
          })
      }
    },
    
    // 显示网络错误提示
    showNetworkErrorTip() {
      weui.alert('请求成绩信息失败，请检查网络连接', {
        title: '错误提示',
        buttons: [{
          label: '确定',
          type: 'primary'
        }]
      })
    },
    
    // 显示自定义错误提示
    showCustomErrorTip(message) {
      weui.alert(message, {
        title: '错误提示',
        buttons: [{
          label: '确定',
          type: 'primary'
        }]
      })
    },
    
    // 更新实时成绩数据
    refreshGradeData() {
      let loading = weui.loading('加载中')
      axios.post('/api/refreshgrade')
        .then(result => {
          loading.hide()
          this.clearGradeInfo()
          if (result.data.success === true) {
            this.handleGradeInfo(result.data.data)
          } else {
            this.showCustomErrorTip(result.data.message)
          }
        })
        .catch(error => {
          loading.hide()
          if (error.response) {
            this.showCustomErrorTip(error.response.data.message)
          } else {
            this.showNetworkErrorTip()
          }
        })
    },
    
    // 显示更多设置
    showOptionMenu() {
      weui.actionSheet([
        {
          label: '管理缓存配置',
          onClick: () => {
            this.$router.push('/privacy')
          }
        }, {
          label: '更新实时数据',
          onClick: () => {
            this.refreshGradeData()
          }
        }
      ], [
        {
          label: '取消',
          onClick: () => {}
        }
      ])
    }
  }
}
</script>

<style scoped>
/* 原有CSS样式 */
</style>