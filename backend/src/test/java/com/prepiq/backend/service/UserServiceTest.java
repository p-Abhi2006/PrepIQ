package com.prepiq.backend.service;

import com.prepiq.backend.dto.UserRegistrationRequest;
import com.prepiq.backend.dto.UserResponse;
import com.prepiq.backend.entity.User;
import com.prepiq.backend.exception.UserAlreadyExistsException;
import com.prepiq.backend.exception.UserNotFoundException;
import com.prepiq.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.RecordComponent;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User sampleUser;
    private UserRegistrationRequest registrationRequest;

    @BeforeEach
    void setUp() {
        sampleUser = new User("Abhishek P", "abhishek@example.com", "encoded-secret-hash");
        sampleUser.setId(1L);
        sampleUser.setCreatedAt(LocalDateTime.now());
        sampleUser.setUpdatedAt(LocalDateTime.now());

        registrationRequest = new UserRegistrationRequest(
                "Abhishek P",
                "abhishek@example.com",
                "StrongPassword123"
        );
    }

    @Test
    void registerUserShouldSaveAndReturnResponseWhenEmailIsUnique() {
        when(userRepository.existsByEmail("abhishek@example.com")).thenReturn(false);
        when(passwordEncoder.encode("StrongPassword123")).thenReturn("encoded-secret-hash");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        UserResponse response = userService.registerUser(registrationRequest);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Abhishek P", response.name());
        assertEquals("abhishek@example.com", response.email());

        // Verify that the entity persisted actually receives the hashed password, not raw
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals("encoded-secret-hash", userCaptor.getValue().getPasswordHash());
        assertEquals("abhishek@example.com", userCaptor.getValue().getEmail());
        assertEquals("Abhishek P", userCaptor.getValue().getName());

        verify(userRepository).existsByEmail("abhishek@example.com");
        verify(passwordEncoder).encode("StrongPassword123");
    }

    @Test
    void registerUserShouldNormalizeEmailWithLocaleRootAndTrimWhitespace() {
        UserRegistrationRequest untrimmedRequest = new UserRegistrationRequest(
                "  Abhishek P  ",
                "   TITLE.USER@EXAMPLE.COM   ",
                "StrongPassword123"
        );

        when(userRepository.existsByEmail("title.user@example.com")).thenReturn(false);
        when(passwordEncoder.encode("StrongPassword123")).thenReturn("encoded-secret-hash");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        userService.registerUser(untrimmedRequest);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        assertEquals("Abhishek P", userCaptor.getValue().getName());
        assertEquals("title.user@example.com", userCaptor.getValue().getEmail());
        verify(userRepository).existsByEmail("title.user@example.com");
    }

    @Test
    void registerUserShouldThrowUserAlreadyExistsExceptionWhenEmailExists() {
        when(userRepository.existsByEmail("abhishek@example.com")).thenReturn(true);

        UserAlreadyExistsException exception = assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.registerUser(registrationRequest)
        );

        assertEquals("User with email 'abhishek@example.com' already exists", exception.getMessage());
        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void userResponseShouldNotExposePasswordHashFieldOrValue() {
        RecordComponent[] components = UserResponse.class.getRecordComponents();
        boolean hasSensitiveField = Arrays.stream(components)
                .anyMatch(c -> c.getName().toLowerCase().contains("password")
                        || c.getName().toLowerCase().contains("hash"));

        assertFalse(hasSensitiveField, "UserResponse must not expose password or hash fields");
    }

    @Test
    void bCryptPasswordEncoderShouldSafelyEncode72BytesAndRejectMoreThan72Bytes() {
        BCryptPasswordEncoder realEncoder = new BCryptPasswordEncoder();

        // 72 ASCII bytes works cleanly
        String valid72 = "p".repeat(72);
        String hash = realEncoder.encode(valid72);
        assertTrue(realEncoder.matches(valid72, hash));

        // 73 ASCII bytes is rejected by BCrypt in Spring Security
        String invalid73 = "p".repeat(73);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> realEncoder.encode(invalid73)
        );
        assertTrue(exception.getMessage().contains("password cannot be more than 72 bytes"));
    }

    @Test
    void getUserByIdShouldReturnUserResponseWhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        UserResponse response = userService.getUserById(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Abhishek P", response.name());
        assertEquals("abhishek@example.com", response.email());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserByIdShouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserById(999L)
        );

        assertEquals("User not found with id: 999", exception.getMessage());
        verify(userRepository).findById(999L);
    }

    @Test
    void getUserByEmailShouldReturnUserResponseWhenUserExists() {
        when(userRepository.findByEmail("abhishek@example.com")).thenReturn(Optional.of(sampleUser));

        UserResponse response = userService.getUserByEmail("  Abhishek@Example.COM  ");

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Abhishek P", response.name());
        assertEquals("abhishek@example.com", response.email());
        verify(userRepository).findByEmail("abhishek@example.com");
    }

    @Test
    void getUserByEmailShouldThrowUserNotFoundExceptionWhenEmailDoesNotExist() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserByEmail("unknown@example.com")
        );

        assertEquals("User not found with email: unknown@example.com", exception.getMessage());
        verify(userRepository).findByEmail("unknown@example.com");
    }
}
