package org.example.model;


import jakarta.validation.constraints.*;

public class UserDTO {
    private long id;
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @Email(message = "Некорректный email")
    private String email;
    @Min(1)
    @Max(120)
    private int age;

    public UserDTO() {
    }

    public UserDTO(long id, String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.id = id;
    }

    // Геттеры и сеттеры
    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}