package com.jayendra.quizapp.dao;

import com.jayendra.quizapp.model.Option;
import com.jayendra.quizapp.model.Question;
import com.jayendra.quizapp.util.DatabaseManager;
import java.sql.*;

public class QuizDAO {

    /**
     * Adds a new quiz to the database.
     * @param title The title of the quiz.
     * @param description A short description of the quiz.
     * @return The auto-generated ID of the new quiz.
     */
    public int addQuiz(String title, String description) {
        String sql = "INSERT INTO quizzes(title, description) VALUES(?,?)";
        int quizId = 0;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.executeUpdate();

            // Get the auto-generated quiz_id
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                quizId = generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return quizId;
    }

    /**
     * Adds a new question to the database, linked to a quiz.
     * @param question The Question object to add.
     * @return The auto-generated ID of the new question.
     */
    public int addQuestion(Question question) {
        String sql = "INSERT INTO questions(quiz_id, question_text) VALUES(?,?)";
        int questionId = 0;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, question.getQuizId());
            pstmt.setString(2, question.getQuestionText());
            pstmt.executeUpdate();

            // Get the auto-generated question_id
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                questionId = generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return questionId;
    }

    /**
     * Adds a new answer option to the database, linked to a question.
     * @param option The Option object to add.
     */
    public void addOption(Option option) {
        String sql = "INSERT INTO options(question_id, option_text, is_correct) VALUES(?,?,?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, option.getQuestionId());
            pstmt.setString(2, option.getOptionText());
            pstmt.setBoolean(3, option.isCorrect());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public java.util.List<com.jayendra.quizapp.model.Quiz> getAllQuizzes() {
        String sql = "SELECT * FROM quizzes";
        java.util.List<com.jayendra.quizapp.model.Quiz> quizzes = new java.util.ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                quizzes.add(new com.jayendra.quizapp.model.Quiz(
                        rs.getInt("quiz_id"),
                        rs.getString("title"),
                        rs.getString("description")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return quizzes;
    }

    public java.util.List<Question> getQuestionsForQuiz(int quizId) {
        String sql = "SELECT * FROM questions WHERE quiz_id = ?";
        java.util.List<Question> questions = new java.util.ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quizId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                questions.add(new Question(
                        rs.getInt("question_id"),
                        rs.getInt("quiz_id"),
                        rs.getString("question_text")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return questions;
    }

    public java.util.List<Option> getOptionsForQuestion(int questionId) {
        String sql = "SELECT * FROM options WHERE question_id = ?";
        java.util.List<Option> options = new java.util.ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, questionId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                options.add(new Option(
                        rs.getInt("option_id"),
                        rs.getInt("question_id"),
                        rs.getString("option_text"),
                        rs.getInt("is_correct") == 1));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return options;
    }
}
