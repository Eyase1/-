package com.shopping.controller;

import com.shopping.dao.CartDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
//移除购物车商品
public class RemoveFromCartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cartItemIdStr = request.getParameter("cartItemId");
        try {
            int cartItemId = Integer.parseInt(cartItemIdStr);
            CartDao cartDao = new CartDao();
            cartDao.removeFromCart(cartItemId);
            response.getWriter().write("商品已从购物车中移除");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input");
        }
    }
}
