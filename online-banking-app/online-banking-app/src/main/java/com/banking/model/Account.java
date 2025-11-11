package com.banking.model;

// This class represents a single bank account (Savings, Current, etc.)
public class Account {
    private String accountNumber;
    private String accountType; // e.g., "SAVINGS", "CURRENT"
    private double balance;
    private String customerId; // Links this account back to a specific customer

    public Account(String accountNumber, String accountType, double initialBalance, String customerId) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = initialBalance;
        this.customerId = customerId;
    }

    // Getters
    public String getAccountNumber() { return accountNumber; }
    public String getAccountType() { return accountType; }
    public double getBalance() { return balance; }
    public String getCustomerId() { return customerId; }

    // Setter for balance (needed for transfers)
    public void setBalance(double newBalance) {
        this.balance = newBalance;
    }

    // Methods to deposit and withdraw funds
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && this.balance >= amount) {
            this.balance -= amount;
            return true; // Withdrawal success
        }
        return false; // Insufficient funds
    }

    @Override
    public String toString() {
        return "Account [No=" + accountNumber + ", Type=" + accountType + ", Balance=" + balance + "]";
    }
}
