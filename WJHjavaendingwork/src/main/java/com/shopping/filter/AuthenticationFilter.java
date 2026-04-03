package com.shopping.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
//检查用户是否已登录，如果未登录则重定向到登录页面
@WebFilter(urlPatterns = {"/profile.jsp", "/cart.jsp", "/orders.jsp", "/changePassword.jsp", "/addProduct.jsp", "/productManagement.jsp", "/orderManagement.jsp", "/editProduct.jsp"})
public class AuthenticationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化代码
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String username = session != null ? (String) session.getAttribute("username") : null;

        if (username == null) {
            httpResponse.sendRedirect("login.jsp");
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // 销毁代码
    }
}
