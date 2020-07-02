<%--
  Created by IntelliJ IDEA.
  User: Bruce
  Date: 2020-06-30
  Time: 22:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>RESTful</title>
</head>
<body>

    <form action="${pageContext.request.contextPath}/rest/api" method="post">

        <input type="hidden" name="_method" value="put">

        <input name="name" value="徐凤年">

        <button>提交</button>
    </form>

</body>
</html>
