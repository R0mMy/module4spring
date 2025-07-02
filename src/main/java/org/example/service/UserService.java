package org.example.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.User;
import org.example.model.UserDTO;
import org.example.repository.UserRepository;
import org.example.repository.UserRepositorySpring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserRepositorySpring springRepo;

    @Autowired
    public UserService(
            @Qualifier("userRepositoryHibernate") UserRepository hibernateRepo,
            UserRepositorySpring springRepo
    ) {
        this.userRepository = hibernateRepo;
        this.springRepo = springRepo;
    }

    // CREATE
    public UserDTO createUser(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        User savedUser = springRepo.save(user);
        return convertToDTO(savedUser);
    }

    // READ (single)
    public UserDTO getUserById(Long id) {
        User user = springRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDTO(user);
    }

    // READ (all)
    public List<UserDTO> getAllUsers() {
        return springRepo.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // UPDATE
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existingUser = springRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setName(userDTO.getName());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setAge(userDTO.getAge());

        User updatedUser = springRepo.save(existingUser);
        return convertToDTO(updatedUser);
    }

    // DELETE
    public void deleteUser(Long id) {
        springRepo.deleteById(id);
    }

    // Конвертация Entity <-> DTO
    private User convertToEntity(UserDTO dto) {
        return new User(
                dto.getName(),
                dto.getEmail(),
                dto.getAge(),
                LocalDate.now()
        );
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getAge());
    }

    public String createName(Scanner scanner) {
        logger.info("Начало метода createName");
        String name = "";
        boolean sw = false;
        while (!sw) {
            name = scanner.nextLine().trim();
            logger.debug("Получено имя: {}", name);
            if (name.isBlank()) {
                logger.warn("Имя не может быть пустым");
                System.out.println("Ошибка: Имя не может быть пустым!");
            } else if (name.length() <= 2 || name.length() > 30) {
                logger.warn("Длина имени не соответствует требованиям");
                System.out.println("Имя не может быть меньше 2х символов и больше 30 символов!");
            } else sw = true;
        }
        logger.info("Завершение метода createName");
        return name;
    }

    public String createEmail(Scanner scanner) {
        logger.info("Начало метода createEmail");
        String email = null;
        boolean sw = false;
        while (!sw) {
            email = scanner.nextLine();
            logger.debug("Получен email: {}", email);
            if (email.isBlank()) {
                logger.warn("Email не может быть пустым");
                System.out.println("Ошибка: Email не может быть пустым!");
            } else if (!(email.contains("@"))) {
                logger.warn("Email некорректный");
                System.out.println("Введите корректный email");
            } else
                sw = true;
        }
        logger.info("Завершение метода createEmail");
        return email;
    }

    public int createAge(Scanner scanner) {
        logger.info("Начало метода createAge");
        int age = 0;
        boolean sw = false;
        while (!sw) {
            age = Integer.parseInt(scanner.nextLine());
            logger.debug("Получен возраст: {}", age);
            if (age == 0) {
                logger.warn("Возраст не может быть нулевым");
                System.out.println("Ошибка: Возраст не может быть пустым!");
            } else if (age < 0 || age > 120) {
                logger.warn("Возраст выходит за допустимые пределы");
                System.out.println("Возраст не может быть меньше 0 или больше 120 лет!");
            } else
                sw = true;
        }
        logger.info("Завершение метода createAge");
        return age;
    }
}
