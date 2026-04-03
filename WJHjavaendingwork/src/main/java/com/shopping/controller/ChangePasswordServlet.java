package com.shopping.controller;

import com.shopping.dao.UserDao;
import com.shopping.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class ChangePasswordServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String oldPassword = request.getParameter("oldPassword");
	    String newPassword = request.getParameter("newPassword");
	    String confirmPassword = request.getParameter("confirmPassword");

	    HttpSession session = request.getSession();
	    String username = (String) session.getAttribute("username");

	    if (username == null) {
	        response.sendRedirect("login.jsp");
	        return;
	    }

	    UserDao userDao = new UserDao();
	    User user = userDao.getUserByUsername(username);

	    System.out.println("User: " + user);
	    System.out.println("Old Password: " + oldPassword);
	    System.out.println("New Password: " + newPassword);
	    System.out.println("Confirm New Password: " + confirmPassword);

	    if (user != null && user.getPassword().equals(oldPassword)) {
	        if (newPassword.equals(confirmPassword)) {
	            user.setPassword(newPassword);
	            userDao.updateUser(user);
	            session.setAttribute("message", "密码修改成功");
	            response.sendRedirect("profile.jsp");
	        } else {
	            request.setAttribute("message", "两次输入的新密码不一致");
	            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
	        }
	    } else {
	        request.setAttribute("message", "原密码错误");
	        request.getRequestDispatcher("changePassword.jsp").forward(request, response);
	    }
	}


}
