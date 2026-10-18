package com.prepiq.backend.controller;

import com.prepiq.backend.dto.UserRegistrationRequest;
import com.prepiq.backend.dto.UserResponse;
import com.prepiq.backend.exception.UserAlreadyExistsException;
import com.prepiq.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void registerUserShouldReturnCreatedAndUserResponseWhenValid() throws Exception {
        when(userService.registerUser(any(UserRegistrationRequest.class)))
                .thenReturn(new UserResponse(
                        1L,
                        "Abhishek",
                        "abhishek@example.com",
                        LocalDateTime.of(2026, 10, 18, 12, 0)
                ));

        String requestBody = """
                {
                    "name": "Abhishek",
                    "email": "abhishek@example.com",
                    "password": "StrongPassword123"
                }
                """;

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Abhishek"))
                .andExpect(jsonPath("$.email").value("abhishek@example.com"))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.passwordHash").doesNotExist());
    }

    @Test
    void registerUserShouldReturnBadRequestWhenNameIsBlank() throws Exception {
        String requestBody = """
                {
                    "name": "   ",
                    "email": "abhishek@example.com",
                    "password": "StrongPassword123"
                }
                """;

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message", containsString("name: Name is required")));

        verifyNoInteractions(userService);
    }

    @Test
    void registerUserShouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        String requestBody = """
                {
                    "name": "Abhishek",
                    "email": "not-an-email",
                    "password": "StrongPassword123"
                }
                """;

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsString("email: Email must be a valid email address")));

        verifyNoInteractions(userService);
    }

    @Test
    void registerUserShouldReturnBadRequestWhenPasswordIsTooShort() throws Exception {
        String requestBody = """
                {
                    "name": "Abhishek",
                    "email": "abhishek@example.com",
                    "password": "short"
                }
                """;

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsString("Password must be between 8 and 72 characters")));

        verifyNoInteractions(userService);
    }

    @Test
    void registerUserShouldReturnBadRequestWhenPasswordExceeds72Bytes() throws Exception {
        String emojiPassword = "😀".repeat(19);
        assertEquals(76, emojiPassword.getBytes(StandardCharsets.UTF_8).length);

        String requestBody = String.format("""
                {
                    "name": "Abhishek",
                    "email": "abhishek@example.com",
                    "password": "%s"
                }
                """, emojiPassword);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsString("Password must not exceed 72 bytes in UTF-8 encoding")));

        verifyNoInteractions(userService);
    }

    @Test
    void registerUserShouldReturnConflictWhenEmailAlreadyExists() throws Exception {
        when(userService.registerUser(any(UserRegistrationRequest.class)))
                .thenThrow(new UserAlreadyExistsException("User with email 'abhishek@example.com' already exists"));

        String requestBody = """
                {
                    "name": "Abhishek",
                    "email": "abhishek@example.com",
                    "password": "StrongPassword123"
                }
                """;

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("A user with this email already exists"));
    }

    @Test
    void registerUserShouldReturnBadRequestWhenJsonIsMalformed() throws Exception {
        String malformedJson = "{\"name\": \"Abhishek\", \"email\": ";

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Malformed JSON request body"));

        verifyNoInteractions(userService);
    }

    @Test
    void registerUserShouldReturnUnsupportedMediaTypeWhenContentTypeIsNotJson() throws Exception {
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_XML)
                        .content("<request></request>"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.status").value(415))
                .andExpect(jsonPath("$.error").value("Unsupported Media Type"));

        verifyNoInteractions(userService);
    }

    @Test
    void shouldReturnNotFoundWhenAttemptingToGetUserById() throws Exception {
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());

        verifyNoInteractions(userService);
    }

    @Test
    void shouldReturnNotFoundWhenPathDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/users/not-found/path"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnMethodNotAllowedWhenMethodNotSupported() throws Exception {
        mockMvc.perform(get("/api/users/register"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value(405))
                .andExpect(jsonPath("$.error").value("Method Not Allowed"));
    }

    @Test
    void shouldReturnInternalServerErrorWithSanitizedMessageOnUnexpectedException() throws Exception {
        when(userService.registerUser(any(UserRegistrationRequest.class)))
                .thenThrow(new RuntimeException("Sensitive database credentials or internal SQL trace"));

        String requestBody = """
                {
                    "name": "Abhishek",
                    "email": "abhishek@example.com",
                    "password": "StrongPassword123"
                }
                """;

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }
}
