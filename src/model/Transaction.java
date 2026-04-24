package model;

import java.time.LocalDateTime;

public class Transaction {
    private String id;
    private LocalDateTime date;
    private TransactionType type;
    private double amount;
    private String description;

    public Transaction(String id, LocalDateTime date, TransactionType type, double amount, String description) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.description = description;
    }

    public String getId() { return id; }
    public LocalDateTime getDate() { return date; }
    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return String.format("[%s] %s: %.2f | %s", date, type, amount, description);
    }
}
