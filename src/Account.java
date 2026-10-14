import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Account {
    private String accountNumber;
    private String ownerUsername;
    private double balance;
    private String createdAt;

    public Account(String accountNumber, String ownerUsername, double balance) {
        this.accountNumber = accountNumber;
        this.ownerUsername = ownerUsername;
        this.balance = balance;
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }
        this.balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        }
        if (amount > this.balance) {
            throw new IllegalArgumentException("Insufficient funds for this withdrawal.");
        }
        this.balance -= amount;
    }

    public static Account fromFileLine(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 4) {
            return null;
        }

        Account account = new Account(parts[0].trim(), parts[1].trim(), Double.parseDouble(parts[2].trim()));
        account.createdAt = parts[3].trim();
        return account;
    }

    public String toFileLine() {
        return accountNumber + "|" + ownerUsername + "|" + balance + "|" + createdAt;
    }
}
