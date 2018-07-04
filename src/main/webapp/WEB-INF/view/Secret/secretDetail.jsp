<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: gdeiassistant
  Date: 2017/11/19
  Time: 20:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>广东第二师范学院树洞</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0,  minimum-scale=1.0, maximum-scale=1.0">
    <link rel="stylesheet" href="/css/secret/secret-detail.css">
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

<div class="weui_cells_title" onclick="javascript:window.location.href='/secret'">返回</div>

<div class="all">

    <!-- 树洞信息 -->
    <div id="${Secret.id}" class="secret theme${Secret.theme}">
        <section>${Secret.content}</section>
        <footer>
            <div>
                <c:choose>
                    <c:when test="${Secret.liked==1}">
                        <i class="good"></i>
                    </c:when>
                    <c:otherwise>
                        <i class="pregood"></i>
                    </c:otherwise>
                </c:choose>
                <span>${Secret.likeCount}</span>
            </div>
            <div><i class="comment"></i>${Secret.commentCount}</div>
        </footer>
    </div>

    <!-- 树洞信息评论 -->
    <c:forEach var="SecretComment" items="${Secret.secretCommentList}" varStatus="status">
        <div class="discuss">
            <img src="/img/avatar/${SecretComment.avatarTheme}.png" alt="">
            <div class="info">
                <p>${SecretComment.comment}</p>
                <span>${status.index+1}楼 ${SecretComment.publishTime}</span>
            </div>
        </div>
    </c:forEach>

</div>

<div class="form">
    <input type="text" name="comment" placeholder="匿名评论">
    <div class="submit">发布</div>
</div>

<!-- 错误提示，显示时用$.show();隐藏时用$.hide(); -->
<div class="weui_toptips weui_warn js_tooltips"></div>

<script type="text/javascript" src="/js/common/jquery-3.2.1.min.js"></script>
<script type="text/javascript">

    $(".secret").height($(".secret").width() * 0.8);

    $(".secret section").each(function () {
        $(this).css({
            "padding-top": ($(".secret").height() - $(this).innerHeight()) / 2,
            "padding-bottom": ($(".secret").height() - $(this).innerHeight()) / 2
        });
    });

    $(".form").on("keyup", "input", function () {
        if ($(this).val().length > 0) {
            $(this).css("width", "80%").next("div").show();
        } else {
            $(this).css("width", "100%").next("div").hide();
        }
    });

    //点赞或取消点赞
    $("body").on("click", ".pregood", function () {
        $e = $(this);
        $.post("/rest/secret/id/" + $(this).closest(".secret").attr("id") + "/like", {"like": "1"}, function (data) {
            if (data.success === true) {
                $e.removeClass("pregood").addClass("good").next("span").text(parseInt($e.next("span").text()) + 1);
            }
        }, "json");
    }).on("click", ".good", function () {
        $e = $(this);
        $.post("/rest/secret/id/" + $(this).closest(".secret").attr("id") + "/like", {"like": "0"}, function (data) {
            if (data.success === true) {
                $e.removeClass("good").addClass("pregood").next("span").text(parseInt($e.next("span").text()) - 1);
            }
        }, "json");
    });

    //提交评论
    $("body").on("click", ".submit", function () {
        if ($("input[name='comment']").val().length > 50) {
            $(".weui_warn").text("评论内容不能超过50字").show().delay(2000).hide(0);
            return false;
        }
        $.post("/rest/secret/id/${Secret.id}/comment", {
            "comment": $("input[name='comment']").val()
        }, function (data) {
            if (data.success === true) {
                window.window.location.reload();
            }
            else {
                $(".weui_warn").text(data.errorMessage).show().delay(2000).hide(0);
            }
        }, "json");
    });

</script>

</body>
</html>