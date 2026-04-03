<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.shopping.dao.OrderDao" %>
<%@ page import="com.shopping.model.Order" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<html>
<head>
    <title>订单详情</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            padding: 20px;
            background-color: #f8f9fa;
        }
        .order {
            background-color: white;
            padding: 10px;
            margin-bottom: 10px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .order img {
            max-width: 100px;
            height: auto;
            margin-right: 20px;
        }
        .order-details {
            flex-grow: 1;
        }
        .order-total {
            font-weight: bold;
        }
        .back-link {
            display: block;
            margin-top: 20px;
            color: #007bff;
            text-decoration: none;
        }
        .back-link:hover {
            text-decoration: underline;
        }
        .title {
            background-color:#87CEEB;
            text-align:center;
        }
    </style>
</head>
<body>
<h1 class="title">您的订单</h1>
<%
    String username = (String) session.getAttribute("username");

    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    OrderDao orderDao = new OrderDao();
    List<Order> orders = orderDao.getOrdersByUserId(username);
%>
<% for (Order order : orders) { 
    String productName = orderDao.getProductNameById(order.getProductId());
%>
<div class="order">
    <div class="order-details">
        <h3>商品名: <%= productName %></h3>
        <p>数量: <%= order.getQuantity() %></p>
        <p>总价: <%= order.getTotalAmount() %> RMB</p>
        <p>交易时间: <%= order.getTransactionTime() %></p>
    </div>
</div>
<% } %>
<a href="index.jsp" class="back-link">返回首页</a>
</body>
</html>
