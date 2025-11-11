package com.banking.model;

import java.time.LocalDateTime;

// This class records details of every action (Deposit, Withdraw, Transfer)
public class Transaction {
    private String transactionId;
    private LocalDateTime timestamp;
    private String type; // "DEPOSIT", "WITHDRAW", "TRANSFER"
    private double amount;
    private String fromAccountId;
    private String toAccountId; // Can be null if it's just a deposit/withdraw

    // Constructor for transfers
    public Transaction(String transactionId, String type, double amount, String fromAccountId, String toAccountId) {
        this.transactionId = transactionId;
        this.timestamp = LocalDateTime.now(); // Auto-set the current time
        this.type = type;
        this.amount = amount;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
    }

    // Getters
    public String getTransactionId() { return transactionId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getFromAccountId() { return fromAccountId; }
    public String getToAccountId() { return toAccountId; }

    @Override
    public String toString() {
        return "Transaction [ID=" + transactionId + ", Type=" + type + ", Amount=" + amount + "]";
    }
}