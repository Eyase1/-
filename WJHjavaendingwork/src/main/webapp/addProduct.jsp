<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>添加商品</title>
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

        button[type="submit"],a {
            padding: 10px 20px; /* 增加宽度 */
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 8px; /* 增加圆角 */
            cursor: pointer;
            font-size: 16px;
            margin: 0 10px;
            transition: background-color 0.3s ease;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 添加阴影 */
            font-family: 'Arial', sans-serif; /* 调整字体 */
        }

        button[type="submit"]:hover,a:hover {
            background-color: #0056b3;
       }

        button[type="submit"]:hover,a:hover {
            background-color: #0056b3;
        }
        .success-message {
            color: green;
            margin-bottom: 20px;
            text-align: center;
        }
    </style>
    <script>
        window.onload = function() {
            var successMessage = document.getElementById('successMessage');
            if (successMessage) {
                setTimeout(function() {
                    window.location.href = 'productManagement.jsp';
                }, 2000);
            }
        };
    </script>
</head>
<body>
    <div class="container">
        <h1>添加商品</h1>
        <%
            String addProductSuccess = (String) session.getAttribute("addProductSuccess");
            if (addProductSuccess != null) {
        %>
        <div class="success-message" id="successMessage"><%= addProductSuccess %></div>
        <%
                session.removeAttribute("addProductSuccess"); // 清除会话属性
            }
        %>
        <form action="addProduct" method="post">
            <label for="name">商品名：</label>
            <input type="text" id="name" name="name" placeholder="商品名称" required>
            <label for="description">商品描述：</label>
            <input type="text" id="description" name="description" placeholder="商品描述" required>
            <label for="price">价格：</label>
            <input type="text" id="price" name="price" placeholder="价格" required>
            <label for="imageUrl">图片链接：</label>
            <input type="text" id="imageUrl" name="imageUrl" placeholder="图片URL" required>
            <div class="button-container">
                <button type="submit">添加</button>
                <a href="productManagement.jsp">返回商品信息管理</a>
            </div>
        </form>
    </div>
</body>
</html>
