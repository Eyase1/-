<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.shopping.dao.CartDao" %>
<%@ page import="com.shopping.dao.ProductDao" %>
<%@ page import="com.shopping.dao.UserDao" %>
<%@ page import="com.shopping.model.CartItem" %>
<%@ page import="com.shopping.model.Product" %>
<%@ page import="com.shopping.model.User" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<html>
<head>
    <title>购物车</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            padding: 20px;
            background-color: #f8f9fa;
        }
        .cart-item {
            background-color: white;
            padding: 10px;
            margin-bottom: 10px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .cart-item img {
            max-width: 100px;
            height: auto;
            margin-right: 20px;
        }
        .cart-item-details {
            flex-grow: 1;
        }
        .cart-item-total {
            font-weight: bold;
        }
        .checkout-button {
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-left: 10px;
        }
        .checkout-button:hover {
            background-color: #0056b3;
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
        .delete-button {
            padding: 5px 10px;
            background-color: #dc3545;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .delete-button:hover {
            background-color: #c82333;
        }
        .total-and-checkout {
            display: flex;
            justify-content: flex-end;
            align-items: center;
            margin-top: 20px;
        }
        .total-and-checkout .cart-item-total {
            margin-right: 20px;
        }
        .continue {
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-left: 10px;
        }
        .title {
            background-color: #87CEEB;
            text-align: center;
        }
        .actions {
            display: flex;
            justify-content: flex-end;
            margin-top: 20px;
        }
        .actions button {
            margin-left: 10px;
        }
    </style>
    <script>
        function deleteCartItem(cartItemId) {
            var xhr = new XMLHttpRequest();
            xhr.open('POST', 'removeFromCart', true);
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    location.reload();
                }
            };
            xhr.send('cartItemId=' + encodeURIComponent(cartItemId));
        }
    </script>
</head>
<body>
<h1 class="title">你的购物车</h1>
<%
    String username = (String) session.getAttribute("username");

    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    UserDao userDao = new UserDao();
    User user = userDao.getUserByUsername(username);
    int userId = user != null ? user.getId() : -1;

    CartDao cartDao = new CartDao();
    ProductDao productDao = new ProductDao();

    List<CartItem> items = cartDao.getCartItemsByUserId(userId);
    double total = 0.0;
    for (CartItem item : items) {
        Product product = productDao.getProductById(item.getProductId());
        if (product != null) {
            total += product.getPrice() * item.getQuantity();
%>
<div class="cart-item">
    <img src="<%= product.getImageUrl() %>" alt="<%= product.getName() %>">
    <div class="cart-item-details">
        <h3><%= product.getName() %></h3>
        <p>Price: $<%= product.getPrice() %></p>
        <p>Quantity: <%= item.getQuantity() %></p>
    </div>
    <div class="cart-item-total">
        Total: $<%= product.getPrice() * item.getQuantity() %>
    </div>
    <button class="delete-button" onclick="deleteCartItem(<%= item.getId() %>)">删除</button>
</div>
<%
        }
    }
%>
<div class="total-and-checkout">
    <div class="cart-item-total">
        <h3>总价为: <%= total %> RMB</h3>
    </div>
    <button class="checkout-button" onclick="window.location.href='payment.jsp'">支付</button>
</div>
<div class="actions">
    <button class="continue" onclick="window.location.href='index.jsp'">继续购物</button>
</div>
</body>
</html>
