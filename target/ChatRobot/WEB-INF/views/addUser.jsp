<%--
  Created by IntelliJ IDEA.
  User: 11101453
  Date: 2019/6/19
  Time: 20:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <!-- jQuery -->
    <script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/DataTables-1.10.15/media/js/jquery.js"></script>
    <script src="${pageContext.request.contextPath}/layer/layer.js"></script>
</head>
<body>
<div class="content">
    <form action="" id="myform">
        <div class="form-group row">
            <div class="left col-xs-3 text-right">
                <label>username:</label>
            </div>
            <div class="right col-xs-8 text-left">
                <input type="text" class="form-control" id="username" name="username" placeholder="">
            </div>
        </div>

        <div class="form-group row">
            <div class="left col-xs-3 text-right">
                <label>password:</label>
            </div>
            <div class="right col-xs-8 text-left">
                <input type="text" class="form-control" id="password" name="password" placeholder="">
            </div>
        </div>

        <div class="form-group row">
            <div class="left col-xs-3 text-right">
                <label>email:</label>
            </div>
            <div class="right col-xs-8 text-left">
                <input type="text" class="form-control" id="email" name="email" placeholder="">
            </div>
        </div>

        <div class="form-group row">
            <div class="left col-xs-3 text-right">
                <label>role:</label>
            </div>
            <div class="right col-xs-8 text-left">
                <input type="text" class="form-control" id="role" name="role" placeholder="">
            </div>
        </div>
        
        <div>
            <button type="button" onclick="addUser()" >提交</button>
        </div>
    </form>
</div>

<script>

    function addUser() {
        var username = $("#username").val();
        var password = $("#password").val();
        var email = $("#email").val();
        var role = $("#role").val();
        
        $.post(
            "http://localhost:8080/dataTables/addUser",{
                "username":username,
                "password":password,
                "email":email,
                "role":role
            },
            function (data) {
                alert("add successful!")
                window.parent.query();
                window.parent.layer.closeAll();
            }
        );
    }
</script>
</body>
</html>
