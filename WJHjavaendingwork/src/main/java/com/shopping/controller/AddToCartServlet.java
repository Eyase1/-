package com.shopping.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.shopping.dao.CartDao;
import com.shopping.dao.UserDao;
import com.shopping.model.CartItem;
import com.shopping.model.User;
//添加到购物车
public class AddToCartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter("productId");
        String quantityStr = request.getParameter("quantity") != null ? request.getParameter("quantity") : "1"; // 默认数量为1

        try {
            int productIdInt = Integer.parseInt(productId);
            int quantity = Integer.parseInt(quantityStr);

            HttpSession session = request.getSession();
            String username = (String) session.getAttribute("username");
            if (username != null) {
                UserDao userDao = new UserDao();
                User user = userDao.getUserByUsername(username);
                if (user != null) {
                    int userId = user.getId();
                    CartDao cartDao = new CartDao();
                    // 检查购物车中是否已存在相同商品
                    CartItem existingItem = cartDao.getCartItemByUserIdAndProductId(userId, productIdInt);
                    if (existingItem != null) {
                        // 如果存在，则增加数量
                        existingItem.setQuantity(existingItem.getQuantity() + quantity);
                        cartDao.updateCartItem(existingItem);
                    } else {
                        // 如果不存在，则添加新商品
                        cartDao.addToCart(new CartItem(0, userId, productIdInt, quantity));
                    }
                }
            }

            // 重定向到购物车页面
            response.sendRedirect("cart.jsp");//添加完商品自动转到购物车界面
        } catch (NumberFormatException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input");
        }
    }
}
