<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.shopping.dao.ProductDao" %>
<%@ page import="com.shopping.model.Product" %>
<html>
<head>
    <title>Home Page</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 0;
        }
        .header {
            color: black;
            text-align: center;
            padding: 20px 0;
            background:
        }
        .nav {
            background-color:#2F4F4F;
            display: flex;
            justify-content: center;
            align-items: center;
            color: white;
            padding: 10px 20px;
        }
        .nav input[type=text] {
            width: 70%;
            padding: 10px;
            margin-right: 10px;
            border: none;
            border-radius: 5px;
            border: solid;
            border-width: 2px;
        }
        .nav button {
            padding: 10px 10px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .nav button:hover {
            background-color: #0056b3;
        }
        .products {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            gap: 20px;
            padding: 20px;
        }
        .product {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: center;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .product img {
            max-width: 100%;
            height: auto;
            border-radius: 5px;
        }
        .product h3 {
            margin: 10px 0;
        }
        .product p {
            margin: 5px 0;
        }
        .product a {
            color: #007bff;
            text-decoration: none;
        }
        .product a:hover {
            text-decoration: underline;
        }
        footer {
            background: #403930;
            color: #fff;
        }
        footer section {
            padding: 0;
            font-size: 90%;
            margin: auto;
        }
        footer dl {
            width: 100%;
            margin: auto;
            text-align: center;
        }
        footer dt {
            font-weight: bold;
            padding-bottom: 10px;
            margin: 10px auto;
            border-bottom: 1px solid #aaa;
        }
        footer dd {
            margin: 0;
        }
        .relogin {
            justify-content: space-between;
        }
        .pic {
            width: 150px;
            height: 150px;
        }
        .search-button {
        width:15px;
        height:15px;
        }
        .profile-button {
        padding: 10px 20px;
        background-color: #6c757d;
        color: white;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        margin-right: 10px;
        }

        .profile-button:hover {
        background-color: #545b62;
        }
        .title {
        text-align:center;
        background:url(D:\eclipse-workplace\WJHjavaendingwork\src\main\webapp\WEB-INF\image\1.png);S
        }
        .price {
        color:red;
        }
    </style>
</head>
<body><!--  style="background: url(https://img95.699pic.com/photo/50057/6280.jpg_wh860.jpg);background-size:100% 100%; background-attachment: fixed" -->
<div class="header">
    <div>
    <h1 class="title">欢迎来到网上购物商城！</h1>
    <h2>给您极致的购物体验</h2>
    </div>
</div>
<div class="nav">
    <form action="search" method="get">
        <input type="text" name="query" placeholder="搜索你想要的商品吧~">
        <button type="submit"><img src="https://ts3.cn.mm.bing.net/th?id=OIP-C.8hCLZgmus2ilhNf0ExauUQHaHb&rs=1&pid=ImgDetMain" class="search-button"></button>
    </form>
    <div class="relogin">
    <% HttpSession session1 = request.getSession(false);
        if (session != null && session.getAttribute("username") != null) { %>
        <button class="profile-button" onclick="location.href='profile.jsp'">个人主页</button>
        <button class="profile-button" onclick="location.href='login.jsp'">切换账号</button>
    <% } else { %>
        <button class="profile-button" onclick="location.href='login.jsp'">登录</button>
        <button class="profile-button" onclick="location.href='register.jsp'">注册</button>
    <% } %>
    <button class="profile-button" onclick="location.href='cart.jsp'">购物车</button>
    <button class="profile-button" onclick="location.href='orders.jsp'">订单</button>
</div>

</div>
<div class="products">
    <%
        ProductDao productDao = new ProductDao();
        List<Product> products = productDao.getAllProducts();
        for (Product product : products) {
    %>
    <div class="product">
        <img src="<%= product.getImageUrl() %>" alt="<%= product.getName() %>" class="pic">
        <h3><%= product.getName() %></h3>
        <p><%= product.getDescription() %></p>
        <p>价格:<label class="price"><%= product.getPrice() %></label>RMB</p>
        <a href="productDetail.jsp?id=<%= product.getId() %>">点击查看商品详情</a>
    </div>
    <% } %>
</div>
</body>
<footer id="footer">
    <section>
        <dl>
            <dt>联系方式</dt>
            <dd>地址：广东省佛山市狮山镇114514号</dd>
            <dd>邮编：6586754685</dd>
            <dd>电话：020-54185418</dd>
            <dd>邮箱：euywe@abc.com</dd>
        </dl>
    </section>
</footer>
</html>
