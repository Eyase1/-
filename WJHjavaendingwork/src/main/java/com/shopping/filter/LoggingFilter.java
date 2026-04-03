package com.shopping.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
//记录用户的访问日志
@WebFilter(urlPatterns = "/*")
public class LoggingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化代码
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String ipAddress = httpRequest.getRemoteAddr();
        String url = httpRequest.getRequestURL().toString();
        String method = httpRequest.getMethod();
        Date date = new Date();

        System.out.println("[" + date + "] " + ipAddress + " " + method + " " + url);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 销毁代码
    }
}
