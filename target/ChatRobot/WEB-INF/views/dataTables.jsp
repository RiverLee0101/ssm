<%--
  Created by IntelliJ IDEA.
  User: 11101453
  Date: 2019/6/18
  Time: 16:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>DataTables</title>
    <!-- DataTables CSS -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables-1.10.15/media/css/jquery.dataTables.css">

    <!-- jQuery -->
    <script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/DataTables-1.10.15/media/js/jquery.js"></script>

    <!-- DataTables -->
    <script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/DataTables-1.10.15/media/js/jquery.dataTables.js"></script>
    
    <%-- layer --%>
    <script src="layer/layer.js"></script>
</head>
<body>

<div class="col-xs-4 text-left" style="padding-left: 50px;">
    <button type="button" class="btn btn-success" onclick="addUser();">添加</button>
</div>

<table id="table1" class="display">
    <thead>
        <tr>
            <th>id</th>
            <th>email</th>
            <th>username</th>
            <th>password</th>
            <th>role</th>
            <th>operation</th>
            <%--<th>regTime</th>--%>
        </tr>
    </thead>
    <tbody>

    </tbody>
</table>

<script>
    var table;
    $(function(){
        table =$('#table1').DataTable({
            language: {
                "sProcessing": "处理中...",
                "sLengthMenu": "显示 _MENU_ 项结果",
                "sZeroRecords": "没有匹配结果",
                "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
                "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
                "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
                "sInfoPostFix": "",
                "sSearch": "搜索:",
                "sUrl": "",
                "sEmptyTable": "表中数据为空",
                "sLoadingRecords": "载入中...",
                "sInfoThousands": ",",
                "oPaginate": {
                    "sFirst": "首页",
                    "sPrevious": "上页",
                    "sNext": "下页",
                    "sLast": "末页"
                },
                "oAria": {
                    "sSortAscending": ": 以升序排列此列",
                    "sSortDescending": ": 以降序排列此列"
                }
            },
            "serverSide":true,
            "iDisplayLength":10,
            "sAjaxSource":"dataTables/list",
            "fnServerData":function (sSource, aoData, fnCallBack){
                aoData.push({"name":"action", "value":"test"});
                $.ajax({
                    "type":"POST",
                    "contentType":"application/json",
                    "url":sSource,
                    "dataType":"json",
                    "data":JSON.stringify(aoData),
                    "success":function(data){
                        try{
                            fnCallBack(data);
                        }catch (e) {
                            //
                        }
                    }
                    
                });
            },
            "columns":[
                {"data":"id"},
                {"data":"email"},
                {"data":"username"},
                {"data":"password"},
                {"data":"role"},
                {"data":""}
            ],
            "fnDrawCallback":function(table){
                $("div#table1_info").append(" <input style='height:28px;line-height:28px;width:40px;' class='margin text-center' id='changePage' type='text'><a class='btn btn-default shiny' style='margin-bottom:5px' href='javascript:void(0);' id='dataTable-btn'>确认</a>");
                var oTable = $("#table1").dataTable();
                $('#dataTable-btn').click(function(e) {
                    if($("#changePage").val() && $("#changePage").val() > 0) {
                        var redirectpage = $("#changePage").val() - 1;
                    } else {
                        var redirectpage = 0;
                    }
                    oTable.fnPageChange(redirectpage);
                });
            },
            "aoColumnDefs": [
                {
                    "aTargets": [5],
                    "mRender": function (data, type, full) {
                        var s = '<shiro:hasPermission name="oversea_appsearch_manager:operationManage:associationKeysDel">';
                        s += '<button type="button" class="btn btn-info btn-xs" style="margin-right: 10px"onclick="deleteAssociationKey(' + full.id + ');">Delete</button>';
                        s += '<button type="button" class="btn btn-info btn-xs" style="margin-right: 10px"onclick="showSearchKeyPage(\'update\',' + full.id + ');">Modify</button>';
                        s += '</shiro:hasPermission>';
                        var purifyKeyword = escape(full.keyword);
                        s += '<shiro:hasPermission name="oversea_appsearch_manager:operationManage:associationKeysEdit">';
                        s += '<button type="button" class="btn btn-info btn-xs" style="margin-right: 10px"onclick="toAssoKeyAppsPage(' + full.id + ',\'' + purifyKeyword +'\',\''+full.zone+ '\');">Interfere</button>'
                        s += '</shiro:hasPermission>';
                        s += '<button type="button" class="btn btn-info btn-xs" onclick="showOptRecs(' + full.id + ',2);">History</button>';
                        return s;
                    }
                }
            ]
        });
    });

    function addUser() {
        layer.open({
            title:'添加用户',
            type: 2,
            area: ['450px', '320px'],
            fix: false, //不固定
            maxmin: true,
            content: 'http://localhost:8080/dataTables/toAddUser',
            success:function(layero,index){
            },
            end:function(){
                var handle_status = $("#handle_status").val();
                if ( handle_status == '1' ) {
                    layer.msg('添加成功！',{
                        icon: 1,
                        time: 2000 //2秒关闭（如果不配置，默认是3秒）
                    },function(){
                        history.go(0);
                    });
                } else if ( handle_status == '2' ) {
                    layer.msg('添加失败！',{
                        icon: 2,
                        time: 2000 //2秒关闭（如果不配置，默认是3秒）
                    },function(){
                        history.go(0);
                    });
                }
            }
        });
    }
    
    function query() {
        table.ajax.reload();
    }
</script>
</body>
</html>
