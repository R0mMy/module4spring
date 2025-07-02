package org.example;

import org.example.controller.UserController;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

    }

    @Bean
    @Profile("!test")
    public CommandLineRunner run(UserController userController) {
        return args -> {
            System.out.println("\n=== Консольный интерфейс запущен ===");
            userController.start();
        };
    }
}
