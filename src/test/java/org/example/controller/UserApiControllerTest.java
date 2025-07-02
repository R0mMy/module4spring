package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.UserDTO;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

public class UserApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    private final UserDTO testUser = new UserDTO(1L, "Test User", "test@example.com", 30);

    @TestConfiguration
    static class MockConfig {
        @Bean
        @Primary
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    @Test
    public void testCreateUser() throws Exception {
        Mockito.when(userService.createUser(Mockito.any(UserDTO.class)))
                .thenReturn(testUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test User")))
                .andExpect(jsonPath("$.email", is("test@example.com")));
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(Long id) {
            super("User with id " + id + " not found");
        }
    }

    @Test
    public void testGetUserById() throws Exception {
        Mockito.when(userService.getUserById(1L))
                .thenReturn(testUser);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test User")));
    }

    @Test
    public void testGetUserById_NotFound() throws Exception {
        // 1. Настройка мока
        Mockito.when(userService.getUserById(999L))
                .thenThrow(new UserNotFoundException(999L));

        // 2. Выполнение и проверки
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllUsers() throws Exception {
        List<UserDTO> users = Arrays.asList(
                new UserDTO(1L, "User1", "user1@test.com", 25),
                new UserDTO(2L, "User2", "user2@test.com", 30)
        );

        Mockito.when(userService.getAllUsers())
                .thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("User1")))
                .andExpect(jsonPath("$[1].name", is("User2")));
    }

    @Test
    public void testUpdateUser() throws Exception {
        UserDTO updatedUser = new UserDTO(1L, "Updated Name", "updated@test.com", 35);

        Mockito.when(userService.updateUser(Mockito.eq(1L), Mockito.any(UserDTO.class)))
                .thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Name")))
                .andExpect(jsonPath("$.email", is("updated@test.com")));
    }

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(userService, Mockito.times(1)).deleteUser(1L);
    }
}