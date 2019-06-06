<html>
<head>
    <title>用户列表</title>
    <script src="${ctx}/webjars/jquery/3.3.1-2/jquery.min.js"></script>
</head>
<body>
    <form id="addUser" action="user/addUser" method="post">
        userName:<input id="name" name="name"><br/>
        password:<input id="password" name="password"><br/>
        <input type="submit" value="添加新用户">
    </form>

<%--<script>--%>
    <%--$(function(){--%>
        <%--$.ajax({--%>
            <%--url:"user/showAllUsers",--%>
            <%--data:{"name":"lijiang"},--%>
            <%--success:function (result) {--%>
                <%--$("#showAllUser").html(result);--%>
            <%--}--%>
        <%--});--%>
    <%--});--%>
<%--</script>--%>
</body>
</html>
