package com.shopping.controller;

import com.shopping.dao.CommentDao;
import com.shopping.model.Comment;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Date;
//保存用户对商品的评论
public class SaveCommentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        try {
            int productId = 0;
            int rating = 0;
            String commentText = null;

            // 获取请求参数并进行有效性检查
            String productIdStr = request.getParameter("productId");
            if (productIdStr == null || productIdStr.isEmpty()) {
                throw new IllegalArgumentException("Invalid product ID");
            }
            productId = Integer.parseInt(productIdStr);

            String ratingStr = request.getParameter("rating");
            if (ratingStr == null || ratingStr.isEmpty()) {
                throw new IllegalArgumentException("Invalid rating");
            }
            rating = Integer.parseInt(ratingStr);

            commentText = request.getParameter("commentText");
            if (commentText == null || commentText.isEmpty()) {
                throw new IllegalArgumentException("评论不能为空！");
            }

            Comment comment = new Comment();
            comment.setProductId(productId);
            comment.setUsername(username);
            comment.setComment(commentText);
            comment.setRating(rating);
            comment.setTimestamp(new Date());

            CommentDao commentDao = new CommentDao();
            commentDao.saveComment(comment);

            response.sendRedirect("productDetail.jsp?id=" + productId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input parameters");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "保存评论失败");
        }
    }
}
