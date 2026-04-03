<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>支付界面</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background-color: #f8f9fa;
        }
        .container {
            background-color: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            max-width: 400px;
            width: 100%;
        }
        .container img {
            max-width: 100%;
            height: auto;
            margin-bottom: 20px;
        }
        .container h2 {
            margin-bottom: 10px;
        }
        .container p {
            margin-bottom: 20px;
        }
        .container button {
            padding: 10px 20px;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .container button:hover {
            background-color: #218838;
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
        .captcha {
            margin-bottom: 20px;
        }
        .captcha img {
            max-width: 150px;
            height: auto;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>支付界面</h2>
    <p>请完成以下步骤以完成支付：</p>
    <form action="processPayment" method="post">
        <input type="password" name="password" placeholder="请输入密码" required>
        <button type="submit">确认支付</button>
    </form>
    <a href="cart.jsp" class="back-link">&larr; 返回购物车</a>
</div>
</body>
</html>
