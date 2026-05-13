package transactions_dispute_portal.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import transactions_dispute_portal.backend.dto.TransactionDTO;
import transactions_dispute_portal.backend.entity.Transaction;
import transactions_dispute_portal.backend.entity.User;
import transactions_dispute_portal.backend.mapper.TransactionMapper;
import transactions_dispute_portal.backend.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    private TransactionService transactionService;

    private final TransactionMapper transactionMapper = new TransactionMapper();

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(transactionRepository, transactionMapper);
    }

    @Test
    void testGetTransactions_AllTransactions() {
        // Given
        PageRequest pageable = PageRequest.of(0, 10);
        List<Transaction> transactions = Arrays.asList(createTransaction(1L), createTransaction(2L));
        Page<Transaction> transactionPage = new PageImpl<>(transactions, pageable, 2);

        when(transactionRepository.findAll(pageable)).thenReturn(transactionPage);
        // When
        Page<TransactionDTO> result = transactionService.getTransactions(null, null, 0, 10);

        // Then
        assertEquals(2, result.getTotalElements());
        verify(transactionRepository, times(1)).findAll(pageable);
        assertEquals("**** **** **** 5678", result.getContent().get(0).getMaskedCardNumber());
    }

    @Test
    void testGetTransactions_ByUserId() {
        // Given
        PageRequest pageable = PageRequest.of(0, 10);
        List<Transaction> transactions = Arrays.asList(createTransaction(1L));
        Page<Transaction> transactionPage = new PageImpl<>(transactions, pageable, 1);

        when(transactionRepository.findByUserId(1L, pageable)).thenReturn(transactionPage);
        // When
        Page<TransactionDTO> result = transactionService.getTransactions(1L, null, 0, 10);

        // Then
        assertEquals(1, result.getTotalElements());
        verify(transactionRepository, times(1)).findByUserId(1L, pageable);
    }

    @Test
    void testGetTransactions_ByDisputed() {
        // Given
        PageRequest pageable = PageRequest.of(0, 10);
        List<Transaction> transactions = Arrays.asList(createTransaction(1L));
        Page<Transaction> transactionPage = new PageImpl<>(transactions, pageable, 1);

        when(transactionRepository.findByDisputed(true, pageable)).thenReturn(transactionPage);
        // When
        Page<TransactionDTO> result = transactionService.getTransactions(null, true, 0, 10);

        // Then
        assertEquals(1, result.getTotalElements());
        verify(transactionRepository, times(1)).findByDisputed(true, pageable);
    }

    @Test
    void testGetTransactions_ByUserIdAndDisputed() {
        // Given
        PageRequest pageable = PageRequest.of(0, 10);
        List<Transaction> transactions = Arrays.asList(createTransaction(1L));
        Page<Transaction> transactionPage = new PageImpl<>(transactions, pageable, 1);

        when(transactionRepository.findByUserIdAndDisputed(1L, true, pageable)).thenReturn(transactionPage);
        // When
        Page<TransactionDTO> result = transactionService.getTransactions(1L, true, 0, 10);

        // Then
        assertEquals(1, result.getTotalElements());
        verify(transactionRepository, times(1)).findByUserIdAndDisputed(1L, true, pageable);
    }

    @Test
    void testDispute_Success() {
        // Given
        Transaction transaction = createTransaction(1L);
        transaction.setDisputed(false);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // When
        TransactionDTO result = transactionService.dispute(1L);

        // Then
        assertTrue(transaction.getDisputed());
        assertEquals("DISPUTED", transaction.getStatus());
        assertEquals(1L, result.getId());
        assertTrue(result.getDisputed());
        assertEquals("DISPUTED", result.getStatus());
        verify(transactionRepository, times(1)).findById(1L);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void testDispute_TransactionNotFound() {
        // Given
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transactionService.dispute(1L));
        assertEquals("Transaction not found", exception.getMessage());
    }

    @Test
    void testDispute_AlreadyDisputed() {
        // Given
        Transaction transaction = createTransaction(1L);
        transaction.setDisputed(true);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transactionService.dispute(1L));
        assertEquals("Transaction already disputed", exception.getMessage());
    }

    private Transaction createTransaction(Long id) {
        Transaction transaction = new Transaction();
        User user = new User();
        user.setCardNumber("1234567812345678");
        transaction.setId(id);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setDisputed(false);
        transaction.setUser(user);
        return transaction;
    }
}
