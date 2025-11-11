package com.banking.service;

import com.banking.dao.XmlDataStorage;
import com.banking.model.Customer;

public class AuthenticationService {

    // The main login method
    public static Customer login(String username, String password) {
        // 1. Try to find the customer in our XML data
        Customer customer = XmlDataStorage.getCustomerByUsername(username);

        // 2. If found, check if the password matches
        if (customer != null && customer.getPassword().equals(password)) {
            System.out.println("Login Successful! Welcome, " + customer.getName());
            return customer;
        } else {
            System.out.println("Login Failed: Invalid username or password.");
            return null;
        }
    }
}