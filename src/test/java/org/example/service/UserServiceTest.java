//package org.example.service;
//
//import org.example.model.User;
//import org.example.repository.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//import java.time.LocalDate;
//import java.util.Scanner;
//
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceUnitTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private UserService userService;
//
//    @Test
//    void createUser_CallsRepo() {
//
//        userService.createUser("Alex", "alex@mail.com", 25, LocalDate.now());
//
//        verify(userRepository).createUser(any(User.class));
//    }
//    @Test
//    void deleteUser_CallsRepo() {
//
//        userService.deleteUser(1);
//
//        verify(userRepository).deleteUser(1);
//    }
//    @Test
//    void readUser_CallsRepo()
//    {
//        userService.readUser(52);
//
//        verify(userRepository).readUser(52);
//
//    }
//    @Test
//    void readAllUsers_CallsRepo()
//    {
//        userService.readAllUsers();
//
//        verify(userRepository).readAllUsers();
//    }
//    @Test
//    void updateUser_CallsRepo()
//    {
//        userService.updateUser(1, "AlexTest", "test@mail.ru", 20);
//
//        verify(userRepository).updateUser(1, "AlexTest", "test@mail.ru", 20);
//
//
//    }
//
//    @Test
//    void createName_ReturnsName() {
//
//        Scanner scanner = new Scanner("Valid Name\n");
//
//        String result = userService.createName(scanner);
//
//        assertEquals("Valid Name", result);
//    }
//
//    @Test
//    void createEmail_ReturnsEmail() {
//
//        Scanner scanner = new Scanner("\ninvalid\nvalid@mail.com\n");
//
//        String result = userService.createEmail(scanner);
//
//        assertEquals("valid@mail.com", result);
//    }
//
//    @Test
//    void createAge_ReturnsAge() {
//
//        Scanner scanner = new Scanner("30");
//
//        int result = userService.createAge(scanner);
//
//        assertEquals(30, result);
//    }
//
//}
