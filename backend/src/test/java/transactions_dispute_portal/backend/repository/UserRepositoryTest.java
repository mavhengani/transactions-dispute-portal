package transactions_dispute_portal.backend.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import transactions_dispute_portal.backend.entity.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUsername() {
        // Given
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("johndoe");
        user.setPassword("password");
        user.setCellphone("1234567890");
        user.setGender("MALE");
        user.setCardNumber("1234567890123456");
        userRepository.save(user);

        // When
        Optional<User> foundUser = userRepository.findByUsername("johndoe");

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("johndoe", foundUser.get().getUsername());
        assertEquals("John", foundUser.get().getFirstName());
    }

    @Test
    void testFindByUsername_NotFound() {
        // When
        Optional<User> foundUser = userRepository.findByUsername("nonexistent");

        // Then
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testExistsByUsername() {
        // Given
        User user = new User();
        user.setUsername("johndoe");
        user.setPassword("password");
        userRepository.save(user);

        // When & Then
        assertTrue(userRepository.existsByUsername("johndoe"));
        assertFalse(userRepository.existsByUsername("nonexistent"));
    }

    @Test
    void testExistsByCellphone() {
        // Given
        User user = new User();
        user.setUsername("johndoe");
        user.setPassword("password");
        user.setCellphone("1234567890");
        userRepository.save(user);

        // When & Then
        assertTrue(userRepository.existsByCellphone("1234567890"));
        assertFalse(userRepository.existsByCellphone("0987654321"));
    }
}
