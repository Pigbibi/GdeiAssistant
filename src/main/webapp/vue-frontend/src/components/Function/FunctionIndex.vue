<template>
  <div>
    <header>
      <h1>广东二师助手</h1>
      <h2>四年时光，广东二师助手陪你一起走过。</h2>
    </header>

    <div class="links">
      <div id="grade" v-show="functionVisible['grade']" @click="$router.push('/grade')">
        <i style="background: url(/img/function/grade.png); background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
        <p>成绩查询</p>
      </div>
      <div id="schedule" v-show="functionVisible['schedule']" @click="$router.push('/schedule')">
        <i style="background: url(/img/function/schedule.png); background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
        <p>课表查询</p>
      </div>
      <div id="cet" v-show="functionVisible['cet']" @click="$router.push('/cet')">
        <i style="background: url(/img/function/cet.png); background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
        <p>四六级查询</p>
      </div>
      <div id="collection" v-show="functionVisible['collection']" @click="$router.push('/collection')">
        <i style="background: url(/img/function/collection.png); background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
        <p>馆藏查询</p>
      </div>
      <div id="book" v-show="functionVisible['book']" @click="$router.push('/book')">
        <i style="background: url(/img/function/library.png); background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
        <p>我的图书馆</p>
      </div>
      <div id="bill" v-show="functionVisible['bill']" @click="$router.push('/card')">
        <i style="background: url(/img/function/card.png); background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
        <p>消费查询</p>
      </div>
      <div id="card" v-show="functionVisible['card']" @click="$router.push('/cardinfo')">
        <i style="background: url(/img/function/cardInfo.png); background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
        <p>我的饭卡</p>
      </div>
      <div id="evaluate" v-show="functionVisible['evaluate']" @click="$router.push('/evaluate')">
        <i style="background: url(/img/function/evaluate.png); background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
        <p>教学评价</p>
      </div>
      <div id="spare" v-show="functionVisible['spare']" @click="$router.push('/spare')">
        <i style="background: url(/img/function/spare.png); background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
        <p>教室查询</p>
      </div>
      <div id="kaoyan" v-show="functionVisible['kaoyan']" @click="$router.push('/kaoyan')">
        <i style="background: url(/img/function/kaoyan.png); background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
        <p>考研查询</p>
      </div>
      <div id="tice" v-show="functionVisible['tice']" @click="linkToPFTSystem">
        <i style="background: url(/img/function/sport.png); background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
        <p>体测查询</p>
      </div>
      <div id="news" v-show="functionVisible['news']" @click="$router.push('/news')">
        <i style="background: url(/img/function/news.png); background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
        <p>新闻通知</p>
      </div>
      <div id="data" v-show="functionVisible['data']" @click="$router.push('/data')">
        <i style="background: url(/img/function/data.png); background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
        <p>信息查询</p>
      </div>
      <div id="ershou" v-show="functionVisible['ershou']" @click="$router.push('/ershou')">
        <i style="background: url(/img/function/ershou.png); background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
        <p>二手交易</p>
      </div>
      <div id="lostandfound" v-show="functionVisible['lostandfound']" @click="$router.push('/lostandfound')">
        <i style="background: url(/img/function/lostandfound.png); background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
        <p>失物招领</p>
      </div>
      <div id="secret" v-show="functionVisible['secret']" @click="$router.push('/secret')">
        <i style="background: url(/img/function/secret.png); background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
        <p>校园树洞</p>
      </div>
      <div id="photograph" v-show="functionVisible['photograph']" @click="$router.push('/photograph')">
        <i style="background: url(/img/function/photograph.png); background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
        <p>拍好校园</p>
      </div>
      <div id="express" v-show="functionVisible['express']" @click="$router.push('/express')">
        <i style="background: url(/img/function/express.png); background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
        <p>表白墙</p>
      </div>
      <div id="dating" v-show="functionVisible['dating']" @click="$router.push('/dating')">
        <i style="background: url(/img/function/dating.png); background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
        <p>卖室友</p>
      </div>
      <div id="topic" v-show="functionVisible['topic']" @click="$router.push('/topic')">
        <i style="background: url(/img/function/topic.png); background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
        <p>话题</p>
      </div>
      <div id="delivery" v-show="functionVisible['delivery']" @click="$router.push('/delivery')">
        <i style="background: url(/img/function/delivery.png); background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
        <p>快递代拿</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FunctionIndex',
  data() {
    return {
      functionVisible: {},
      wechatUser: false,
      yibanUser: false
    };
  },
  mounted() {
    this.checkBrowser();
    this.loadProfile();
    this.loadFunctionDisplaySetting();
  },
  methods: {
    checkBrowser() {
      // 检查是否为微信浏览器
      if (navigator.userAgent.toLowerCase().match(/MicroMessenger/i) == "micromessenger") {
        this.wechatUser = true;
      }
    },
    loadProfile() {
      // 加载个人姓名信息
      this.$axios.get('/api/profile')
        .then(response => {
          if (response.data.success) {
            document.getElementById('right_name').textContent = response.data.data.nickname;
          }
        });
      
      // 加载头像地址
      this.$axios.get('/api/avatar')
        .then(response => {
          if (response.data.success && response.data.data) {
            document.getElementById('right_avatar').setAttribute('src', response.data.data);
          }
        });
    },
    loadFunctionDisplaySetting() {
      const displaySetting = localStorage.getItem("functionDisplaySetting");
      const functions = ['grade', 'schedule', 'cet', 'collection', 'book', 'bill', 'card', 'evaluate', 'spare', 'kaoyan', 'tice', 'news', 'data', 'ershou', 'lostandfound', 'secret', 'photograph', 'express', 'dating', 'topic', 'delivery'];
      
      if (displaySetting) {
        const setting = JSON.parse(displaySetting);
        functions.forEach(func => {
          this.functionVisible[func] = (setting[func] !== undefined) ? setting[func] : true;
        });
      } else {
        functions.forEach(func => {
          this.functionVisible[func] = true;
        });
      }
      
      this.$nextTick(() => {
        this.adjustCellBorders();
      });
    },
    adjustCellBorders() {
      // 调整单元格边框属性
      const visibleDivs = document.querySelectorAll('.links div:visible');
      let j = 0;
      
      visibleDivs.forEach(div => {
        j++;
        if (j % 3 === 0) {
          div.style.borderRight = '0px';
        } else {
          div.style.borderRight = '1px solid #E2E0E3';
        }
      });
      
      // 如果显示的功能菜单不足三个，则隐藏第一行对应的上边框
      if (visibleDivs.length < 3) {
        const linksContainer = document.querySelector('.phone .links');
        if (linksContainer) {
          linksContainer.style.borderTop = '0px';
        }
        
        visibleDivs.forEach(div => {
          div.style.borderTop = '1px solid #E2E0E3';
        });
      }
    },
    linkToPFTSystem() {
      if (this.wechatUser) {
        window.location.href = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxa2d196aa4b8a7600&redirect_uri=http%3A%2F%2F5itsn.com%2FWeixin%2FOAuth2%2FUserInfoCallback&response_type=code&scope=snsapi_userinfo&state=TestUrlTestResult&connect_redirect=1#wechat_redirect';
      } else {
        this.$toast("请关注微信公众号体适能使用体测查询功能");
      }
    },
    linkToYueKangCode() {
      if (this.wechatUser) {
        window.location.href = 'weixin://app/wxd930ea5d5a258f4f/jumpWxa/?userName=gh_1ac06b5a8f4e&path=operation_plus/pages/yiqing/daka/user/index/index.html';
      } else {
        this.$toast("请使用微信客户端访问以启用该功能");
      }
    }
  }
};
</script>

<style scoped>
/* 保持原有的CSS样式不变 */
header {
  text-align: center;
  padding: 20px 0;
}

header h1 {
  font-size: 24px;
  margin: 0 0 10px 0;
}

header h2 {
  font-size: 14px;
  color: #888;
  margin: 0;
}

.links {
  display: flex;
  flex-wrap: wrap;
  border-top: 1px solid #E2E0E3;
}

.links div {
  width: 33.33%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px 0;
  border-right: 1px solid #E2E0E3;
  border-bottom: 1px solid #E2E0E3;
}

.links div i {
  width: 60px;
  height: 60px;
  margin-bottom: 10px;
}

.links div p {
  margin: 0;
  font-size: 14px;
}
</style>