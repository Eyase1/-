package com.shopping.dao;

import com.shopping.model.Order;
import com.shopping.model.Product;
import com.shopping.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {
    private static final String DB_URL = "jdbc:DM://localhost:5236/SYSDBA";
    private static final String USER = "SYSDBA";
    private static final String PASS = "SYSDBA";

    public void saveOrder(Order order) {
        try {
            Class.forName("dm.jdbc.driver.DmDriver");
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                String sql = "INSERT INTO \"ORDERS\" (\"USER_ID\", \"PRODUCT_ID\", \"QUANTITY\", \"TOTAL_AMOUNT\") VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, order.getUserId());
                stmt.setInt(2, order.getProductId());
                stmt.setInt(3, order.getQuantity());
                stmt.setDouble(4, order.getTotalAmount());
                stmt.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Order> getOrdersByUserId(String username) {
        List<Order> orders = new ArrayList<>();
        try {
            Class.forName("dm.jdbc.driver.DmDriver");
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                String sql = "SELECT * FROM \"ORDERS\" WHERE \"USER_ID\" = (SELECT \"ID\" FROM \"USERS\" WHERE \"USERNAME\" = ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getInt("id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setProductId(rs.getInt("product_id"));
                    order.setQuantity(rs.getInt("quantity"));
                    order.setTotalAmount(rs.getDouble("total_amount"));
                    order.setTransactionTime(rs.getTimestamp("transaction_time"));
                    orders.add(order);
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public String getProductNameById(int productId) {
        String productName = null;
        try {
            Class.forName("dm.jdbc.driver.DmDriver");
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                String sql = "SELECT \"NAME\" FROM \"PRODUCTS\" WHERE \"ID\" = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, productId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    productName = rs.getString("name");
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return productName;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        try {
            Class.forName("dm.jdbc.driver.DmDriver");
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                String sql = "SELECT * FROM \"ORDERS\"";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getInt("id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setProductId(rs.getInt("product_id"));
                    order.setQuantity(rs.getInt("quantity"));
                    order.setTotalAmount(rs.getDouble("total_amount"));
                    order.setTransactionTime(rs.getTimestamp("transaction_time"));
                    orders.add(order);
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public String getUsernameById(int userId) {
        String username = null;
        try {
            Class.forName("dm.jdbc.driver.DmDriver");
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                String sql = "SELECT \"USERNAME\" FROM \"USERS\" WHERE \"ID\" = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    username = rs.getString("username");
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return username;
    }
}
