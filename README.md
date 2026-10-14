# Secure Banking Application

## Description
This project is a simple console-based banking application developed in Java. It was built to show my understanding of object-oriented programming, user authentication, transaction processing, and file-based data persistence. The aim of the project is to demonstrate a secure and structured approach to building a basic banking system as part of my studies at the Harare Institute of Technology.

## Student Details
- Student Name: Bracely Tarisai Makoni
- Registration Number: H250504N
- Institution: Harare Institute of Technology, Zimbabwe

## Features
- User registration and login system
- Account creation for authenticated users
- Balance checking for each account
- Deposit of money into a selected account
- Withdrawal of money with balance validation
- Persistence of user and account data in text files
- Automatic loading of saved data when the application starts
- Transaction history tracking for each account

## Project Overview
The application allows a user to sign up, log in, create a bank account, and carry out basic banking operations. The system stores important information in text files inside the data folder, which means the data remains available even after the program is closed and restarted. This approach is useful for learning persistence in Java without using a database.

## How to Run
1. Make sure Java is installed on your computer.
2. Open a terminal in the project directory.
3. Compile the project using the following command:
   javac -d out src/*.java
4. Run the application using:
   java -cp out Main

## Project Structure
- src/Main.java - Contains the main menu and user interaction flow
- src/User.java - Represents a user and stores login credentials
- src/Account.java - Represents a bank account and manages balance updates
- src/Transaction.java - Stores transaction records for each account
- src/BankingSystem.java - Handles core logic, file persistence, and operations
- data/users.txt - Stores user credentials
- data/accounts.txt - Stores account details and balances
- data/transactions.txt - Stores transaction history

## Security Notes
- For this coursework, passwords are stored in plain text to keep the project simple and to meet the file-persistence requirement.
- In a real-world banking system, I would use hashing and salting techniques such as bcrypt or PBKDF2 to protect user credentials.
- This project focuses on showing good Java programming practices, secure coding awareness, and basic persistence in a student-level application.
