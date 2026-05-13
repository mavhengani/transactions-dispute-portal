package transactions_dispute_portal.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import transactions_dispute_portal.backend.entity.Transaction;
import transactions_dispute_portal.backend.entity.User;
import transactions_dispute_portal.backend.repository.TransactionRepository;
import transactions_dispute_portal.backend.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private User testUser;
    private Transaction testTransaction;

    @BeforeEach
    void setUp() {
        // Create test user
        testUser = new User();
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setUsername("testuser");
        testUser.setPassword("hashedpassword");
        testUser.setCellphone("1234567890");
        testUser.setGender("MALE");
        testUser.setCardNumber("1234567890123456");
        testUser = userRepository.save(testUser);

        // Create test transaction
        testTransaction = new Transaction();
        testTransaction.setMerchantName("Test Merchant");
        testTransaction.setAmount(new BigDecimal("100.00"));
        testTransaction.setCurrency("USD");
        testTransaction.setTransactionType("PURCHASE");
        testTransaction.setStatus("COMPLETED");
        testTransaction.setReferenceNumber("REF123");
        testTransaction.setPaymentMethod("CREDIT_CARD");
        testTransaction.setLocation("Test City");
        testTransaction.setDisputed(false);
        testTransaction.setTransactionDate(LocalDateTime.now());
        testTransaction.setUser(testUser);
        testTransaction = transactionRepository.save(testTransaction);
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetTransactions_All() throws Exception {
        mockMvc.perform(get("/api/transactions")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(testTransaction.getId()))
                .andExpect(jsonPath("$.content[0].merchantName").value("Test Merchant"))
                .andExpect(jsonPath("$.content[0].amount").value(100.00))
                .andExpect(jsonPath("$.content[0].maskedCardNumber").value("**** **** **** 3456"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetTransactions_ByUserId() throws Exception {
        mockMvc.perform(get("/api/transactions")
                        .param("userId", testUser.getId().toString())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(testTransaction.getId()));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetTransactions_ByDisputed() throws Exception {
        mockMvc.perform(get("/api/transactions")
                        .param("disputed", "false")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(testTransaction.getId()));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetTransactions_ByUserIdAndDisputed() throws Exception {
        mockMvc.perform(get("/api/transactions")
                        .param("userId", testUser.getId().toString())
                        .param("disputed", "false")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(testTransaction.getId()));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testDisputeTransaction_Success() throws Exception {
        mockMvc.perform(put("/api/transactions/{id}/dispute", testTransaction.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testTransaction.getId()))
                .andExpect(jsonPath("$.disputed").value(true))
                .andExpect(jsonPath("$.status").value("DISPUTED"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testDisputeTransaction_NotFound() throws Exception {
        mockMvc.perform(put("/api/transactions/{id}/dispute", 999L))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testDisputeTransaction_AlreadyDisputed() throws Exception {
        // First dispute the transaction
        testTransaction.setDisputed(true);
        testTransaction.setStatus("DISPUTED");
        transactionRepository.save(testTransaction);

        // Try to dispute again
        mockMvc.perform(put("/api/transactions/{id}/dispute", testTransaction.getId()))
                .andExpect(status().isBadRequest());
    }
}
