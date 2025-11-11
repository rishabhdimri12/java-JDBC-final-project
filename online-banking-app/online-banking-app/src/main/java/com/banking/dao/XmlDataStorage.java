package com.banking.dao;

import com.banking.model.Customer;
import com.banking.model.Account;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

// This class handles ALL reading and writing to XML files.
public class XmlDataStorage {

    private static final String FILE_PATH = "banking_data.xml";

    // INITIALIZE: Check if the XML file exists, if not, create a complete empty one.
    public static void initializeFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.newDocument();

                // Root element <bank>
                Element rootElement = doc.createElement("bank");
                doc.appendChild(rootElement);

                // Sub-root elements for organization
                Element customersElement = doc.createElement("customers");
                rootElement.appendChild(customersElement);

                // Write to file
                saveDocument(doc);
                System.out.println("Initialized new banking_data.xml file.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // SAVE CUSTOMER: Adds a new customer to the XML file
    public static void saveCustomer(Customer customer) {
        try {
            Document doc = loadDocument();
            Element root = doc.getDocumentElement();
            Element customersNode = (Element) root.getElementsByTagName("customers").item(0);

            // Create <customer> element
            Element customerElement = doc.createElement("customer");
            customerElement.setAttribute("id", customer.getCustomerId());

            // Add simple fields
            customerElement.appendChild(createElement(doc, "name", customer.getName()));
            customerElement.appendChild(createElement(doc, "username", customer.getUsername()));
            customerElement.appendChild(createElement(doc, "password", customer.getPassword()));

            // Add <accounts> wrapper
            Element accountsWrapper = doc.createElement("accounts");
            for (Account acc : customer.getAccounts()) {
                Element accountElement = doc.createElement("account");
                accountElement.setAttribute("number", acc.getAccountNumber());
                accountElement.appendChild(createElement(doc, "type", acc.getAccountType()));
                accountElement.appendChild(createElement(doc, "balance", String.valueOf(acc.getBalance())));
                accountsWrapper.appendChild(accountElement);
            }
            customerElement.appendChild(accountsWrapper);

            // Add new customer to the list
            customersNode.appendChild(customerElement);

            // Save changes
            saveDocument(doc);
            System.out.println("Customer saved to XML successfully: " + customer.getUsername());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- HELPER METHODS (Private) ---

    private static Document loadDocument() throws Exception {
        File file = new File(FILE_PATH);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return dBuilder.parse(file);
    }

    private static void saveDocument(Document doc) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        // This line makes the XML readable with new lines
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(FILE_PATH));
        transformer.transform(source, result);
    }

    private static Element createElement(Document doc, String tagName, String value) {
        Element node = doc.createElement(tagName);
        node.appendChild(doc.createTextNode(value));
        return node;
    }

    // --- NEW HELPER: Find customer by username for login ---
    // FIND CUSTOMER: Find customer by username for login
    public static Customer getCustomerByUsername(String username) {
        try {
            Document doc = loadDocument();
            NodeList customerNodes = doc.getElementsByTagName("customer");
            for (int i = 0; i < customerNodes.getLength(); i++) {
                Element customerElement = (Element) customerNodes.item(i);
                String storedUsername = customerElement.getElementsByTagName("username").item(0).getTextContent();

                if (storedUsername.equals(username)) {
                    // Found them! Now reconstruct the Customer object.
                    String id = customerElement.getAttribute("id");
                    String name = customerElement.getElementsByTagName("name").item(0).getTextContent();
                    String password = customerElement.getElementsByTagName("password").item(0).getTextContent();
                    Customer loadedCustomer = new Customer(id, name, storedUsername, password, "", "");

                    // --- LOAD ACCOUNTS CORRECTLY ---
                    // 1. Find the <accounts> wrapper first
                    NodeList accountsWrapperList = customerElement.getElementsByTagName("accounts");
                    if (accountsWrapperList.getLength() > 0) {
                        Element accountsWrapper = (Element) accountsWrapperList.item(0);
                        // 2. Now get all <account> tags INSIDE that wrapper
                        NodeList accountNodes = accountsWrapper.getElementsByTagName("account");

                        for (int j = 0; j < accountNodes.getLength(); j++) {
                            Element accElement = (Element) accountNodes.item(j);
                            String accNum = accElement.getAttribute("number");
                            String type = accElement.getElementsByTagName("type").item(0).getTextContent();
                            double balance = Double.parseDouble(accElement.getElementsByTagName("balance").item(0).getTextContent());
                            loadedCustomer.addAccount(new Account(accNum, type, balance, id));
                        }
                    }
                    // -------------------------------

                    return loadedCustomer;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Customer not found
    }

    // --- UPDATE BALANCE: Critical for money transfers ---
    public static boolean updateAccountBalance(String accountNumber, double newBalance) {
        try {
            Document doc = loadDocument();
            NodeList accountNodes = doc.getElementsByTagName("account");
            for (int i = 0; i < accountNodes.getLength(); i++) {
                Element accElement = (Element) accountNodes.item(i);
                if (accElement.getAttribute("number").equals(accountNumber)) {
                    // Found the account! Update the balance element.
                    accElement.getElementsByTagName("balance").item(0).setTextContent(String.valueOf(newBalance));
                    saveDocument(doc);
                    System.out.println("Balance updated successfully for account: " + accountNumber);
                    return true; // Update successful
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Account not found or error
    }
}
