package com.shopping.controller;

import com.shopping.dao.ProductDao;
import com.shopping.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
//处理商品搜索请求
public class SearchServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        //从请求参数中获取搜索查询
        ProductDao productDao = new ProductDao();
        List<Product> results = null;
		try {
			results = productDao.searchProducts(query);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

        boolean noResults = results.isEmpty();

        request.setAttribute("results", results);
        request.setAttribute("noResults", noResults);

        request.getRequestDispatcher("searchresult.jsp").forward(request, response);
    }
}
