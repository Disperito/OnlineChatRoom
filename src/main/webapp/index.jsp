<%--
  Created by IntelliJ IDEA.
  User: Disperito
  Date: 2019/11/25
  Time: 22:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge,chrome=1">
    <title>index</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <script src="js/jquery-2.1.1.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/cos-js-sdk-v5.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
    <script src="js/WebSocket.js"></script>
    <script src="js/chat.js"></script>
    <script src="js/cos.js"></script>
    <link rel="stylesheet" href="css/app.css">
</head>
<body>

<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container-fluid">

        <div class="navbar-header">
            <a class="navbar-brand">
                在线多人聊天室
            </a>
        </div>

        <!--导航栏的其他内容-->
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav" id="mytabs">
                <li class="active"><a href="#">首页</a></li>
                <li><a href="#">222</a></li>
                <li><a href="#">333</a></li>
            </ul>

            <!--指定.navbar-right使其居右-->
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        click me
                        <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu" role="menu" id="right-dropdown">
                        <li><a href="#" role="menuitem">1</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>


<c:if test="${sessionScope.userId != null}" var="logged"/>
<c:if test="${logged}">
<script>webSocketInit(${sessionScope.userId})</script>
<div class="container" id="main">
    <div class="row">
        <div class="col-xs-2">
            <div class="well" id="users_list_well">
                <form class="form-inline" id="users_list_form">
                    <div class="form-group">
                        <label for="users_list_search_input"></label>
                        <input type="text" class="form-control" id="users_list_search_input">
                    </div>
                    <div class="form-group">
                        <button class="btn btn-default btn-xs"><span class="glyphicon glyphicon-search"></span></button>
                    </div>
                </form>
                <p class="text-center" style="margin-top: 15px;">在线用户</p>
                <ul id="user_list"></ul>
            </div>
        </div>
        <div class="col-xs-10">
            <div class="well" id="main_well">
                <div class="well" id="dialog_well">
                    <div class="message-container">

                        <div v-for="message in record" :class="messageClass(message)">
                            <div class="message-source">{{message.sourceName}}</div>
                            <div class="message-content" v-html="message.content"></div>
                            <div class="message-datetime">{{message.createTime}}</div>
                        </div>

                    </div>
                </div>
                <label for="input_area"></label>
                <textarea rows="6" cols="130" id="input_area"></textarea>
                <button class="btn btn-default" id="send_btn" onclick="send()">发送</button>
            </div>
        </div>
    </div>
    <div class="row" style="position: relative; left: 200px; margin-top: 5px">
        <input type="file" id="file">
        <button class="btn btn-default" onclick="uploadFile()">发送文件</button>
    </div>
</div>
</c:if>
<c:if test="${!logged}">
<div class="container">
    <div class="jumbotron" id="non-logged-welcome-info">
        <h2 class="text-center">欢迎来到在线多人聊天室，请先登录！</h2>
        <button type="button" class="btn btn-primary btn-lg center-block" data-toggle="modal"
                data-target="#login_modal">
            登录
        </button>
    </div>
</div>
</c:if>


<div class="modal fade" id="login_modal" role="dialog" aria-labelledby="myModalTitle"
     aria-describedby="This is a modal."
     tabindex="-1">
    <div class="modal-dialog" aria-hidden="true">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <p class="modal-title">请登录</p>
            </div>
            <div class="modal-body">
                <form role="form" class="form" id="login_form">
                    <div class="form-group">
                        <label for="id_input" class="control-label">用户ID</label>
                        <input type="text" id="id_input" class="form-control">
                    </div>
                    <br>
                    <div class="form-group">
                        <label for="password_input" class="control-label">密码</label>
                        <input type="password" id="password_input" class="form-control">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-toggle="modal" data-target="#register_modal">注册
                </button>
                <button type="button" class="btn btn-primary" id="login_btn" onclick="login()">登录</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="register_modal" role="dialog" aria-labelledby="myModalTitle"
     aria-describedby="This is a modal."
     tabindex="-1">
    <div class="modal-dialog" aria-hidden="true">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <p class="modal-title">注册</p>
            </div>
            <div class="modal-body">
                <form role="form" class="form" id="register_form">
                    <div class="form-group">
                        <label for="register_name" class="control-label">用户名</label>
                        <input type="text" id="register_name" class="form-control">
                    </div>
                    <br>
                    <div class="form-group">
                        <label for="register_password" class="control-label">密码</label>
                        <input type="password" id="register_password" class="form-control">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" onclick="register()">注册</button>
            </div>
        </div>
    </div>
</div>

<script src="js/app.js"></script>
<script>
    afterLogin("${sessionScope.userName}");
</script>
</html>
