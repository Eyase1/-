<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<html>
<head>
    <title>个人主页</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 1500px;
            margin: 0 auto;
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            text-align: center; /* 使文本居中 */
        }
        .profile-item {
            margin-bottom: 10px;
        }
        .profile-item p {
            margin: 0;
        }
        .profile-item label {
            font-weight: bold;
        }
        .action-buttons {
            margin-top: 20px;
        }
        .action-buttons a, .action-buttons button {
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-right: 10px;
        }
        .action-buttons a:hover, .action-buttons button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>个人主页</h1>
        <%
            String username = (String) session.getAttribute("username");
        %>
        <% if (username != null) { %>
        <div class="profile-item">
            <label>用户名:</label>
            <p><%= username %></p>
        </div>
        <div class="action-buttons">
            <a href="cart.jsp">购物车</a>
            <a href="orders.jsp">订单</a>
            <a href="changePassword.jsp">修改密码</a>
        </div>
        <% } else { %>
        <p>请先登录。</p>
        <% } %>
    </div>
</body>
</html>
