package com.shopping.controller;

import com.shopping.dao.AdminDao;
import com.shopping.model.Admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
//处理管理员信息的显示
public class AdminServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String adminUsername = (String) request.getSession().getAttribute("admin");
        AdminDao adminDao = new AdminDao();//使用 AdminDao 获取管理员详细信息
        try {
            Admin admin = adminDao.getAdminByUsername(adminUsername);
            request.setAttribute("admin", admin);//从会话中获取管理员用户名
            request.getRequestDispatcher("/adminInfo.jsp").forward(request, response); //将管理员信息设置到请求属性中，并转发到 adminInfo.jsp 页面
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while retrieving admin information");
        }
    }
}
