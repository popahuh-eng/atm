import exception.AtmException;
import io.FileStorage;
import model.Language;
import model.Transaction;
import model.User;
import service.AtmService;
import util.ConsoleColors;
import util.Localization;

import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final String DATA_FILE = "data.txt";
    private static FileStorage fileStorage;
    private static AtmService atmService;
    private static Scanner scanner;

    public static void main(String[] args) {
        // Set console to UTF-8 to support Kazakh/Russian characters
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
        
        scanner = new Scanner(System.in, java.nio.charset.StandardCharsets.UTF_8);
        
        // Language Selection
        System.out.println(ConsoleColors.CYAN_BOLD + "Select Language / Выберите язык / Тілді таңдаңыз:" + ConsoleColors.RESET);
        System.out.println("1. Русский (RU)");
        System.out.println("2. Қазақша (KZ)");
        System.out.println("3. English (EN)");
        System.out.print("> ");
        String langChoice = scanner.nextLine();
        
        if ("1".equals(langChoice)) {
            Localization.setLanguage(Language.RU);
        } else if ("2".equals(langChoice)) {
            Localization.setLanguage(Language.KZ);
        } else {
            Localization.setLanguage(Language.EN);
        }

        fileStorage = new FileStorage(DATA_FILE);
        Map<String, User> users = fileStorage.load();
        atmService = new AtmService(users);

        System.out.println(ConsoleColors.YELLOW_BOLD + Localization.get("welcome") + ConsoleColors.RESET);

        while (true) {
            try {
                if (atmService.getCurrentUser() == null) {
                    showGuestMenu();
                } else {
                    showUserMenu();
                }
            } catch (Exception e) {
                System.out.println(ConsoleColors.RED_BOLD + Localization.get("unexpected_error") + e.getMessage() + ConsoleColors.RESET);
                scanner.nextLine(); // clear buffer
            }
        }
    }

    private static void showGuestMenu() {
        System.out.println(ConsoleColors.CYAN + "\n" + Localization.get("menu_guest") + ConsoleColors.RESET);
        System.out.println(Localization.get("menu_guest_login"));
        System.out.println(Localization.get("menu_guest_register"));
        System.out.println(Localization.get("menu_guest_exit"));
        System.out.print(ConsoleColors.YELLOW + Localization.get("choose_option") + ConsoleColors.RESET);

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
                    System.out.println(ConsoleColors.RED + Localization.get("invalid_option") + ConsoleColors.RESET);
            }
        } catch (AtmException e) {
            System.out.println(ConsoleColors.RED_BOLD + Localization.get("err") + Localization.get(e.getMessage()) + ConsoleColors.RESET);
        } catch (IllegalArgumentException e) {
            System.out.println(ConsoleColors.RED + Localization.get("input_err") + Localization.get(e.getMessage()) + ConsoleColors.RESET);
        }
    }

    private static void showUserMenu() {
        String menuTitle = String.format(Localization.get("menu_user"), atmService.getCurrentUser().getCardNumber());
        System.out.println(ConsoleColors.CYAN + "\n" + menuTitle + ConsoleColors.RESET);
        System.out.println(Localization.get("menu_user_balance"));
        System.out.println(Localization.get("menu_user_deposit"));
        System.out.println(Localization.get("menu_user_withdraw"));
        System.out.println(Localization.get("menu_user_transfer"));
        System.out.println(Localization.get("menu_user_history"));
        System.out.println(Localization.get("menu_user_logout"));
        System.out.print(ConsoleColors.YELLOW + Localization.get("choose_option") + ConsoleColors.RESET);

        String choice = scanner.nextLine();

        try {
            switch (choice) {
                case "1":
                    String balanceStr = String.format(Localization.get("balance_msg"), atmService.getBalance());
                    System.out.println(ConsoleColors.GREEN_BOLD + balanceStr + ConsoleColors.RESET);
                    break;
                case "2":
                    System.out.print(Localization.get("enter_deposit"));
                    double depAmount = Double.parseDouble(scanner.nextLine());
                    atmService.deposit(depAmount);
                    System.out.println(ConsoleColors.GREEN + Localization.get("deposit_success") + ConsoleColors.RESET);
                    saveData();
                    break;
                case "3":
                    System.out.print(Localization.get("enter_withdraw"));
                    double withAmount = Double.parseDouble(scanner.nextLine());
                    atmService.withdraw(withAmount);
                    System.out.println(ConsoleColors.GREEN + Localization.get("withdraw_success") + ConsoleColors.RESET);
                    saveData();
                    break;
                case "4":
                    System.out.print(Localization.get("enter_target_card"));
                    String targetCard = scanner.nextLine();
                    System.out.print(Localization.get("enter_transfer_amount"));
                    double transAmount = Double.parseDouble(scanner.nextLine());
                    atmService.transfer(targetCard, transAmount);
                    System.out.println(ConsoleColors.GREEN + Localization.get("transfer_success") + ConsoleColors.RESET);
                    saveData();
                    break;
                case "5":
                    System.out.println(ConsoleColors.BLUE_BOLD + Localization.get("tx_history") + ConsoleColors.RESET);
                    for (Transaction tx : atmService.getCurrentUser().getAccount().getTransactions()) {
                        System.out.println(ConsoleColors.BLUE + tx.toString() + ConsoleColors.RESET);
                    }
                    break;
                case "6":
                    atmService.logout();
                    System.out.println(ConsoleColors.GREEN + Localization.get("logout_success") + ConsoleColors.RESET);
                    break;
                default:
                    System.out.println(ConsoleColors.RED + Localization.get("invalid_option") + ConsoleColors.RESET);
            }
        } catch (AtmException e) {
            String errorMsg = e.getMessage();
            // Handle parameterized exception strings (e.g. err_wrong_pin_attempts:2)
            if (errorMsg.contains(":")) {
                String[] parts = errorMsg.split(":");
                System.out.println(ConsoleColors.RED_BOLD + Localization.get("err") + String.format(Localization.get(parts[0]), Integer.parseInt(parts[1])) + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED_BOLD + Localization.get("err") + Localization.get(e.getMessage()) + ConsoleColors.RESET);
            }
        } catch (NumberFormatException e) {
            System.out.println(ConsoleColors.RED + Localization.get("input_err") + Localization.get("enter_valid_number") + ConsoleColors.RESET);
        } catch (IllegalArgumentException e) {
            System.out.println(ConsoleColors.RED + Localization.get("input_err") + Localization.get(e.getMessage()) + ConsoleColors.RESET);
        }
    }

    private static void login() {
        System.out.print(Localization.get("enter_card"));
        String cardNumber = scanner.nextLine();
        System.out.print(Localization.get("enter_pin"));
        String pin = scanner.nextLine();

        if (atmService.login(cardNumber, pin)) {
            System.out.println(ConsoleColors.GREEN_BOLD + Localization.get("login_success") + ConsoleColors.RESET);
            saveData();
        }
    }

    private static void register() {
        System.out.print(Localization.get("enter_new_card"));
        String cardNumber = scanner.nextLine();
        System.out.print(Localization.get("enter_new_pin"));
        String pin = scanner.nextLine();

        atmService.registerUser(cardNumber, pin);
        System.out.println(ConsoleColors.GREEN_BOLD + Localization.get("register_success") + ConsoleColors.RESET);
        saveData();
    }

    private static void saveData() {
        fileStorage.save(atmService.getAllUsers().stream().collect(
            java.util.stream.Collectors.toMap(User::getCardNumber, u -> u)
        ));
    }

    private static void exitApp() {
        System.out.println(ConsoleColors.YELLOW + Localization.get("saving_data") + ConsoleColors.RESET);
        saveData();
        System.out.println(ConsoleColors.GREEN_BOLD + Localization.get("goodbye") + ConsoleColors.RESET);
        System.exit(0);
    }
}
