package com.jayendra.quizapp.model;

public class User {
    private int id;
    private String username;
    private String passwordHash;
    private boolean isAdmin;

    public User(int id, String username, String passwordHash, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.isAdmin = isAdmin;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public boolean isAdmin() { return isAdmin; }
}
