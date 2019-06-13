<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>用户列表</title>
    <script src="${ctx}/webjars/jquery/3.3.1-2/jquery.min.js"></script>
    <!-- DataTables CSS -->
    <link rel="stylesheet" type="text/css" href="http://cdn.datatables.net/1.10.15/css/jquery.dataTables.css">
    <!-- DataTables -->
    <script type="text/javascript" charset="utf8" src="http://cdn.datatables.net/1.10.15/js/jquery.dataTables.js"></script>
</head>
<body>

<%--提交form表单的demo--%>
<div>
    <h1 align="center">提交Form表单到服务端</h1>
    <table align="center" border="1" cellspacing="0">
        <tr>
            <th>id</th>
            <th>name</th>
            <th>password</th>
            <th>email</th>
            <th>register time</th>
        </tr>
        <c:forEach items="${users}" var="u" varStatus="st">
            <tr>
                <td>${u.id}</td>
                <td>${u.username}</td>
                <td>${u.password}</td>
                <td>${u.email}</td>
                <td>${u.regtime}</td>
            </tr>
        </c:forEach>
    </table>
    <br/>
    <form align="center" id="addUser" action="/addUser" method="post">
        userName:<input id="name" name="name"><br/>
        password:<input id="password" name="password"><br/>
        email:<input id="email" name="email"><br/>
        <input type="submit" value="添加新用户">
    </form>
</div>
<br/>

<%--ajax请求响应demo--%>
<div>
    <h1>ajax请求服务端数据</h1>
    <script>
        $(function(){
            $.ajax({
                url:"/ajaxRequest",
                type:"post",
                data:{name:"lijiang"},
                dataType:"json",
                success:function (users) {
                    $("#listUsers").html(JSON.stringify(users.get("users")));
                }
            });
        });
    </script>
    <div id="listUsers">
        展示所有users
    </div>

</div>

</body>
</html>
