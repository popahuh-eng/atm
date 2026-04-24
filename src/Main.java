import exception.AtmException;
import io.FileStorage;
import model.Transaction;
import model.User;
import service.AtmService;

import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final String DATA_FILE = "data.txt";
    private static FileStorage fileStorage;
    private static AtmService atmService;
    private static Scanner scanner;

    public static void main(String[] args) {
        fileStorage = new FileStorage(DATA_FILE);
        Map<String, User> users = fileStorage.load();
        atmService = new AtmService(users);
        scanner = new Scanner(System.in);

        System.out.println("Welcome to the Console ATM Simulator");

        while (true) {
            try {
                if (atmService.getCurrentUser() == null) {
                    showGuestMenu();
                } else {
                    showUserMenu();
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                scanner.nextLine(); // clear buffer
            }
        }
    }

    private static void showGuestMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");

        String choice = scanner.nextLine();

        try {
            switch (choice) {
                case "1":
                    login();
                    break;
                case "2":
                    register();
                    break;
                case "3":
                    exitApp();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } catch (AtmException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Input Error: " + e.getMessage());
        }
    }

    private static void showUserMenu() {
        System.out.println("\n--- USER MENU (" + atmService.getCurrentUser().getCardNumber() + ") ---");
        System.out.println("1. Check Balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Transaction History");
        System.out.println("6. Logout");
        System.out.print("Choose an option: ");

        String choice = scanner.nextLine();

        try {
            switch (choice) {
                case "1":
                    System.out.printf("Current Balance: %.2f\n", atmService.getBalance());
                    break;
                case "2":
                    System.out.print("Enter deposit amount: ");
                    double depAmount = Double.parseDouble(scanner.nextLine());
                    atmService.deposit(depAmount);
                    System.out.println("Deposit successful.");
                    fileStorage.save(atmService.getAllUsers().stream().collect(
                        java.util.stream.Collectors.toMap(User::getCardNumber, u -> u)
                    ));
                    break;
                case "3":
                    System.out.print("Enter withdrawal amount: ");
                    double withAmount = Double.parseDouble(scanner.nextLine());
                    atmService.withdraw(withAmount);
                    System.out.println("Withdrawal successful.");
                    fileStorage.save(atmService.getAllUsers().stream().collect(
                        java.util.stream.Collectors.toMap(User::getCardNumber, u -> u)
                    ));
                    break;
                case "4":
                    System.out.print("Enter target card number: ");
                    String targetCard = scanner.nextLine();
                    System.out.print("Enter transfer amount: ");
                    double transAmount = Double.parseDouble(scanner.nextLine());
                    atmService.transfer(targetCard, transAmount);
                    System.out.println("Transfer successful.");
                    fileStorage.save(atmService.getAllUsers().stream().collect(
                        java.util.stream.Collectors.toMap(User::getCardNumber, u -> u)
                    ));
                    break;
                case "5":
                    System.out.println("--- Transaction History ---");
                    for (Transaction tx : atmService.getCurrentUser().getAccount().getTransactions()) {
                        System.out.println(tx);
                    }
                    break;
                case "6":
                    atmService.logout();
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } catch (AtmException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Input Error: Please enter a valid number.");
        } catch (IllegalArgumentException e) {
            System.out.println("Input Error: " + e.getMessage());
        }
    }

    private static void login() {
        System.out.print("Enter card number: ");
        String cardNumber = scanner.nextLine();
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();

        if (atmService.login(cardNumber, pin)) {
            System.out.println("Login successful.");
            fileStorage.save(atmService.getAllUsers().stream().collect(
                java.util.stream.Collectors.toMap(User::getCardNumber, u -> u)
            ));
        }
    }

    private static void register() {
        System.out.print("Enter new card number: ");
        String cardNumber = scanner.nextLine();
        System.out.print("Enter new PIN: ");
        String pin = scanner.nextLine();

        atmService.registerUser(cardNumber, pin);
        System.out.println("Registration successful. You can now login.");
        
        // Save to file immediately
        fileStorage.save(atmService.getAllUsers().stream().collect(
            java.util.stream.Collectors.toMap(User::getCardNumber, u -> u)
        ));
    }

    private static void exitApp() {
        System.out.println("Saving data...");
        Map<String, User> allUsers = atmService.getAllUsers().stream().collect(
            java.util.stream.Collectors.toMap(User::getCardNumber, u -> u)
        );
        fileStorage.save(allUsers);
        System.out.println("Goodbye!");
        System.exit(0);
    }
}
