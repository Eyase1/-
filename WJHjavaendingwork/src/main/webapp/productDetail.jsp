<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.shopping.model.Product" %>
<%@ page import="com.shopping.dao.ProductDao" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="java.util.List" %>
<%@ page import="com.shopping.model.Comment" %>
<%
    int productId = Integer.parseInt(request.getParameter("id"));
    ProductDao productDao = new ProductDao();
    Product product = productDao.getProductById(productId);

    if (product == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    
    String username = (String) session.getAttribute("username");
    List<Comment> comments = product.getComments();
%>
<html>
<head>
    <title>商品详情页</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 800px;
            margin: 20px auto;
            padding: 20px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .product-image {
            max-width: 600px;
            height: 600px;
            border-radius: 8px;
            margin-bottom: 20px;
        }
        .product-details {
            margin-bottom: 20px;
        }
        .product-details h1 {
            margin-top: 0;
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
        .add-to-cart-form {
            text-align: center;
        }
        .add-to-cart-form button {
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        .add-to-cart-form button:hover {
            background-color: #0056b3;
        }
        .login-required {
            color: red;
            margin-bottom: 10px;
        }
        .comment-form {
            margin-top: 20px;
        }
        .comment-form textarea {
            width: 100%;
            padding: 10px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            resize: vertical;
        }
        .comment-form input[type="number"] {
            width: 60px;
            padding: 10px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .comment-form button {
            padding: 10px 20px;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        .comment-form button:hover {
            background-color: #218838;
        }
        .comments {
            margin-top: 20px;
        }
        .comment {
            background-color: #f1f1f1;
            padding: 15px;
            margin-bottom: 10px;
            border-radius: 5px;
            border: 1px solid #ddd;
        }
        .comment .username {
            font-weight: bold;
            font-size: 16px;
        }
        .comment .rating {
            width:30px;
            margin-left: 10px;
            color: #ffc107;
            font-size: 14px;
        }
        .comment .timestamp {
            margin-left: 10px;
            color: #888;
            font-size: 14px;
        }
        .comment p {
            margin-top: 10px;
            font-size: 14px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="product-image">
        <img src="<%= product.getImageUrl() %>" alt="<%= product.getName() %>" class="product-image">
    </div>
    <div class="product-details">
        <h1><%= product.getName() %></h1>
        <p><%= product.getDescription() %></p>
        <p class="price">Price: $<%= product.getPrice() %></p>
    </div>
    <div class="add-to-cart-form">
        <% if (username == null) { %><!-- 判断是否登录 -->
        <script>
            setTimeout(function() {
                window.location.href = "login.jsp";/*返回登录界面进行登录*/
                }, 2000);
        </script>
        <div class="login-required">请先登录再进行下一步操作</div>
        <% } else { %>
        <form action="addToCart" method="post"><!-- 提交表单，传递到购物车界面 -->
            <input type="hidden" name="productId" value="<%= product.getId() %>">
            <button type="submit">添加购物车-></button>
        </form>
        <% } %>
    </div>
    <% if (username != null) { %>
    <div class="comment-form">
        <form action="saveComment" method="post">
            <input type="hidden" name="productId" value="<%= product.getId() %>">
            <textarea name="commentText" placeholder="请输入评论" required></textarea>
            <input type="number" name="rating" min="1" max="5" placeholder="评分（1-5）" required>
            <button type="submit">提交评论</button>
        </form>
    </div>
    <% } %>
    <div class="comments">
        <% for (Comment comment : comments) { %>
        <div class="comment">
            <span class="username"><%= comment.getUsername() %></span>
            <span class="rating">评分: <%= comment.getRating() %> 星</span>
            <span class="timestamp">时间: <%= comment.getTimestamp() %></span>
            <p><%= comment.getComment() %></p>
        </div>
        <% } %>
    </div>
</div>
</body>
</html>
