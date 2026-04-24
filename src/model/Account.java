package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Account {
    private double balance;
    private final List<Transaction> transactions;

    public Account(double initialBalance) {
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        // keep only the last 10 transactions as per requirements
        if (transactions.size() > 10) {
            transactions.remove(0);
        }
    }

    // For file loading
    public void addTransactionNoLimit(Transaction transaction) {
        transactions.add(transaction);
    }
}
