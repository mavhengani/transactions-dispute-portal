package transactions_dispute_portal.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import transactions_dispute_portal.backend.dto.AuthRequestDTO;
import transactions_dispute_portal.backend.dto.RegisterRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@Transactional
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegister_Success() throws Exception {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setUsername("johndoe");
        request.setPassword("password123");
        request.setCellphone("1234567890");
        request.setGender("MALE");
        request.setCardNumber("1234567890123456");

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.username").value("johndoe"))
                .andExpect(jsonPath("$.cellphone").value("1234567890"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.cardNumber").value("1234567890123456"));
    }

    @Test
    void testRegister_DuplicateUsername() throws Exception {
        // Given - First register a user
        RegisterRequest request1 = new RegisterRequest();
        request1.setFirstName("John");
        request1.setLastName("Doe");
        request1.setUsername("johndoe");
        request1.setPassword("password123");
        request1.setCellphone("1234567890");
        request1.setGender("MALE");
        request1.setCardNumber("1234567890123456");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isOk());

        // Now try to register with same username
        RegisterRequest request2 = new RegisterRequest();
        request2.setFirstName("Jane");
        request2.setLastName("Smith");
        request2.setUsername("johndoe"); // Same username
        request2.setPassword("password456");
        request2.setCellphone("0987654321");
        request2.setGender("FEMALE");
        request2.setCardNumber("6543210987654321");

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin_Success() throws Exception {
        // Given - First register a user
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        registerRequest.setUsername("johndoe");
        registerRequest.setPassword("password123");
        registerRequest.setCellphone("1234567890");
        registerRequest.setGender("MALE");
        registerRequest.setCardNumber("1234567890123456");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        // Now login
        AuthRequestDTO loginRequest = new AuthRequestDTO();
        loginRequest.setUsername("johndoe");
        loginRequest.setPassword("password123");

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.user.id").exists())
                .andExpect(jsonPath("$.user.firstName").value("John"))
                .andExpect(jsonPath("$.user.lastName").value("Doe"))
                .andExpect(jsonPath("$.user.username").value("johndoe"))
                .andExpect(jsonPath("$.user.cellphone").value("1234567890"));
    }

    @Test
    void testLogin_InvalidCredentials() throws Exception {
        // Given
        AuthRequestDTO loginRequest = new AuthRequestDTO();
        loginRequest.setUsername("nonexistent");
        loginRequest.setPassword("wrongpassword");

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }
}
