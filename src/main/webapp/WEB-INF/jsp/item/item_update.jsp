<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改商品信息</title>
<link rel="stylesheet" href="${pageContext.request.contextPath }/static/css/bootstrap.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath }/static/css/2.css"/>
</head>
<body> 
<div class="page-header">
    <h1>修改商品</h1>
    <span id="updateInfo" style="margin-left:200px;color: red"></span>
</div>
<form action="" method="" >
	<input type="hidden" name="id" id="id" value="${item.id}" />
    <table class="table table-hover">
        <tr class="active">
            <td>  <label for="name">商品名称:</label></td>
            <td>
                <input type="text" value="${item.name}" class="form-control" name="name" id="name" placeholder="商品名称">
            </td>
        </tr>
        <tr class="success">
            <td><label for="price">商品价格:</label></td>
            <td>
                <input type="text" value="${item.price}" class="form-control" name="price" id="price" placeholder="商品名称">
            </td>
        </tr>
        <tr class="success">
            <td><label for="productionDate">生产日期:</label></td>
            <td>
                <input type="text"  name="productionDate" id="productionDate" value="${item.productionDate}"
                       data-options="{'type':'YYYY-MM-DD','beginYear':2010,'endYear':2088}"class="form-control" placeholder="请输入生产日期" />
            </td>
        </tr>
        <tr class="warning">
            <td><label for="description">商品简介:</label></td>
            <td>
                <div class="form-group">
                    <textarea id="description"style="resize: none" name="description" class="form-control" rows="3"placeholder="请描述商品">${item.description}</textarea>
                </div>
            </td>
        </tr>
        <tr class="info">
            <td><label for="picFile">上传图片:</label></td>
            <td>
                <div id="preview">
                    <div class="img">
                        <img src="${item.pic}" />
                    </div>
                </div>
                <div class="form-group">
                    <input type="file" onchange="preview(this)" name="picFile" id="picFile">
                </div>
            </td>
        </tr>
        <tr class="danger">
            <td colspan="2">
                <input type="button" class="btn btn-danger" onclick="update()" value="修改商品"/>
            </td>
        </tr>
    </table>
	</form>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath }/static/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/static/js/jquery.date.js"></script>
<script>

    // 提交当前form表单
    function update(){
        // 封装数据
        var formData = new FormData();
        formData.append("id",$("#id").val())
        formData.append("picFile",$("#picFile")[0].files[0]);
        formData.append("name",$("#name").val());
        formData.append("price",$("#price").val());
        formData.append("productionDate",$("#productionDate").val());
        formData.append("description",$("#description").val());

        // alert($("#id").val());
        // preview(file);

        // $.date('#productionDate');

        // 发送ajax请求
        $.ajax({
            url:"${pageContext.request.contextPath}/item/update",
            type:"post",
            data:formData,
            dateType:"json",
            success:function (result) {
                if (result.code == 0){
                    location.href = "${pageContext.request.contextPath}/item/list";
                }else {
                    $("#updateInfo").html(result.msg);
                }
            },
            error:function () {
                console.log("服务器出错了！");
            },
            contentType:false,
            processData:false
        });
    }

    $.date('#productionDate');
    function preview(file){
        var prevDiv = document.getElementById('preview');
        if (file.files && file.files[0]){
            reader = new FileReader();
            reader.onload = function(evt){
                prevDiv.innerHTML = '<img src="' + evt.target.result + '" />';
            }
            reader.readAsDataURL(file.files[0]);
        }else{
            prevDiv.innerHTML = '<div class="img" style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src=\'' + file.value + '\'"></div>';
        }
    }
</script>
</html>