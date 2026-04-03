package com.shopping.controller;

import com.shopping.dao.UserDao;
import com.shopping.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
//注册功能
public class RegisterServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request, response);
}

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //获取用户账号密码
        UserDao userDao = new UserDao();
        userDao.addUser(new User(username, password));

        request.setAttribute("message", "注册成功，请重新登录！");
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}

