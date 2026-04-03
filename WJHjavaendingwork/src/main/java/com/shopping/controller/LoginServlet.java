package com.shopping.controller;

import com.shopping.dao.AdminDao;
import com.shopping.dao.UserDao;
import com.shopping.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //获取账号密码
        UserDao userDao = new UserDao();
        AdminDao adminDao = new AdminDao();
        try {
            if (adminDao.checkAdminLogin(username, password)) {
                HttpSession session = request.getSession();
                session.setAttribute("admin", username);
                response.sendRedirect("admin.jsp"); // 管理员界面
                System.out.println("Admin login successful for username: " + username);
            } else if (userDao.checkLogin(username, password)) {
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                response.sendRedirect("index.jsp"); // 用户首页
                System.out.println("User login successful for username: " + username);
            } else {
                request.setAttribute("message", "帐号密码错误!");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                System.out.println("Login failed for username: " + username);//传递错误信息到日志当中
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
