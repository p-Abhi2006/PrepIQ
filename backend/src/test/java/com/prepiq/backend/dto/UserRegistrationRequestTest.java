package com.prepiq.backend.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRegistrationRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldPassValidationWithValidData() {
        UserRegistrationRequest request = new UserRegistrationRequest(
                "Abhishek",
                "abhishek@example.com",
                "securePassword123"
        );

        Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenNameIsBlank() {
        UserRegistrationRequest request = new UserRegistrationRequest(
                "   ",
                "abhishek@example.com",
                "securePassword123"
        );

        Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Name is required", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenNameExceeds100Characters() {
        String longName = "A".repeat(101);
        UserRegistrationRequest request = new UserRegistrationRequest(
                longName,
                "abhishek@example.com",
                "securePassword123"
        );

        Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Name must not exceed 100 characters", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenEmailIsInvalid() {
        UserRegistrationRequest request = new UserRegistrationRequest(
                "Abhishek",
                "not-an-email",
                "securePassword123"
        );

        Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Email must be a valid email address", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenEmailIsBlank() {
        UserRegistrationRequest request = new UserRegistrationRequest(
                "Abhishek",
                "",
                "securePassword123"
        );

        Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(request);

        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Email is required")));
    }

    @Test
    void shouldFailValidationWhenPasswordIsTooShort() {
        UserRegistrationRequest request = new UserRegistrationRequest(
                "Abhishek",
                "abhishek@example.com",
                "short"
        );

        Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Password must be between 8 and 72 characters", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenPasswordIsBlank() {
        UserRegistrationRequest request = new UserRegistrationRequest(
                "Abhishek",
                "abhishek@example.com",
                "   "
        );

        Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(request);

        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Password is required")));
    }

    @Test
    void shouldPassValidationWhenPasswordIsExactly72AsciiCharacters() {
        String password72 = "p".repeat(72);
        UserRegistrationRequest request = new UserRegistrationRequest(
                "Abhishek",
                "abhishek@example.com",
                password72
        );

        Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenPasswordExceeds72AsciiCharacters() {
        String password73 = "p".repeat(73);
        UserRegistrationRequest request = new UserRegistrationRequest(
                "Abhishek",
                "abhishek@example.com",
                password73
        );

        Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(request);

        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Password must be between 8 and 72 characters")
                || v.getMessage().contains("Password must not exceed 72 bytes")));
    }

    @Test
    void shouldPassValidationWhenPasswordIsMultibyteAndAtMost72Bytes() {
        // '\u0905' (Devanagari letter A) is 3 bytes in UTF-8. 24 characters = 72 bytes.
        String multibyte72Bytes = "\u0905".repeat(24);
        UserRegistrationRequest request = new UserRegistrationRequest(
                "Abhishek",
                "abhishek@example.com",
                multibyte72Bytes
        );

        Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenPasswordIsMultibyteAndExceeds72Bytes() {
        // 25 characters of '\u0905' = 75 bytes (character length 25 <= 72, but UTF-8 byte length > 72)
        String multibyte75Bytes = "\u0905".repeat(25);
        UserRegistrationRequest request = new UserRegistrationRequest(
                "Abhishek",
                "abhishek@example.com",
                multibyte75Bytes
        );

        Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Password must not exceed 72 bytes in UTF-8 encoding", violations.iterator().next().getMessage());
    }

    @Test
    void shouldPassValidationWhenPasswordContainsEmojisWithin72Bytes() {
        // Unicode character '\u2705' (WHITE HEAVY CHECK MARK) is 3 bytes in UTF-8. 24 * 3 = 72 bytes.
        String emojiPassword = "\u2705".repeat(24);
        UserRegistrationRequest request = new UserRegistrationRequest(
                "Abhishek",
                "abhishek@example.com",
                emojiPassword
        );

        Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldPassValidationWhenPasswordContainsSupplementaryEmojisAtExactly72Bytes() {
        // '😀' (GRINNING FACE, U+1F600) is 4 bytes in UTF-8. 18 characters * 4 bytes = 72 bytes.
        // In Java UTF-16, each supplementary emoji is 2 chars (surrogate pair), so 18 * 2 = 36 chars.
        String emoji72Bytes = "😀".repeat(18);
        UserRegistrationRequest request = new UserRegistrationRequest(
                "Abhishek",
                "abhishek@example.com",
                emoji72Bytes
        );

        Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenPasswordContainsSupplementaryEmojisExceeding72Bytes() {
        // 19 '😀' * 4 bytes = 76 bytes (Java char length 38 is within 8..72, but UTF-8 byte length is 76 > 72).
        String emoji76Bytes = "😀".repeat(19);
        UserRegistrationRequest request = new UserRegistrationRequest(
                "Abhishek",
                "abhishek@example.com",
                emoji76Bytes
        );

        Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Password must not exceed 72 bytes in UTF-8 encoding", violations.iterator().next().getMessage());
    }
}
