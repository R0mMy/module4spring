package org.example.repository;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;

import java.util.List;

//Класс с реализацией Hibernate
@Repository("userRepositoryHibernate")
public class UserRepositoryHibernate implements UserRepository {
    private final SessionFactory sessionFactory;
    private static final Logger logger = LogManager.getLogger(UserRepositoryHibernate.class);

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public UserRepositoryHibernate() {
        Configuration configuration = new Configuration();
        configuration.configure(); //читает hibernate.cfg.xml
        this.sessionFactory = configuration.buildSessionFactory();
    }

    @Override
    public void createUser(User user) {
        try (
                Session session = sessionFactory.openSession();
        ) {
            logger.debug("Начало транзакции");
            Transaction transaction = session.beginTransaction();
            logger.info("Сохранение пользователя в БД");
            session.persist(user);
            transaction.commit();
            logger.info("Пользователь успешно сохранен. ID: {}", user.getId());
        }
        logger.info("Завершение метода createUser");

    }

    @Override
    public void readUser(long id) {
        try (Session session = sessionFactory.openSession();
        ) {
            logger.debug("Начало транзакции");
            Transaction transaction = session.beginTransaction();
            logger.info("Поиск пользователя с ID: {}", id);
            User user = session.find(User.class, id);
            System.out.println("ID: " + user.getId() + "\n" +
                    "Name: " + user.getName() + "\n" +
                    "age: " + user.getAge() + "\n" +
                    "email: " + user.getEmail() + "\n" +
                    "created at: " + user.getCreated_at());
            transaction.commit();
            logger.debug("Транзакция успешно завершена");
        } catch (NullPointerException e) {
            logger.warn("Пользователь с ID {} не найден", id);
            System.out.println("Пользователь с id " + id + " не найден!");

        }
        logger.info("Завершение метода readUser");

    }

    @Override
    public void readAllUsers() {
        try (Session session = sessionFactory.openSession();
        ) {
            logger.debug("Начало транзакции");
            Transaction transaction = session.beginTransaction();
            logger.info("Получение всех пользователей");
            List<User> users = session.createQuery("FROM User", User.class).getResultList();
            System.out.println("ID\tName\tage\t Email\t\tCreated at");
            for (User user : users) {
                System.out.println(user.getId() + " | " +
                        user.getName() + " | " +
                        user.getAge() + " | " +
                        user.getEmail() + " | " +
                        user.getCreated_at());
            }
            transaction.commit();
            logger.debug("Транзакция успешно завершена");
        } catch (EntityNotFoundException e) {
            logger.error("Ошибка при получении пользователей", e);
            e.printStackTrace();
        }
        logger.info("Завершение метода readAllUser");

    }

    @Override
    public void updateUser(long id, String name, String email, int age) {
        try (Session session = sessionFactory.openSession();
        ) {
            logger.debug("Начало транзакции");
            Transaction transaction = session.beginTransaction();
            logger.info("Поиск пользователя с ID: {}", id);
            User user = session.find(User.class, id);
            if (name != null) {
                logger.info("Обновление имени на: {}", name);
                user.setName(name);
            } else if (age != 0) {
                logger.info("Обновление возраста на: {}", age);
                user.setAge(age);
            } else if (email != null) {
                logger.info("Обновление email на: {}", email);
                user.setEmail(email);
            }
            transaction.commit();
            logger.info("Пользователь успешно обновлен");
            System.out.println("Пользователь Изменен!");


        } catch (NullPointerException e) {
            logger.warn("Пользователь с ID {} не найден", id);
            System.out.println("Пользователь с id " + id + " не найден!");
        }
        logger.info("Конец метода UpdateUser");

    }

    @Override
    public void deleteUser(long id) {
        try (Session session = sessionFactory.openSession();
        ) {
            logger.debug("Начало транзакции");
            Transaction transaction = session.beginTransaction();
            logger.info("Поиск пользователя с ID: {}", id);
            User user = session.find(User.class, id);
            if (user != null) {
                logger.info("Удаление пользователя с ID: {}", id);
                session.remove(user);
                transaction.commit();
                logger.info("Пользователь успешно удален");
                System.out.println("Пользователь удален!");
            } else throw new EntityNotFoundException();

        } catch (EntityNotFoundException e) {
            logger.warn("Пользователь с ID {} не найден", id);
            System.out.println("Пользователь с id " + id + " не найден!");
        }
        logger.info("Завершение метода deleteUser");

    }
}
