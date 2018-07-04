<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: gdeiassistant
  Date: 2018/1/4
  Time: 01:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>广东二师助手</title>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
    <meta name="keywords" content="二师助手,广二师助手,广东二师助手,广东第二师范学院">
    <meta name="description"
          content="广东二师助手是为广东第二师范学院专属打造的校园服务应用，不仅提供了课表查询、成绩查询、四六级考试成绩查询、空课室查询、图书借阅查询、馆藏图书查询、校园卡充值、校园卡挂失、消费查询等综合性的教务功能，还提供了二手交易、失物招领、校园树洞、恋爱交友、表白墙、全民快递、话题等社区交流平台。为广东第二师范学院的校友们带来便携的教务、社交服务，给学生们提供最快最便捷获取校园生活、社团、信息的方式。四年时光，广东二师助手陪你一起走过。">
    <link rel="stylesheet" href="/css/index/index.css">
    <link rel="stylesheet" href="/css/common/weui-1.1.1.min.css">
    <link rel="stylesheet" href="/css/common/weui-0.2.2.min.css">
    <script type="text/javascript" src="/js/common/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="/js/common/weui.min.js"></script>
    <script type="application/javascript" src="/js/common/fastclick.js"></script>
    <script type="text/javascript">

        //消除iOS点击延迟
        $(function () {
            FastClick.attach(document.body);
        });

        //加载个人姓名信息和头像地址
        $(function () {
            $.ajax({
                url: '/rest/profile',
                async: true,
                type: 'get',
                success: function (result) {
                    if (result.success === true) {
                        let realname = result.data.realname == null ? "暂未录入" : result.data.realname;
                        $("#right_name").text(realname);
                    }
                }
            });
            $.ajax({
                url: '/rest/avatar',
                async: true,
                type: 'get',
                success: function (result) {
                    if (result.success === true) {
                        if (result.data !== '') {
                            $("#right_avatar").attr("src", result.data);
                        }
                    }
                }
            });
            //隐藏部分不可用功能
            let functionSize = $("[class='links']").find("div").length;
            if (navigator.userAgent.toLowerCase().match(/MicroMessenger/i) == "micromessenger") {
                //显示微信功能
                $("#wechat").show();
                $("#tice").show();
            }
            <c:if test="${sessionScope.yiBanUserID!=null}">
            //易班授权登录，显示绑定易班菜单
            $("#yiban").show();
            </c:if>
            //重新调整单元格边框属性
            var j = 0;
            for (var i = 0; i < functionSize; i++) {
                if (!$("[class='links']").find("div").eq(i).is(":hidden")) {
                    if ((j + 1) % 1 == 0) {
                        $("[class='links']").find("div").eq(i).css("border-right", "1px solid #E2E0E3")
                    }
                    else if ((j + 1) % 2 == 0) {
                        $("[class='links']").find("div").eq(i).css("border-right", "1px solid #E2E0E3")
                    }
                    if ((j + 1) % 3 == 0) {
                        $("[class='links']").find("div").eq(i).css("border-right", "0px");
                    }
                    j++;
                }
            }
        });

        //弹出重新绑定易班确认框
        function showYibanAttachConfirm() {
            weui.confirm('重新绑定易班账号将退出当前账号，你确定吗？', {
                title: '重新绑定易班',
                buttons: [{
                    label: '取消',
                    type: 'default'
                }, {
                    label: '确定',
                    type: 'primary',
                    onClick: function () {
                        window.location.href = '/yiban/attach';
                    }
                }]
            });
        }

        //弹出退出确认框
        function showLogoutConfirm() {
            weui.confirm('你确定退出当前账号并清除账号缓存吗？', {
                title: '退出当前账号',
                buttons: [{
                    label: '取消',
                    type: 'default'
                }, {
                    label: '退出',
                    type: 'primary',
                    onClick: function () {
                        window.location.href = '/logout';
                    }
                }]
            });
        }

    </script>
</head>
<body>

<div class="phone">

    <div onclick="window.location.href='/profile'" style="margin-top:10px;float: right">
        <p id="right_name" style="margin-left:5px;margin-right:5px;color: #888;display: block;float: right"></p>
        <img id="right_avatar" style="float:right;border-radius: 50%;width: 25px;height: 25px;"
             src="/img/avatar/default.png"/>
    </div>

    <header style="margin-top:45px;clear: both">
        <div>
            <h1>广东二师助手</h1>
            <h2>四年时光，广东二师助手陪你一起走过。</h2>
        </div>
    </header>

    <div class="links">
        <div onclick="window.location.href='/grade'">
            <i style="background: url(/img/function/grade.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
            <p>成绩查询</p>
        </div>
        <div onclick="window.location.href='/schedule'">
            <i style="background: url(/img/function/schedule.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
            <p>课表查询</p>
        </div>
        <div onclick="window.location.href='/cet'">
            <i style="background: url(/img/function/cet.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
            <p>四六级查询</p>
        </div>
        <div onclick="window.location.href='/collection'">
            <i style="background: url(/img/function/collection.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
            <p>馆藏查询</p>
        </div>
        <div onclick="window.location.href='/card'">
            <i style="background: url(/img/function/card.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
            <p>消费查询</p>
        </div>
        <div onclick="window.location.href='/cardinfo'">
            <i style="background: url(/img/function/cardInfo.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
            <p>我的饭卡</p>
        </div>
        <div onclick="window.location.href='/evaluate'">
            <i style="background: url(/img/function/evaluate.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
            <p>一键评教</p>
        </div>
        <div onclick="window.location.href='/spare'">
            <i style="background: url(/img/function/spare.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
            <p>教室查询</p>
        </div>
        <div onclick="window.location.href='/kaoyan'">
            <i style="background: url(/img/function/kaoyan.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
            <p>考研查询</p>
        </div>
        <div onclick="window.location.href='/ershou'">
            <i style="background: url(/img/function/ershou.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
            <p>二手交易</p>
        </div>
        <div onclick="window.location.href='/lostandfound'">
            <i style="background: url(/img/function/lostandfound.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
            <p>失物招领</p>
        </div>
        <div onclick="window.location.href='/secret'">
            <i style="background: url(/img/function/secret.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
            <p>校园树洞</p>
        </div>
        <div onclick="window.location.href='/dating'">
            <i style="background: url(/img/function/dating.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
            <p>恋爱交友</p>
        </div>
        <div onclick="window.location.href='http://msg.weixiao.qq.com/t/7b9106e19588a57898465736a60ca7f2'">
            <i style="background: url(/img/function/calendar.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
            <p>学期校历</p>
        </div>
        <div onclick="window.location.href='/download'">
            <i style="background: url(/img/function/download.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
            <p>客户端下载</p>
        </div>
        <div id="tice" style="display: none"
             onclick="window.location.href='https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxa2d196aa4b8a7600&redirect_uri=http%3A%2F%2F5itsn.com%2FWeixin%2FOAuth2%2FUserInfoCallback&response_type=code&scope=snsapi_userinfo&state=TestUrlTestResult&connect_redirect=1#wechat_redirect'">
            <i style="background: url(/img/function/sport.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
            <p>体测查询</p>
        </div>
        <div id="wechat" style="display: none" onclick="window.location.href='/wechat/attach'">
            <i style="background: url(/img/function/wechat.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
            <p>绑定微信</p>
        </div>
        <div id="yiban" style="display: none" onclick="showYibanAttachConfirm()">
            <i style="background: url(/img/function/yiban.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
            <p>绑定易班</p>
        </div>
        <div onclick="showLogoutConfirm()">
            <i style="background: url(/img/function/exit.png) ; background-repeat: no-repeat; background-position: center; background-size: 85% auto"></i>
            <p>安全退出</p>
        </div>
    </div>
</div>

<br><br>

</body>
</html>