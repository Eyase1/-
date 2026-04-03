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
    <title>修改商品</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
            padding: 20px;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .container {
            max-width: 800px; /* 增加最大宽度 */
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            text-align: left; /* 文字左对齐 */
        }
        h1 {
            margin-bottom: 20px;
            text-align: center; /* 标题居中 */
        }
        form {
            display: grid;
            grid-template-columns: 150px 1fr; /* 标签和输入框横向对齐 */
            gap: 10px; /* 标签和输入框之间的间距 */
        }
        label {
            text-align: left; /* 标签左对齐 */
            margin-right: 10px; /* 标签和输入框之间的间距 */
        }
        input[type="text"] {
            padding: 10px;
            border: 1px solid #ced4da;
            border-radius: 4px;
            width: 100%;
            box-sizing: border-box;
        }
        input[type="text"]:focus {
            border-color: #80bdff;
            outline: 0;
            box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
        }
        .button-container {
            display: flex;
            justify-content: center;
            margin-top: 20px;
        }
        button[type="submit"],
        a {
            padding: 5px 30px; /* 减小高度，增加宽度 */
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px; /* 统一字体大小 */
            margin: 0 10px; /* 按钮之间的间距 */
            transition: background-color 0.3s ease;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        button[type="submit"]:hover,
        a:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>修改商品</h1>
        <form action="editProduct" method="post">
            <input type="hidden" name="id" value="<%= product.getId() %>">
            <label for="name">商品名：</label>
            <input type="text" id="name" name="name" value="<%= product.getName() %>" placeholder="商品名称" required>
            <label for="description">商品描述：</label>
            <input type="text" id="description" name="description" value="<%= product.getDescription() %>" placeholder="商品描述" required>
            <label for="price">价格：</label>
            <input type="text" id="price" name="price" value="<%= product.getPrice() %>" placeholder="价格" required>
            <label for="imageUrl">图片链接：</label>
            <input type="text" id="imageUrl" name="imageUrl" value="<%= product.getImageUrl() %>" placeholder="图片URL" required>
            <div class="button-container">
                <button type="submit">修改</button>
                <a href="productDetailAdmin.jsp?id=<%= product.getId() %>">返回商品详情</a>
            </div>
        </form>
    </div>
</body>
</html>
