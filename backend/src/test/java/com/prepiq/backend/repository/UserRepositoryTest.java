package com.prepiq.backend.repository;

import com.prepiq.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindUserByEmail() {
        User user = new User("Test Student", "test.student@example.com", "hashed_secret");

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertNotNull(savedUser.getCreatedAt());
        assertNotNull(savedUser.getUpdatedAt());

        Optional<User> found = userRepository.findByEmail("test.student@example.com");
        assertTrue(found.isPresent());
        assertEquals("Test Student", found.get().getName());
        assertEquals("hashed_secret", found.get().getPasswordHash());
    }

    @Test
    void shouldCheckIfExistsByEmail() {
        User user = new User("Jane Doe", "jane.doe@example.com", "test-hash-jane");
        userRepository.save(user);

        assertTrue(userRepository.existsByEmail("jane.doe@example.com"));
        assertFalse(userRepository.existsByEmail("unknown@example.com"));
    }
}
