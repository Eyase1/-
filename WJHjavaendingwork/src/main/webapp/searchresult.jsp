<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.shopping.model.Product" %>
<html>
<head>
    <title>搜索结果页</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 20px;
            color: #333;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .products {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            gap: 20px;
        }
        .product {
            border: 1px solid #ddd;
            padding: 15px;
            text-align: center;
            border-radius: 8px;
            transition: transform 0.2s;
        }
        .product:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .product img {
            max-width: 100%;
            height: auto;
            border-radius: 8px;
        }
        .product h3 {
            margin: 10px 0;
            font-size: 18px;
            color: #333;
        }
        .product p {
            margin: 5px 0;
            font-size: 14px;
            color: #666;
        }
        .product a {
            display: inline-block;
            margin-top: 10px;
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            transition: background-color 0.2s;
        }
        .product a:hover {
            background-color: #0056b3;
        }
        .result {
            text-align: center;
            margin-bottom: 20px;
            font-size: 24px;
            color: #333;
        }
        .back-link {
            display: block;
            text-align: center;
            margin-top: 20px;
        }
        .backbutton {
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.2s;
        }
        .backbutton:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
<div class="container">
    <%
        boolean noResults = (Boolean) request.getAttribute("noResults");
    %>
    <div class="result">
        <% if (noResults) { %>
            <h1>没有您想要的商品，您可以看看这些您可能感兴趣的商品</h1>
        <% } else { %>
            <h1>搜索结果</h1>
        <% } %>
    </div>

    <div class="products">
        <%
            List<Product> results = (List<Product>) request.getAttribute("results");
            if (!noResults && results != null) {
                for (Product product : results) {
        %>
        <div class="product">
            <img src="<%= product.getImageUrl() %>" alt="<%= product.getName() %>">
            <h3><%= product.getName() %></h3>
            <p><%= product.getDescription() %></p>
            <p>价格: <%= product.getPrice() %> RMB</p>
            <a href="productDetail.jsp?id=<%= product.getId() %>">点击查看详情</a>
        </div>
        <%
                }
            }
        %>
    </div>
    <div class="back-link">
        <a href="index.jsp" class="backbutton">返回</a>
    </div>
</div>
</body>
</html>
