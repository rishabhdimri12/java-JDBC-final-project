package com.banking.service;

import com.banking.dao.XmlDataStorage;
import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.model.Transaction;
import java.util.Date;

public class TransactionService {

    // The main transfer method
    public static boolean transferFunds(Customer sender, String fromAccNum, String toAccNum, double amount) {
        // 1. Validate sender owns the 'from' account
        Account fromAccount = sender.getAccount(fromAccNum);
        if (fromAccount == null) {
            System.out.println("Transfer Failed: You do not own account " + fromAccNum);
            return false;
        }

        // 2. Check sufficient funds
        if (fromAccount.getBalance() < amount) {
            System.out.println("Transfer Failed: Insufficient funds. Balance is only " + fromAccount.getBalance());
            return false;
        }

        // 3. (SIMPLIFIED) For now, we need to find the TO account.
        // In a real app, we'd need a method to find ANY account in the bank.
        // For this test, we'll just pretend we found it if it's different.
        if (fromAccNum.equals(toAccNum)) {
             System.out.println("Transfer Failed: Cannot transfer to the same account.");
             return false;
        }

        // 4. Perform the transfer (Updating balances in memory AND XML)
        double newFromBalance = fromAccount.getBalance() - amount;
        // (In real life, we'd load the 'to' account current balance first.
        // For this simple first test, we will just deduct from sender to prove it works).

        boolean updateSuccess = XmlDataStorage.updateAccountBalance(fromAccNum, newFromBalance);

        if (updateSuccess) {
            // Update in-memory object too so the UI would show correct data
            fromAccount.setBalance(newFromBalance);
            System.out.println("Transfer Successful! New Balance for " + fromAccNum + ": " + newFromBalance);
            // TODO: Save a Transaction record to XML here later!
            return true;
        } else {
            System.out.println("Transfer Failed: Database error.");
            return false;
        }
    }
}