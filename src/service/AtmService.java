package service;

import exception.AuthException;
import exception.InsufficientFundsException;
import model.Account;
import model.Transaction;
import model.TransactionType;
import model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class AtmService {
    private static final double DAILY_WITHDRAWAL_LIMIT = 300000.0;
    private static final double COMMISSION_THRESHOLD = 300000.0;
    private static final double COMMISSION_RATE = 0.01;

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
            throw new IllegalArgumentException("err_user_exists");
        }
        User newUser = new User(cardNumber, pin);
        userDatabase.put(cardNumber, newUser);
    }

    public boolean login(String cardNumber, String pin) {
        User user = userDatabase.get(cardNumber);
        if (user == null) {
            throw new AuthException("err_card_not_found");
        }
        if (user.isBlocked()) {
            throw new AuthException("err_blocked");
        }

        if (user.getPin().equals(pin)) {
            user.resetFailedAttempts();
            currentUser = user;
            return true;
        } else {
            user.incrementFailedAttempts();
            if (user.getFailedLoginAttempts() >= 3) {
                user.setBlocked(true);
                throw new AuthException("err_wrong_pin_blocked");
            }
            throw new AuthException(String.format("err_wrong_pin_attempts:%d", (3 - user.getFailedLoginAttempts())));
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
            throw new IllegalArgumentException("err_amount_positive");
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

    public double withdraw(double amount) {
        checkAuth();
        if (amount <= 0) {
            throw new IllegalArgumentException("err_amount_positive");
        }
        Account account = currentUser.getAccount();
        
        // Calculate daily withdrawn amount
        double dailyWithdrawn = account.getTransactions().stream()
            .filter(t -> t.getType() == TransactionType.WITHDRAWAL)
            .filter(t -> t.getDate().toLocalDate().equals(LocalDate.now()))
            .mapToDouble(Transaction::getAmount)
            .sum();

        if (dailyWithdrawn + amount > DAILY_WITHDRAWAL_LIMIT) {
            throw new IllegalArgumentException("err_daily_limit");
        }

        double commission = 0;
        if (amount > COMMISSION_THRESHOLD) {
            commission = amount * COMMISSION_RATE;
        }

        double totalDeduction = amount + commission;

        if (account.getBalance() < totalDeduction) {
            throw new InsufficientFundsException("err_insufficient_funds");
        }
        
        account.setBalance(account.getBalance() - totalDeduction);
        
        String desc = "Cash withdrawal";
        if (commission > 0) {
            desc += String.format(" (incl. %.2f commission)", commission);
        }

        Transaction tx = new Transaction(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            TransactionType.WITHDRAWAL,
            amount, // We record the base amount or total? Requirements often record base amount, or total. Let's record total deduction so balance history makes sense.
            desc
        );
        // Correcting: actually recording totalDeduction makes it align with balance change
        tx = new Transaction(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            TransactionType.WITHDRAWAL,
            totalDeduction,
            desc
        );
        account.addTransaction(tx);
        return commission;
    }

    public void transfer(String targetCardNumber, double amount) {
        checkAuth();
        if (amount <= 0) {
            throw new IllegalArgumentException("err_amount_positive");
        }
        if (currentUser.getCardNumber().equals(targetCardNumber)) {
            throw new IllegalArgumentException("err_transfer_self");
        }

        User targetUser = userDatabase.get(targetCardNumber);
        if (targetUser == null) {
            throw new IllegalArgumentException("err_target_not_found");
        }

        Account sourceAccount = currentUser.getAccount();
        if (sourceAccount.getBalance() < amount) {
            throw new InsufficientFundsException("err_insufficient_funds");
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
            throw new AuthException("err_not_auth");
        }
    }
}
