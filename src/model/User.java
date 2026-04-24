package model;

public class User {
    private String cardNumber;
    private String pin;
    private int failedLoginAttempts;
    private boolean isBlocked;
    private Account account;

    public User(String cardNumber, String pin) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.failedLoginAttempts = 0;
        this.isBlocked = false;
        this.account = new Account(0.0);
    }

    public String getCardNumber() { return cardNumber; }
    public String getPin() { return pin; }
    
    public int getFailedLoginAttempts() { return failedLoginAttempts; }
    public void incrementFailedAttempts() { this.failedLoginAttempts++; }
    public void resetFailedAttempts() { this.failedLoginAttempts = 0; }
    public void setFailedLoginAttempts(int attempts) { this.failedLoginAttempts = attempts; }

    public boolean isBlocked() { return isBlocked; }
    public void setBlocked(boolean blocked) { isBlocked = blocked; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
}
