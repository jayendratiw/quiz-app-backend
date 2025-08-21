package com.jayendra.quizapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DATABASE_URL = "jdbc:sqlite:quiz_app.db";

    public static void createNewDatabase() {
        String usersTableSql = "CREATE TABLE IF NOT EXISTS users (user_id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL UNIQUE, password_hash TEXT NOT NULL, is_admin INTEGER NOT NULL DEFAULT 0);";
        String quizzesTableSql = "CREATE TABLE IF NOT EXISTS quizzes (quiz_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, description TEXT);";
        String questionsTableSql = "CREATE TABLE IF NOT EXISTS questions (question_id INTEGER PRIMARY KEY AUTOINCREMENT, quiz_id INTEGER NOT NULL, question_text TEXT NOT NULL);";
        String optionsTableSql = "CREATE TABLE IF NOT EXISTS options (option_id INTEGER PRIMARY KEY AUTOINCREMENT, question_id INTEGER NOT NULL, option_text TEXT NOT NULL, is_correct INTEGER NOT NULL);";

        // --- NEW --- SQL for quiz_attempts table
        String attemptsTableSql = "CREATE TABLE IF NOT EXISTS quiz_attempts (attempt_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER NOT NULL, quiz_id INTEGER NOT NULL, score INTEGER NOT NULL, total_questions INTEGER NOT NULL, quiz_title TEXT, attempt_date DATETIME DEFAULT CURRENT_TIMESTAMP);";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(usersTableSql);
            stmt.execute(quizzesTableSql);
            stmt.execute(questionsTableSql);
            stmt.execute(optionsTableSql);
            stmt.execute(attemptsTableSql); // --- NEW ---
        } catch (SQLException e) {
            System.out.println("Error creating database tables: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }
}