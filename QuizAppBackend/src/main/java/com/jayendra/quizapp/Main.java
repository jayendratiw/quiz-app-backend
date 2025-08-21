package com.jayendra.quizapp;

import com.jayendra.quizapp.dao.QuizDAO;
import com.jayendra.quizapp.dao.ScoreDAO; // New Import
import com.jayendra.quizapp.dao.UserDAO;
import com.jayendra.quizapp.model.*; // New Import to get all models
import com.jayendra.quizapp.util.DatabaseManager;
import com.jayendra.quizapp.util.PasswordUtil;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
    // New DAO for scores
    private static ScoreDAO scoreDAO = new ScoreDAO();
    private static UserDAO userDAO = new UserDAO();
    private static QuizDAO quizDAO = new QuizDAO();
    private static Scanner scanner = new Scanner(System.in);
    private static User loggedInUser = null;

    public static void main(String[] args) {
        DatabaseManager.createNewDatabase();
        System.out.println("Welcome to the Online Quiz Application!");
        while (true) {
            if (loggedInUser == null) {
                showMainMenu();
            } else {
                if (loggedInUser.isAdmin()) { showAdminMenu(); } else { showUserMenu(); }
            }
        }
    }

    private static int getUserInput() {
        try { return Integer.parseInt(scanner.nextLine()); } catch (NumberFormatException e) { return -1; }
    }

    private static void showMainMenu() {
        System.out.println("\n--- Main Menu ---\n1. Login\n2. Register\n3. Exit");
        System.out.print("Please enter your choice: ");
        int choice = getUserInput();
        switch (choice) {
            case 1: handleLogin(); break;
            case 2: handleRegistration(); break;
            case 3: System.out.println("Goodbye!"); System.exit(0); break;
            default: System.out.println("Invalid choice.");
        }
    }

    private static void showUserMenu() {
        System.out.println("\n--- User Menu ---\nWelcome, " + loggedInUser.getUsername());
        System.out.println("1. Take a Quiz");
        System.out.println("2. View My Scores"); // This will now work
        System.out.println("3. Logout");
        System.out.print("Please enter your choice: ");
        int choice = getUserInput();
        switch (choice) {
            case 1: handleTakeQuiz(); break;
            case 2: handleViewScores(); break; // New method call
            case 3: logout(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    // --- NEW METHOD to view scores ---
    private static void handleViewScores() {
        List<QuizAttempt> attempts = scoreDAO.getAttemptsForUser(loggedInUser.getId());
        if (attempts.isEmpty()) {
            System.out.println("\nYou have not attempted any quizzes yet.");
            return;
        }
        System.out.println("\n--- Your Past Quiz Scores ---");
        for (QuizAttempt attempt : attempts) {
            System.out.printf("Quiz: '%s' | Score: %d/%d | Date: %s\n",
                    attempt.getQuizTitle(),
                    attempt.getScore(),
                    attempt.getTotalQuestions(),
                    attempt.getAttemptDate().toLocalDateTime().toLocalDate());
        }
    }

    private static void handleTakeQuiz() {
        List<Quiz> quizzes = quizDAO.getAllQuizzes();
        if (quizzes.isEmpty()) { System.out.println("Sorry, there are no quizzes available."); return; }
        System.out.println("\n--- Available Quizzes ---");
        for (Quiz quiz : quizzes) { System.out.println("ID: " + quiz.getId() + " - Title: " + quiz.getTitle()); }
        System.out.print("Enter the ID of the quiz you want to take: ");
        int quizIdToTake = getUserInput();

        // Find the quiz title for saving the score later
        String quizTitle = quizzes.stream().filter(q -> q.getId() == quizIdToTake).findFirst().map(Quiz::getTitle).orElse("Unknown Quiz");

        List<Question> questions = quizDAO.getQuestionsForQuiz(quizIdToTake);
        Collections.shuffle(questions);
        int score = 0;
        int questionNumber = 1;
        System.out.println("\nStarting quiz: " + quizTitle + "\n------------------------------------");
        for (Question question : questions) {
            System.out.println("\nQuestion " + questionNumber++ + ": " + question.getQuestionText());
            List<Option> options = quizDAO.getOptionsForQuestion(question.getId());
            char optionChar = 'A';
            int correctOptionIndex = -1;
            for (int i = 0; i < options.size(); i++) {
                System.out.println("  " + optionChar + ") " + options.get(i).getOptionText());
                if (options.get(i).isCorrect()) { correctOptionIndex = i; }
                optionChar++;
            }
            System.out.print("Your answer (A, B, C, D...): ");
            String answer = scanner.nextLine().toUpperCase();
            if (answer.isEmpty()) { System.out.println("No answer provided. Marked as incorrect."); continue; }
            int selectedOptionIndex = answer.charAt(0) - 'A';
            if (selectedOptionIndex == correctOptionIndex) { System.out.println("Correct!"); score++; } else { System.out.println("Incorrect."); }
            System.out.println("------------------------------------");
        }
        System.out.println("\nQuiz Complete!");
        System.out.println("Your final score is: " + score + " out of " + questions.size());

        // --- NEW: Save the attempt to the database ---
        QuizAttempt attempt = new QuizAttempt(0, loggedInUser.getId(), quizIdToTake, score, questions.size(), quizTitle, null);
        scoreDAO.saveAttempt(attempt);
        System.out.println("Your score has been saved.");
    }


    private static void showAdminMenu() { System.out.println("\n--- Admin Menu ---\nWelcome, " + loggedInUser.getUsername() + " (Admin)\n1. Create New Quiz\n2. Logout"); System.out.print("Please enter your choice: "); int choice = getUserInput(); switch (choice) { case 1: handleCreateQuiz(); break; case 2: logout(); break; default: System.out.println("Invalid choice."); } }
    private static void handleCreateQuiz() { System.out.println("\n--- Create New Quiz ---"); System.out.print("Enter quiz title: "); String title = scanner.nextLine(); System.out.print("Enter quiz description: "); String description = scanner.nextLine(); int quizId = quizDAO.addQuiz(title, description); if (quizId == 0) { System.out.println("Error: Could not create quiz."); return; } System.out.println("Quiz '" + title + "' created successfully with ID: " + quizId); String addAnotherQuestion; do { System.out.print("\nEnter question text: "); String qText = scanner.nextLine(); int qId = quizDAO.addQuestion(new Question(0, quizId, qText)); System.out.println("Question added with ID: " + qId); for (int i = 1; i <= 4; i++) { System.out.print("  Enter text for option " + i + ": "); String oText = scanner.nextLine(); System.out.print("  Is this the correct answer? (yes/no): "); boolean isCorrect = scanner.nextLine().equalsIgnoreCase("yes"); quizDAO.addOption(new Option(0, qId, oText, isCorrect)); } System.out.println("Options added for this question."); System.out.print("\nAdd another question to this quiz? (yes/no): "); addAnotherQuestion = scanner.nextLine(); } while (addAnotherQuestion.equalsIgnoreCase("yes")); System.out.println("Quiz creation complete."); }
    private static void handleRegistration() { System.out.println("\n--- New User Registration ---"); System.out.print("Enter username: "); String username = scanner.nextLine(); System.out.print("Enter password: "); String password = scanner.nextLine(); userDAO.addUser(username, password, false); System.out.println("Registration successful! Please log in."); }
    private static void handleLogin() { System.out.println("\n--- User Login ---"); System.out.print("Enter username: "); String username = scanner.nextLine(); System.out.print("Enter password: "); String password = scanner.nextLine(); User user = userDAO.getUserByUsername(username); if (user != null && PasswordUtil.checkPassword(password, user.getPasswordHash())) { loggedInUser = user; System.out.println("Login successful!"); } else { System.out.println("Invalid username or password."); } }
    private static void logout() { loggedInUser = null; System.out.println("You have been logged out."); }
}