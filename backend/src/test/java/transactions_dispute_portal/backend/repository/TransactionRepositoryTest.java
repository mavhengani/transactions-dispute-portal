package transactions_dispute_portal.backend.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import transactions_dispute_portal.backend.entity.Transaction;
import transactions_dispute_portal.backend.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Transaction testTransaction1;
    private Transaction testTransaction2;

    @BeforeEach
    void setUp() {
        // Create test user
        testUser = new User();
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setCellphone("1234567890");
        testUser.setGender("MALE");
        testUser.setCardNumber("1234567890123456");
        testUser = userRepository.save(testUser);

        // Create test transactions
        testTransaction1 = new Transaction();
        testTransaction1.setMerchantName("Merchant 1");
        testTransaction1.setAmount(new BigDecimal("100.00"));
        testTransaction1.setCurrency("USD");
        testTransaction1.setTransactionType("PURCHASE");
        testTransaction1.setStatus("COMPLETED");
        testTransaction1.setReferenceNumber("REF001");
        testTransaction1.setPaymentMethod("CREDIT_CARD");
        testTransaction1.setLocation("City 1");
        testTransaction1.setDisputed(false);
        testTransaction1.setTransactionDate(LocalDateTime.now());
        testTransaction1.setUser(testUser);
        testTransaction1 = transactionRepository.save(testTransaction1);

        testTransaction2 = new Transaction();
        testTransaction2.setMerchantName("Merchant 2");
        testTransaction2.setAmount(new BigDecimal("200.00"));
        testTransaction2.setCurrency("USD");
        testTransaction2.setTransactionType("PURCHASE");
        testTransaction2.setStatus("COMPLETED");
        testTransaction2.setReferenceNumber("REF002");
        testTransaction2.setPaymentMethod("CREDIT_CARD");
        testTransaction2.setLocation("City 2");
        testTransaction2.setDisputed(true);
        testTransaction2.setTransactionDate(LocalDateTime.now());
        testTransaction2.setUser(testUser);
        testTransaction2 = transactionRepository.save(testTransaction2);
    }

    @Test
    void testFindAll() {
        // When
        Page<Transaction> transactions = transactionRepository.findAll(PageRequest.of(0, 10));

        // Then
        assertEquals(2, transactions.getTotalElements());
    }

    @Test
    void testFindByUserId() {
        // When
        Page<Transaction> transactions = transactionRepository.findByUserId(
                testUser.getId(), PageRequest.of(0, 10));

        // Then
        assertEquals(2, transactions.getTotalElements());
        assertTrue(transactions.getContent().stream()
                .allMatch(tx -> tx.getUser().getId().equals(testUser.getId())));
    }

    @Test
    void testFindByUserIdAndDisputed() {
        // When
        Page<Transaction> disputedTransactions = transactionRepository.findByUserIdAndDisputed(
                testUser.getId(), true, PageRequest.of(0, 10));

        Page<Transaction> nonDisputedTransactions = transactionRepository.findByUserIdAndDisputed(
                testUser.getId(), false, PageRequest.of(0, 10));

        // Then
        assertEquals(1, disputedTransactions.getTotalElements());
        assertEquals(1, nonDisputedTransactions.getTotalElements());
        assertTrue(disputedTransactions.getContent().get(0).getDisputed());
        assertFalse(nonDisputedTransactions.getContent().get(0).getDisputed());
    }

    @Test
    void testFindByDisputed() {
        // When
        Page<Transaction> disputedTransactions = transactionRepository.findByDisputed(
                true, PageRequest.of(0, 10));

        Page<Transaction> nonDisputedTransactions = transactionRepository.findByDisputed(
                false, PageRequest.of(0, 10));

        // Then
        assertEquals(1, disputedTransactions.getTotalElements());
        assertEquals(1, nonDisputedTransactions.getTotalElements());
    }
}
