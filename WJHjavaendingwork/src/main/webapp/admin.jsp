<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<html>
<head>
    <title>管理员界面</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
            padding: 20px;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .container {
            max-width: 600px;
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            text-align: center; /* 使文本居中 */
        }
        .action-buttons, .navigation {
            margin: 10px 0;
        }
        .action-buttons a, .navigation a {
            display: block; /* 使按钮竖向排列 */
            margin: 5px 0;
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-align: center; /* 使按钮文本居中 */
        }
        .action-buttons a:hover, .navigation a:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>欢迎来到管理员界面</h1>
        <div class="action-buttons">
            <a href="login.jsp">返回登录界面</a>
            <a href="adminInfo.jsp">管理者信息</a>
        </div>
        <div class="navigation">
            <a href="productManagement.jsp">商品信息</a>
            <a href="orderManagement.jsp">订单信息</a>
        </div>
    </div>
</body>
</html>
