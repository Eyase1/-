package com.shopping.dao;

import com.shopping.model.Product;
import com.shopping.model.Comment;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {
    private static final String DB_URL = "jdbc:dm://localhost:5236/SYSDBA";
    private static final String USER = "SYSDBA";
    private static final String PASS = "SYSDBA";

    public void deleteProduct(int productId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "DELETE FROM \"PRODUCTS\" WHERE \"ID\"=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public void addProduct(Product product) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "INSERT INTO \"PRODUCTS\" (\"NAME\", \"DESCRIPTION\", \"PRICE\", \"IMAGE_URL\") VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setString(4, product.getImageUrl());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public void updateProduct(Product product) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "UPDATE \"PRODUCTS\" SET \"NAME\"=?, \"DESCRIPTION\"=?, \"PRICE\"=?, \"IMAGE_URL\"=? WHERE \"ID\"=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setString(4, product.getImageUrl());
            stmt.setInt(5, product.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public List<Product> getAllProducts() throws ClassNotFoundException {
        List<Product> products = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "SELECT * FROM \"PRODUCTS\"";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getDouble("price"), rs.getString("image_url"));
                product.setComments(getCommentsByProductId(product.getId()));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public Product getProductById(int productId) throws ClassNotFoundException {
        Product product = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "SELECT * FROM \"PRODUCTS\" WHERE \"ID\"=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                product = new Product(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getDouble("price"), rs.getString("image_url"));
                product.setComments(getCommentsByProductId(product.getId()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public List<Product> searchProducts(String query) throws ClassNotFoundException {
        List<Product> products = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "SELECT * FROM \"PRODUCTS\" WHERE \"NAME\" LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + query + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getDouble("price"), rs.getString("image_url"));
                product.setComments(getCommentsByProductId(product.getId()));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    private List<Comment> getCommentsByProductId(int productId) throws ClassNotFoundException, SQLException {
        CommentDao commentDao = new CommentDao();
        return commentDao.getCommentsByProductId(productId);
    }
}
