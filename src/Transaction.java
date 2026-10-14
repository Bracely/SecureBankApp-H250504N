public class Transaction {
    private static int counter = 1;

    private int transactionId;
    private String accountNumber;
    private String type;
    private double amount;
    private String timestamp;
    private String description;

    public Transaction(String accountNumber, String type, double amount, String timestamp, String description) {
        this.transactionId = counter++;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
        this.description = description;
    }

    public static void resetCounter() {
        counter = 1;
    }

    public static void setCounter(int nextId) {
        counter = nextId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    public static Transaction fromFileLine(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 6) {
            return null;
        }

        Transaction transaction = new Transaction(parts[1].trim(), parts[2].trim(), Double.parseDouble(parts[3].trim()), parts[4].trim(), parts[5].trim());
        transaction.transactionId = Integer.parseInt(parts[0].trim());
        return transaction;
    }

    public String toFileLine() {
        return transactionId + "|" + accountNumber + "|" + type + "|" + amount + "|" + timestamp + "|" + description;
    }
}
