package transactions_dispute_portal.backend.mapper;

import org.junit.jupiter.api.Test;
import transactions_dispute_portal.backend.dto.TransactionDTO;
import transactions_dispute_portal.backend.entity.Transaction;
import transactions_dispute_portal.backend.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionMapperTest {

    private final TransactionMapper mapper = new TransactionMapper();

    @Test
    void testToDTO() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setCardNumber("1234567890123456");

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setMerchantName("Test Merchant");
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setCurrency("USD");
        transaction.setTransactionType("PURCHASE");
        transaction.setStatus("COMPLETED");
        transaction.setReferenceNumber("REF123");
        transaction.setPaymentMethod("CREDIT_CARD");
        transaction.setLocation("New York");
        transaction.setDisputed(false);
        transaction.setDisputeReason(null);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setUser(user);

        // When
        TransactionDTO dto = mapper.toDTO(transaction);

        // Then
        assertEquals(1L, dto.getId());
        assertEquals("Test Merchant", dto.getMerchantName());
        assertEquals(new BigDecimal("100.00"), dto.getAmount());
        assertEquals("USD", dto.getCurrency());
        assertEquals("PURCHASE", dto.getTransactionType());
        assertEquals("COMPLETED", dto.getStatus());
        assertEquals("REF123", dto.getReferenceNumber());
        assertEquals("CREDIT_CARD", dto.getPaymentMethod());
        assertEquals("New York", dto.getLocation());
        assertFalse(dto.getDisputed());
        assertNull(dto.getDisputeReason());
        assertNotNull(dto.getTransactionDate());
        assertEquals("**** **** **** 3456", dto.getMaskedCardNumber());
    }

    @Test
    void testToDTOWithShortCardNumber() {
        // Given
        User user = new User();
        user.setCardNumber("123");

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setUser(user);

        // When & Then - This should fail because card number is too short
        assertThrows(StringIndexOutOfBoundsException.class, () -> mapper.toDTO(transaction));
    }

    @Test
    void testToDTOWithNullUser() {
        // Given
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setUser(null);

        // When & Then
        assertThrows(NullPointerException.class, () -> mapper.toDTO(transaction));
    }
}
