package com.shopping.controller;

import com.shopping.dao.ProductDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DeleteProductServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("id"));
        ProductDao productDao = new ProductDao();
        try {
            productDao.deleteProduct(productId);
            response.sendRedirect("productManagement.jsp");//重定向到productManagement.jsp界面
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "删除出错！");
        }
    }
}
