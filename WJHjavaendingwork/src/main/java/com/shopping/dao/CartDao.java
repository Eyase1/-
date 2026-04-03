package com.shopping.dao;

import com.shopping.model.CartItem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartDao {
    private static final String DB_URL = "jdbc:DM://localhost:5236/SYSDBA";
    private static final String USER = "SYSDBA";
    private static final String PASS = "SYSDBA";

    public void addToCart(CartItem item) throws ClassNotFoundException {
        Class.forName("dm.jdbc.driver.DmDriver");
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "INSERT INTO \"CARTITEMS\"(\"USER_ID\", \"PRODUCT_ID\", \"QUANTITY\") VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, item.getUserId());
            stmt.setInt(2, item.getProductId());
            stmt.setInt(3, item.getQuantity());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //根据用户 ID 获取购物车项
    public List<CartItem> getCartItemsByUserId(int userId) {
        List<CartItem> items = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "SELECT * FROM \"CARTITEMS\" WHERE \"USER_ID\"=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                CartItem item = new CartItem(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("product_id"), rs.getInt("quantity"));
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
    //根据用户 ID 和商品 ID 获取购物车项
    public CartItem getCartItemByUserIdAndProductId(int userId, int productId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "SELECT * FROM \"CARTITEMS\" WHERE \"USER_ID\"=? AND \"PRODUCT_ID\"=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new CartItem(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("product_id"), rs.getInt("quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateCartItem(CartItem item) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "UPDATE \"CARTITEMS\" SET \"QUANTITY\"=? WHERE \"ID\"=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, item.getQuantity());
            stmt.setInt(2, item.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void removeFromCart(int cartItemId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "DELETE FROM \"CARTITEMS\" WHERE \"ID\"=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cartItemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void clearCart(int userId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "DELETE FROM \"CARTITEMS\" WHERE \"USER_ID\"=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
