package com.jayendra.quizapp.model;

public class Question {
    private int id;
    private int quizId; // This links the question to a quiz
    private String questionText;

    public Question(int id, int quizId, String questionText) {
        this.id = id;
        this.quizId = quizId;
        this.questionText = questionText;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
}
