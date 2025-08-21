package com.jayendra.quizapp.dao;
import com.jayendra.quizapp.model.User;
import com.jayendra.quizapp.util.DatabaseManager;
import com.jayendra.quizapp.util.PasswordUtil;
import java.sql.*;

public class UserDAO {
    public void addUser(String username, String plainPassword, boolean isAdmin) {
        String sql = "INSERT INTO users(username, password_hash, is_admin) VALUES(?,?,?)";
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.setInt(3, isAdmin ? 1 : 0);
            pstmt.executeUpdate();
        } catch (SQLException e) { System.out.println("Error adding user: " + e.getMessage()); }
    }
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password_hash"), rs.getInt("is_admin") == 1);
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }
}
