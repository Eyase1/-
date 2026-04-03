package com.shopping.dao;

import com.shopping.model.Comment;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentDao {
    private static final String DB_URL = "jdbc:DM://localhost:5236/SYSDBA";
    private static final String USER = "SYSDBA";
    private static final String PASS = "SYSDBA";

    public List<Comment> getCommentsByProductId(int productId) throws ClassNotFoundException, SQLException {
        Class.forName("dm.jdbc.driver.DmDriver");
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "SELECT * FROM \"COMMENTS\" WHERE \"PRODUCT_ID\"=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            List<Comment> comments = new ArrayList<>();
            while (rs.next()) {
                Comment comment = new Comment(rs.getInt("id"), rs.getInt("product_id"), rs.getString("username"), rs.getString("comment_text"), rs.getInt("rating"), rs.getDate("timestamp"));
                comments.add(comment);
            }
            return comments;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void saveComment(Comment comment) throws ClassNotFoundException, SQLException {
        Class.forName("dm.jdbc.driver.DmDriver");
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "INSERT INTO \"COMMENTS\" (\"PRODUCT_ID\", \"USERNAME\", \"COMMENT_TEXT\", \"RATING\", \"TIMESTAMP\") VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, comment.getProductId());
            stmt.setString(2, comment.getUsername());
            stmt.setString(3, comment.getComment());
            stmt.setInt(4, comment.getRating());
            stmt.setTimestamp(5, new java.sql.Timestamp(comment.getTimestamp().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
