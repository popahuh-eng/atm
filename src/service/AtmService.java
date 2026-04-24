package service;

import exception.AuthException;
import exception.InsufficientFundsException;
import model.Account;
import model.Transaction;
import model.TransactionType;
import model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class AtmService {
    private final Map<String, User> userDatabase;
    private User currentUser;

    public AtmService(Map<String, User> userDatabase) {
        this.userDatabase = userDatabase;
    }

    public Collection<User> getAllUsers() {
        return userDatabase.values();
    }

    public void registerUser(String cardNumber, String pin) {
        if (userDatabase.containsKey(cardNumber)) {
            throw new IllegalArgumentException("User with this card number already exists.");
        }
        User newUser = new User(cardNumber, pin);
        userDatabase.put(cardNumber, newUser);
    }

    public boolean login(String cardNumber, String pin) {
        User user = userDatabase.get(cardNumber);
        if (user == null) {
            throw new AuthException("Card not found.");
        }
        if (user.isBlocked()) {
            throw new AuthException("Account is blocked due to too many failed attempts.");
        }

        if (user.getPin().equals(pin)) {
            user.resetFailedAttempts();
            currentUser = user;
            return true;
        } else {
            user.incrementFailedAttempts();
            if (user.getFailedLoginAttempts() >= 3) {
                user.setBlocked(true);
                throw new AuthException("Wrong PIN. Account has been blocked.");
            }
            throw new AuthException("Wrong PIN. Attempts left: " + (3 - user.getFailedLoginAttempts()));
        }
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public double getBalance() {
        checkAuth();
        return currentUser.getAccount().getBalance();
    }

    public void deposit(double amount) {
        checkAuth();
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        Account account = currentUser.getAccount();
        account.setBalance(account.getBalance() + amount);
        
        Transaction tx = new Transaction(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            TransactionType.DEPOSIT,
            amount,
            "Cash deposit"
        );
        account.addTransaction(tx);
    }

    public void withdraw(double amount) {
        checkAuth();
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        Account account = currentUser.getAccount();
        if (account.getBalance() < amount) {
            throw new InsufficientFundsException("Not enough funds on balance.");
        }
        
        account.setBalance(account.getBalance() - amount);
        
        Transaction tx = new Transaction(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            TransactionType.WITHDRAWAL,
            amount,
            "Cash withdrawal"
        );
        account.addTransaction(tx);
    }

    public void transfer(String targetCardNumber, double amount) {
        checkAuth();
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        if (currentUser.getCardNumber().equals(targetCardNumber)) {
            throw new IllegalArgumentException("Cannot transfer to yourself.");
        }

        User targetUser = userDatabase.get(targetCardNumber);
        if (targetUser == null) {
            throw new IllegalArgumentException("Target card not found.");
        }

        Account sourceAccount = currentUser.getAccount();
        if (sourceAccount.getBalance() < amount) {
            throw new InsufficientFundsException("Not enough funds for transfer.");
        }

        Account targetAccount = targetUser.getAccount();

        // Perform transfer
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);

        // Record transactions
        Transaction txOut = new Transaction(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            TransactionType.TRANSFER,
            amount,
            "Transfer to " + targetCardNumber
        );
        sourceAccount.addTransaction(txOut);

        Transaction txIn = new Transaction(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            TransactionType.TRANSFER,
            amount,
            "Transfer from " + currentUser.getCardNumber()
        );
        targetAccount.addTransaction(txIn);
    }

    private void checkAuth() {
        if (currentUser == null) {
            throw new AuthException("Not authenticated.");
        }
    }
}
