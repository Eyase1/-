<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.shopping.dao.ProductDao" %>
<%@ page import="com.shopping.model.Product" %>
<%
    int productId = Integer.parseInt(request.getParameter("id"));
    ProductDao productDao = new ProductDao();
    Product product = productDao.getProductById(productId);
%>
<html>
<head>
    <title>商品详情</title>
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
        .product-details {
            margin-bottom: 20px;
        }
        .product-details h3 {
            margin: 0;
            font-size: 24px;
            color: #333;
        }
        .product-details p {
            font-size: 16px;
            color: #666;
        }
        .product-details .price {
            font-size: 18px;
            color: #28a745;
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
        .pic {
        width:600px;
        height:600px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>商品详情</h1>
        <img src="<%= product.getImageUrl() %>" alt="<%= product.getName() %>" class="pic">
        <div class="product-details">
            <h3><%= product.getName() %></h3>
            <p>描述: <%= product.getDescription() %></p>
            <p class="price">价格: <%= product.getPrice() %> RMB</p>
            <p>图片链接: <%= product.getImageUrl() %></p>
        </div>
        <div class="action-buttons">
            <a href="editProduct.jsp?id=<%= product.getId() %>">修改商品信息</a>
            <button onclick="confirmDelete(<%= product.getId() %>)">删除商品信息</button>
            <a href="productManagement.jsp">返回商品信息管理</a>
        </div>
        <script>
            function confirmDelete(productId) {
                if (confirm("是否删除商品信息？")) {
                    window.location.href = "deleteProduct?id=" + productId;
                }
            }
        </script>
    </div>
</body>
</html>
