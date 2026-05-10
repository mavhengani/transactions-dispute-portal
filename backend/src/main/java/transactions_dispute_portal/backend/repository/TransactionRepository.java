package transactions_dispute_portal.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;

import transactions_dispute_portal.backend.entity.Transaction;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByUserId(
            Long userId,
            Pageable pageable
    );

    Page<Transaction> findByUserIdAndDisputed(
            Long userId,
            Boolean disputed,
            Pageable pageable
    );

    Page<Transaction> findByDisputed(
            Boolean disputed,
            Pageable pageable
    );
}