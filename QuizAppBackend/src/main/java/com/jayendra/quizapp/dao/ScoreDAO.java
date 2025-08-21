package com.jayendra.quizapp.dao;

import com.jayendra.quizapp.model.QuizAttempt;
import com.jayendra.quizapp.util.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreDAO {

    public void saveAttempt(QuizAttempt attempt) {
        String sql = "INSERT INTO quiz_attempts(user_id, quiz_id, score, total_questions, quiz_title) VALUES(?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, attempt.getUserId());
            pstmt.setInt(2, attempt.getQuizId());
            pstmt.setInt(3, attempt.getScore());
            pstmt.setInt(4, attempt.getTotalQuestions());
            pstmt.setString(5, attempt.getQuizTitle());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving score: " + e.getMessage());
        }
    }

    public List<QuizAttempt> getAttemptsForUser(int userId) {
        String sql = "SELECT * FROM quiz_attempts WHERE user_id = ? ORDER BY attempt_date DESC";
        List<QuizAttempt> attempts = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                attempts.add(new QuizAttempt(
                        rs.getInt("attempt_id"),
                        rs.getInt("user_id"),
                        rs.getInt("quiz_id"),
                        rs.getInt("score"),
                        rs.getInt("total_questions"),
                        rs.getString("quiz_title"),
                        rs.getTimestamp("attempt_date")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving scores: " + e.getMessage());
        }
        return attempts;
    }
}
