<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>注册页面</title>
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
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .container button:hover {
            background-color: #218838;
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
        .success-message {
            color: green;
            margin-bottom: 10px;
        }
        .registertext {
        text-align:center;
        }
    </style>
</head>
<body style="background: url(https://img95.699pic.com/photo/40174/6692.jpg_wh860.jpg);background-size:100% 100%; background-attachment: fixed">
<div class="container">
    <h2>欢迎来到购物商城</h2>
    <h2 class="registertext">请注册</h2>
    <%
        String message = (String) request.getAttribute("message");
        if (message != null) {
    %>
    <div class="success-message"><%= message %></div>
    <%
        }
    %>
    <form action="register" method="post">
        <input type="text" name="username" placeholder="用户名" required>
        <input type="password" name="password" placeholder="密码" required>
        <button type="submit">注册</button>
    </form>
    <a href="login.jsp">已有账户了？点击这里登录！</a>
</div>
</body>
</html>
