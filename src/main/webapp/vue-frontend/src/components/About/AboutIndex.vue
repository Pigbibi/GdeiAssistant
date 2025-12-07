<template>
  <div>
    <!-- 顶部导航栏 -->
    <div class="margin-bottom60">
      <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <a class="navbar-brand" href="/">广东二师助手</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
          <ul class="navbar-nav mr-auto">
            <li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle" href="javascript:" role="button"
                 data-toggle="dropdown"
                 aria-haspopup="true" aria-expanded="false">
                使用帮助
              </a>
              <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                <a class="dropdown-item" href="/about/security">安全技术规格说明</a>
                <a class="dropdown-item" href="/about/account">校园网络账号说明</a>
              </div>
            </li>
            <li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle" href="javascript:" role="button"
                 data-toggle="dropdown"
                 aria-haspopup="true" aria-expanded="false">
                协议与政策
              </a>
              <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                <a class="dropdown-item" href="/agreement">用户协议</a>
                <a class="dropdown-item" href="/policy/privacy">隐私政策</a>
                <a class="dropdown-item" href="/policy/social">社区准则</a>
                <a class="dropdown-item" href="/license">开源协议</a>
                <a class="dropdown-item" href="/policy/cookie">Cookie政策</a>
                <a class="dropdown-item" href="/policy/intellectualproperty">知识产权声明</a>
              </div>
            </li>
            <li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle" href="javascript:" role="button"
                 data-toggle="dropdown"
                 aria-haspopup="true" aria-expanded="false">
                友情链接
              </a>
              <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                <a class="dropdown-item" href="http://www.moe.gov.cn">教育部</a>
                <a class="dropdown-item" href="https://edu.gd.gov.cn">广东省教育厅</a>
                <a class="dropdown-item" href="http://jyj.gz.gov.cn">广州市教育局</a>
                <a class="dropdown-item" href="https://www.gd.gov.cn">广东省人民政府</a>
                <a class="dropdown-item" href="http://www.gz.gov.cn">广州市人民政府</a>
                <a class="dropdown-item" href="http://www.edu.cn">中国教育科研网</a>
                <a class="dropdown-item" href="https://www.cnki.net">中国知网</a>
                <a class="dropdown-item" href="https://www.chsi.com.cn">学信网</a>
                <div class="dropdown-divider"></div>
                <a class="dropdown-item" href="https://www.scut.edu.cn">华南理工大学</a>
                <a class="dropdown-item" href="https://www.sysu.edu.cn">中山大学</a>
                <a class="dropdown-item" href="https://www.jnu.edu.cn">暨南大学</a>
                <a class="dropdown-item" href="https://www.scnu.edu.cn">华南师范大学</a>
                <a class="dropdown-item" href="http://www.gdei.edu.cn">广东第二师范学院</a>
              </div>
            </li>
            <li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle" href="javascript:" role="button"
                 data-toggle="dropdown"
                 aria-haspopup="true" aria-expanded="false">
                智慧党建
              </a>
              <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                <a class="dropdown-item" href="http://cpc.people.com.cn/index.html">中国共产党新闻网</a>
                <a class="dropdown-item"
                   href="http://chuxin.people.cn/GB/index.html">“不忘初心、牢记使命”主题教育</a>
                <a class="dropdown-item"
                   href="http://cpc.people.com.cn/GB/67481/431601/index.html">人民战“疫”党旗飘扬</a>
                <a class="dropdown-item" href="http://dangshi.people.cn">党史学习教育</a>
                <a class="dropdown-item"
                   href="http://dangjian.people.com.cn/GB/136058/447038/index.html">我奋斗家国美</a>
                <a class="dropdown-item" href="http://cpc.people.com.cn/20th">二十大专题报道</a>
              </div>
            </li>
          </ul>
        </div>
      </nav>
    </div>

    <!-- 主体内容 -->
    <div class="banxin">
      <div class="app_logo margin-bottom60">
        <img src="/img/about/application/logo.png" width="120" height="120">
      </div>

      <!-- 功能模块启用状态检查结果提示信息 仅开发模式下显示 -->
      <div class="function-module-check-result margin-bottom60">
        <text class='core-module-check-result-text'></text>
        <text class='module-check-result-text'></text>
      </div>

      <div class="main-function-items-area">
        <div class="login-items" onclick="window.location.href='/login'">
          <div>
            <strong>
              <a href="javascript:">进入系统</a>
            </strong>
          </div>
        </div>
      </div>

      <hr class="margin-bottom60">
      <div class="desp">
        <h2>应用介绍</h2>
        <div class="dsp margin-bottom60">
          <section>
            广东第二师范学院校园助手，是为广东第二师范学院专属打造的校园服务应用。它不仅提供了课表查询、成绩查询、四六级考试成绩查询、空课室查询、图书借阅查询、馆藏图书查询、教学质量评价、电费查询、黄页信息查询、校园卡充值、校园卡挂失、消费查询等综合性的教务功能，还提供了二手交易、失物招领、校园树洞、恋爱交友、表白墙、全民快递、拍好校园、话题、匿名评教等社区交流平台。广东二师助手旨在为广东第二师范学院的在校师生们提供最优质的教育教学、校园生活、社团活动、文化娱乐和教务服务等信息。四年时光，广东二师助手陪你一起走过。
          </section>
        </div>
      </div>
      <hr class="margin-bottom60">

      <div>
        <h2>应用截图</h2>
        <div class="table-responsive">
          <table class="table">
            <tbody>
              <tr>
                <td>
                  <div>
                    <img src="/img/about/application/preview_0.jpg">
                  </div>
                </td>
                <td>
                  <div>
                    <img src="/img/about/application/preview_1.jpg">
                  </div>
                </td>
                <td>
                  <div>
                    <img src="/img/about/application/preview_2.jpg">
                  </div>
                </td>
                <td>
                  <div>
                    <img src="/img/about/application/preview_3.jpg">
                  </div>
                </td>
                <td>
                  <div>
                    <img src="/img/about/application/preview_4.jpg">
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <hr class="margin-bottom60">
    </div>

    <!-- 底部信息 -->
    <div class="foot">
      <div class="media">
        <img src="/img/about/media/wechat.png" >
        <img src="/img/about/media/weibo.png" >
        <img src="/img/about/media/bilibili.png" >
        <img src="/img/about/media/xiaohongshu.png" >
        <img src="/img/about/media/douyin.png" >
      </div>
      <p>Copyright © 2016 - 2023 GdeiAssistant</p>
      <p>All rights reserved</p>
      <div class="beian_area">
        <p class="beian_p"><a class="beian" href="http://www.beian.miit.gov.cn">粤ICP备17087427号-1</a></p>
        <p class="beian_p"><a class="beian"
                              href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=44010502001297">粤公网安备44010502001297号</a>
        </p>
      </div>
    </div>

    <!-- Cookie提示 -->
    <div id="rnw_cookies_banner" style="display: none">
      <a class="spritefont-close" id="rnw_cookies_banner_button_close" title="关闭"
         @click="hideCookieBanner" href="javascript:">×</a>
      <div id="rnw_cookies_banner_text">
        本网站使用Cookie的目的是为您提供更加简捷和个性化的上网体验。Cookie将有用的信息存储在您的电脑上，可帮助我们改善您浏览我们网站的效率以及对您的实用性。在某些情况下，它们是网站正常运行所必不可少的。如果您访问本网站，即表示您同意我们使用Cookie。
        请点击<a href="/policy/cookie" id="rnw_cookies_banner_button_more">Cookie政策</a>了解更多信息。
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AboutIndex',
  mounted() {
    // 检测浏览器窗口大小并显示对应下载页
    if (document.body.clientWidth > 500) {
      document.getElementById('desktop').style.display = 'block';
    } else {
      document.getElementById('phone').style.display = 'block';
    }

    // 弹出Cookie政策提示
    if (!localStorage.getItem("cookiePolicy")) {
      document.getElementById('rnw_cookies_banner').style.display = 'block';
    }
  },
  methods: {
    hideCookieBanner() {
      document.getElementById('rnw_cookies_banner').style.display = 'none';
      localStorage.setItem('cookiePolicy', 1);
    }
  }
}
</script>

<style scoped>
/* 保持原有CSS不变，这里可以引入外部CSS文件 */
</style>