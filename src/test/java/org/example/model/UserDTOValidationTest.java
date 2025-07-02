package org.example.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserDTOValidationTest {

    @Autowired
    private LocalValidatorFactoryBean validator;

    @Test
    public void testValidUserDTO() {
        UserDTO user = new UserDTO(1L, "Valid Name", "valid@example.com", 30);
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidName() {
        UserDTO user = new UserDTO(1L, "", "valid@example.com", 30);
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    public void testInvalidEmail() {
        UserDTO user = new UserDTO(1L, "Valid Name", "invalid-email", 30);
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testInvalidAge() {
        UserDTO user = new UserDTO(1L, "Valid Name", "valid@example.com", 150);
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }
}