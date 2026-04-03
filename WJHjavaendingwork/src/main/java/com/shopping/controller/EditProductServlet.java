package com.shopping.controller;

import com.shopping.dao.ProductDao;
import com.shopping.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
//编辑商品信息
public class EditProductServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        double price = Double.parseDouble(request.getParameter("price"));
        String imageUrl = request.getParameter("imageUrl");
        //获取商品信息
        Product product = new Product(id, name, description, price, imageUrl);
        ProductDao productDao = new ProductDao();//调用连接数据库的方法
        try {
            productDao.updateProduct(product);
            response.sendRedirect("productManagement.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "修改商品信息出错!");
        }
    }
}
