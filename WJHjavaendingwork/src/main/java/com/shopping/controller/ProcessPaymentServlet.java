package com.shopping.controller;

import com.shopping.dao.CartDao;
import com.shopping.dao.OrderDao;
import com.shopping.dao.ProductDao;
import com.shopping.dao.UserDao;
import com.shopping.model.CartItem;
import com.shopping.model.Order;
import com.shopping.model.Product;
import com.shopping.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
//处理支付过程，保存订单信息并清空购物车
public class ProcessPaymentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String password = request.getParameter("password");
        
        HttpSession session = request.getSession();//从会话中获取用户名，从请求参数中获取密码
        String username = (String) session.getAttribute("username");
        if (username != null) {
            UserDao userDao = new UserDao();
            User user = userDao.getUserByUsername(username);
            if (user != null && user.getPassword().equals(password)) {
                // 支付成功，保存订单信息到数据库
                CartDao cartDao = new CartDao();
                ProductDao productDao = new ProductDao();
                List<CartItem> items = cartDao.getCartItemsByUserId(user.getId());
                double total = 0.0;
                for (CartItem item : items) {
                    Product product = null;
					try {
						product = productDao.getProductById(item.getProductId());
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
                    if (product != null) {
                        Order order = new Order();
                        order.setUserId(user.getId());
                        order.setProductId(item.getProductId());
                        order.setQuantity(item.getQuantity());
                        order.setTotalAmount(item.getQuantity() * product.getPrice());
                        OrderDao orderDao = new OrderDao();
                        orderDao.saveOrder(order);
                        total += order.getTotalAmount();
                    }
                }

                // 清空购物车
                cartDao.clearCart(user.getId());

                // 重定向到订单详情页面
                response.sendRedirect("orders.jsp");
            } else {
                // 密码错误，返回支付界面并显示错误信息
                request.setAttribute("message", "密码错误，请重试！");
                request.getRequestDispatcher("payment.jsp").forward(request, response);
            }
        } else {
            // 用户未登录，重定向到登录界面
            response.sendRedirect("login.jsp");
        }
    }
}
