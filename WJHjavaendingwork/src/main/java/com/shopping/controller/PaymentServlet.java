package com.shopping.controller;

import com.shopping.dao.UserDao;
import com.shopping.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
//处理用户支付请求
public class PaymentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        String password = request.getParameter("password");

        UserDao userDao = new UserDao();
        User user = userDao.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            // 支付成功，重定向到首页
            response.sendRedirect("index.jsp");
        } else {
            // 支付失败，返回支付页面并显示错误信息
            request.setAttribute("message", "密码错误，请重试！");
            request.getRequestDispatcher("payment.jsp").forward(request, response);
        }
    }
}