package transactions_dispute_portal.backend.service;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Service;

import transactions_dispute_portal.backend.dto.TransactionDTO;

import transactions_dispute_portal.backend.entity.Transaction;

import transactions_dispute_portal.backend.mapper.TransactionMapper;

import transactions_dispute_portal.backend.repository.TransactionRepository;

@Service
public class TransactionService {

    private final TransactionRepository repo;

    private final TransactionMapper mapper;

    public TransactionService(
            TransactionRepository repo,
            TransactionMapper mapper
    ) {

        this.repo = repo;

        this.mapper = mapper;
    }

    public Page<TransactionDTO> getTransactions(

            Long userId,

            Boolean disputed,

            int page,

            int size
    ) {

        PageRequest pageable =
                PageRequest.of(page, size);

        Page<Transaction> transactions;

        if (userId == null && disputed == null) {

            transactions = repo.findAll(pageable);

        } else if (userId == null) {

            transactions = repo.findByDisputed(
                    disputed,
                    pageable
            );

        } else if (disputed == null) {

            transactions =
                    repo.findByUserId(
                            userId,
                            pageable
                    );

        } else {

            transactions =
                    repo.findByUserIdAndDisputed(
                            userId,
                            disputed,
                            pageable
                    );
        }

        return transactions.map(mapper::toDTO);
    }

    @Transactional
    public TransactionDTO dispute(Long id) {

        Transaction tx =
                repo.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Transaction not found"
                                )
                        );

        if (Boolean.TRUE.equals(
                tx.getDisputed()
        )) {

            throw new RuntimeException(
                    "Transaction already disputed"
            );
        }

        tx.setDisputed(true);

        tx.setStatus("DISPUTED");

        Transaction saved =
                repo.save(tx);

        return mapper.toDTO(saved);
    }
}