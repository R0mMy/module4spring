package org.example.repository;

import org.example.model.User;

public interface UserRepository {
    void createUser(User user);

    void readUser(long id);

    void readAllUsers();

    void updateUser(long id, String name, String email, int age);

    void deleteUser(long id);

}
