package util;

import model.Language;
import java.util.HashMap;
import java.util.Map;

public class Localization {
    private static Language currentLanguage = Language.EN;
    private static final Map<Language, Map<String, String>> dictionary = new HashMap<>();

    static {
        // EN
        Map<String, String> en = new HashMap<>();
        en.put("welcome", "Welcome to the Console ATM Simulator");
        en.put("menu_guest", "--- MAIN MENU ---");
        en.put("menu_guest_login", "1. Login");
        en.put("menu_guest_register", "2. Register");
        en.put("menu_guest_exit", "3. Exit");
        en.put("choose_option", "Choose an option: ");
        en.put("invalid_option", "Invalid option. Please try again.");
        en.put("unexpected_error", "An unexpected error occurred: ");
        en.put("err", "Error: ");
        en.put("input_err", "Input Error: ");
        en.put("enter_valid_number", "Please enter a valid number.");
        
        en.put("menu_user", "--- USER MENU (%s) ---");
        en.put("menu_user_balance", "1. Check Balance");
        en.put("menu_user_deposit", "2. Deposit");
        en.put("menu_user_withdraw", "3. Withdraw");
        en.put("menu_user_transfer", "4. Transfer");
        en.put("menu_user_history", "5. Transaction History");
        en.put("menu_user_logout", "6. Logout");
        
        en.put("balance_msg", "Current Balance: %.2f");
        en.put("enter_deposit", "Enter deposit amount: ");
        en.put("deposit_success", "Deposit successful.");
        en.put("enter_withdraw", "Enter withdrawal amount: ");
        en.put("withdraw_success", "Withdrawal successful.");
        en.put("enter_target_card", "Enter target card number: ");
        en.put("enter_transfer_amount", "Enter transfer amount: ");
        en.put("transfer_success", "Transfer successful.");
        en.put("tx_history", "--- Transaction History ---");
        en.put("logout_success", "Logged out successfully.");
        
        en.put("enter_card", "Enter card number: ");
        en.put("enter_pin", "Enter PIN: ");
        en.put("login_success", "Login successful.");
        
        en.put("enter_new_card", "Enter new card number: ");
        en.put("enter_new_pin", "Enter new PIN: ");
        en.put("register_success", "Registration successful. You can now login.");
        
        en.put("saving_data", "Saving data...");
        en.put("goodbye", "Goodbye!");

        // Error keys from AtmService
        en.put("err_user_exists", "User with this card number already exists.");
        en.put("err_card_not_found", "Card not found.");
        en.put("err_blocked", "Account is blocked due to too many failed attempts.");
        en.put("err_wrong_pin_blocked", "Wrong PIN. Account has been blocked.");
        en.put("err_wrong_pin_attempts", "Wrong PIN. Attempts left: %d");
        en.put("err_amount_positive", "Amount must be positive.");
        en.put("err_insufficient_funds", "Not enough funds.");
        en.put("err_transfer_self", "Cannot transfer to yourself.");
        en.put("err_target_not_found", "Target card not found.");
        en.put("err_not_auth", "Not authenticated.");
        
        // RU
        Map<String, String> ru = new HashMap<>();
        ru.put("welcome", "Добро пожаловать в симулятор банкомата");
        ru.put("menu_guest", "--- ГЛАВНОЕ МЕНЮ ---");
        ru.put("menu_guest_login", "1. Войти");
        ru.put("menu_guest_register", "2. Регистрация");
        ru.put("menu_guest_exit", "3. Выход");
        ru.put("choose_option", "Выберите опцию: ");
        ru.put("invalid_option", "Неверный выбор. Попробуйте еще раз.");
        ru.put("unexpected_error", "Произошла непредвиденная ошибка: ");
        ru.put("err", "Ошибка: ");
        ru.put("input_err", "Ошибка ввода: ");
        ru.put("enter_valid_number", "Пожалуйста, введите корректное число.");
        
        ru.put("menu_user", "--- МЕНЮ ПОЛЬЗОВАТЕЛЯ (%s) ---");
        ru.put("menu_user_balance", "1. Проверить баланс");
        ru.put("menu_user_deposit", "2. Пополнить счет");
        ru.put("menu_user_withdraw", "3. Снять наличные");
        ru.put("menu_user_transfer", "4. Перевод");
        ru.put("menu_user_history", "5. История транзакций");
        ru.put("menu_user_logout", "6. Выйти");
        
        ru.put("balance_msg", "Текущий баланс: %.2f");
        ru.put("enter_deposit", "Введите сумму пополнения: ");
        ru.put("deposit_success", "Счет успешно пополнен.");
        ru.put("enter_withdraw", "Введите сумму снятия: ");
        ru.put("withdraw_success", "Наличные успешно сняты.");
        ru.put("enter_target_card", "Введите номер карты получателя: ");
        ru.put("enter_transfer_amount", "Введите сумму перевода: ");
        ru.put("transfer_success", "Перевод успешно выполнен.");
        ru.put("tx_history", "--- История транзакций ---");
        ru.put("logout_success", "Вы успешно вышли из системы.");
        
        ru.put("enter_card", "Введите номер карты: ");
        ru.put("enter_pin", "Введите PIN-код: ");
        ru.put("login_success", "Успешный вход.");
        
        ru.put("enter_new_card", "Введите новый номер карты: ");
        ru.put("enter_new_pin", "Введите новый PIN-код: ");
        ru.put("register_success", "Регистрация успешна. Теперь вы можете войти.");
        
        ru.put("saving_data", "Сохранение данных...");
        ru.put("goodbye", "До свидания!");

        ru.put("err_user_exists", "Пользователь с такой картой уже существует.");
        ru.put("err_card_not_found", "Карта не найдена.");
        ru.put("err_blocked", "Аккаунт заблокирован из-за слишком большого числа неудачных попыток.");
        ru.put("err_wrong_pin_blocked", "Неверный PIN-код. Аккаунт заблокирован.");
        ru.put("err_wrong_pin_attempts", "Неверный PIN-код. Осталось попыток: %d");
        ru.put("err_amount_positive", "Сумма должна быть положительной.");
        ru.put("err_insufficient_funds", "Недостаточно средств.");
        ru.put("err_transfer_self", "Нельзя перевести средства самому себе.");
        ru.put("err_target_not_found", "Карта получателя не найдена.");
        ru.put("err_not_auth", "Вы не авторизованы.");

        // KZ
        Map<String, String> kz = new HashMap<>();
        kz.put("welcome", "Банкомат симуляторына қош келдіңіз");
        kz.put("menu_guest", "--- БАСТЫ МӘЗІР ---");
        kz.put("menu_guest_login", "1. Кіру");
        kz.put("menu_guest_register", "2. Тіркелу");
        kz.put("menu_guest_exit", "3. Шығу");
        kz.put("choose_option", "Нұсқаны таңдаңыз: ");
        kz.put("invalid_option", "Қате таңдау. Қайталап көріңіз.");
        kz.put("unexpected_error", "Күтпеген қате пайда болды: ");
        kz.put("err", "Қате: ");
        kz.put("input_err", "Енгізу қатесі: ");
        kz.put("enter_valid_number", "Дұрыс сан енгізіңіз.");
        
        kz.put("menu_user", "--- ПАЙДАЛАНУШЫ МӘЗІРІ (%s) ---");
        kz.put("menu_user_balance", "1. Теңгерімді тексеру");
        kz.put("menu_user_deposit", "2. Шотты толтыру");
        kz.put("menu_user_withdraw", "3. Қолма-қол ақшаны алу");
        kz.put("menu_user_transfer", "4. Аударым");
        kz.put("menu_user_history", "5. Транзакциялар тарихы");
        kz.put("menu_user_logout", "6. Шығу");
        
        kz.put("balance_msg", "Ағымдағы теңгерім: %.2f");
        kz.put("enter_deposit", "Толтыру сомасын енгізіңіз: ");
        kz.put("deposit_success", "Шот сәтті толтырылды.");
        kz.put("enter_withdraw", "Алу сомасын енгізіңіз: ");
        kz.put("withdraw_success", "Қолма-қол ақша сәтті алынды.");
        kz.put("enter_target_card", "Алушының карта нөмірін енгізіңіз: ");
        kz.put("enter_transfer_amount", "Аударым сомасын енгізіңіз: ");
        kz.put("transfer_success", "Аударым сәтті орындалды.");
        kz.put("tx_history", "--- Транзакциялар тарихы ---");
        kz.put("logout_success", "Жүйеден сәтті шықтыңыз.");
        
        kz.put("enter_card", "Карта нөмірін енгізіңіз: ");
        kz.put("enter_pin", "PIN-кодты енгізіңіз: ");
        kz.put("login_success", "Сәтті кіру.");
        
        kz.put("enter_new_card", "Жаңа карта нөмірін енгізіңіз: ");
        kz.put("enter_new_pin", "Жаңа PIN-кодты енгізіңіз: ");
        kz.put("register_success", "Тіркелу сәтті аяқталды. Енді жүйеге кіре аласыз.");
        
        kz.put("saving_data", "Деректерді сақтау...");
        kz.put("goodbye", "Сау болыңыз!");

        kz.put("err_user_exists", "Мұндай картасы бар пайдаланушы тіркелген.");
        kz.put("err_card_not_found", "Карта табылмады.");
        kz.put("err_blocked", "Тым көп сәтсіз әрекеттерге байланысты аккаунт бұғатталды.");
        kz.put("err_wrong_pin_blocked", "Қате PIN-код. Аккаунт бұғатталды.");
        kz.put("err_wrong_pin_attempts", "Қате PIN-код. Қалған әрекеттер саны: %d");
        kz.put("err_amount_positive", "Сома оң болуы керек.");
        kz.put("err_insufficient_funds", "Қаражат жеткіліксіз.");
        kz.put("err_transfer_self", "Өзіңізге қаражат аудара алмайсыз.");
        kz.put("err_target_not_found", "Алушының картасы табылмады.");
        kz.put("err_not_auth", "Сіз авторизациядан өтпедіңіз.");

        dictionary.put(Language.EN, en);
        dictionary.put(Language.RU, ru);
        dictionary.put(Language.KZ, kz);
    }

    public static void setLanguage(Language language) {
        currentLanguage = language;
    }

    public static String get(String key) {
        Map<String, String> map = dictionary.get(currentLanguage);
        if (map != null && map.containsKey(key)) {
            return map.get(key);
        }
        return key; // return key if translation missing
    }
}
