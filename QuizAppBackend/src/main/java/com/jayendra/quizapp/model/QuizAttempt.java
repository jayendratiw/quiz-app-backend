package com.jayendra.quizapp.model;

import java.sql.Timestamp;

public class QuizAttempt {
    private int id;
    private int userId;
    private int quizId;
    private int score;
    private int totalQuestions;
    private String quizTitle;
    private Timestamp attemptDate;

    public QuizAttempt(int id, int userId, int quizId, int score, int totalQuestions, String quizTitle, Timestamp attemptDate) {
        this.id = id;
        this.userId = userId;
        this.quizId = quizId;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.quizTitle = quizTitle;
        this.attemptDate = attemptDate;
    }

    // --- GETTERS (This is the corrected section) ---
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;  // <-- This was the missing method
    }

    public int getQuizId() {
        return quizId;  // <-- This was the missing method
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public Timestamp getAttemptDate() {
        return attemptDate;
    }
}