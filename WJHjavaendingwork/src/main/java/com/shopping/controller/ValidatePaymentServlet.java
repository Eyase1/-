package com.shopping.controller;

import com.shopping.dao.UserDao;
import com.shopping.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
//验证支付信息
public class ValidatePaymentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //获取用户账号密码
        UserDao userDao = new UserDao();
        try {
            if (userDao.checkLogin(username, password)) {
                PrintWriter out = response.getWriter();
                out.print("支付成功！");
            } else {
                PrintWriter out = response.getWriter();
                out.print("支付失败，密码错误！");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
