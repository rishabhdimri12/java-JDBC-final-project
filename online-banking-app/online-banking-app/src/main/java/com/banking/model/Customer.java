package com.banking.model;

import java.util.ArrayList;
import java.util.List;

// This class represents a bank customer.
// It holds their personal info, login credentials, and a list of their accounts.
public class Customer {
    private String customerId;
    private String name;
    private String username;
    private String password; // In a real app, this should be hashed!
    private String email;
    private String phoneNumber;
    private List<Account> accounts;

    // Constructor
    public Customer(String customerId, String name, String username, String password, String email, String phoneNumber) {
        this.customerId = customerId;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.accounts = new ArrayList<>();
    }

    // Getters and Setters (Required for accessing private data)
    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public List<Account> getAccounts() { return accounts; }

    // Helper method to add an account to this customer
    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    // Helper to find a specific account by its number
    public Account getAccount(String accountNumber) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(accountNumber)) {
                return acc;
            }
        }
        return null; // Account not found for this customer
    }

    @Override
    public String toString() {
        return "Customer [id=" + customerId + ", name=" + name + ", username=" + username + "]";
    }
}
