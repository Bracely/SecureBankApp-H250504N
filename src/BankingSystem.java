import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankingSystem {
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Path dataDirectory;
    private final Path usersFile;
    private final Path accountsFile;
    private final Path transactionsFile;

    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Account> accounts = new HashMap<>();
    private final List<Transaction> transactions = new ArrayList<>();

    private String currentUser;

    public BankingSystem() {
        this.dataDirectory = Paths.get("data");
        this.usersFile = dataDirectory.resolve("users.txt");
        this.accountsFile = dataDirectory.resolve("accounts.txt");
        this.transactionsFile = dataDirectory.resolve("transactions.txt");

        try {
            Files.createDirectories(dataDirectory);
        } catch (IOException e) {
            System.out.println("Unable to create data directory: " + e.getMessage());
        }

        loadUsers();
        loadAccounts();
        loadTransactions();
    }

    public boolean registerUser(String username, String password, String fullName) {
        if (!isValidUsername(username)) {
            System.out.println("Invalid username. Use 3-20 characters and only letters, numbers, underscores or dots.");
            return false;
        }

        if (!isValidPassword(password)) {
            System.out.println("Invalid password. Use at least 6 characters with letters and numbers.");
            return false;
        }

        if (users.containsKey(username)) {
            System.out.println("Username already exists. Please choose another one.");
            return false;
        }

        users.put(username, new User(username, password, fullName));
        saveUsers();
        System.out.println("Registration successful for: " + username);
        return true;
    }

    public boolean login(String username, String password) {
        if (!users.containsKey(username)) {
            System.out.println("Invalid username.");
            return false;
        }

        User user = users.get(username);
        if (!user.getPassword().equals(password)) {
            System.out.println("Invalid password.");
            return false;
        }

        currentUser = username;
        System.out.println("Login successful. Welcome " + user.getFullName() + "!");
        return true;
    }

    public void logout() {
        currentUser = null;
        System.out.println("You have been logged out.");
    }

    public boolean createAccount(String accountName) {
        if (currentUser == null) {
            System.out.println("You must log in before creating an account.");
            return false;
        }

        String accountNumber = generateAccountNumber();
        Account account = new Account(accountNumber, currentUser, 0.0);
        accounts.put(accountNumber, account);
        saveAccounts();
        System.out.println("Account created successfully. Account Number: " + accountNumber);
        return true;
    }

    public List<Account> getCurrentUserAccounts() {
        List<Account> userAccounts = new ArrayList<>();
        for (Account account : accounts.values()) {
            if (account.getOwnerUsername().equals(currentUser)) {
                userAccounts.add(account);
            }
        }
        return userAccounts;
    }

    public Account getAccountByNumber(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public void deposit(String accountNumber, double amount) {
        Account account = getAccountByNumber(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        if (!account.getOwnerUsername().equals(currentUser)) {
            System.out.println("This account does not belong to the current user.");
            return;
        }

        try {
            account.deposit(amount);
            addTransaction(accountNumber, "DEPOSIT", amount, "Bank deposit");
            saveAccounts();
            saveTransactions();
            System.out.println("Deposit successful. New balance: $" + String.format("%.2f", account.getBalance()));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void withdraw(String accountNumber, double amount) {
        Account account = getAccountByNumber(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        if (!account.getOwnerUsername().equals(currentUser)) {
            System.out.println("This account does not belong to the current user.");
            return;
        }

        try {
            account.withdraw(amount);
            addTransaction(accountNumber, "WITHDRAWAL", amount, "Bank withdrawal");
            saveAccounts();
            saveTransactions();
            System.out.println("Withdrawal successful. New balance: $" + String.format("%.2f", account.getBalance()));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void viewBalance(String accountNumber) {
        Account account = getAccountByNumber(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        if (!account.getOwnerUsername().equals(currentUser)) {
            System.out.println("This account does not belong to the current user.");
            return;
        }

        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Current Balance: $" + String.format("%.2f", account.getBalance()));
    }

    public void displayTransactions(String accountNumber) {
        boolean found = false;
        System.out.println("\nTransaction History for account " + accountNumber);

        for (Transaction transaction : transactions) {
            if (transaction.getAccountNumber().equals(accountNumber)) {
                found = true;
                System.out.println(
                        "ID: " + transaction.getTransactionId() +
                                " | Type: " + transaction.getType() +
                                " | Amount: $" + String.format("%.2f", transaction.getAmount()) +
                                " | Date: " + transaction.getTimestamp() +
                                " | Notes: " + transaction.getDescription());
            }
        }

        if (!found) {
            System.out.println("No transactions found for this account.");
        }
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    private void addTransaction(String accountNumber, String type, double amount, String description) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        transactions.add(new Transaction(accountNumber, type, amount, timestamp, description));
    }

    private String generateAccountNumber() {
        long randomNumber = (long) (Math.random() * 9000000L) + 1000000L;
        return "SBA-" + randomNumber;
    }

    private boolean isValidUsername(String username) {
        return username != null && username.matches("[A-Za-z0-9_.]{3,20}");
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 6 && password.matches(".*[A-Za-z].*") && password.matches(".*[0-9].*");
    }

    private void loadUsers() {
        try {
            if (Files.notExists(usersFile)) {
                Files.write(usersFile, new byte[0], StandardOpenOption.CREATE);
                return;
            }

            List<String> lines = Files.readAllLines(usersFile, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line == null || line.trim().isEmpty()) {
                    continue;
                }
                User user = User.fromFileLine(line);
                if (user != null) {
                    users.put(user.getUsername(), user);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    private void loadAccounts() {
        try {
            if (Files.notExists(accountsFile)) {
                Files.write(accountsFile, new byte[0], StandardOpenOption.CREATE);
                return;
            }

            List<String> lines = Files.readAllLines(accountsFile, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line == null || line.trim().isEmpty()) {
                    continue;
                }
                Account account = Account.fromFileLine(line);
                if (account != null) {
                    accounts.put(account.getAccountNumber(), account);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading accounts: " + e.getMessage());
        }
    }

    private void loadTransactions() {
        try {
            if (Files.notExists(transactionsFile)) {
                Files.write(transactionsFile, new byte[0], StandardOpenOption.CREATE);
                return;
            }

            List<String> lines = Files.readAllLines(transactionsFile, StandardCharsets.UTF_8);
            int maxTransactionId = 0;

            for (String line : lines) {
                if (line == null || line.trim().isEmpty()) {
                    continue;
                }
                Transaction transaction = Transaction.fromFileLine(line);
                if (transaction != null) {
                    transactions.add(transaction);
                    maxTransactionId = Math.max(maxTransactionId, transaction.getTransactionId());
                }
            }

            Transaction.resetCounter();
            if (maxTransactionId > 0) {
                Transaction.setCounter(maxTransactionId + 1);
            }
        } catch (IOException e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }
    }

    private void saveUsers() {
        try {
            List<String> lines = new ArrayList<>();
            for (User user : users.values()) {
                lines.add(user.toFileLine());
            }
            Files.write(usersFile, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    private void saveAccounts() {
        try {
            List<String> lines = new ArrayList<>();
            for (Account account : accounts.values()) {
                lines.add(account.toFileLine());
            }
            Files.write(accountsFile, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }

    private void saveTransactions() {
        try {
            List<String> lines = new ArrayList<>();
            for (Transaction transaction : transactions) {
                lines.add(transaction.toFileLine());
            }
            Files.write(transactionsFile, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }
}
