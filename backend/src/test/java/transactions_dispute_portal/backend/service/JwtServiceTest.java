package transactions_dispute_portal.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void testExtractUsername() {
        // Given
        UserDetails userDetails = User.withUsername("testuser")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
        String token = jwtService.generateToken(userDetails);

        // When
        String username = jwtService.extractUsername(token);

        // Then
        assertEquals("testuser", username);
    }

    @Test
    void testGenerateToken() {
        // Given
        UserDetails userDetails = User.withUsername("testuser")
                .password("password")
                .authorities(Collections.emptyList())
                .build();

        // When
        String token = jwtService.generateToken(userDetails);

        // Then
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertEquals("testuser", jwtService.extractUsername(token));
    }

    @Test
    void testIsTokenValid() {
        // Given
        UserDetails userDetails = User.withUsername("testuser")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
        String token = jwtService.generateToken(userDetails);

        // When & Then
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void testIsTokenValidWithWrongUser() {
        // Given
        UserDetails userDetails1 = User.withUsername("testuser1")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
        UserDetails userDetails2 = User.withUsername("testuser2")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
        String token = jwtService.generateToken(userDetails1);

        // When & Then
        assertFalse(jwtService.isTokenValid(token, userDetails2));
    }

    @Test
    void testIsTokenNotExpired() {
        // Given
        UserDetails userDetails = User.withUsername("testuser")
                .password("password")
                .authorities(Collections.emptyList())
                .build();

        // Create a token
        String token = jwtService.generateToken(userDetails);

        // When & Then
        // Token should not be expired immediately after creation
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }
}
