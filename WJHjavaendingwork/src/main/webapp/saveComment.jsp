<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.shopping.model.Comment" %>
<%@ page import="com.shopping.dao.CommentDao" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<%
    int productId = Integer.parseInt(request.getParameter("productId"));
    String commentText = request.getParameter("comment");
    String username = (String) session.getAttribute("username");

    if (username != null) {
        Comment comment = new Comment(); // 使用默认构造函数
        comment.setProductId(productId);
        comment.setUsername(username);
        comment.setComment(commentText);

        CommentDao commentDao = new CommentDao();
        try {
            commentDao.saveComment(comment);
            response.sendRedirect("productDetail.jsp?id=" + productId);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("productDetail.jsp?id=" + productId);
        }
    } else {
        response.sendRedirect("login.jsp");
    }
%>
