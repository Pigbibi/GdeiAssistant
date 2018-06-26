<%--
  Created by IntelliJ IDEA.
  User: gdeiassistant
  Date: 2017/8/27
  Time: 20:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>图书详细信息</title>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <!-- 如果使用双核浏览器，强制使用webkit来进行页面渲染 -->
    <meta name="renderer" content="webkit"/>
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <link rel="stylesheet" href="/css/common/weui-1.1.1.min.css">
    <link rel="stylesheet" href="/css/common/common.css">
    <link rel="stylesheet" href="/css/common/weui-0.2.2.min.css">
    <script type="application/javascript" src="/js/common/fastclick.js"></script>
    <script>
        //消除iOS点击延迟
        $(function () {
            FastClick.attach(document.body);
        });
    </script>
</head>
<body>

<%-- 不缓存页面 --%>
<%
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Cache-Control", "must-revalidate");
%>

<div class="hd">
    <h1 class="page_title">图书详细信息</h1>
    <p class="page_desc">广东第二师范学院移动图书馆</p>
</div>

<c:choose>

    <c:when test="${QueryErrorMessage==null and fn:length(CollectionDetail.collectionDistributionList)>0}">

        <div id="content">

            <div class="weui-msg">
                <div class="weui-msg__icon-area"><i class="weui-icon-success weui-icon_msg"></i></div>
                <div class="weui-msg__text-area">
                    <h2 class="weui-msg__title">详细信息</h2>
                    <p class="weui-msg__desc">${CollectionDetail.detailContent}</p>
                </div>
            </div>

            <div class="weui-cells__title" style="font-size: 16px">馆藏分布情况</div>

            <c:forEach items="${CollectionDetail.collectionDistributionList}" var="collectionDistributionList">

                <div class="weui-cells">
                    <div class="weui-cell">
                        <div class="weui-cell__bd">
                            <p>馆藏地</p>
                        </div>
                        <div class="weui-cell__ft">${collectionDistributionList.location}</div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__bd">
                            <p>索取号</p>
                        </div>
                        <div class="weui-cell__ft">${collectionDistributionList.callNumber}</div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__bd">
                            <p>登录号</p>
                        </div>
                        <div class="weui-cell__ft">${collectionDistributionList.accessionNumber}</div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__bd">
                            <p>状态</p>
                        </div>
                        <div class="weui-cell__ft">${collectionDistributionList.state}</div>
                    </div>
                </div>

            </c:forEach>

            <div class="weui-msg__opr-area">
                <p class="weui-btn-area">
                    <a href="javascript:window.history.back(-1);" class="weui-btn weui-btn_primary">返回</a>
                </p>
            </div>

        </div>
    </c:when>

    <c:otherwise>

        <div id="tip">

            <div class="weui-msg">
                <div class="weui-msg__icon-area"><i class="weui-icon-warn weui-icon_msg"></i></div>
                <div class="weui-msg__text-area">
                    <h2 class="weui-msg__title">查询详细信息失败</h2>
                    <p class="weui-msg__desc">${QueryErrorMessage}</p>
                </div>
            </div>

            <div class="weui-msg__opr-area">
                <p class="weui-btn-area">
                    <a href="javascript:window.history.back(-1);" class="weui-btn weui-btn_primary">返回</a>
                </p>
            </div>

        </div>

    </c:otherwise>

</c:choose>

</body>
</html>