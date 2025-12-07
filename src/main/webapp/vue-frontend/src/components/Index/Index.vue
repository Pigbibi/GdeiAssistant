<template>
  <div class="weui-tab">
    <div class="weui-tab__panel">
      <div class="phone">
        <div class="tabPanelItem" v-show="activeTab === 0">
          <header>
            <h1>广东二师助手</h1>
            <h2>四年时光，广东二师助手陪你一起走过。</h2>
          </header>
          
          <div class="links">
            <div id="grade" @click="$router.push('/grade')" :style="{ display: showFunction('grade') ? 'block' : 'none' }">
              <i style="background: url(/img/function/grade.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>成绩查询</p>
            </div>
            <div id="schedule" @click="$router.push('/schedule')" :style="{ display: showFunction('schedule') ? 'block' : 'none' }">
              <i style="background: url(/img/function/schedule.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>课表查询</p>
            </div>
            <div id="cet" @click="$router.push('/cet')" :style="{ display: showFunction('cet') ? 'block' : 'none' }">
              <i style="background: url(/img/function/cet.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>四六级查询</p>
            </div>
            <div id="collection" @click="$router.push('/collection')" :style="{ display: showFunction('collection') ? 'block' : 'none' }">
              <i style="background: url(/img/function/collection.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>馆藏查询</p>
            </div>
            <div id="book" @click="$router.push('/book')" :style="{ display: showFunction('book') ? 'block' : 'none' }">
              <i style="background: url(/img/function/library.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>我的图书馆</p>
            </div>
            <div id="bill" @click="$router.push('/card')" :style="{ display: showFunction('bill') ? 'block' : 'none' }">
              <i style="background: url(/img/function/card.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>消费查询</p>
            </div>
            <div id="card" @click="$router.push('/cardinfo')" :style="{ display: showFunction('card') ? 'block' : 'none' }">
              <i style="background: url(/img/function/cardInfo.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>我的饭卡</p>
            </div>
            <div id="evaluate" @click="$router.push('/evaluate')" :style="{ display: showFunction('evaluate') ? 'block' : 'none' }">
              <i style="background: url(/img/function/evaluate.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>教学评价</p>
            </div>
            <div id="spare" @click="$router.push('/spare')" :style="{ display: showFunction('spare') ? 'block' : 'none' }">
              <i style="background: url(/img/function/spare.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>教室查询</p>
            </div>
            <div id="kaoyan" @click="$router.push('/kaoyan')" :style="{ display: showFunction('kaoyan') ? 'block' : 'none' }">
              <i style="background: url(/img/function/kaoyan.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>考研查询</p>
            </div>
            <div id="tice" @click="linkToPFTSystem()" :style="{ display: showFunction('tice') ? 'block' : 'none' }">
              <i style="background: url(/img/function/sport.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>体测查询</p>
            </div>
            <div id="news" @click="$router.push('/news')" :style="{ display: showFunction('news') ? 'block' : 'none' }">
              <i style="background: url(/img/function/news.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>新闻通知</p>
            </div>
            <div id="data" @click="$router.push('/data')" :style="{ display: showFunction('data') ? 'block' : 'none' }">
              <i style="background: url(/img/function/data.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>信息查询</p>
            </div>
            <div id="ershou" @click="$router.push('/ershou')" :style="{ display: showFunction('ershou') ? 'block' : 'none' }">
              <i style="background: url(/img/function/ershou.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>二手交易</p>
            </div>
            <div id="lostandfound" @click="$router.push('/lostandfound')" :style="{ display: showFunction('lostandfound') ? 'block' : 'none' }">
              <i style="background: url(/img/function/lostandfound.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>失物招领</p>
            </div>
            <div id="secret" @click="$router.push('/secret')" :style="{ display: showFunction('secret') ? 'block' : 'none' }">
              <i style="background: url(/img/function/secret.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>校园树洞</p>
            </div>
            <div id="photograph" @click="$router.push('/photograph')" :style="{ display: showFunction('photograph') ? 'block' : 'none' }">
              <i style="background: url(/img/function/photograph.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>拍好校园</p>
            </div>
            <div id="express" @click="$router.push('/express')" :style="{ display: showFunction('express') ? 'block' : 'none' }">
              <i style="background: url(/img/function/express.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>表白墙</p>
            </div>
            <div id="dating" @click="$router.push('/dating')" :style="{ display: showFunction('dating') ? 'block' : 'none' }">
              <i style="background: url(/img/function/dating.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>卖室友</p>
            </div>
            <div id="topic" @click="$router.push('/topic')" :style="{ display: showFunction('topic') ? 'block' : 'none' }">
              <i style="background: url(/img/function/topic.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>话题</p>
            </div>
            <div id="delivery" @click="$router.push('/delivery')" :style="{ display: showFunction('delivery') ? 'block' : 'none' }">
              <i style="background: url(/img/function/delivery.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>全民快递</p>
            </div>
            <div id="calendar" @click="window.location.href='http://msg.weixiao.qq.com/t/bca2e28bc30ce67907e032f483e82d7f'" :style="{ display: showFunction('calendar') ? 'block' : 'none' }">
              <i style="background: url(/img/function/calendar.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>学期校历</p>
            </div>
            <div id="government" @click="window.location.href='https://www.gdzwfw.gov.cn/portal/personal/hot'" :style="{ display: showFunction('government') ? 'block' : 'none' }">
              <i style="background: url(/img/function/goverment.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>政务服务</p>
            </div>
            <div id="student" @click="window.location.href='https://www.chsi.com.cn'" :style="{ display: showFunction('student') ? 'block' : 'none' }">
              <i style="background: url(/img/function/student.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>学信网</p>
            </div>
            <div id="volunteer" @click="window.location.href='https://www.gdzyz.cn/'" :style="{ display: showFunction('volunteer') ? 'block' : 'none' }">
              <i style="background: url(/img/function/volunteer.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>i志愿</p>
            </div>
            <div id="healthcode" @click="linkToYueKangCode()" :style="{ display: showFunction('healthcode') ? 'block' : 'none' }">
              <i style="background: url(/img/function/healthcode.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>粤康码</p>
            </div>
            <div id="travelcode" @click="window.location.href='https://xc.caict.ac.cn/'" :style="{ display: showFunction('travelcode') ? 'block' : 'none' }">
              <i style="background: url(/img/function/travelcode.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>通信行程码</p>
            </div>
            <div id="ncov" @click="window.location.href = 'https://ncov.dxy.cn/ncovh5/view/pneumonia'" :style="{ display: showFunction('ncov') ? 'block' : 'none' }">
              <i style="background: url(/img/function/ncov.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>疫情动态</p>
            </div>
            <div id="wechat" @click="$router.push('/wechat/attach')" :style="{ display: showFunction('wechat') ? 'block' : 'none' }">
              <i style="background: url(/img/function/wechat.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>绑定微信</p>
            </div>
            <div id="about" @click="$router.push('/about')" :style="{ display: showFunction('about') ? 'block' : 'none' }">
              <i style="background: url(/img/function/about.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
              <p>关于应用</p>
            </div>
          </div>
        </div>
        
        <div class="tabPanelItem" v-show="activeTab === 1">
          <!-- 资讯信息页面内容 -->
          <div>资讯信息</div>
        </div>
        
        <div class="tabPanelItem" v-show="activeTab === 2">
          <!-- 个人中心页面内容 -->
          <div>个人中心</div>
        </div>
      </div>
    </div>
    
    <div class="weui-tabbar">
      <a href="javascript:" @click="switchTabPanel(0)" :class="['weui-tabbar__item', { 'weui-bar__item_on': activeTab === 0 }]">
        <img src="/img/index/home.png" class="weui-tabbar__icon">
        <p class="weui-tabbar__label">功能主页</p>
      </a>
      <a href="javascript:" @click="switchTabPanel(1)" :class="['weui-tabbar__item', { 'weui-bar__item_on': activeTab === 1 }]">
        <img src="/img/index/info.png" class="weui-tabbar__icon">
        <p class="weui-tabbar__label">资讯信息</p>
      </a>
      <a href="javascript:" @click="switchTabPanel(2)" :class="['weui-tabbar__item', { 'weui-bar__item_on': activeTab === 2 }]">
        <img src="/img/index/personal.png" class="weui-tabbar__icon">
        <p class="weui-tabbar__label">个人中心</p>
      </a>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import weui from 'weui'
import $ from 'jquery'

export default {
  name: 'Index',
  data() {
    return {
      activeTab: 0,
      wechatUser: false,
      functionDisplaySetting: {},
      nickname: '',
      avatar: ''
    }
  },
  mounted() {
    this.checkUserBrowser()
    this.loadUserInfo()
    this.loadFunctionDisplaySetting()
  },
  methods: {
    // 检查用户浏览器属性
    checkUserBrowser() {
      // 检查是否为微信浏览器
      if (navigator.userAgent.toLowerCase().match(/MicroMessenger/i) == "micromessenger") {
        this.wechatUser = true
      }
    },
    
    // 加载用户信息
    loadUserInfo() {
      // 加载个人姓名信息
      axios.get('/api/profile')
        .then(result => {
          if (result.data.success === true) {
            this.nickname = result.data.data.nickname
          }
        })
        
      // 加载头像地址
      axios.get('/api/avatar')
        .then(result => {
          if (result.data.success === true && result.data.data !== '') {
            this.avatar = result.data.data
          }
        })
    },
    
    // 加载功能显示设置
    loadFunctionDisplaySetting() {
      if (localStorage.getItem("functionDisplaySetting")) {
        this.functionDisplaySetting = JSON.parse(localStorage.getItem("functionDisplaySetting"))
      }
    },
    
    // 判断功能是否显示
    showFunction(functionId) {
      if (!this.functionDisplaySetting || Object.keys(this.functionDisplaySetting).indexOf(functionId) === -1) {
        return true
      }
      return this.functionDisplaySetting[functionId]
    },
    
    // 切换Tab面板
    switchTabPanel(index) {
      this.activeTab = index
    },
    
    // 进行体测查询系统
    linkToPFTSystem() {
      if (this.wechatUser) {
        window.location.href = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxa2d196aa4b8a7600&redirect_uri=http%3A%2F%2F5itsn.com%2FWeixin%2FOAuth2%2FUserInfoCallback&response_type=code&scope=snsapi_userinfo&state=TestUrlTestResult&connect_redirect=1#wechat_redirect'
      } else {
        weui.alert('请关注微信公众号体适能使用体测查询功能', {
          title: '请使用微信客户端进行登录',
          buttons: [{ label: '确定', type: 'primary' }]
        })
      }
    },
    
    // 进入粤康码系统
    linkToYueKangCode() {
      if (this.wechatUser) {
        window.location.href = 'weixin://app/wxd930ea5d5a258f4f/jumpWxa/?userName=gh_1ac06b5a8f4e&path=operation_plus/pages/yiqing/daka/user/index/index.html'
      } else {
        weui.alert('请使用微信客户端访问以启用该功能', {
          title: '请使用微信客户端进行登录',
          buttons: [{ label: '确定', type: 'primary' }]
        })
      }
    }
  }
}
</script>

<style scoped>
/* 原有CSS样式将保持不变，由外部CSS文件提供 */
</style>