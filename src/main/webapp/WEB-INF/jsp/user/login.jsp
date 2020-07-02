<%@ page language="java" import="java.util.*"  contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <title>登录页面</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath }/static/css/bootstrap.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath }/static/css/1.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/static/css/verify.css">
</head>
<body> 
<div class="page-header">
    <h1>用户登录</h1>
</div>
<form id="user" action="" method="">
    <div class="form-group">
        <span style="color:red;margin-left: 50px;" id="loginInfo"></span>
    </div>
    <div class="form-group">
        <label for="username">用户名:</label>
        <input type="text" class="form-control" id="username" name="username" placeholder="请输入用户名" />
    </div>
    <div class="form-group">
        <label for="password">密码:</label>
        <input type="password" class="form-control" id="password" name="password" placeholder="请输入密码" />
    </div>

    <div class="form-group">
        <label for="loginCode">验证码:</label>
        <div id="mpanel3" style="margin-top: 5px;margin-left: 50px;"></div>
        <input type="text" class="form-control" id="loginCode"  placeholder="请输入结果" />
    </div>

    <button type="button" id="check-btn2" class="btn btn-success">登录</button>
    &nbsp;&nbsp;&nbsp;
    <button type="button" onclick="location.href = '${pageContext.request.contextPath}/user/register-ui'" class="btn btn-primary">注册账号</button>
</form>

</body>
<script type="text/javascript" src="${pageContext.request.contextPath }/static/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/static/js/verify.js" ></script>
<%--<script type="text/javascript" src="${pageContext.request.contextPath }/static/js/bootstrap.js"></script>--%>
<script type="text/javascript">
	//验证码..
    $('#mpanel3').codeVerify({
        type : 2,
        figure : 10,	//位数，仅在type=2时生效
        arith : 0,	//算法，支持加减乘，不填为随机，仅在type=2时生效
        width : '200px',
        height : '50px',
        fontSize : '30px',
        btnId : 'check-btn2',
        success : function() {
            //-------------执行登录.---------------------
            // 获取用户名和密码
            var data = $("#user").serialize();
            // 发送ajax请求
            $.post(
                "${pageContext.request.contextPath}/user/login",
                data,
                function (result) {
                    if (result.code == 0){
                        // 登录成功，跳转item首页
                        location.href = "${pageContext.request.contextPath}/item/list";
                    }else {
                        // 登录失败，给用户提示信息
                        $("#loginInfo").text(result.msg);
                    }
                },
                "json"
            );
        },
        error : function() {
            alert('验证码不匹配！');
        }
    });
</script>
</html>