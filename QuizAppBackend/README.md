# Core Java Quiz Application

This is a command-line quiz application built with core Java, developed as part of the InnoByte Services internship program.

## Description

The application allows users to interact with a quiz system directly from the console. It connects to a PostgreSQL database to store and retrieve quiz questions and user data. The program features a menu-driven interface for navigating its functionalities.

## Tech Stack

- **Java 21**
- **JDBC** (Java Database Connectivity) for database interaction
- **Maven** (for managing dependencies like the PostgreSQL driver)
- **PostgreSQL** (as the database)

## Features

- User can register an account.
- User can log in to their account.
- An authenticated user can create a new quiz with multiple-choice questions.
- An authenticated user can take an existing quiz and see their score at the end.

## How to Run Locally

This project uses Maven to manage dependencies. You can compile and run it from the command line.

1.  Clone the repository.
2.  Open a terminal or command prompt in the project's root directory.
3.  Compile the project using Maven:
    ```bash
    mvn compile
    ```
4.  Run the application using the following Maven command. **Make sure to replace `com.yourpackage.MainClass` with the actual full path to your main class.**
    ```bash
    mvn exec:java -Dexec.mainClass="com.yourpackage.MainClass"
    ```
5.  The application will start running in your terminal.