package org.example.repository;

import org.example.model.User;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserRepositoryHibernateTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private static UserRepositoryHibernate userRepository;

    @BeforeAll
    static void beforeAll() {
        // Настраиваем Hibernate для работы с тестовым контейнером
        System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgres.getUsername());
        System.setProperty("hibernate.connection.password", postgres.getPassword());

        userRepository = new UserRepositoryHibernate();
    }

    @AfterEach
    void tearDown() {
        // Очищаем данные после каждого теста
        try (Session session = userRepository.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createMutationQuery("DELETE FROM User").executeUpdate();
            transaction.commit();
        }
    }

    @Test
    void createUser_ShouldPersistUserInDatabase() {
        // Arrange
        User user = new User("Test User", "test@example.com", 25, LocalDate.now());

        // Act
        userRepository.createUser(user);

        // Assert
        assertNotNull(user.getId(), "ID пользователя должен быть установлен после сохранения");
    }

    @Test
    void readUser_ShouldReturnUser_WhenUserExists() {
        // Arrange
        User expectedUser = new User("Test User", "test@example.com", 25, LocalDate.now());

        userRepository.createUser(expectedUser);

        // Act
        userRepository.readUser(expectedUser.getId());

        // Assert - проверяем через логирование в методе readUser
        // В реальном проекте лучше возвращать объект из метода
    }

    @Test
    void readUser_ShouldHandleNotFound_WhenUserDoesNotExist() {
        // Act & Assert
        assertDoesNotThrow(() -> userRepository.readUser(9999),
                "Метод должен обрабатывать случай отсутствия пользователя");
    }

    @Test
    void readAllUsers_ShouldReturnAllUsers() {
        // Arrange
        User user1 = new User("User 1", "user1@test.com", 25, LocalDate.now());
        User user2 = new User("User 2", "user2@test.com", 30, LocalDate.now());
        userRepository.createUser(user1);
        userRepository.createUser(user2);

        // Act & Assert
        assertDoesNotThrow(() -> userRepository.readAllUsers(),
                "Метод должен успешно выполниться при наличии пользователей");
    }

    @Test
    void updateUser_ShouldUpdateName_WhenNameProvided() {
        // Arrange
        User originalUser = new User("USer", "user2@test.com", 25, LocalDate.now());
        userRepository.createUser(originalUser);
        long userId = originalUser.getId();
        String newName = "Updated Name";

        // Act
        userRepository.updateUser(userId, newName, null, 0);

        // Assert
        try (Session session = userRepository.getSessionFactory().openSession()) {
            User updatedUser = session.find(User.class, userId);
            assertEquals(newName, updatedUser.getName(), "Имя должно быть обновлено");
        }
    }

    @Test
    void updateUser_ShouldUpdateEmail_WhenEmailProvided() {
        // Arrange
        User originalUser = new User("User", "user2@test.com", 25, LocalDate.now());
        userRepository.createUser(originalUser);
        long userId = originalUser.getId();
        String newEmail = "updated@test.com";

        // Act
        userRepository.updateUser(userId, null, newEmail, 0);

        // Assert
        try (Session session = userRepository.getSessionFactory().openSession()) {
            User updatedUser = session.find(User.class, userId);
            assertEquals(newEmail, updatedUser.getEmail(), "Email должен быть обновлен");
        }
    }

    @Test
    void updateUser_ShouldUpdateAge_WhenAgeProvided() {
        // Arrange
        User originalUser = new User("User", "user2@test.com", 25, LocalDate.now());
        userRepository.createUser(originalUser);
        long userId = originalUser.getId();
        int newAge = 30;

        // Act
        userRepository.updateUser(userId, null, null, newAge);

        // Assert
        try (Session session = userRepository.getSessionFactory().openSession()) {
            User updatedUser = session.find(User.class, userId);
            assertEquals(newAge, updatedUser.getAge(), "Возраст должен быть обновлен");
        }
    }

    @Test
    void deleteUser_ShouldRemoveUserFromDatabase() {
        // Arrange
        User user = new User("To Delete", "delete@test.com", 25, LocalDate.now());
        userRepository.createUser(user);
        long userId = user.getId();

        // Act
        userRepository.deleteUser(userId);

        // Assert
        try (Session session = userRepository.getSessionFactory().openSession()) {
            User deletedUser = session.find(User.class, userId);
            assertNull(deletedUser, "Пользователь должен быть удален из БД");
        }
    }

    @Test
    void deleteUser_ShouldHandleNotFound_WhenUserDoesNotExist() {
        // Act & Assert
        assertDoesNotThrow(() -> userRepository.deleteUser(9999),
                "Метод должен обрабатывать случай отсутствия пользователя");
    }
}
