package io;

import model.Account;
import model.Transaction;
import model.TransactionType;
import model.User;

import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class FileStorage {
    private final String filePath;

    public FileStorage(String filePath) {
        this.filePath = filePath;
    }

    public void save(Map<String, User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (User user : users.values()) {
                // USER;cardNumber;pin;failedAttempts;isBlocked;balance
                String userLine = String.format("USER;%s;%s;%d;%b;%f",
                        user.getCardNumber(),
                        user.getPin(),
                        user.getFailedLoginAttempts(),
                        user.isBlocked(),
                        user.getAccount().getBalance());
                writer.write(userLine);
                writer.newLine();

                for (Transaction tx : user.getAccount().getTransactions()) {
                    // TX;id;date;type;amount;description
                    String txLine = String.format("TX;%s;%s;%s;%f;%s",
                            tx.getId(),
                            tx.getDate().toString(),
                            tx.getType().name(),
                            tx.getAmount(),
                            tx.getDescription());
                    writer.write(txLine);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving data to file: " + e.getMessage());
        }
    }

    public Map<String, User> load() {
        Map<String, User> users = new HashMap<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return users;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            User currentUser = null;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(";", -1);
                String type = parts[0];

                if ("USER".equals(type)) {
                    String cardNumber = parts[1];
                    String pin = parts[2];
                    int failedAttempts = Integer.parseInt(parts[3]);
                    boolean isBlocked = Boolean.parseBoolean(parts[4]);
                    double balance = Double.parseDouble(parts[5].replace(",", "."));

                    currentUser = new User(cardNumber, pin);
                    currentUser.setFailedLoginAttempts(failedAttempts);
                    currentUser.setBlocked(isBlocked);
                    currentUser.getAccount().setBalance(balance);

                    users.put(cardNumber, currentUser);
                } else if ("TX".equals(type) && currentUser != null) {
                    String id = parts[1];
                    LocalDateTime date = LocalDateTime.parse(parts[2]);
                    TransactionType txType = TransactionType.valueOf(parts[3]);
                    double amount = Double.parseDouble(parts[4].replace(",", "."));
                    String desc = parts[5];

                    Transaction tx = new Transaction(id, date, txType, amount, desc);
                    currentUser.getAccount().addTransactionNoLimit(tx);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading data from file: " + e.getMessage());
        }

        return users;
    }
}
