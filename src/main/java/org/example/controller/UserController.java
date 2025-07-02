package org.example.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.UserDTO;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


import java.util.List;
import java.util.Scanner;

//Общение с пользователем через консоль
@Controller
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);
    private final UserService userService;
    private final Scanner scanner;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        printMainMenu();
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                processMainChoice(choice);
            } catch (NumberFormatException e) {
                logger.warn("Некорректный ввод");
                System.out.println("Введите число от 0 до 4!");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\nВыберите действие:");
        System.out.println("1. Создать пользователя");
        System.out.println("2. Обновить пользователя");
        System.out.println("3. Просмотреть пользователей");
        System.out.println("4. Удалить пользователя");
        System.out.println("0. Выйти");
        System.out.print("Ваш выбор: ");
    }

    private void processMainChoice(int choice) {
        switch (choice) {
            case 0 -> exit();
            case 1 -> createUserConsole();
            case 2 -> updateUserConsole();
            case 3 -> readUsersConsole();
            case 4 -> deleteUserConsole();
            default -> System.out.println("Неверный выбор!");
        }
    }

    private void createUserConsole() {
        System.out.println("\nСоздание нового пользователя");

        UserDTO userDTO = new UserDTO();
        userDTO.setName(getValidatedName());
        userDTO.setEmail(getValidatedEmail());
        userDTO.setAge(getValidatedAge());

        UserDTO createdUser = userService.createUser(userDTO);
        System.out.println("Пользователь создан!");
    }

    private void updateUserConsole() {
        System.out.println("\nОбновление пользователя");
        System.out.print("Введите ID пользователя: ");
        long id = getValidatedId();

        try {
            UserDTO existingUser = userService.getUserById(id);
            System.out.println("Текущие данные:");
            printUserDetails(existingUser);

            UserDTO updatedUser = new UserDTO();
            updatedUser.setName(getValidatedName("Введите новое имя (оставьте пустым чтобы не менять): ", true));
            updatedUser.setEmail(getValidatedEmail("Введите новый email (оставьте пустым чтобы не менять): ", true));
            updatedUser.setAge(getValidatedAge("Введите новый возраст (0 чтобы не менять): ", true));

            // Сохраняем только измененные поля
            if (updatedUser.getName() == null) updatedUser.setName(existingUser.getName());
            if (updatedUser.getEmail() == null) updatedUser.setEmail(existingUser.getEmail());
            if (updatedUser.getAge() == 0) updatedUser.setAge(existingUser.getAge());

            UserDTO result = userService.updateUser(id, updatedUser);
            System.out.println("Данные обновлены:");
            printUserDetails(result);
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void readUsersConsole() {
        System.out.println("\nПросмотр пользователей");
        System.out.println("1. Найти по ID");
        System.out.println("2. Показать всех");
        System.out.print("Ваш выбор: ");

        int choice = Integer.parseInt(scanner.nextLine());
        switch (choice) {
            case 1 -> {
                System.out.print("Введите ID пользователя: ");
                long id = getValidatedId();
                try {
                    UserDTO user = userService.getUserById(id);
                    printUserDetails(user);
                } catch (RuntimeException e) {
                    System.out.println("Пользователь не найден");
                }
            }
            case 2 -> {
                List<UserDTO> users = userService.getAllUsers();
                if (users.isEmpty()) {
                    System.out.println("Нет пользователей в базе");
                } else {
                    System.out.println("\nСписок пользователей:");
                    System.out.println("ID\tИмя\tEmail\tВозраст");
                    users.forEach(u -> System.out.printf(
                            "%d\t%s\t%s\t%d\n",
                            u.getId(), u.getName(), u.getEmail(), u.getAge()
                    ));
                }
            }
            default -> System.out.println("Неверный выбор!");
        }
    }

    private void deleteUserConsole() {
        System.out.println("\nУдаление пользователя");
        System.out.print("Введите ID пользователя: ");
        long id = getValidatedId();

        try {
            userService.deleteUser(id);
            System.out.println("Пользователь успешно удален");
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void exit() {
        System.out.println("Завершение работы...");
        System.exit(0);
    }

    // Вспомогательные методы валидации
    private String getValidatedName() {
        return getValidatedName("Введите имя: ", false);
    }

    private String getValidatedName(String prompt, boolean allowEmpty) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (allowEmpty && input.isEmpty()) {
                return null;
            }

            if (input.length() < 2 || input.length() > 30) {
                System.out.println("Имя должно быть от 2 до 30 символов");
                continue;
            }
            return input;
        }
    }

    private String getValidatedEmail() {
        return getValidatedEmail("Введите email: ", false);
    }

    private String getValidatedEmail(String prompt, boolean allowEmpty) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (allowEmpty && input.isEmpty()) {
                return null;
            }

            if (!input.contains("@")) {
                System.out.println("Некорректный email");
                continue;
            }
            return input;
        }
    }

    private int getValidatedAge() {
        return getValidatedAge("Введите возраст: ", false);
    }

    private int getValidatedAge(String prompt, boolean allowZero) {
        while (true) {
            System.out.print(prompt);
            try {
                int age = Integer.parseInt(scanner.nextLine());
                if (age == 0 && allowZero) {
                    return 0;
                }
                if (age < 1 || age > 120) {
                    System.out.println("Возраст должен быть от 1 до 120");
                    continue;
                }
                return age;
            } catch (NumberFormatException e) {
                System.out.println("Введите число!");
            }
        }
    }

    private long getValidatedId() {
        while (true) {
            try {
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Введите корректный ID!");
            }
        }
    }

    private void printUserDetails(UserDTO user) {
        System.out.println("\nДанные пользователя:");
        System.out.println("ID: " + user.getId());
        System.out.println("Имя: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Возраст: " + user.getAge());
    }
}
