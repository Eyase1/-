<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登陆界面</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background-color: #f8f9fa;
        }
        .container {
            background-color: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .container h2 {
            margin-bottom: 20px;
        }
        .container input[type=text], .container input[type=password] {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .container button {
            width: 100%;
            padding: 10px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .container button:hover {
            background-color: #0056b3;
        }
        .container a {
            display: block;
            text-align: right;
            margin-top: 10px;
            color: #007bff;
            text-decoration: none;
        }
        .container a:hover {
            text-decoration: underline;
        }
        .error-message {
            color: red;
            margin-bottom: 10px;
        }
        .logintext {
            text-align:center;
        }
        .browse-button {
            width: 100%;
            padding: 10px;
            background-color: #ADD8E6;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-top: 10px;
        }
        .browse-button:hover {
            background-color: #87CEEB;
        }
    </style>
</head>
<body style="background: url(https://img95.699pic.com/photo/40174/6692.jpg_wh860.jpg);background-size:100% 100%; background-attachment: fixed">
<div class="container">
    <h2>欢迎来到购物商城</h2>
    <h2 class="logintext">请登录</h2>
    <%
        String message = (String) request.getAttribute("message");
        if (message != null) {
    %>
    <div class="error-message"><%= message %></div>
    <%
        }
    %>
    <form action="login" method="post">
        <input type="text" name="username" placeholder="Username" required>
        <input type="password" name="password" placeholder="Password" required>
        <button type="submit">登录</button>
    </form>
    <button class="browse-button" onclick="location.href='index.jsp'">仅浏览</button>
    <a href="register.jsp">没有账户？点击这里注册</a>
</div>
</body>
</html>
