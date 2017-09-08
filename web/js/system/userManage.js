/**
 * 用户管理Js改造
 */
require(['../config'], function (config) {

    require(['jquery', 'bootstrap', 'common/dt'], function ($, bootstrap, dataTable) {

        var userTable = dataTable('tblUserInfo', {
            ajax: {
                url: config.basePath + '/system/userManage/list'
            },
            columns: [
                {data: null},
                {data: 'user_id'},
                {data: 'user_name'},
                {data: 'user_role_name'},
                {
                    data: {
                        _: 'reg_date',
                        filter: 'reg_date',
                        display: 'reg_date_display'
                    }
                },
                {
                    data: {
                        _: 'last_login',
                        filter: 'last_login',
                        display: 'last_login_display'
                    }
                },
                {
                    data: 'login_status',
                    render: function (data) {
                        return data == 0 ? '<span class="text-danger">离线</span>' : '在线';
                    }
                }
            ],
            columnDefs: [
                {
                    targets: 7,
                    data: function (row) {
                        var operator = '<a class="modifyUser" data-toggle="modal" href="#modifyUserModal" title="修改用户"><img src="../../images/editCol.png" height="18" width="18"/></a>'
                            + '&nbsp;&nbsp;' + '<a class="deleteUser" data-toggle="modal" href="#popModal" title="删除用户"><img src="../../images/deleteCol.png" height="18" width="18"/></a>';
                        return operator;
                    }
                }
            ],
            ordering: false,
            serverSide: true,
            drawCallback: function (settings) {
                var api = this.api();
                var startIndex = api.context[0]._iDisplayStart;//获取到本页开始的条数
                api.column(0).nodes().each(function(cell, i) {
                    cell.innerHTML = startIndex + i + 1;
                });
            }
        });

        function loadUserRoleList(dom, async) {
            $.ajax({
                url: config.basePath + '/system/userManage/getRolesList',
                dataType: 'json',
                async: async,
                success: function (result) {
                    $('#' + dom).empty();
                    $.each(result, function(idx, obj) {
                        if (idx > 0 && idx % 3 == 0) {
                            $('#' + dom).append('<br/>');
                        }
                        $('#' + dom).append('<input type="checkbox" name="role_id" value="' + obj.role_id + '" title="' + obj.role_name + '"/>' + obj.role_name);
                    });
                }
            });
        }

        $('#addUserModal').on('show.bs.modal', function () {
            // 用户类型下拉列表roleList user_priority
            $('#roleCheckIdAdd').hide();
            loadUserRoleList('roleCheckIdAdd', true);
        });

        /*用户名失去焦点验证是否存在此用户 */
        $("#useridAdd").on('blur', function () {
            var userId = $('#useridAdd').val();
            if ($.trim(userId) != "") {
                var params = $.param({
                    "userId" : userId
                });
                $.ajax({
                    url: config.basePath + '/system/userManage/find',
                    type : "post",
                    data : params,
                    dataType : "json",
                    success : function(msg) {
                        if (msg.code != null && msg.code != 0)	{
                            alert(msg.msg);
                            $("#useridAdd")[0].focus();
                        }
                    }
                });
            } else {
                alert("用户ID不能为空!");
                $("#useridAdd")[0].focus();
            }
        });

        $('#rolePicAdd').on('click', function() {
            $("#roleCheckIdAdd").toggle();
        });

        $('#rolePic').on('click', function () {
            $("#roleCheckId").toggle();
        });

        $("#btnAddOk").on('click', function (e) {
            e.preventDefault();

            var userRole = $('#roleCheckIdAdd input[type="checkbox"]:checked').map(function () {
                return $(this).val();
            }).get().join(',');

            var params = JSON.stringify({
                user_id: $('#useridAdd').val(),
                user_name: $('#usernameAdd').val(),
                user_pass: $('#userpasswordAdd').val(),
                user_role_name: userRole,
                enabled: $('#userenableAdd').val(),
                idx_url: ''
            });

            $.ajax({
                url : config.basePath + '/system/userManage/create',
                type : "post",
                data : params,
                contentType: 'application/json',
                dataType : "json",
                success : function (result) {
                    if (result.code != 0) {
                        alert(result.msg);
                    } else {
                        location.reload();
                    }
                }
            });
        });

        /* 响应刷新按钮 */
        $('#btnRefresh').click(function() {
            location.reload();
        });

        $('#modifyUserModal').on('show.bs.modal', function (e) {
            var tr = $(e.relatedTarget).parents('tr');
            var data = userTable.row(tr).data();
            $("#userenable").val(data.enabled);
            $("#userid").val(data.user_id);
            $("#username").val(data.user_name);
            $("#userpassword").val(data.user_pass);
            $('#roleCheckId').hide();
            loadUserRoleList('roleCheckId', false);
            if (data.user_role_name != null) {
                var userRoleName = data.user_role_name.split(',');
                userRoleName.map(function (value) {
                    $('#roleCheckId input[type="checkbox"][title="' + value +'"]').attr('checked', true);
                });
            }
        });

        $("#btnModifyOk").on('click', function (e) {
            e.preventDefault();

            var userRole = $('#roleCheckId input[type="checkbox"]:checked').map(function () {
                return $(this).val();
            }).get().join(',');

            var params = JSON.stringify({
                user_id: $('#userid').val(),
                user_name: $('#username').val(),
                user_pass: $('#userpassword').val(),
                user_role_name: userRole,
                enabled: $("#userenable").val()
            });

            $.ajax({
                url: config.basePath + '/system/userManage/modify',
                type: 'POST',
                data: params,
                contentType: 'application/json',
                dataType: 'json',
                success: function (result) {
                    if (result.code != 0) {
                        alert(result.msg);
                    } else {
                        location.reload();
                    }
                }
            });
        });

        $('#popModal').on('show.bs.modal', function (e) {
            var tr = $(e.relatedTarget).parents('tr');
            var data = userTable.row(tr).data();
            $('#warningText').text('确定要删除用户ID为：' + data.user_id + '的用户？');
            $('#deleteUserId').val(data.user_id);
        });

        $('#btnPopOk').on('click', function (e) {
            e.preventDefault();
            var params = $.param({
                "userId" : $('#deleteUserId').val()
            });
            $.ajax({
                url: config.basePath + '/system/userManage/delete',
                type: 'POST',
                data: params,
                dataType: 'json',
                success: function(result) {
                    if (result.code != 0) {
                        alert(result.msg);
                    } else {
                        location.reload();
                    }
                }
            });
        });

    });
});