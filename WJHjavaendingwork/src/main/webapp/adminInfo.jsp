<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.shopping.dao.AdminDao" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="com.shopping.model.Admin" %>
<html>
<head>
    <title>管理员信息</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
            padding: 20px;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .info-item {
            margin-bottom: 10px;
        }
        .info-item p {
            margin: 0;
        }
        .info-item label {
            font-weight: bold;
        }
        .action-buttons {
            margin-top: 20px;
        }
        .action-buttons a {
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-right: 10px;
        }
        .action-buttons a:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>管理员信息</h1>
        <%
            String adminUsername = (String) session.getAttribute("admin");
            AdminDao adminDao = new AdminDao();
            Admin admin = adminDao.getAdminByUsername(adminUsername);
        %>
        <% if (admin != null) { %>
        <div class="info-item">
            <label>用户名:</label>
            <p><%= admin.getUsername() %></p>
        </div>
        <div class="info-item">
            <label>最后登录时间:</label>
            <p><%= admin.getLastLogin() %></p>
        </div>
        <div class="info-item">
            <label>登录历史:</label>
            <p><%= admin.getLoginHistory() %></p>
        </div>
        <% } else { %>
        <p>管理员信息未找到。</p>
        <% } %>
        <div class="action-buttons">
            <a href="login.jsp">返回登录界面</a>
            <a href="admin.jsp">管理者界面</a>
        </div>
    </div>
</body>
</html>
