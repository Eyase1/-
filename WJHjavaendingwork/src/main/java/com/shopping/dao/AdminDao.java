package com.shopping.dao;

import com.shopping.model.Admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdminDao {
    private static final String DB_URL = "jdbc:DM://localhost:5236";
    private static final String USER = "SYSDBA";
    private static final String PASS = "SYSDBA";

    public boolean checkAdminLogin(String username, String password) throws ClassNotFoundException {
        Class.forName("dm.jdbc.driver.DmDriver");
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "SELECT * FROM \"ADMIN\" WHERE \"USERNAME\"=? AND \"PASSWORD\"=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            boolean result = rs.next();
            System.out.println("Admin login check for username: " + username + " - Result: " + result);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Admin getAdminByUsername(String username) {
        try {
            Class.forName("dm.jdbc.driver.DmDriver");
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                String sql = "SELECT * FROM \"ADMIN\" WHERE \"USERNAME\"=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    Admin admin = new Admin();
                    admin.setId(rs.getInt("id"));
                    admin.setUsername(rs.getString("username"));
                    admin.setPassword(rs.getString("password"));
                    admin.setLastLogin(rs.getTimestamp("last_login"));
                    // 假设有一个方法获取登录历史
                    admin.setLoginHistory(getLoginHistory(username));
                    return admin;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getLoginHistory(String username) {
        // 实现获取登录历史的逻辑
        return "登录历史记录";
    }
}
