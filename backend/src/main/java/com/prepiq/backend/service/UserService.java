package com.prepiq.backend.service;

import com.prepiq.backend.dto.UserRegistrationRequest;
import com.prepiq.backend.dto.UserResponse;
import com.prepiq.backend.entity.User;
import com.prepiq.backend.exception.UserAlreadyExistsException;
import com.prepiq.backend.exception.UserNotFoundException;
import com.prepiq.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse registerUser(UserRegistrationRequest request) {
        String normalizedEmail = request.email().trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new UserAlreadyExistsException("User with email '" + normalizedEmail + "' already exists");
        }

        String hashedPassword = passwordEncoder.encode(request.password());

        User user = new User(
                request.name().trim(),
                normalizedEmail,
                hashedPassword
        );

        User savedUser = userRepository.save(user);
        return toUserResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::toUserResponse)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        return userRepository.findByEmail(normalizedEmail)
                .map(this::toUserResponse)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + normalizedEmail));
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
