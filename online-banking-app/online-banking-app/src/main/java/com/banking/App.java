package com.banking;

import com.banking.dao.XmlDataStorage;
import com.banking.model.Customer;
import com.banking.model.Account;
import com.banking.service.AuthenticationService;
import com.banking.service.TransactionService;
import java.util.Scanner;

public class App {
    private static Scanner scanner = new Scanner(System.in);
    private static Customer loggedInUser = null;

    public static void main(String[] args) {
        XmlDataStorage.initializeFile();
        // Ensure our test user exists for the demo
        createDemoUserIfNotExists();

        while (true) {
            if (loggedInUser == null) {
                showMainMenu();
            } else {
                showCustomerMenu();
            }
        }
    }

    // --- MENUS ---

    private static void showMainMenu() {
        System.out.println("\n=== ONLINE BANKING SYSTEM ===");
        System.out.println("1. Login");
        System.out.println("2. Exit");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                performLogin();
                break;
            case "2":
                System.out.println("Thank you for using our bank. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Try again.");
        }
    }

    private static void showCustomerMenu() {
        System.out.println("\n--- WELCOME, " + loggedInUser.getName().toUpperCase() + " ---");
        System.out.println("1. Check Balance");
        System.out.println("2. Transfer Funds");
        System.out.println("3. Logout");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                showBalance();
                break;
            case "2":
                performTransfer();
                break;
            case "3":
                System.out.println("Logging out...");
                loggedInUser = null;
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    // --- ACTIONS ---

    private static void performLogin() {
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        loggedInUser = AuthenticationService.login(username, password);
        if (loggedInUser == null) {
             // Error message is already printed by the service
        }
    }

    private static void showBalance() {
        System.out.println("\n--- YOUR ACCOUNTS ---");
        for (Account acc : loggedInUser.getAccounts()) {
            System.out.println("Account: " + acc.getAccountNumber() + " | Type: " + acc.getAccountType() + " | Balance: $" + acc.getBalance());
        }
    }

    private static void performTransfer() {
        if (loggedInUser.getAccounts().isEmpty()) {
            System.out.println("You have no accounts to transfer from.");
            return;
        }
        // For simplicity, always use their first account as source
        String fromAcc = loggedInUser.getAccounts().get(0).getAccountNumber();

        System.out.println("\n--- TRANSFER FUNDS ---");
        System.out.println("Sending from your default account: " + fromAcc);
        
        System.out.print("Enter Destination Account Number: ");
        String toAcc = scanner.nextLine();

        System.out.print("Enter Amount to Transfer: $");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            boolean success = TransactionService.transferFunds(loggedInUser, fromAcc, toAcc, amount);
            if (success) {
                // Re-login silently to refresh balance from XML
                loggedInUser = AuthenticationService.login(loggedInUser.getUsername(), loggedInUser.getPassword());
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered.");
        }
    }

    // --- SETUP DEMO DATA ---
    private static void createDemoUserIfNotExists() {
        if (AuthenticationService.login("akshit", "pass123") == null) {
             Customer newUser = new Customer("C101", "Akshit Bisht", "akshit", "pass123", "ak@email.com", "9999999999");
             newUser.addAccount(new Account("ACC-1001", "SAVINGS", 5000.00, "C101"));
             XmlDataStorage.saveCustomer(newUser);
             System.out.println("(Demo user 'akshit' / 'pass123' created)");
        }
    }
}