import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BankingSystem bankingSystem = new BankingSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Secure Banking Application ===");
            if (bankingSystem.isLoggedIn()) {
                showLoggedInMenu(bankingSystem, scanner);
            } else {
                showLoggedOutMenu(bankingSystem, scanner);
            }
        }
    }

    private static void showLoggedOutMenu(BankingSystem bankingSystem, Scanner scanner) {
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");

        String option = scanner.nextLine().trim();

        switch (option) {
            case "1":
                System.out.print("Enter username: ");
                String username = scanner.nextLine().trim();
                System.out.print("Enter password: ");
                String password = scanner.nextLine().trim();
                System.out.print("Enter full name: ");
                String fullName = scanner.nextLine().trim();
                bankingSystem.registerUser(username, password, fullName);
                break;
            case "2":
                System.out.print("Enter username: ");
                String loginUser = scanner.nextLine().trim();
                System.out.print("Enter password: ");
                String loginPassword = scanner.nextLine().trim();
                bankingSystem.login(loginUser, loginPassword);
                break;
            case "3":
                System.out.println("Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Try again.");
        }
    }

    private static void showLoggedInMenu(BankingSystem bankingSystem, Scanner scanner) {
        System.out.println("1. Create Account");
        System.out.println("2. View Accounts");
        System.out.println("3. Deposit");
        System.out.println("4. Withdraw");
        System.out.println("5. View Balance");
        System.out.println("6. View Transactions");
        System.out.println("7. Logout");
        System.out.print("Choose an option: ");

        String option = scanner.nextLine().trim();

        switch (option) {
            case "1":
                bankingSystem.createAccount("primary");
                break;
            case "2":
                List<Account> accounts = bankingSystem.getCurrentUserAccounts();
                if (accounts.isEmpty()) {
                    System.out.println("You have no accounts yet.");
                } else {
                    for (Account account : accounts) {
                        System.out.println("Account: " + account.getAccountNumber() + " | Balance: $" + String.format("%.2f", account.getBalance()));
                    }
                }
                break;
            case "3":
                System.out.print("Enter account number: ");
                String depositAccountNumber = scanner.nextLine().trim();
                System.out.print("Enter deposit amount: ");
                try {
                    double amount = Double.parseDouble(scanner.nextLine().trim());
                    bankingSystem.deposit(depositAccountNumber, amount);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid amount entered.");
                }
                break;
            case "4":
                System.out.print("Enter account number: ");
                String withdrawalAccountNumber = scanner.nextLine().trim();
                System.out.print("Enter withdrawal amount: ");
                try {
                    double withdrawalAmount = Double.parseDouble(scanner.nextLine().trim());
                    bankingSystem.withdraw(withdrawalAccountNumber, withdrawalAmount);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid amount entered.");
                }
                break;
            case "5":
                System.out.print("Enter account number: ");
                String balanceAccountNumber = scanner.nextLine().trim();
                bankingSystem.viewBalance(balanceAccountNumber);
                break;
            case "6":
                System.out.print("Enter account number: ");
                String transactionAccountNumber = scanner.nextLine().trim();
                bankingSystem.displayTransactions(transactionAccountNumber);
                break;
            case "7":
                bankingSystem.logout();
                break;
            default:
                System.out.println("Invalid option. Try again.");
        }
    }
}
