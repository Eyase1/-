<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.shopping.dao.OrderDao" %>
<%@ page import="com.shopping.model.Order" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>订单信息管理</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
            padding: 20px;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .order-item {
            border-bottom: 1px solid #ddd;
            padding: 10px 0;
        }
        .order-item:last-child {
            border-bottom: none;
        }
        .order-item h3 {
            margin: 0;
            font-size: 18px;
        }
        .order-item p {
            margin: 5px 0;
        }
        .order-item a {
            color: #007bff;
            text-decoration: none;
        }
        .order-item a:hover {
            text-decoration: underline;
        }
        .action-buttons {
            margin-top: 10px;
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
        <h1>订单信息管理</h1>
        <div class="action-buttons">
            <a href="admin.jsp">返回管理员界面</a>
        </div>
        <%
            OrderDao orderDao = new OrderDao();
            List<Order> orders = orderDao.getAllOrders(); // 假设 OrderDao 有一个 getAllOrders 方法
            for (Order order : orders) {
                String username = orderDao.getUsernameById(order.getUserId());
                String productName = orderDao.getProductNameById(order.getProductId());
        %>
        <div class="order-item">
            <h3>订单ID: <%= order.getId() %></h3>
            <p>用户名: <%= username %></p>
            <p>商品名: <%= productName %></p>
            <p>数量: <%= order.getQuantity() %></p>
            <p>支付金额: <%= order.getTotalAmount() %> RMB</p>
            <p>支付时间: <%= order.getTransactionTime() %></p>
        </div>
        <% } %>
    </div>
</body>
</html>
