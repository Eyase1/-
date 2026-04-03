package com.shopping.controller;

import com.shopping.dao.ProductDao;
import com.shopping.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
//添加商品
public class AddProductServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        double price = Double.parseDouble(request.getParameter("price"));
        String imageUrl = request.getParameter("imageUrl");

        Product product = new Product(0, name, description, price, imageUrl);
        ProductDao productDao = new ProductDao();
        try {
            productDao.addProduct(product);
            HttpSession session = request.getSession();
            session.setAttribute("addProductSuccess", "添加成功");
            response.sendRedirect("productManagement.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "添加商品出错！");
        }
    }
}
