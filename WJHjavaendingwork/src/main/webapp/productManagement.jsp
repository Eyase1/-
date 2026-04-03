<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.shopping.dao.ProductDao" %>
<%@ page import="com.shopping.model.Product" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>商品信息管理</title>
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
        .product-item {
            border-bottom: 1px solid #ddd;
            padding: 10px 0;
            display: flex;
            align-items: center;
        }
        .product-item:last-child {
            border-bottom: none;
        }
        .product-item img {
            max-width: 100px;
            height: auto;
            margin-right: 20px;
        }
        .product-item h3 {
            margin: 0;
            font-size: 18px;
        }
        .product-item p {
            margin: 5px 0;
        }
        .product-item a {
            color: #007bff;
            text-decoration: none;
        }
        .product-item a:hover {
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
        <h1>商品信息管理</h1>
        <div class="action-buttons">
            <a href="admin.jsp">返回管理员界面</a>
            <a href="addProduct.jsp">添加商品</a>
        </div>
        <% 
            ProductDao productDao = new ProductDao();
            List<Product> products = productDao.getAllProducts();
            for (Product product : products) {
        %>
        <div class="product-item">
            <img src="<%= product.getImageUrl() %>" alt="<%= product.getName() %>">
            <div>
                <h3><%= product.getName() %></h3>
                <p>描述: <%= product.getDescription() %></p>
                <p>价格: <%= product.getPrice() %> RMB</p>
                <p>图片链接: <%= product.getImageUrl() %></p>
                <div class="action-buttons">
                    <a href="productDetailAdmin.jsp?id=<%= product.getId() %>">查看详情</a>
                </div>
            </div>
        </div>
        <% } %>
    </div>
</body>
</html>
